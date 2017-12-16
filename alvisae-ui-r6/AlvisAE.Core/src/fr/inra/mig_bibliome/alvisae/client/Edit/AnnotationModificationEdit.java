/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;

public abstract class AnnotationModificationEdit extends AnnotationEdit {
       
    public AnnotationModificationEdit(AnnotatedTextHandler annotatedDoc) {
        super(annotatedDoc);
    } 

    public abstract Annotation getAnnotation();
    
}
