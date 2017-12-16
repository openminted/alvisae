/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertiesDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyDefinition;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author fpapazian
 */
public class PropertiesDefinitionImpl extends JavaScriptObject implements PropertiesDefinition {

    protected PropertiesDefinitionImpl() {
    }

    @Override
    public final native boolean isPropertyKeySupported(String key) /*-{ return this.hasOwnProperty(key); }-*/;

    @Override
    public final Collection<PropertyDefinition> getPropertyDefinitions() {
        ArrayList<PropertyDefinition> result = new ArrayList<PropertyDefinition>();
        for (String key : new JSONObject(this).keySet()) {
            result.add(_getPropertyDefinition(key));
        }
        return result;
    }

    private final native PropertyDefinition _getPropertyDefinition(String key) /*-{ return this[key]; }-*/;
}
