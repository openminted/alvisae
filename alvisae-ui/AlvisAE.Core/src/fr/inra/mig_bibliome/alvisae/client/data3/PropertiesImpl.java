/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class PropertiesImpl extends JavaScriptObject implements Properties {

    public static final PropertiesImpl create() {
        return JavaScriptObject.createObject().cast();
    }

    protected PropertiesImpl() {
    }

    /**
     * Return the property keys defined.
     * The returned value is read-only.
     * Will not return null;
     * @return
     */
    @Override
    public final Set<String> getKeys() {
        return new JSONObject(this).keySet();
    }

    /**
     * Return either the specified key is defined.
     * @param key
     * @return
     * @throws NullPointerException if key is null
     */
    @Override
    public final native boolean hasKey(String key) /*-{ return this.hasOwnProperty(key); }-*/;

    /**
     * Return the values for the specified property keys.
     * Return null if the specified key is not defined.
     * The returned value is read-only.
     * The returned list will not be empty.
     * @param key
     * @return
     * @throws NullPointerException if key is null
     */
    @Override
    public final List<String> getValues(String key) {
        if (key == null) {
            throw new NullPointerException("key should not be null");
        }
        List<String> result = null;
        if (hasKey(key)) {
            JsArrayString array = _getPropValues(key);
            if (array != null) {
                result = new ArrayList<String>();
                for (int i = 0, n = array.length(); i < n; ++i) {
                    //@SuppressWarnings("unchecked")
                    String value = array.get(i);
                    result.add(value);
                }
                result = Collections.unmodifiableList(result);
            }
        }
        return result;
    }

    private final native JsArrayString _getPropValues(String key) /*-{ return this[key]; }-*/;

    /**
     * Add the specified value to the property with the specified key.
     * @param key
     * @param value
     * @throws NullPointerException if either key or value is null
     */
    @Override
    public final void addValue(String key, String value) {
        if (key == null) {
            throw new NullPointerException("key should not be null");
        }
        _addValue(key, value);
    }

    private final native void _addValue(String key, String value) /*-{ if (!this.hasOwnProperty(key)) { this[key] = new Array(); } this[key].push(value); }-*/;

    /**
     * Remove all values for the specified key.
     * The specified key will not be defined after the call to this method.
     * @param key
     * @throws NullPointerException if key is null
     */
    @Override
    public final void removeKey(String key) {
        if (key == null) {
            throw new NullPointerException("key should not be null");
        }
        _removeKey(key);
    }

    private final native void _removeKey(String key) /*-{ if (this.hasOwnProperty(key)) { delete this[key]; } }-*/;

    /**
     * Remove the specified value from the specified key.
     * If the specified value was the unique value for the specified key, then the key will not be defined.
     * @param key
     * @param value
     * @throws NullPointerException if either key or value is null
     */
    @Override
    public final void removeValue(String key, String value) {
        if (key == null) {
            throw new NullPointerException("key should not be null");
        } else if (value == null) {
            throw new NullPointerException("value should not be null");
        }
        _removeValue(key, value);
    }

    private final native void _removeValue(String key, String value) /*-{ if (this.hasOwnProperty(key)) { var a = this[key]; for (i in a) { if (a[i]==value) { a.splice(i,1); break; } } } }-*/;

    /**
     * Remove the value at the specified index.
     * If the specified value was the unique value for the specified index, then the key will not be defined.
     * @param key
     * @param index
     * @throws NullPointerException if either key or value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public final void removeValue(String key, int index) {
        if (key == null) {
            throw new NullPointerException("key should not be null");
        } else if (index >= _getValuesCount(key)) {
            throw new IndexOutOfBoundsException("Out of bounds; index: " + index + ", size: " + _getValuesCount(key));
        }
        _removeValue(key, index);
    }

    private final native void _removeValue(String key, int index) /*-{ if (this.hasOwnProperty(key)) { this[key].splice(index,1); } }-*/;

    private final native int _getValuesCount(String key) /*-{ if (this.hasOwnProperty(key)) { return this[key].length; } else return 0; }-*/;

    /**
     * Replace the value at the specified index
     * @param key
     * @param index
     * @param value
     * @throws NullPointerException if either key or value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public final void replaceValue(String key, int index, String value) {
        if (key == null) {
            throw new NullPointerException("key should not be null");
        } else if (value == null) {
            throw new NullPointerException("value should not be null");
        } else if (index >= _getValuesCount(key)) {
            throw new IndexOutOfBoundsException("Out of bounds; index: " + index + ", size: " + _getValuesCount(key));
        }
        _replaceValue(key, index, value);
    }

    private final native void _replaceValue(String key, int index, String value) /*-{ if (this.hasOwnProperty(key)) { var a = this[key]; a.splice(index,1, value); } }-*/;

    public final void mergeWith(Properties properties) {
        if (properties != null) {
            for (String key : properties.getKeys()) {
                for (String value : properties.getValues(key)) {
                    addValue(key, value);
                }
            }
        }
    }

    @Override
    public final void replaceAll(Properties properties) {
        for (String key : getKeys()) {
            _removeKey(key);
        }
        mergeWith(properties);
    }
}
