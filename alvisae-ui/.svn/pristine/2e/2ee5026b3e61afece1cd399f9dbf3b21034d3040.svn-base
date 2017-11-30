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
import java.util.List;

/**
 * Specialized Edit corresponding to the modification of several values of a property of an Annotation.
 * @author fpapazian
 */
public class AnnotationMultipleValuesPropertyEdit extends AnnotationPropertyEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final List<String> oldValues;
    private final List<String> newValues;

    public AnnotationMultipleValuesPropertyEdit(AnnotatedTextHandler document, Annotation annotation, String key, List<String> oldValues, List<String> newValues) {
        super(document, annotation, key);
        this.oldValues = oldValues;
        this.newValues = newValues;
    }

    @Override
    protected void undoIt() {
        getMapper().replaceProperty(getAnnotation(), getKey(), oldValues);
    }

    @Override
    protected boolean doIt() {
        return getMapper().replaceProperty(getAnnotation(), getKey(), newValues);
    }

    @Override
    public String getPresentationName() {
        return messages.annotationPropertyEditPresentationName(getAnnotation().getId(), getKey());
    }

    @Override
    public String getUndoPresentationName() {
        StringBuilder values = new StringBuilder();
        if (oldValues != null) {
            for (String v : oldValues) {
                values.append(v).append(" | ");
            }
        }
        return messages.undoAnnotationPropertyEditPresentationName(getAnnotation().getId(), getKey() + "='" + values.toString()+"'");
    }

    @Override
    public String getRedoPresentationName() {
        StringBuilder values = new StringBuilder();
        if (newValues != null) {
            for (String v : newValues) {
                values.append(v).append(" | ");
            }
        }
        return messages.redoAnnotationPropertyEditPresentationName(getAnnotation().getId(), getKey() + "='" + values.toString()+"'");
    }
}
