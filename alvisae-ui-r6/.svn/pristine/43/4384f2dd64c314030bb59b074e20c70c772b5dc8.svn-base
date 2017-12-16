/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationGroupDefinition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class AnnotationGroupDefinitionImpl extends JavaScriptObject implements AnnotationGroupDefinition {

    protected AnnotationGroupDefinitionImpl() {
    }

    @Override
    public final native int getMinComponents() /*-{ return this.minComp; }-*/;

    @Override
    public final native int getMaxComponents() /*-{ return this.maxComp; }-*/;

    @Override
    public final List<String> getComponentsTypes() {
        ArrayList<String> result = new ArrayList<String>();
        JsArrayString array = _getComponentsTypes();
        for (int i = 0, n = array.length(); i < n; ++i) {
            result.add(array.get(i));
        }
        return result;
    }

    private final native JsArrayString _getComponentsTypes() /*-{ return this.compType; }-*/;

    @Override
    public final native boolean isHomogeneous() /*-{ return this.homogeneous; }-*/;
}
