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
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClassNTerms;

/**
 *
 * @author fpapazian
 */
public class SemClassNTermsImpl extends SemClassBasicImpl implements SemClassNTerms {

    public final static SemClassNTermsImpl createFromJSON(String jsonStr) {
        SemClassNTermsImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }
    
    protected SemClassNTermsImpl() {
    }

    @Override
    public final native JsArray<TermMemberImpl> getTermMembers() /*-{ return this.termMembers; }-*/;
}
