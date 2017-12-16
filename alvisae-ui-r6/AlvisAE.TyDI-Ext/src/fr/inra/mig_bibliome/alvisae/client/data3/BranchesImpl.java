/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import fr.inra.mig_bibliome.alvisae.shared.data3.Branches;

/**
 *
 * @author fpapazian
 */
public class BranchesImpl extends JavaScriptObject implements Branches {

    public final static BranchesImpl createFromJSON(String jsonStr) {
        BranchesImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }
        
    protected BranchesImpl() {
    }

    @Override
    public final native SemClassImpl getFromSemClass() /*-{ return this.fromGroup; }-*/;

    @Override
    public final native SemClassImpl getToSemClass() /*-{ return this.toGroup; }-*/;

    @Override
    public final native JsArray<PathImpl> getPaths() /*-{ return this.paths; }-*/;

}
