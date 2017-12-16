/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationSchemaCell;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationTable;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationTable.CombinedAnnotationColumn;
import fr.inra.mig_bibliome.alvisae.client.Annotation.CombinedAnnotationCell;
import fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata.PropertiesTree.GenericPropInfo;
import fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata.PropertiesTree.PropKeyInfo;
import fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata.PropertiesTree.PropValueInfo;
import fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata.PropertiesTree.PropValueNodeChangedHandler;
import fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata.PropertiesTree.PropertiesTreeViewModel;
import fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata.PropertiesTree.RealPropKeyInfo;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDocumentViewMapper;
import fr.inra.mig_bibliome.alvisae.client.Document.ConsolidationStatusDisplayer.ConsoStatusColumn;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationGroupEditionEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationRelationEditionEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationStatusChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceBroadcastInfoEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceDirectSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceSelectionChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.client.Widgets.CellListSelectPopupPanel;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationReferenceImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSetImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.ArgumentImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.SetSafeAnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.ConsolidationStatus;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDIResourceRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDISemClassRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.Relation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Relation.Argument;
import fr.inra.mig_bibliome.alvisae.shared.data3.SourceAnnotations;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDIConceptRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class AnnotationPropertySheet extends Composite implements TyDIResourceSelectionChangedEventHandler, AnnotationStatusChangedEventHandler {

    public static abstract class AnnotationTypeColumn<T> extends Column<T, Annotation> {

        public static class AnnotationTypeCell extends CombinedAnnotationCell {

            @Override
            public void render(Context context, Annotation annotation, SafeHtmlBuilder sb) {
                if (annotation != null) {

                    AnnotationSchemaCell.renderUnNamedType(annotation.getAnnotationType(), sb);
                }
            }
        }

        public AnnotationTypeColumn() {
            super(new AnnotationTypeCell());
        }
    };

    public static abstract class RelationRoleColumn<T> extends Column<T, Relation.Argument> {

        public static class RelationRoleCell extends AbstractCell<Relation.Argument> {

            @Override
            public void render(Context context, Relation.Argument argument, SafeHtmlBuilder sb) {
                if (argument != null) {
                    sb.appendHtmlConstant("<b>").appendEscaped(argument.getRole()).appendHtmlConstant("</b>:");
                }
            }
        }

        public RelationRoleColumn() {
            super(new RelationRoleCell());
        }
    };
    public static final ProvidesKey<AnnotationBackReference> KEY_PROVIDER = new ProvidesKey<AnnotationBackReference>() {
        @Override
        public Object getKey(AnnotationBackReference item) {
            return item == null ? null : item.getAnnotationSetId() + "." + item.getAnnotationId();
        }
    };

    public static abstract class CombinedAnnotationChangeHandler<T> {

        protected static final int COMPARG_POPUPWIDTH = 300;
        private AnnotatedTextHandler annotatedText = null;
        private Annotation combinedAnnotation = null;
        private boolean readOnly = true;

        public AnnotatedTextHandler getAnnotatedText() {
            return annotatedText;
        }

        public Annotation getAnnotation() {
            return combinedAnnotation;
        }

        public boolean isReadonly() {
            return readOnly;
        }

        public void setAnnotation(AnnotatedTextHandler annotatedText, Annotation annotation, boolean readOnly) {
            this.annotatedText = annotatedText;
            this.combinedAnnotation = annotation;
            this.readOnly = readOnly;
        }

        public abstract void remove(T value);

        public abstract void add(Element displayRef, T value);
    }

    public static class RelationChangeHandler extends CombinedAnnotationChangeHandler<Argument> {

        CellListSelectPopupPanel<Annotation> selectionPopupPanel = null;

        private CellListSelectPopupPanel<Annotation> getArgumentSelectPopupPanel() {
            if (selectionPopupPanel == null) {
                List<Annotation> annotations = getAnnotatedText().getReferenceableAnnotations();
                //remove parent Relation to avoid auto-referencing
                annotations.remove(getAnnotation());
                selectionPopupPanel = new CellListSelectPopupPanel<Annotation>(new AnnotationIdNTypeCell(getAnnotatedText(), null), annotations);
            }
            return selectionPopupPanel;
        }

        @Override
        public void setAnnotation(AnnotatedTextHandler annotatedText, Annotation annotation, boolean readOnly) {
            super.setAnnotation(annotatedText, annotation, readOnly);
            if (!annotation.getAnnotatedText().equals(getAnnotatedText())) {
                selectionPopupPanel = null;
            }
        }

        @Override
        public void remove(Argument argument) {
            if (isReadonly()) {
                throw new IllegalArgumentException("Editing forbidden while in read only mode!");
            }
            Map<String, AnnotationReference> argumentRoleMap = new HashMap<String, AnnotationReference>(getAnnotation().getRelation().getRolesArguments());
            argumentRoleMap.remove(argument.getRole());
            AnnotationRelationEditionEdit relationChangeEdit = new AnnotationRelationEditionEdit(getAnnotatedText(), getAnnotation(), getAnnotation().getAnnotationType(), argumentRoleMap);
            relationChangeEdit.redo();
        }

        @Override
        public void add(Element displayRef, final Argument argRole) {
            if (isReadonly()) {
                throw new IllegalArgumentException("Editing forbidden while in read only mode!");
            }
            String preferredWidth = Math.max(displayRef.getClientWidth(), COMPARG_POPUPWIDTH) + "px";
            getArgumentSelectPopupPanel().display(displayRef.getAbsoluteLeft(), displayRef.getAbsoluteTop(), preferredWidth,
                    new ValueUpdater<Annotation>() {
                @Override
                public void update(Annotation newArgument) {
                    Map<String, AnnotationReference> argumentRoleMap = new HashMap<String, AnnotationReference>(getAnnotation().getRelation().getRolesArguments());
                    //FIXME : add annoationSetId
                    argumentRoleMap.put(argRole.getRole(), AnnotationReferenceImpl.create(newArgument.getId(), null));
                    AnnotationRelationEditionEdit relationChangeEdit = new AnnotationRelationEditionEdit(getAnnotatedText(), getAnnotation(), getAnnotation().getAnnotationType(), argumentRoleMap);
                    relationChangeEdit.redo();
                }
            });
        }
    }

    public static class GroupChangeHandler extends CombinedAnnotationChangeHandler<Annotation> {

        CellListSelectPopupPanel<Annotation> selectionPopupPanel = null;

        private CellListSelectPopupPanel<Annotation> getComponentSelectPopupPanel() {
            if (selectionPopupPanel == null) {
                List<Annotation> annotations = new ArrayList<Annotation>();
                AnnotationSetImpl editableAnnSet = getAnnotatedText().getEditableUsersAnnotationSet();
                Integer editableAnnSetId = editableAnnSet != null ? editableAnnSet.getId() : null;

                Map<AnnotationReference, Annotation> annotationRefs = getAnnotatedText().getReferenceableAnnotationRefs();
                Annotation group = getAnnotation();
                Set<AnnotationReference> componentRefs = new HashSet<AnnotationReference>();
                for (AnnotationReference aRef : group.getAnnotationGroup().getComponentRefs()) {
                    componentRefs.add(new SetSafeAnnotationReference(aRef, editableAnnSetId));
                }

                for (Map.Entry<AnnotationReference, Annotation> e : annotationRefs.entrySet()) {
                    Annotation current = e.getValue();
                    if (group.equals(current)) {
                        //don't include parent Group to avoid auto-referencing
                        continue;
                    } else if (componentRefs.contains(e.getKey())) {
                        //remove annotations already components of the group
                        continue;
                    } else {
                        annotations.add(current);
                    }
                }
                selectionPopupPanel = new CellListSelectPopupPanel<Annotation>(new AnnotationIdNTypeCell(getAnnotatedText(), null), annotations);
            }
            return selectionPopupPanel;
        }

        private List<AnnotationReference> getComponentsExcept(Annotation rejected) {
            List<AnnotationReference> components = new ArrayList<AnnotationReference>();
            for (AnnotationReference aRef : getAnnotation().getAnnotationGroup().getComponentRefs()) {
                if (rejected == null || !aRef.getAnnotationId().equals(rejected.getId())) {
                    components.add(aRef);
                }
            }
            return components;
        }

        @Override
        public void setAnnotation(AnnotatedTextHandler annotatedText, Annotation annotation, boolean readOnly) {
            super.setAnnotation(annotatedText, annotation, readOnly);
            if (!annotation.getAnnotatedText().equals(getAnnotatedText())) {
                selectionPopupPanel = null;
            }
        }

        @Override
        public void remove(Annotation component) {
            if (isReadonly()) {
                throw new IllegalArgumentException("Editing forbidden while in read only mode!");
            }
            List<AnnotationReference> components = getComponentsExcept(component);
            AnnotationGroupEditionEdit groupChangeEdit = new AnnotationGroupEditionEdit(getAnnotatedText(), getAnnotation(), getAnnotation().getAnnotationType(), components);
            groupChangeEdit.redo();
        }

        public void replace(Element displayRef, final Annotation component) {
            if (isReadonly()) {
                throw new IllegalArgumentException("Editing forbidden while in read only mode!");
            }
            String preferredWidth = Math.max(displayRef.getClientWidth(), COMPARG_POPUPWIDTH) + "px";
            getComponentSelectPopupPanel().display(displayRef.getAbsoluteLeft(), displayRef.getAbsoluteTop(), preferredWidth,
                    new ValueUpdater<Annotation>() {
                @Override
                public void update(Annotation newComponent) {
                    List<AnnotationReference> components = getComponentsExcept(component);

                    //FIXME : add annoationSetId


                    components.add(AnnotationReferenceImpl.create(newComponent.getId(), null));
                    AnnotationGroupEditionEdit groupChangeEdit = new AnnotationGroupEditionEdit(getAnnotatedText(), getAnnotation(), getAnnotation().getAnnotationType(), components);
                    groupChangeEdit.redo();
                }
            });
        }

        @Override
        public void add(Element displayRef, Annotation component) {
            replace(displayRef, null);
        }
    }
    private static final String DELARGCOMPBTN_CLASSVAL = "aae_DelArgCompBtn";
    private static final String ADDARGCOMPBTN_CLASSVAL = "aae_AddArgCompBtn";
    private static final SafeHtml DelSmallIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.DelSmallIcon()).getHTML());
    private static final SafeHtml AddSmallIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.AddSmallIcon()).getHTML());

    static interface PropertiesSheetTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span " + DELARGCOMPBTN_CLASSVAL + "='true' title='{0}' style='background-color:white;'>{1}</span>")
        public SafeHtml spanDelButton(String title, SafeHtml icon);

        @SafeHtmlTemplates.Template("<span " + ADDARGCOMPBTN_CLASSVAL + "='true' title='{0}' style='background-color:white;'>{1}</span>")
        public SafeHtml spanAddButton(String title, SafeHtml icon);
    }
    static final PropertiesSheetTemplates TEMPLATES = GWT.create(PropertiesSheetTemplates.class);

    public static abstract class RemoveArgumentColumn<T> extends Column<T, Relation.Argument> {

        public static class RemoveArgumentCell extends AbstractCell<Relation.Argument> {

            private final RelationChangeHandler changeHandler;

            public RemoveArgumentCell(RelationChangeHandler changeHandler) {
                super("click");
                this.changeHandler = changeHandler;
            }

            @Override
            public void render(Context context, Relation.Argument argument, SafeHtmlBuilder sb) {
                if (!changeHandler.isReadonly()) {
                    if (argument != null && argument.getArgument() != null) {
                        sb.append(TEMPLATES.spanDelButton("clear argument", DelSmallIcon));
                    } else {
                        sb.append(TEMPLATES.spanAddButton("add argument", AddSmallIcon));
                    }
                }
            }

            @Override
            public void onBrowserEvent(Context context, Element parent, Argument value, NativeEvent event, ValueUpdater<Argument> valueUpdater) {
                super.onBrowserEvent(context, parent, value, event, valueUpdater);
                if (!changeHandler.isReadonly()) {
                    String eventType = event.getType();
                    if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                        EventTarget evtTarget = event.getEventTarget();
                        Element targetElement = evtTarget.cast();
                        if (targetElement.getParentElement().getAttribute(DELARGCOMPBTN_CLASSVAL).equals("true")) {
                            changeHandler.remove(value);
                        } else if (targetElement.getParentElement().getAttribute(ADDARGCOMPBTN_CLASSVAL).equals("true")) {
                            changeHandler.add(targetElement.getParentElement(), value);
                        }
                    }
                }

            }
        }

        public RemoveArgumentColumn(RelationChangeHandler changeHandler) {
            super(new RemoveArgumentCell(changeHandler));
        }
    };

    public static abstract class RemoveComponentColumn<T> extends Column<T, Annotation> {

        public static class RemoveComponentCell extends AbstractCell<Annotation> {

            private final GroupChangeHandler changeHandler;

            public RemoveComponentCell(GroupChangeHandler changeHandler) {
                super("click");
                this.changeHandler = changeHandler;
            }

            @Override
            public void render(Context context, Annotation component, SafeHtmlBuilder sb) {
                if (!changeHandler.isReadonly()) {
                    if (component != null) {
                        sb.append(TEMPLATES.spanDelButton("remove component", DelSmallIcon));
                    } else {
                    }
                }
            }

            @Override
            public void onBrowserEvent(Context context, Element parent, Annotation value, NativeEvent event, ValueUpdater<Annotation> valueUpdater) {
                super.onBrowserEvent(context, parent, value, event, valueUpdater);
                if (!changeHandler.isReadonly()) {
                    String eventType = event.getType();
                    if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                        EventTarget evtTarget = event.getEventTarget();
                        Element targetElement = evtTarget.cast();
                        if (targetElement.getParentElement().getAttribute(DELARGCOMPBTN_CLASSVAL).equals("true")) {
                            changeHandler.remove(value);
                        } else if (targetElement.getParentElement().getAttribute(ADDARGCOMPBTN_CLASSVAL).equals("true")) {
                            changeHandler.add(targetElement.getParentElement(), value);
                        }
                    }
                }

            }
        }

        public RemoveComponentColumn(GroupChangeHandler changeHandler) {
            super(new RemoveComponentCell(changeHandler));
        }
    };

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    interface Binder extends UiBinder<Widget, AnnotationPropertySheet> {
    }
    private static Binder uiBinder = GWT.create(Binder.class);
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    //Override some rules of the default table CSS
    interface CompArgGridStyle extends DataGrid.Style {
    }

    interface CompArgGridResources extends DataGrid.Resources {

        @ClientBundle.Source({DataGrid.Style.DEFAULT_CSS, "CompArgGrid.css"})
        @Override
        CompArgGridStyle dataGridStyle();
    }
    CompArgGridResources resources = GWT.create(CompArgGridResources.class);

    public interface Styles extends CssResource {

        String ForefrontNVisible();

        String BackgroundNHidden();
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @UiField
    Panel globalPanel;
    @UiField
    Image veiledIcon;
    @UiField
    Image unveiledIcon;
    @UiField(provided = true)
    CellList<Annotation> idWidget;
    @UiField
    Panel compargPanel;
    @UiField
    Panel componentsPanel;
    @UiField
    Panel argumentsPanel;
    @UiField
    Label compargLabel;
    @UiField
    Image addComponent;
    @UiField
    ResizeLayoutPanel componentsContainer;
    @UiField(provided = true)
    DataGrid<Annotation> componentsGrid;
    @UiField
    ResizeLayoutPanel argumentsContainer;
    @UiField(provided = true)
    DataGrid<Relation.Argument> argumentsGrid;
    @UiField
    Panel propsPanel;
    @UiField(provided = true)
    PropertiesTree propsCellTree;
    @UiField
    Panel sourcePanel;
    @UiField
    ResizeLayoutPanel sourceContainer;
    @UiField(provided = true)
    DataGrid<AnnotationBackReference> sourceGrid;
    @UiField
    Styles style;
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private final ListDataProvider<Annotation> compAnnotProvider;
    private final List<Annotation> componentsAnnotations = new ArrayList<Annotation>();
    private final ListDataProvider<Relation.Argument> argAnnotProvider;
    private final List<Relation.Argument> argumentsAnnotations = new ArrayList<Relation.Argument>();
    private final ListDataProvider<AnnotationBackReference> sourcesAnnotProvider;
    private final Map<AnnotationBackReference, Annotation> sourcesAnnotations = new LinkedHashMap<AnnotationBackReference, Annotation>();
    private final AnnotationIdNTypeSelectCell annotationIdCell;
    private final PropertiesTreeViewModel model;
    private TyDISemClassRef lastSelectedSemClassRef = null;
    private CombinedAnnotationCell combinedAnnotationCell = new AnnotationTable.SelectorCombinedAnnotationCell(true);
    private final AnnotationTable.AnnotationIdColumn<Relation.Argument> argIdColumn;
    private final AnnotationTable.AnnotationIdColumn<Annotation> compIdColumn;
    private final AnnotationTable.AnnotationIdColumn<AnnotationBackReference> sourceIdColumn;
    private final RelationChangeHandler relationChangeHandler = new RelationChangeHandler();
    private final GroupChangeHandler groupChangeHandler = new GroupChangeHandler();

    public AnnotationPropertySheet() {

        SingleSelectionModel<GenericPropInfo> selectionModel = new SingleSelectionModel<PropertiesTree.GenericPropInfo>();
        model = new PropertiesTreeViewModel(new PropValueNodeChangedHandler() {
            @Override
            public void onValueEntryCreated(PropValueInfo valInfo) {
                //select the newly created value node
                //FIXME : should set focus on editable cell is possible
                if (valInfo != null) {
                    getPropsCellTree().openNodeAndSelect(valInfo);

                    //if the created entry is a TyDIConceptRef, then automatically affect the value with last selected Concept, if any
                    if (getLastSelectedSemClassRef() != null && valInfo.getKeyInfo() instanceof RealPropKeyInfo) {
                        RealPropKeyInfo keyInfo = (RealPropKeyInfo) valInfo.getKeyInfo();
                        PropertyType valType = keyInfo.getPropDef().getValuesType();
                        PropType_TyDIConceptRef conceptRefType = valType.getAsTyDIConceptRefType();
                        if (conceptRefType != null) {
                            valInfo.setValue(getLastSelectedSemClassRef().toString());
                            model.performEditUpdatePropValue(valInfo);
                        }
                    }

                }
            }

            @Override
            public void onValueEntryRemoved(PropKeyInfo keyInfo) {
                //select the key header node
                if (keyInfo != null) {
                    propsCellTree.getSelectionModel().setSelected(keyInfo, true);
                }
            }
        }, selectionModel);

        propsCellTree = new PropertiesTree(model, selectionModel);
        // ---

        //
        annotationIdCell = new AnnotationIdNTypeSelectCell();

        idWidget = new CellList<Annotation>(annotationIdCell);
        // ---
        componentsGrid = new DataGrid<Annotation>(50, resources);
        argumentsGrid = new DataGrid<Relation.Argument>(50, resources);
        sourceGrid = new DataGrid<AnnotationBackReference>(50, resources);

        initWidget(uiBinder.createAndBindUi(this));

        {
            //column used to remove the corresponding component
            RemoveComponentColumn<Annotation> removeCompColumn = new RemoveComponentColumn<Annotation>(groupChangeHandler) {
                @Override
                public Annotation getValue(Annotation annotation) {
                    return annotation;
                }
            };
            componentsGrid.addColumn(removeCompColumn);


            AnnotationTable.AnnotationKindColumn<Annotation> kindColumn = new AnnotationTable.AnnotationKindColumn<Annotation>() {
                @Override
                public Annotation getValue(Annotation annotation) {
                    return annotation;
                }

                @Override
                public void onBrowserEvent(Context context, Element elem, Annotation object, NativeEvent event) {
                    super.onBrowserEvent(context, elem, object, event);
                    String eventType = event.getType();
                    if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                        groupChangeHandler.replace(elem.getParentElement(), object);
                    }
                }
            };
            componentsGrid.addColumn(kindColumn);

            compIdColumn = new AnnotationTable.AnnotationIdColumn<Annotation>() {
                @Override
                public AnnotationReference getValue(Annotation annotation) {
                    return getIdCell().getAnnotatedText().getAnnotationReference(annotation.getId());
                }
            };
            componentsGrid.addColumn(compIdColumn);
            AnnotationTypeColumn<Annotation> typeColumn = new AnnotationTypeColumn<Annotation>() {
                @Override
                public Annotation getValue(Annotation annotation) {
                    return annotation;
                }
            };
            componentsGrid.addColumn(typeColumn);

            CombinedAnnotationColumn<Annotation> detailColumn = new AnnotationTable.CombinedAnnotationColumn<Annotation>(combinedAnnotationCell) {
                @Override
                public Annotation getValue(Annotation annotation) {
                    return annotation;
                }
            };
            componentsGrid.addColumn(detailColumn);

            compAnnotProvider = new ListDataProvider<Annotation>(AnnotationTable.KEY_PROVIDER);
            compAnnotProvider.addDataDisplay(componentsGrid);

            componentsGrid.setColumnWidth(removeCompColumn, 12.0, Unit.PX);
            componentsGrid.setColumnWidth(kindColumn, 26.0, Unit.PX);
            componentsGrid.setColumnWidth(compIdColumn, 7.0, Unit.EM);
            componentsGrid.setColumnWidth(typeColumn, 26.0, Unit.PX);
        }

        {
            final AnnotationTable.AnnotationKindColumn<Relation.Argument> kindColumn;
            final AnnotationTypeColumn<Relation.Argument> typeColumn;

            //column used to remove/add the argument corresponding to that role

            RemoveArgumentColumn<Relation.Argument> removeArgumentColumn = new RemoveArgumentColumn<Relation.Argument>(relationChangeHandler) {
                @Override
                public Argument getValue(Argument argument) {
                    return argument;
                }
            };

            argumentsGrid.addColumn(removeArgumentColumn);

            final RelationRoleColumn<Relation.Argument> relationRoleColumn = new RelationRoleColumn<Relation.Argument>() {
                @Override
                public Argument getValue(Argument argument) {
                    return argument;
                }
            };
            argumentsGrid.addColumn(relationRoleColumn);

            kindColumn = new AnnotationTable.AnnotationKindColumn<Relation.Argument>() {
                @Override
                public Annotation getValue(Relation.Argument item) {
                    return item.getArgument();
                }

                @Override
                public void onBrowserEvent(Context context, Element elem, Relation.Argument object, NativeEvent event) {
                    super.onBrowserEvent(context, elem, object, event);
                    String eventType = event.getType();
                    if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                        relationChangeHandler.add(elem.getParentElement(), object);
                    }
                }
            };

            argumentsGrid.addColumn(kindColumn);

            argIdColumn = new AnnotationTable.AnnotationIdColumn<Relation.Argument>() {
                @Override
                public AnnotationReference getValue(Relation.Argument item) {
                    Annotation arg = item.getArgument();
                    if (arg != null) {
                        return getIdCell().getAnnotatedText().getAnnotationReference(arg.getId());
                    } else {
                        return null;
                    }
                }
            };
            argumentsGrid.addColumn(argIdColumn);

            typeColumn = new AnnotationTypeColumn<Relation.Argument>() {
                @Override
                public Annotation getValue(Relation.Argument item) {
                    return item.getArgument();
                }
            };
            argumentsGrid.addColumn(typeColumn);

            CombinedAnnotationColumn<Relation.Argument> detailColumn = new AnnotationTable.CombinedAnnotationColumn<Relation.Argument>(combinedAnnotationCell) {
                @Override
                public Annotation getValue(Relation.Argument item) {
                    return item.getArgument();
                }
            };
            argumentsGrid.addColumn(detailColumn);

            argAnnotProvider = new ListDataProvider<Relation.Argument>(new ProvidesKey<Relation.Argument>() {
                @Override
                public Object getKey(Relation.Argument item) {
                    return item.getRole();
                }
            });

            argAnnotProvider.addDataDisplay(argumentsGrid);

            argumentsGrid.setColumnWidth(removeArgumentColumn, 12.0, Unit.PX);
            argumentsGrid.setColumnWidth(kindColumn, 26.0, Unit.PX);
            argumentsGrid.setColumnWidth(argIdColumn, 7.0, Unit.EM);
            argumentsGrid.setColumnWidth(typeColumn, 26.0, Unit.PX);

            argumentsGrid.setSelectionModel(new SingleSelectionModel<Argument>());

        }

        {

            ConsoStatusColumn<AnnotationBackReference> statusColumn = new ConsoStatusColumn<AnnotationBackReference>() {
                @Override
                public ConsolidationStatus getValue(AnnotationBackReference annRef) {
                    return annRef.getConsolidationStatus();
                }
            };
            sourceGrid.addColumn(statusColumn);

            AnnotationTable.AnnotationKindColumn<AnnotationBackReference> kindColumn = new AnnotationTable.AnnotationKindColumn<AnnotationBackReference>() {
                @Override
                public Annotation getValue(AnnotationBackReference annRef) {
                    return sourcesAnnotations.get(annRef);
                }
            };
            sourceGrid.addColumn(kindColumn);

            sourceIdColumn = new AnnotationTable.AnnotationIdColumn<AnnotationBackReference>() {
                @Override
                public AnnotationReference getValue(AnnotationBackReference annRef) {
                    return annRef;
                }
            };
            sourceGrid.addColumn(sourceIdColumn);

            AnnotationTypeColumn<AnnotationBackReference> typeColumn = new AnnotationTypeColumn<AnnotationBackReference>() {
                @Override
                public Annotation getValue(AnnotationBackReference annRef) {
                    return sourcesAnnotations.get(annRef);
                }
            };
            sourceGrid.addColumn(typeColumn);

            CombinedAnnotationColumn<AnnotationBackReference> detailColumn = new AnnotationTable.CombinedAnnotationColumn<AnnotationBackReference>(combinedAnnotationCell) {
                @Override
                public Annotation getValue(AnnotationBackReference annRef) {
                    return sourcesAnnotations.get(annRef);
                }
            };
            sourceGrid.addColumn(detailColumn);

            sourcesAnnotProvider = new ListDataProvider<AnnotationBackReference>(KEY_PROVIDER);
            sourcesAnnotProvider.addDataDisplay(sourceGrid);

            sourceGrid.setColumnWidth(statusColumn, 18.0, Unit.PX);
            sourceGrid.setColumnWidth(kindColumn, 26.0, Unit.PX);
            sourceGrid.setColumnWidth(sourceIdColumn, 7.0, Unit.EM);
            sourceGrid.setColumnWidth(typeColumn, 26.0, Unit.PX);
        }


        ClickHandler veilClickHnd = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                AnnotationDocumentViewMapper mapper = AnnotationDocumentViewMapper.getMapper(model.getAnnotatedTextHandler().getAnnotatedText());
                boolean veiledStatus = mapper.toggleVeiledStatus(model.getAnnotation().getId());
            }
        };
        veiledIcon.addClickHandler(veilClickHnd);
        unveiledIcon.addClickHandler(veilClickHnd);

    }

    private PropertiesTree getPropsCellTree() {
        return propsCellTree;
    }

    private TyDISemClassRef getLastSelectedSemClassRef() {
        return lastSelectedSemClassRef;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void setAnnotationTypeUpdater(ValueUpdater<String> annotationTypeUpdater) {
        annotationIdCell.setAnnotationTypeUpdater(annotationTypeUpdater);
    }
    final int MAXDISPLAYEDCOMPARG = 5;

    private void refreshVeilButton() {
        if (model.getAnnotatedTextHandler() != null) {
            String annotation = model.getAnnotation().getId();
            AnnotationDocumentViewMapper mapper = AnnotationDocumentViewMapper.getMapper(model.getAnnotatedTextHandler().getAnnotatedText());
            if (annotation != null) {
                boolean veiled = mapper.isVeiled(annotation);
                Element veiledLayer = veiledIcon.getElement().getParentElement();
                Element unveiledLayer = unveiledIcon.getElement().getParentElement();
                if (veiled) {
                    veiledLayer.removeClassName(style.BackgroundNHidden());
                    veiledLayer.addClassName(style.ForefrontNVisible());
                    unveiledLayer.removeClassName(style.ForefrontNVisible());
                    unveiledLayer.addClassName(style.BackgroundNHidden());
                } else {
                    veiledLayer.removeClassName(style.ForefrontNVisible());
                    veiledLayer.addClassName(style.BackgroundNHidden());
                    unveiledLayer.removeClassName(style.BackgroundNHidden());
                    unveiledLayer.addClassName(style.ForefrontNVisible());
                }
            }
        }
    }

    public void display(AnnotatedTextHandler document, final Annotation annotation, boolean withProperties, boolean readonly, List<AnnotatedTextHandler> sourceAnnotatedTexts) {

        final boolean refreshDisplayed = annotation != null && model.getAnnotation() != null && annotation.getId().equals(model.getAnnotation().getId());

        //remember previously display annotation to restore the selected Node
        final Object previouslySelected;
        if (refreshDisplayed) {
            previouslySelected = propsCellTree.getSelectionModel().getSelectedObject();
        } else {
            previouslySelected = null;
        }

        annotationIdCell.setReadonly(readonly);
        annotationIdCell.setAnnotatedText(document);
        idWidget.setRowCount(0, true);
        idWidget.setRowData(0, new ArrayList<Annotation>(0));

        ScheduledCommand expandCmd = null;
        globalPanel.setVisible(annotation != null);
        if (annotation != null) {

            combinedAnnotationCell.setDocument(document);
            compIdColumn.getIdCell().setDocument(document);
            argIdColumn.getIdCell().setDocument(document);
            sourceIdColumn.getIdCell().setDocument(document);

            List<Annotation> annSingleton = new ArrayList<Annotation>(1);
            annSingleton.add(annotation);
            idWidget.setRowCount(1, true);
            idWidget.setRowData(0, annSingleton);

            propsPanel.setVisible(withProperties);
            refreshVeilButton();

            switch (annotation.getAnnotationKind()) {
                case TEXT:
                    compargPanel.setVisible(false);
                    break;
                case GROUP:
                    addComponent.setVisible(!readonly);
                    groupChangeHandler.setAnnotation(document, annotation, readonly);
                    displayGroup(document, annotation.getAnnotationGroup().getComponentRefs());
                    break;
                case RELATION:
                    String annType = annotation.getAnnotationType();
                    AnnotationTypeDefinition annTypeDef = document.getAnnotatedText().getAnnotationSchema().getAnnotationTypeDefinition(annType);
                    relationChangeHandler.setAnnotation(document, annotation, readonly);
                    displayRelation(document, annTypeDef, annotation.getRelation().getRolesArguments());
                    break;
            }

            //display source annotations in case of an Annotation created during a review
            sourcesAnnotProvider.getList().clear();
            sourcesAnnotations.clear();
            SourceAnnotations sources = annotation.getSourceAnnotations();

            if (sources != null && !sources.getAnnotationBackReferences().isEmpty()) {
                sourcePanel.setVisible(true);
                if (sourceAnnotatedTexts != null) {
                    for (AnnotationBackReference aRef : sources.getAnnotationBackReferences()) {
                        Annotation referencedAnnotation = null;
                        //! Sources Annotations usually belongs to non-loaded AnnotationSets
                        for (AnnotatedTextHandler doc : sourceAnnotatedTexts) {
                            referencedAnnotation = doc.getAnnotation(aRef.getAnnotationId());
                            if (referencedAnnotation != null) {
                                sourcesAnnotations.put(aRef, referencedAnnotation);
                                break;
                            }
                        }
                    }
                }
                sourcesAnnotProvider.getList().addAll(sources.getAnnotationBackReferences());
                sourceGrid.setVisibleRange(0, sourcesAnnotProvider.getList().size());

                double nbSourceDisplayed = sourcesAnnotations.size();
                if (nbSourceDisplayed > MAXDISPLAYEDCOMPARG) {
                    nbSourceDisplayed = MAXDISPLAYEDCOMPARG;
                }
                nbSourceDisplayed += 0.5;
                sourceContainer.setHeight((1.8 * nbSourceDisplayed) + "EM");

            } else {
                sourcePanel.setVisible(false);
            }

            expandCmd = propsCellTree.expandAndSelect(previouslySelected);

        } else {
            combinedAnnotationCell.setDocument(null);
            compIdColumn.getIdCell().setDocument(null);
            argIdColumn.getIdCell().setDocument(null);
            sourceIdColumn.getIdCell().setDocument(null);
            compargPanel.setVisible(false);
            propsPanel.setVisible(false);
        }
        model.setAnnotation(document, annotation, readonly, expandCmd);

    }

    private void displayGroup(AnnotatedTextHandler document, Collection<AnnotationReference> componentRefs) {
        compargPanel.setVisible(true);
        componentsPanel.setVisible(true);
        argumentsPanel.setVisible(false);
        addComponent.setVisible(true);


        compAnnotProvider.getList().clear();
        componentsAnnotations.clear();
        for (AnnotationReference aRef : componentRefs) {
            Annotation referencedAnnotation = document.getAnnotation(aRef.getAnnotationId());
            componentsAnnotations.add(referencedAnnotation);
        }

        int nbComponents = componentsAnnotations.size();
        if (nbComponents > 0) {
            compargLabel.setText("Components (" + nbComponents + ")");
        } else {
            compargLabel.setText("Components");
        }

        double nbCompDisplayed = nbComponents;
        if (nbCompDisplayed > MAXDISPLAYEDCOMPARG) {
            nbCompDisplayed = MAXDISPLAYEDCOMPARG;
        }
        nbCompDisplayed += 0.5;
        componentsContainer.setHeight((1.8 * nbCompDisplayed) + "EM");
        compAnnotProvider.getList().addAll(componentsAnnotations);
        componentsGrid.setVisibleRange(0, compAnnotProvider.getList().size());

    }

    private void displayRelation(AnnotatedTextHandler document, AnnotationTypeDefinition annTypeDef, Map<String, AnnotationReference> rolesArguments) {
        compargPanel.setVisible(true);
        componentsPanel.setVisible(false);
        argumentsPanel.setVisible(true);
        addComponent.setVisible(false);

        compargLabel.setText("Arguments");

        argAnnotProvider.getList().clear();
        argumentsAnnotations.clear();

        Collection<String> orderedRoles;
        if (annTypeDef != null) {
            orderedRoles = annTypeDef.getRelationDefinition().getSupportedRoles();
        } else {
            orderedRoles = rolesArguments.keySet();
        }

        for (String role : orderedRoles) {
            AnnotationReference aRef = rolesArguments.get(role);
            if (aRef != null) {
                Annotation referencedAnnotation = document.getAnnotation(aRef.getAnnotationId());

                argumentsAnnotations.add(new ArgumentImpl(role, referencedAnnotation));
            } else {
                argumentsAnnotations.add(new ArgumentImpl(role, null));
            }
        }

        double nbArgDisplayed = argumentsAnnotations.size();
        if (nbArgDisplayed > MAXDISPLAYEDCOMPARG) {
            nbArgDisplayed = MAXDISPLAYEDCOMPARG;
        }
        nbArgDisplayed += 0.5;
        argumentsContainer.setHeight((1.8 * nbArgDisplayed) + "EM");
        argAnnotProvider.getList().addAll(argumentsAnnotations);
        argumentsGrid.setVisibleRange(0, argAnnotProvider.getList().size());
    }

    @UiHandler("addComponent")
    void addComponentButtonClick(ClickEvent e) {
        groupChangeHandler.add(addComponent.getElement(), null);
    }

    @Override
    public void onTyDIResourceSelectionChanged(TyDIResourceSelectionChangedEvent event) {
        if (event instanceof TyDIResourceDirectSelectionChangedEvent) {
            TyDIResourceRef resRef = event.getTyDIResourceRef();
            if (resRef != null) {
                if (resRef instanceof TyDISemClassRef) {
                    lastSelectedSemClassRef = (TyDISemClassRef) resRef;
                }
            } else {
                lastSelectedSemClassRef = null;
            }
        }
    }

    @Override
    public void onAnnotationStatusChanged(AnnotationStatusChangedEvent event) {
        refreshVeilButton();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        EventBus eventBus = injector.getMainEventBus();
        TyDIResourceSelectionChangedEvent.register(eventBus, this);
        AnnotationStatusChangedEvent.register(eventBus, this);
        TyDIResourceBroadcastInfoEvent.register(eventBus, model);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        TyDIResourceSelectionChangedEvent.unregister(this);
        AnnotationStatusChangedEvent.unregister(this);
        TyDIResourceBroadcastInfoEvent.unregister(model);
    }
}