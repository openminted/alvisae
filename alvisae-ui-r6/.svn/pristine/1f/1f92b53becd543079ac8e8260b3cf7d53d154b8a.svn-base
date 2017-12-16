/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public interface ConsolidableAnnotation {

    public AnnotationKind getAnnotationKind();

    //Reference of this ConsolidableAnnotation
    public AnnotationReference getAnnotationRef();

    public int getAdjudicationLevel();

    //Ids of referenced Annotation belonging to the same Annotation Set as this ConsolidableAnnotation
    public List<String> getReferencedAnnotationIds();
}
