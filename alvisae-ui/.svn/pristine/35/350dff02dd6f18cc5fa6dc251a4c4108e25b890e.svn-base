/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.List;

/**
 *
 * @author fpapazian
 */
public interface AnnotationSet extends AnnotationSetCore {

    List<? extends Annotation> getTextAnnotations();

    List<? extends Annotation> getGroups();

    List<? extends Annotation> getRelations();

    /**
     *
     * @return in case of this AnnotationSet is the result of a Review, list of
     * all remaining Annotation References coming from reviewed AnnotationSets
     * (i.e. the ones that were not consolidated in derived Annotation)
     */
    SourceAnnotations getUnmatchedSourceAnnotations();

    SourceAnnotations setUnmatchedSourceAnnotations(List<AnnotationBackReference> backRefs);
    
    /**
     *
     * @return the JSON string representation of this AnnotationSet
     */
    String getJSON();

    /**
     *
     * @return the CSV string representation of this AnnotationSet
     */
    String getCSV();
}
