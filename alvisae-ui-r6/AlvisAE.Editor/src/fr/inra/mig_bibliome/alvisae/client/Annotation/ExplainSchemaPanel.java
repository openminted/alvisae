/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import fr.inra.mig_bibliome.alvisae.client.StaneCoreResources;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import java.util.ArrayList;

/**
 *
 * @author fpapazian
 */
public class ExplainSchemaPanel extends ResizeComposite {

    public static class AnnotationTypeInfo implements Comparable<AnnotationTypeInfo> {

        public static final ProvidesKey<AnnotationTypeInfo> KEY_PROVIDER = new ProvidesKey<AnnotationTypeInfo>() {

            @Override
            public Object getKey(AnnotationTypeInfo item) {
                return item == null ? null : item.getAnnTypeDef().getType();
            }
        };
        private final AnnotationTypeDefinition annotationTypeDef;

        public AnnotationTypeInfo(AnnotationTypeDefinition annotationTypeDef) {
            this.annotationTypeDef = annotationTypeDef;
        }

        public AnnotationTypeDefinition getAnnTypeDef() {
            return annotationTypeDef;
        }

        @Override
        public final int compareTo(AnnotationTypeInfo o) {
            int result = this.getAnnTypeDef().getAnnotationKind().compareTo(o.getAnnTypeDef().getAnnotationKind());
            if (result == 0) {
                result = this.getAnnTypeDef().getType().compareTo(o.getAnnTypeDef().getType());
            }
            return result;
        }
    }

    public static class AnnotationTypeCell extends AbstractCell<AnnotationTypeInfo> {

        public static interface AnnotationTypeCellTemplates extends SafeHtmlTemplates {

            @SafeHtmlTemplates.Template("<span class='{0}'>{1}</span> ")
            public SafeHtml textAnnotation(String className, String annotationTypeName);

            @SafeHtmlTemplates.Template("<b>{0}</b>")
            public SafeHtml boldedSpan(String text);

