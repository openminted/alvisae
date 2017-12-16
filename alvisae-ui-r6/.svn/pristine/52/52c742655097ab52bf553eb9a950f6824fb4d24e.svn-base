/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import fr.inra.mig_bibliome.alvisae.shared.data3.UserMeBasicResponse;

/**
 *
 * @author fpapazian
 */
public class Basic_UserMe_Response extends JavaScriptObject implements UserMeBasicResponse {

    /**
     * @throws IllegalArgumentException if invalid or not expected JSON 
     */
    public final static Basic_UserMe_Response createFromJSON(String jsonStr) {
        Basic_UserMe_Response result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    @Override
    public final native int getUserId() /*-{ return this.id; }-*/;

    protected Basic_UserMe_Response() {
    }

}
