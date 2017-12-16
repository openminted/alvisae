/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.view.client.ProvidesKey;
import fr.inra.mig_bibliome.alvisae.shared.data3.CampaignAnnotators;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class CampaignAnnotatorsImpl extends JavaScriptObject implements CampaignAnnotators {

    public static final ProvidesKey<CampaignAnnotatorsImpl> KEY_PROVIDER = new ProvidesKey<CampaignAnnotatorsImpl>() {

        @Override
        public Object getKey(CampaignAnnotatorsImpl item) {
            return item == null ? null : item.getCampaignId();
        }
    };

    protected CampaignAnnotatorsImpl() {
    }
    
    @Override
    public final native int getCampaignId() /*-{ return this.campaign_id; }-*/;

    public final native JsArrayInteger _getAnnotatorIds()  /*-{ return this.user_ids; }-*/;

    @Override
    public final Set<Integer> getAnnotatorIds() {
        HashSet<Integer> result = new HashSet<Integer>();
        JsArrayInteger ids = _getAnnotatorIds();
        for (int row = 0; row < ids.length(); row++) {
            result.add(ids.get(row));
        }
        return result;
    }
}