            @Template("<a href='{0}' target='_blank' title='{0}'>{1}</a> ")
            public SafeHtml guidelineLink(String url, SafeHtml icon);
        }
        private static final SafeHtml GuidelinesImage = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneCoreResources.INSTANCE.GuidelinesIcon()).getHTML());
        private static final AnnotationTypeCellTemplates TEMPLATES = GWT.create(AnnotationTypeCellTemplates.class);
        private final AnnotationSchemaDefinition schemaDefinition;

        public AnnotationTypeCell(AnnotationSchemaDefinition schemaDefinition) {
            this.schemaDefinition = schemaDefinition;
        }

        @Override
        public void render(Context context, AnnotationTypeInfo value, SafeHtmlBuilder sb) {

            boolean once = false;
            AnnotationKind kind = value.getAnnTypeDef().getAnnotationKind();

            sb.appendHtmlConstant("<div style='border-bottom:1px solid silver; min-height:2.4em; margin: 0.4em;'>");            
            sb.appendHtmlConstant("<div>");
            CombinedAnnotationCell.renderKind(value.getAnnTypeDef().getAnnotationKind(), sb);
            String typeName = value.getAnnTypeDef().getType();
            AnnotationSchemaCell.renderNamedType(typeName, sb);

            String url = value.getAnnTypeDef().getUrl();
            if (url != null) {
                sb.appendHtmlConstant("&nbsp;").append(TEMPLATES.guidelineLink(url, GuidelinesImage));
            }

            if (!kind.equals(AnnotationKind.TEXT)) {
                sb.appendHtmlConstant("&nbsp;:");
            }
            sb.appendHtmlConstant("</div>");

            switch (kind) {
                case TEXT:
                    break;

                case GROUP:
                    sb.appendHtmlConstant("<div style='margin-left: 2em; height:auto;'>");
                    sb.append(TEMPLATES.boldedSpan("{ "));
                    for (String ct : value.getAnnTypeDef().getAnnotationGroupDefinition().getComponentsTypes()) {
                        if (once) {
                            sb.append(TEMPLATES.boldedSpan(" / "));
                        } else {
                            once = true;
                        }

                        AnnotationTypeDefinition refAnnType = schemaDefinition.getAnnotationTypeDefinition(ct);
                        CombinedAnnotationCell.renderKind(refAnnType.getAnnotationKind(), sb);
                        sb.appendHtmlConstant("&nbsp;");
                        String refTypeName = refAnnType.getType();
                        AnnotationSchemaCell.renderNamedType(refTypeName, sb);

                    }
                    sb.append(TEMPLATES.boldedSpan(" }"));
                    sb.appendHtmlConstant("</div>");
                    break;
                case RELATION:
                    sb.appendHtmlConstant("<div style='margin-left: 2em; height:auto;'>");
                    for (String role : value.getAnnTypeDef().getRelationDefinition().getSupportedRoles()) {
                        if (once) {
                            sb.append(TEMPLATES.boldedSpan(" + "));
                        } else {
                            once = true;
                        }
                        sb.appendHtmlConstant("<i>").appendEscaped(role).appendHtmlConstant("</i> ");

                        sb.append(TEMPLATES.boldedSpan("( "));

                        boolean severalTypesForRole = false;
                        for (String rolefTypeName : value.getAnnTypeDef().getRelationDefinition().getArgumentTypes(role)) {
                            if (severalTypesForRole) {
                                sb.append(TEMPLATES.boldedSpan(" / "));
                            }
                            AnnotationTypeDefinition refAnnType = schemaDefinition.getAnnotationTypeDefinition(rolefTypeName);
                            CombinedAnnotationCell.renderKind(refAnnType.getAnnotationKind(), sb);
                            sb.appendHtmlConstant("&nbsp;");
                            String refTypeName = refAnnType.getType();
                            AnnotationSchemaCell.renderNamedType(refTypeName, sb);

                            severalTypesForRole = true;
                        }

                        sb.append(TEMPLATES.boldedSpan(" )"));
                    }
                    sb.appendHtmlConstant("</div>");
                    break;
            }
            
            sb.appendHtmlConstant("</div>");
            
        }
    }
    //
    private final AnnotationSchemaDefinition schemaDefinition;
    private final TaskDefinition taskDef;
    private final CellList<AnnotationTypeInfo> cellList;

    public ExplainSchemaPanel(AnnotationSchemaDefinition schemaDefinition, TaskDefinition taskDef) {
        this.schemaDefinition = schemaDefinition;
        this.taskDef = taskDef;

        cellList = new CellList<AnnotationTypeInfo>(new AnnotationTypeCell(schemaDefinition));

        SelectionModel<AnnotationTypeInfo> selectionModel = new NoSelectionModel<AnnotationTypeInfo>();
        cellList.setSelectionModel(selectionModel);
        ScrollPanel scrollPanel = new ScrollPanel(cellList);
        if (taskDef != null) {
            final CheckBox ck = new CheckBox("restrict to Annotation Types of current Task");
            ck.setValue(true);
            ck.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    refresh(ck.getValue());
                }
            });
            DockLayoutPanel vp = new DockLayoutPanel(Style.Unit.EM);
            vp.setHeight("400px");
            vp.setWidth("600px");
            vp.addNorth(ck, 3);
            vp.add(scrollPanel);
            initWidget(vp);
        } else {
            scrollPanel.setHeight("400px");
            initWidget(scrollPanel);
        }

        refresh(true);
    }

    private void refresh(boolean restrictToTask) {
        cellList.setRowCount(0, true);

        ArrayList<AnnotationTypeInfo> annotationTypeDefs = new ArrayList<AnnotationTypeInfo>();
        for (String typeName : schemaDefinition.getAnnotationTypes()) {
            if (!restrictToTask || (taskDef == null) || taskDef.getEditedAnnotationTypes().contains(typeName)) {
                annotationTypeDefs.add(new AnnotationTypeInfo(schemaDefinition.getAnnotationTypeDefinition(typeName)));
            }
        }

        cellList.setRowCount(annotationTypeDefs.size(), true);
        cellList.setRowData(0, annotationTypeDefs);
    }
}
