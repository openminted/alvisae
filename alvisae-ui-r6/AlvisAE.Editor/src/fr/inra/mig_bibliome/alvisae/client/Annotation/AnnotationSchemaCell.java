/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 *
 * @author fpapazian
 */
public abstract class AnnotationSchemaCell extends AbstractCell<String> {

    public static interface AnnotationSchemaCellTemplates extends SafeHtmlTemplates {

        @Template("<span class='{0}' style='width:2em;'>&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;<span>{0}</span>")
        public SafeHtml classedSpan(String className);

        @Template("<span class='{0}' title='{0}' style='width:2em;'>&nbsp;&nbsp;&nbsp;&nbsp;</span>")
        public SafeHtml classedUnNamedSpan(String className);

        @Template("<span class='{0}'>&nbsp;{0}&nbsp;</span>")
        public SafeHtml classedNamedSpan(String className);
    }
    private static final AnnotationSchemaCellTemplates TEMPLATES = GWT.create(AnnotationSchemaCellTemplates.class);

    public static void renderType(String annotationType, SafeHtmlBuilder sb) {
        sb.append(TEMPLATES.classedSpan(annotationType));
    }

    public static void renderUnNamedType(String annotationType, SafeHtmlBuilder sb) {
        sb.append(TEMPLATES.classedUnNamedSpan(annotationType));
    }

    public static SafeHtml renderType(String annotationType) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        renderType(annotationType, sb);
        return sb.toSafeHtml();
    }

    public static SafeHtml renderUnNamedType(String annotationType) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        renderUnNamedType(annotationType, sb);
        return sb.toSafeHtml();
    }

    public static void renderNamedType(String annotationType, SafeHtmlBuilder sb) {
        sb.append(TEMPLATES.classedNamedSpan(annotationType));
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    
    @Override
    public abstract void render(Context context, String annotationType, SafeHtmlBuilder sb);
}
