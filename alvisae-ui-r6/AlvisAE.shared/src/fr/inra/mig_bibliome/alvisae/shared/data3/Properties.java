/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.List;
import java.util.Set;

/**
 * Multi-valued property
 * @author fpapazian
 */
public interface Properties {

    /**
     * Return the property keys defined.
     * The returned value is read-only.
     * Will not return null;
     * @return
     */
    Set<String> getKeys();

    /**
     * Return either the specified key is defined.
     * @param key
     * @return
     * @throws NullPointerException if key is null
     */
    boolean hasKey(String key);

    /**
     * Return the values for the specified property keys.
     * Return null if the specified key is not defined.
     * The returned value is read-only.
     * The returned list will not be empty.
     * @param key
     * @return
     * @throws NullPointerException if key is null
     */
    List<String> getValues(String key);

    /**
     * Add the specified value to the property with the specified key.
     * @param key
     * @param value
     * @throws NullPointerException if either key or value is null
     */
    void addValue(String key, String value);

    /**
     * Remove all values for the specified key.
     * The specified key will not be defined after the call to this method.
     * @param key
     * @throws NullPointerException if key is null
     */
    void removeKey(String key);

    /**
     * Remove the specified value from the specified key.
     * If the specified value was the unique value for the specified key, then the key will not be defined.
     * @param key
     * @param value
     * @throws NullPointerException if either key or value is null
     */
    void removeValue(String key, String value);

    /**
     * Remove the value at the specified index.
     * If the specified value was the unique value for the specified index, then the key will not be defined.
     * @param key
     * @param index
     * @throws NullPointerException if either key or value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void removeValue(String key, int index);

    /**
     * Replace the value at the specified index
     * @param key
     * @param index
     * @param value
     * @throws NullPointerException if either key or value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void replaceValue(String key, int index, String alue);

    /**
     * Replace the current properties values by the ones specified
     * @param properties
     */
    void replaceAll(Properties properties);

}
