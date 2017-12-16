/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

/**
 * Reference recorded during the adjudication process to keep track of source Annotations.
 * Held by Annotations derived from others Annotations or by Annotation Set for rejected source Annotations
 * @author fpapazian
 */
public interface AnnotationBackReference extends AnnotationReference {

    /** 
     * @return the status that was used during adjudication for this AnnotationReference
     */
    public ConsolidationStatus getConsolidationStatus();
}
