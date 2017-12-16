package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.TextBinding;

public interface TextBindingDefinition {
	/**
	 * Return the minimum expected number of fragments in annotations of this type.
	 * @return
	 */
	int getMinFragments();
	
	/**
	 * Return the maximum expected number of fragments in annotations of this type.
	 * @return
	 */
	int getMaxFragments();
	
	/**
	 * Return the type of annotations on which boundaries must be aligned.
	 * Return null if boundaries are not constrained.
	 * @return
	 */
	String getBoundariesReferenceType();
    
    /**
     * 
     * @return true if annotation of this type can cross the boundaries of other annotations within the same Annotation Set
     */
    boolean isCrossingAllowed();
    
}
