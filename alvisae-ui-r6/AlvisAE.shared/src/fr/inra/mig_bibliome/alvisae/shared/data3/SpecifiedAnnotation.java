/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

/**
 * Strong reference to an Annotation (@see AnnotationReference)
 */
public interface SpecifiedAnnotation {

    Annotation getAnnotation();

    AnnotationSet getAnnotationSet();
}
