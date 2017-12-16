/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClassChange;
import fr.inra.mig_bibliome.alvisae.shared.data3.StructTermChanges;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class StructTermChangesImpl extends JavaScriptObject implements StructTermChanges {

    public final static StructTermChangesImpl createFromJSON(String jsonStr) {
        StructTermChangesImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    protected StructTermChangesImpl() {
    }

    @Override
    public final native int getFromVersion() /*-{ return this.fromVersNum; }-*/;

    @Override
    public final native int getToVersion() /*-{ return this.toVersNum; }-*/;

    private final native JsArray<SemClassChangeImpl> _getChanges() /*-{ return this.changes; }-*/;

    @Override
    public final List<SemClassChange> getChanges() {
        return new ArrayList<SemClassChange>(new JsArrayDecorator<SemClassChangeImpl>(_getChanges()));
    }
}
