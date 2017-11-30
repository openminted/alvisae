/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetInfo;

/**
 *
 * @author fpapazian
 */
public class AnnotationSetDescriptionCell extends AbstractCell<AnnotationSetInfo> {

    public static interface AnnotationSetCellTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span title='{0}'>{1}&nbsp;{2}</span>")
        public SafeHtml outdatedAnnSetDescription(String title, SafeHtml icon, String description);

        @SafeHtmlTemplates.Template("<span>{0}</span>")
        public SafeHtml annSetDescription(String description);
    }
    public static final AnnotationSetCellTemplates TEMPLATES = GWT.create(AnnotationSetCellTemplates.class);
    public static SafeHtml exclamationIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.ExclamationIcon()).getHTML());

    public static void render(AnnotationSetInfo value, SafeHtmlBuilder sb) {
        if (value.isHead()) {
            sb.append(TEMPLATES.annSetDescription(value.getDescription()));
        } else {
            //FIXME Not I18N
            sb.append(TEMPLATES.outdatedAnnSetDescription("this AnnotationSet is outdated!", exclamationIcon, value.getDescription()));
        }
    }

    @Override
    public void render(Cell.Context context, AnnotationSetInfo value, SafeHtmlBuilder sb) {
        render(value, sb);
    }
}
