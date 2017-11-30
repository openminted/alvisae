/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;

/**
 *
 * @author fpapazian
 */
public class CDXWS_DocInternalIdWithExternalId_Response extends JavaScriptObject {

    /**
     * @throws IllegalArgumentException if invalid or not expected JSON 
     */
    public final static CDXWS_DocInternalIdWithExternalId_Response createFromJSON(String jsonStr) {
        CDXWS_DocInternalIdWithExternalId_Response result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    public final native int getDocumentId() /*-{ return this.doc_id; }-*/;

    public final native int getCampaignId() /*-{ return this.campaign_id; }-*/;

    public final native int getTaskId() /*-{ return this.task_id; }-*/;
    
    protected CDXWS_DocInternalIdWithExternalId_Response() {
    }
}
