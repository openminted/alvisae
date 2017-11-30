/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.RelationDefinition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class RelationDefinitionImpl extends JavaScriptObject implements RelationDefinition {

    protected RelationDefinitionImpl() {
    }

    private final JsArray<? extends JavaScriptObject> asArray() {
        return this.cast();
    }

    @Override
    public final List<String> getSupportedRoles() {
        List<String> result = null;
        JsArray<? extends JavaScriptObject> allRolesDef = asArray();
        if (allRolesDef != null) {
            result = new ArrayList<String>();
            for (int i = 0, n = allRolesDef.length(); i < n; ++i) {
                //@SuppressWarnings("unchecked")
                JavaScriptObject value = allRolesDef.get(i);
                JSONObject jsRoleDef = new JSONObject(value);
                for (String r : jsRoleDef.keySet()) {
                    result.add(r);
                }
            }
            result = Collections.unmodifiableList(result);
        }
        return result;
    }

    @Override
    public final List<String> getArgumentTypes(String role) {
        List<String> result = null;
        JsArrayString array = getArgumentTypesForRole(role);
        if (array.length() > 0) {
            result = new ArrayList<String>();
            for (int i = 0, n = array.length(); i < n; ++i) {
                result.add(array.get(i));
            }
            result = Collections.unmodifiableList(result);
        }
        return result;
    }

    private final JsArrayString getArgumentTypesForRole(String role) {
        JsArrayString result = null;
        JsArray<? extends JavaScriptObject> allRolesDef = asArray();
        if (allRolesDef != null) {
            for (int i = 0, n = allRolesDef.length(); i < n; ++i) {
                //@SuppressWarnings("unchecked")
                JavaScriptObject value = allRolesDef.get(i);
                JSONObject jsRoleDef = new JSONObject(value);
                for (String r : jsRoleDef.keySet()) {
                    if (role.equals(r)) {
                        result = (JsArrayString) jsRoleDef.get(r).isArray().getJavaScriptObject();
                        break;
                    }
                }
            }
        }
        return result;
    }
}
