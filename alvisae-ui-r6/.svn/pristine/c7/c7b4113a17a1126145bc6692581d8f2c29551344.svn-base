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
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.UserAuthorizations;
import java.util.AbstractList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class UserAuthorizationsImpl extends JavaScriptObject implements UserAuthorizations {

    static class JsArrayIntegerDecorator extends AbstractList<Integer> {

        private final JsArrayInteger jsArray;

        public JsArrayIntegerDecorator(JsArrayInteger jsArray) {
            super();
            this.jsArray = jsArray;
        }

        @Override
        public Integer get(int index) {
            return jsArray.get(index);
        }

        @Override
        public int size() {
            return jsArray.length();
        }
    }

    /**
     * @throws IllegalArgumentException if invalid or not expected JSON
     */
    public final static UserAuthorizationsImpl createFromJSON(String jsonStr) {
        UserAuthorizationsImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    protected UserAuthorizationsImpl() {
    }

    public final String getJSON() {
        return new JSONObject(this).toString();
    }

    private final native JsArrayInteger _getGlobalAuths() /*-{ return this.global; }-*/;
    
    private final native JsArrayInteger _resetGlobalAuths() /*-{ this.global = new Array(); return this.global; }-*/;

    private final native JsArrayInteger _getAuthsByCampaign(int campaignId) /*-{ return this.bycampaign[campaignId]; }-*/;

    private final native JsArrayInteger _resetCampaignAuths(int campaignId) /*-{ this.bycampaign[campaignId] = new Array(); return this.bycampaign[campaignId]; }-*/;

    private final native JavaScriptObject _getAuthsByCampaigns() /*-{ return this.bycampaign; }-*/;

    private final native void _removeKey(String key) /*-{ if (this.hasOwnProperty(key)) { delete this[key]; } }-*/;
    
    
    @Override
    public final Set<Integer> getGlobalAuths() {
        return new HashSet<Integer>(new JsArrayIntegerDecorator(_getGlobalAuths()));
    }

    @Override
    public final Set<Integer> getAuthsByCampaign(int campaignId) {
        return new HashSet<Integer>(new JsArrayIntegerDecorator(_getAuthsByCampaign(campaignId)));
    }

    @Override
    public final Set<Integer> getCampaigns() {
        LinkedHashSet<Integer> campaignIds = new LinkedHashSet<Integer>();
        Set<String> k = new JSONObject(_getAuthsByCampaigns()).keySet();
        for (String c : new JSONObject(_getAuthsByCampaigns()).keySet()) {
            campaignIds.add(Integer.valueOf(c));
        }
        return campaignIds;
    }
    
    public final native void removeCampaignAuthInfos(int campaignId) /*-{ if (this.bycampaign.hasOwnProperty(campaignId)) { delete this.bycampaign[campaignId]; } }-*/;

    public final void setGlobalAuths(Set<Integer> newAuths) {
        JsArrayInteger auths = _resetGlobalAuths();
        for (int auth_id : newAuths) {
            auths.push(auth_id);
        }
    }

    public final void setAuthsForCampaign(int campaignId, Set<Integer> newAuths) {
        JsArrayInteger auths = _resetCampaignAuths(campaignId);
        for (int auth_id : newAuths) {
            auths.push(auth_id);
        }
    }
}
