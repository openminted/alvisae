/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;


/**
 *
 * @author fpapazian
 */
@Deprecated
public class AnnotationTypeDropDownList extends CellDropDownList<String> {
    


    public AnnotationTypeDropDownList() {
        super(new AnnotationSchemaCell() {

            @Override
            public void render(Context context, String annotationType, SafeHtmlBuilder sb) {
                renderType(annotationType, sb);
            }
        });
        
    }

}