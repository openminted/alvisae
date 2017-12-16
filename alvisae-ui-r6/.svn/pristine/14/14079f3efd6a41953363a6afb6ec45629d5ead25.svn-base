package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

import java.util.List;

import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;

public interface PropertyDefinition {
	/**
	 * Return the key of the property defined by this object.
	 * Will not return null.
	 * @return
	 */
	String getKey();
	
	/**
	 * Return either the property is mandatory.
	 * @return
	 */
	boolean isMandatory();
	
	/**
	 * Return the minimum expected number of values for this property.
	 * @return
	 */
	int getMinValues();
	
	/**
	 * Return the maximum expected number of values for this property.
	 * @return
	 */
	int getMaxValues();
	
	/**
	 * Return the type definition of the property values.
	 * @return
	 */
	PropertyType getValuesType();
}
