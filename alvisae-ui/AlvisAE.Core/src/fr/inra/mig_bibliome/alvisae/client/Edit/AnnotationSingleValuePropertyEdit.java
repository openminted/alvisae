/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;

/**
 * Specialized Edit corresponding to the modification of a single value of a property of an Annotation.
 * @author fpapazian
 */
public class AnnotationSingleValuePropertyEdit extends AnnotationPropertyEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final int propIndex;
    private final String oldValue;
    private final String newValue;

    public AnnotationSingleValuePropertyEdit(AnnotatedTextHandler document, Annotation annotation, String key, int propIndex, String oldValue, String newValue) {
        super(document, annotation, key);
        this.propIndex = propIndex;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    protected void undoIt() {
        getMapper().replaceProperty(getAnnotation(), getKey(), propIndex, newValue, oldValue);
    }

    @Override
    protected boolean doIt() {
        return getMapper().replaceProperty(getAnnotation(), getKey(), propIndex, oldValue, newValue);
    }

    @Override
    public String getPresentationName() {
        return messages.annotationPropertyEditPresentationName(getAnnotation().getId(), getKey());
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoAnnotationPropertyEditPresentationName(getAnnotation().getId(), getKey() + "='" + oldValue+"'");
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoAnnotationPropertyEditPresentationName(getAnnotation().getId(), getKey() + "='" + newValue+"'");
    }
}
