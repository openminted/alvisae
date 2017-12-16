/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationSchemaCell;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationTable;
import fr.inra.mig_bibliome.alvisae.client.Annotation.CombinedAnnotationCell;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;

/**
 *
 * @author fpapazian
 */
public class AnnotationIdNTypeCell extends AbstractCell<Annotation> {

    public static interface AnnotationIdNTypeCellTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<div class='{0}'>{1}</div>")
        public SafeHtml row(String style, SafeHtml rowContent);

        @SafeHtmlTemplates.Template("<span style='white-space:nowrap;' title='{0}'>{1}</span>")
        public SafeHtml textDetail(String title, SafeHtml fragments);
    }
    private static final AnnotationIdNTypeCellTemplates TEMPLATES = GWT.create(AnnotationIdNTypeCellTemplates.class);
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private AnnotatedTextHandler annotatedText;
    private final String separator;

    public AnnotationIdNTypeCell(AnnotatedTextHandler annotatedText, String separator, String... consumedEvents) {
        super(consumedEvents);
        this.annotatedText = annotatedText;
        this.separator = separator;
    }

    public void setAnnotatedText(AnnotatedTextHandler annotatedText) {
        this.annotatedText = annotatedText;
    }

    @Override
    public void render(Cell.Context context, Annotation annotation, SafeHtmlBuilder sb) {
        if (annotation != null) {
            SafeHtmlBuilder rowContent = new SafeHtmlBuilder();
            rowContent.append(CombinedAnnotationCell.getKindImage(annotation.getAnnotationKind()));
            rowContent.appendHtmlConstant("&nbsp;");
            AnnotationTable.renderId(annotatedText, annotation, rowContent);
            rowContent.appendHtmlConstant(" ");
            if (annotatedText != null) {
                if (AnnotationKind.TEXT.equals(annotation.getAnnotationKind())) {
                    rowContent.append(PropertiesTree.TREETEMPLATES.spanChangeType(AnnotationSchemaCell.renderType(annotation.getAnnotationType())));
                    if (separator == null) {
                        rowContent.appendHtmlConstant(" ");
                    } else {
                        rowContent.appendHtmlConstant(separator);
                    }
                    rowContent.appendHtmlConstant("\"");
                    CombinedAnnotationCell.renderDetail(annotatedText, annotation, true, rowContent);
                    rowContent.appendHtmlConstant("\"");
                } else {
                    rowContent.append(PropertiesTree.TREETEMPLATES.spanChangeType(AnnotationSchemaCell.renderType(annotation.getAnnotationType())));
                }
            } else {
                rowContent.append(PropertiesTree.TREETEMPLATES.spanChangeType(AnnotationSchemaCell.renderType(annotation.getAnnotationType())));
            }
            sb.append(TEMPLATES.row(StanEditorResources.INSTANCE.css().cwRow(), rowContent.toSafeHtml()));
        }
    }
}
