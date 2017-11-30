/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.TextBindingDefinition;

/**
 *
 * @author fpapazian
 */
public class TextBindingDefinitionImpl extends JavaScriptObject implements TextBindingDefinition {

    protected TextBindingDefinitionImpl() {
    }

    @Override
    public final native int getMinFragments() /*-{ return this.minFrag; }-*/;

    @Override
    public final native int getMaxFragments() /*-{ return this.maxFrag; }-*/;

    private final native String _getBoundariesReferenceType() /*-{ return this.boundRef; }-*/;
    
    @Override
    public final String getBoundariesReferenceType() {
        String boundRefType = _getBoundariesReferenceType();
        if (boundRefType!=null && boundRefType.trim().isEmpty()) {
            boundRefType = null;
        }
        return boundRefType;
    }

    @Override
    public final native boolean isCrossingAllowed() /*-{ return this.crossingAllowed; }-*/;
}
