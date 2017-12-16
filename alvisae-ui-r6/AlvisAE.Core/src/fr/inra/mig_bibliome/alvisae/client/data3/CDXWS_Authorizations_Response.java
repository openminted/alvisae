/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;

/**
 *
 * @author fpapazian
 */
public class CDXWS_Authorizations_Response extends JavaScriptObject {

    /**
     * @throws IllegalArgumentException if invalid or not expected JSON 
     */
    public final static CDXWS_Authorizations_Response createFromJSON(String jsonStr) {
        CDXWS_Authorizations_Response result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }
        
    protected CDXWS_Authorizations_Response() {
    }

    public final native AuthorizationListImpl getAuthorizations() /*-{ return this.authorizations; }-*/;
}
