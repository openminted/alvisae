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
import com.google.gwt.json.client.JSONObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClass;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClassTreeLevel;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fpapazian
 */
public class SemClassTreeLevelImpl extends SemClassImpl implements SemClassTreeLevel {

    public final static SemClassTreeLevelImpl createFromJSON(String jsonStr) {
        SemClassTreeLevelImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    protected SemClassTreeLevelImpl() {
    }

    @Override
    public final native int getAddedTermId() /*-{ if (this.hasOwnProperty('addedTermId')) { return this.addedTermId; } else { return -1; } }-*/;
    
    private final native JavaScriptObject _getHypoGroupsDetails() /*-{ return this.hypoGroupsDetails; }-*/;

    private final native SemClassImpl _getHypoGroupDetail(String hypoId) /*-{ return this.hypoGroupsDetails[hypoId]; }-*/;
    
    @Override
    public final Map<Integer, SemClass> getHypoGroupsDetails() {
        Map<Integer, SemClass> result = new HashMap<Integer, SemClass>();
        JSONObject details = new JSONObject(_getHypoGroupsDetails());

        for (String hypoId : details.keySet()) {
            result.put(Integer.valueOf(hypoId), _getHypoGroupDetail(hypoId));
        }
        return result;
    }
}
