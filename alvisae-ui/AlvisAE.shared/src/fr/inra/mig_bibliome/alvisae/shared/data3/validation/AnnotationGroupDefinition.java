/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

import java.util.List;


public interface AnnotationGroupDefinition {
	/**
	 * Return the expected minimum number of components for annotations of this type.
	 * @return
	 */
	int getMinComponents();
	
	/**
	 * Return the expected maximum number of components for annotations of this type.
	 * @return
	 */
	int getMaxComponents();
	
	/**
	 * Return the expected types of components of annotations of this type.
	 * Will not return null, neither an empty collection.
	 * Returned value is read-only.
	 * @return
	 */
	List<String> getComponentsTypes();
	
	/**
	 * Return either all components should have the same type.
	 * @return
	 */
	boolean isHomogeneous();
}
