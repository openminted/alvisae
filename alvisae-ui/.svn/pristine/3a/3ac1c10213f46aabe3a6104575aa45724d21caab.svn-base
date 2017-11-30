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
public class SemClassListImpl extends JsArray<SemClassImpl> {

    public final static SemClassListImpl createFromJSON(String jsonStr) {
        SemClassListImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    protected SemClassListImpl() {
    }
}
