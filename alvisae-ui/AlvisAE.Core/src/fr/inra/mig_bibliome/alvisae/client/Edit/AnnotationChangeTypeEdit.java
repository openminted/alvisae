/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;

/**
 * Specialized Edit corresponding to the modification of an Annotation.
 * @author fpapazian
 */
public class AnnotationChangeTypeEdit extends AnnotationModificationEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final String oldAnnotationId;
    private final String oldAnnotationType;
    private final String newAnnotationType;
    private Annotation newAnnotation;

    public AnnotationChangeTypeEdit(AnnotatedTextHandler annotatedDoc, Annotation annotation, String newAnnotationType) {
        super(annotatedDoc);
        this.newAnnotationType = newAnnotationType;
        oldAnnotationId = annotation.getId();
        oldAnnotationType = annotation.getAnnotationType();
    }

    @Override
    protected void undoIt() {
        newAnnotation = getMapper().modifyAnnotationType(oldAnnotationId, oldAnnotationType);
    }

    @Override
    protected boolean doIt() {
        newAnnotation = getMapper().modifyAnnotationType(oldAnnotationId, newAnnotationType);
        return true;
    }

    @Override
    public String getPresentationName() {
        return messages.annotationTypeEditionPresentationName(oldAnnotationId, oldAnnotationType);
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoAnnotationTypeEditionPresentationName(oldAnnotationId, oldAnnotationType);
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoAnnotationTypeEditionPresentationName(oldAnnotationId, newAnnotationType);
    }


    @Override
    public Annotation getAnnotation() {
        return newAnnotation;
    }
}
