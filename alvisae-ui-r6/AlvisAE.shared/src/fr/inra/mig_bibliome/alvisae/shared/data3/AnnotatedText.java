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
import java.util.Set;

/**
 * Main data structure used for exchange between server and client.
 * @author fpapazian
 */
public interface AnnotatedText {

    static interface AnnotationProcessor {

        /**
         * Method invoked for each annotation during a scan
         * @param annotationSet
         * @param annotation
         * @return false to stop the scan 
         */
        public boolean process(AnnotationSet annotationSet, Annotation annotation);
    }

    Document getDocument();

    List<? extends AnnotationSet> getAnnotationSetList();

    List<? extends AnnotationSetInfo> getAnnotationSetInfoList();

    AnnotationSchemaDefinition getAnnotationSchema();

    /**
     * 
     * @return the Task definition of the edited Task
     */
    TaskDefinition getEditedTask();
    
    /**
     * 
     * @return Set containing the identifiers of (referenced) outdated AnnotationSets  
     */    
    Set<Integer> getOutdatedAnnotationSetIds();
    
    /**
     *  Perform an exhaustive scan of annotations contained in this AnnotatedText, and invoke the specified AnnotationProcessor for each annotation
     */
    void scanAnnotations(AnnotationProcessor processor);

    /**
     *
     * @return the JSON string representation of this AnnotatedText
     */
    String getJSON();

    /**
     * Return all annotations for this text.
     * The returned value is read-only.
     * Will not return null.
     * @return
     */
    Collection<Annotation> getAnnotations();

    /**
     * Return all annotation of the specified type.
     * The returned value is read-only.
     * Will not return null.
     * @param type
     * @return
     * @throws NullPointerException if type is null
     */
    Collection<Annotation> getAnnotations(String type);

    /**
     *
     * @param kind
     * @return all annotation of the specified kind
     * @throws NullPointerException if kind is null
     */
    List<Annotation> getAnnotations(AnnotationKind kind);

    /**
     * Return the annotation with the specified identifier.
     * Return null if there is no annotation with the specified identifier.
     * @param AnnotationId
     * @return
     * @throws NullPointerException if AnnotationId is null
     */
    Annotation getAnnotation(String AnnotationId);

}