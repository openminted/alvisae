/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ExtendedAnnotatedText {
    

    /**
     * Return the annotation with the specified identifier.
     * Return null if there is no annotation with the specified identifier.
     * @param id
     * @return
     * @throws NullPointerException if id is null
     */
    Annotation getAnnotation(String id);

    /**
     * 
     * @param annotationId
     * @return the list of annotations referenced by the specified annotation id
     * @throws NullPointerException if id is null
     * @throws IllegalArgumentException if the specified Annotation id does not belong to this text 
     */
    List<Annotation> getReferencesToAnnotation(String annotationId);

    /**
     * 
     * @param annotationId the annotation that is checked for being referenced
     * @param referentAnnId the annotation that potentially reference the above annotation
     * @return true if the checked annotation is equal to the specified referent,
     *         or if the checked annotation is referenced by the referent directly or indirectly,
     *         false otherwise.
     * @throws IllegalArgumentException if the specified Annotation id does not belong to this text 
     */
    boolean isEqualToOrReferencedBy(String annotationId, String referentAnnId);
    
    /**
     * Creates a new text annotation.
     * @param id identifier of the new annotation, if null this object will generate an unique identifier
     * @param type type of the annotation
     * @param fragments fragments of the created text annotation
     * @return the created text annotation
     * @throws NullPointerException if either type or fragments is null
     * @throws IllegalArgumentException if the specified Annotation id is already in use
     */
    Annotation createTextAnnotation(String id, String type, Collection<Fragment> fragments, Properties props, List<AnnotationBackReference> backRefs);

    /**
     * Creates a new group annotation.
     * @param id identifier of the new annotation, if null this object will generate an unique identifier
     * @param type type of the annotation
     * @param components components of the created group annotation
     * @return the created group annotation
     * @throws NullPointerException if either type or components is null
     * @throws IllegalArgumentException if the specified Annotation id is already in use
     */
    Annotation createGroupAnnotation(String id, String type, Collection<AnnotationReference> components, Properties props, List<AnnotationBackReference> backRefs);

    /**
     * Creates a new relation annotation.
     * @param id identifier of the new annotation, if null this object will generate an unique identifier
     * @param type type of the annotation
     * @param arguments arguments of the created relation annotation, map keys are roles
     * @return the created relation annotation
     * @throws NullPointerException if either type or components is null
     * @throws IllegalArgumentException if the specified Annotation id is already in use
     */
    Annotation createRelationAnnotation(String id, String type, Map<String, AnnotationReference> arguments, Properties props, List<AnnotationBackReference> backRefs);

    /**
     * Remove the specified Annotation from this AnnotatedText
     * @param annotationId the Annotation to be removed from the AnnotatedText
     * @return false if the removal has not been performed due to remaining references to this annotation (in Group or Relation), true otherwise
     * @throws NullPointerException if annotationId is null
     * @throws IllegalArgumentException if the specified Annotation does not belong to this AnnotatedText
     */
    public boolean removeAnnotation(String annotationId);

    /**
     * Add fragments to an existing Annotation; in case of overlapping the added fragment will be merged with the existing ones
     * @param annotationId Id of the Annotation to be augmented
     * @param fragments the Fragments to be added
     */
    public void fragmentsAdditionToAnnotation(String annotationId, Collection<Fragment> fragments);

    /**
     * Remove fragments from an existing Annotation; The existing fragment will be reduced by the span covered by the specified fragments
     * @param annotationId Id of the Annotation to be reduced
     * @param fragments the Fragments to be substracted
     * @return false if the operation can not be performed because it would result in emptying the fragment list of the specified annotation
     */
    public boolean fragmentsSubstractionToAnnotation(String annotationId, Collection<Fragment> fragments);
    
    /**
     * Produce a new id that can be used to create a new Annotation
     * @return
     */
    public String getNewAnnotationId();    
}
