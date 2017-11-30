/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyType;

/**
 *
 * @author fpapazian
 */
public class PropertyDefinitionImpl extends JavaScriptObject implements PropertyDefinition {

    protected PropertyDefinitionImpl() {
    }

    @Override
    public final native String getKey() /*-{ return this.key; }-*/;

    @Override
    public final native boolean isMandatory() /*-{ return this.mandatory; }-*/;

    @Override
    public final native int getMinValues() /*-{ return this.minVal; }-*/;

    @Override
    public final native int getMaxValues() /*-{ return this.maxVal; }-*/;

    @Override
    public final native PropertyType getValuesType() /*-{ return this.valType; }-*/;
}
