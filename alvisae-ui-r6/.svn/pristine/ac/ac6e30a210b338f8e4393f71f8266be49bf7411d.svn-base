/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import fr.inra.mig_bibliome.alvisae.client.data3.validation.ConsolidationBlockImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.validation.ConsolidableAnnotationImpl;


/**
 *
 * @author fpapazian
 */
public class CDWXS_ConsoBlocks_ResponseImpl extends JavaScriptObject {

    /**
     * @throws IllegalArgumentException if invalid or not expected JSON
     */
    public final static CDWXS_ConsoBlocks_ResponseImpl createFromJSON(String jsonStr) {
        CDWXS_ConsoBlocks_ResponseImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    protected CDWXS_ConsoBlocks_ResponseImpl() {
    }

    public final native JsArray<ConsolidationBlockImpl> getBlocks() /*-{ return this.blocks; }-*/;

    public final native JsArray<ConsolidableAnnotationImpl> getHigherOrderAnnotations() /*-{ return this.higherorderanns; }-*/;
}
