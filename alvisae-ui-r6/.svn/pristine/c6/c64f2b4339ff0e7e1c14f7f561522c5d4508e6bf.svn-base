/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;

/**
 *
 * @author fpapazian
 */
public class AnnotationListImpl extends JsArray<AnnotationImpl> {

    /**
     * @throws IllegalArgumentException if invalid or not expected JSON 
     */
    public final static AnnotationListImpl createFromJSON(String jsonStr) {
        AnnotationListImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    protected AnnotationListImpl() {
    }

}
