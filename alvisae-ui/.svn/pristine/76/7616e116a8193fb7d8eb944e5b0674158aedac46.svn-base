package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.TextBinding;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class Suggestions {
    private Suggestions() {}

    /*
     * Ici tu mets les signatures de methodes statiques pour les suggestions.
     * On verra plus tard pour l'implementation.
     */


    
    /**
     * Retrieve the possible Annotation Types when creating a new Annotation of the specified Annotation Kind
     * @param annotatedText the document being annotated
     * @param annotationKind the Kind of the new Annotation
     * <p/>
     * <i>use case : the user initiates the creation of a certain kind of Annotation</i>
     * @return the list of all possible Annotation Types for the specified Annotation Kind (null if it is forbidden to create Annotation of the specified kind)
     * <br/>the list is ordered by preference, the most preferable before (this order is likely to be conserved when displayed in the UI)
     */
    public static List<String> suggestAnnotationTypesForAnnotationKind(AnnotatedText annotatedText, AnnotationKind annotationKind) {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    /**
     * Indicates if TextBindings of an Annotation must snap to the boundaries of previously defined Annotation (of another type)
     * <p/>
     * <i>use case : the user selects an Annotation  text fragment(s) in the document text, then clicks the "create Annotation" button.</i>
     * @param annotatedText the document of the Annotation
     * @param annotationType the Annotation Type of the Annotation
     * @return the list of Annotation Type (null if no constraints)
     * <br/>the list is ordered by preference, the most preferable before (this order is likely to be conserved when displayed in the UI)
     */
    public static List<String> suggestAnnotationTypesForFragmentBoundariesSnapping(AnnotatedText annotatedText, String annotationType) {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    /**
     * Compute the most suitable Annotation Type for a new Annotation that would be created with the provided TextBinding in the specified AnnotatedText
     * <p/>
     * <i>use case : the user selects text fragment(s) in the document text, then clicks the "create Annotation" button.</i>
     * @param annotatedText the document in which the new Annotation would be created
     * @param textBinding the text binding of the new Annotation
     * @return a list of the suggested Annotation type, ordered by likelihood, the most probable first (null if no Annotation can be created with the specified TextBinding)
     */
    public static List<String> suggestNewAnnotationTypesForTextBinding(AnnotatedText annotatedText, TextBinding textBinding) {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    /**
     * Compute the most suitable Annotation Type for an existing Annotation
     * <p/>
     * <i>use case : the user starts editing an annotation and decides to change its Type.</i>
     * @param annotatedText the document of the edited Annotation
     * @param annotation the edited Annotation
     * @return a list of the suggested Annotation type, ordered by likelihood, the most probable first (null if the Annotation type could not be changed)
     */
    public static List<String> suggestAnnotationTypes(AnnotatedText annotatedText, Annotation annotation) {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    /**
     * Compute the most suitable Annotation Type for a new Annotation that would be created with the provided AnnotationGroup in the specified AnnotatedText
     * <p/>
     * <i>use case : the user selects Annotation(s) in the UI, then clicks the "create Annotation Group" button.</i>
     * @param annotatedText the document in which the new Annotation would be created
     * @param AnnotationGroup the proposed AnnotationGroup of the new Annotation
     * @return a list of the suggested Annotation type, ordered by likelihood, the most probable first (null if no Annotation can be created with the specified AnnotationGroup)
     */
    public static List<String> suggestNewAnnotationTypesForAnnotationGroup(AnnotatedText annotatedText, Collection<Annotation> annotationGroup) {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    /**
     * Compute the most suitable Annotation Type for a new Annotation that would be created with the provided annotationRelation in the specified AnnotatedText
     * <p/>
     * <i>use case : the user selects Annotation(s) in the UI, then clicks the "create Annotation Relation" button.</i>
     * @param annotatedText the document in which the new Annotation would be created
     * @param annotationRelation the proposed AnnotationRelation of the new Annotation
     * @return a list of the suggested Annotation type, ordered by likelihood, the most probable first (null if no Annotation can be created with the specified AnnotationGroup)
     */
    public static List<String> suggestNewAnnotationTypesForAnnotationRelation(AnnotatedText annotatedText, Map<String,Annotation> annotationRelation) {
        throw new UnsupportedOperationException("not implemented yet!");
    }



}
