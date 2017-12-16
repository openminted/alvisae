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

/**
 *
 * @author fpapazian
 */
public class CDXWS_UserCampaignDocuments_Response extends JavaScriptObject {

    /**
     * @throws IllegalArgumentException if invalid or not expected JSON 
     */
    public final static CDXWS_UserCampaignDocuments_Response createFromJSON(String jsonStr) {
        CDXWS_UserCampaignDocuments_Response result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    public final native DocumentInfoListImpl getDocumentInfoList() /*-{ return this.documents; }-*/;

    protected CDXWS_UserCampaignDocuments_Response() {
    }
}
