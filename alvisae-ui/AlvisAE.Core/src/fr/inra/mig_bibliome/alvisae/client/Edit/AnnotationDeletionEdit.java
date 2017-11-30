/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;

/**
 *
 * @author fpapazian
 */
public abstract class AnnotationDeletionEdit extends AnnotationEdit {
       
    public AnnotationDeletionEdit(AnnotatedTextHandler annotatedDoc) {
        super(annotatedDoc);
    } 

    /**
     * 
     * @return a loose copy of the deleted Annotation
     */
    public abstract Annotation getDeletedAnnotation();
    
}
