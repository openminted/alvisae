/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.Relation;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author fpapazian
 */
public class RelationImpl extends JavaScriptObject implements Relation {

    public static final RelationImpl create() {
        return JavaScriptObject.createObject().cast();
    }

    protected RelationImpl() {
    }

    @Override
    public final Collection<String> getRoles() {
        return new JSONObject(this).keySet();
    }

    @Override
    public final Map<String, AnnotationReference> getRolesArguments() {
        Map<String, AnnotationReference> result = new HashMap<String, AnnotationReference>();
        for (String role : getRoles()) {
            result.put(role, getArgumentRef(role));
        }
        return result;
    }

    public final void setRolesArguments(Map<String, AnnotationReference> rolesArguments) {
        for (Entry<String, AnnotationReference> entry : rolesArguments.entrySet()) {
            if (entry.getKey() == null) {
                throw new NullPointerException("role should not be null");
            } else if (entry.getValue() == null) {
                throw new NullPointerException("argument should not be null");
            }
        }
        for (String role : getRoles()) {
            removeArgument(role);
        }
        for (Entry<String, AnnotationReference> entry : rolesArguments.entrySet()) {
            setArgument(entry.getKey(), entry.getValue(), true);
        }
    }

    @Override
    public final AnnotationReference getArgumentRef(String role) {
        if (role == null) {
            throw new NullPointerException("role should not be null");
        }
        return _getArgumentRef(role);
    }

    private final native AnnotationReferenceImpl _getArgumentRef(String role) /*-{ return this[role]; }-*/;

    @Override
    public final boolean setArgument(String role, AnnotationReference argument, boolean overwrite) {
        if (role == null) {
            throw new NullPointerException("role should not be null");
        } else if (argument == null) {
            throw new NullPointerException("argument should not be null");
        }
        boolean result = !_hasRole(role) || overwrite;
        if (result) {
            
            _setArgument(role, AnnotationReferenceImpl.create(argument));
        }
        return result;
    }

    private final native boolean _hasRole(String role) /*-{ return this.hasOwnProperty(role); }-*/;

    private final native void _setArgument(String role, AnnotationReferenceImpl argumentRef) /*-{ this[role]=argumentRef; }-*/;

    @Override
    public final Collection<String> getRoles(AnnotationReference argument) {
        if (argument == null) {
            throw new NullPointerException("argument should not be null");
        }
        List<String> result = null;
        String argumentId = argument.getAnnotationId();
        for (String role : getRoles()) {
            if (argumentId.equals(_getArgumentRef(role).getAnnotationId())) {
                result.add(role);
            }
        }
        return result;
    }

    @Override
    public final native void removeArgument(String role) /*-{ if (this.hasOwnProperty(role)) { delete this[role]; } }-*/;

    private final static String INVALIDFIELD_PREFIX = "Invalid field ";

    /**
     * Check that the Relation parsed from a JSON string conforms to the
     * expected structure
     *
     * @throws IllegalArgumentException
     */
    public final void checkStructure() {
        for (String role : getRoles()) {
            if (role == null || role.trim().isEmpty()) {
                throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "-> Argument.Role should not be null");
            }
            try {
                AnnotationReferenceImpl annRef = _getArgumentRef(role);
                if (annRef == null) {
                    throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "-> Argument.AnnotationReference should not be null");
                }
                annRef.checkStructure();
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "in Relation role=" + role, ex);
            }
        }
    }
}
