/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.SourceAnnotations;
import java.util.List;

/**
 * Specialized Edit corresponding to the removal of an Annotation.
 *
 * @author fpapazian
 */
public class TextAnnotationRemovalEdit extends AnnotationDeletionEdit implements PreventableEdit, TextAnnotationCoverageEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final String annotationId;
    private Annotation oldAnnotation;

    public TextAnnotationRemovalEdit(AnnotatedTextHandler annotatedDoc, String annotationId) {
        super(annotatedDoc);

        this.annotationId = annotationId;
        Annotation annotation = getMapper().getAnnotation(annotationId);
        oldAnnotation = AnnotationImpl.create((AnnotationImpl) annotation);
    }

    @Override
    protected void undoIt() {
        SourceAnnotations sourceAnns = oldAnnotation.getSourceAnnotations();
        Annotation newAnnotation = getMapper().addAnnotation(annotationId, oldAnnotation.getAnnotationType(), oldAnnotation.getTextBinding().getFragments(), oldAnnotation.getProperties(), sourceAnns == null ? null : sourceAnns.getAnnotationBackReferences());
        newAnnotation.getProperties().replaceAll(oldAnnotation.getProperties());

        Annotation annotation = getMapper().getAnnotation(annotationId);
        getInjector().getMainEventBus().fireEvent(new TextAnnotationSelectionChangedEvent(getAnnotatedTextHandler(), annotation));
    }

    @Override
    protected boolean doIt() {
        if (!getMapper().removeAnnotation(oldAnnotation)) {
            return false;
        } else {
            getInjector().getMainEventBus().fireEvent(new TextAnnotationSelectionEmptiedEvent(getAnnotatedTextHandler()));
        }
        return true;
    }

    @Override
    public String getPresentationName() {
        return messages.annotationRemovalPresentationName(annotationId, oldAnnotation.getAnnotationType());
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoAnnotationRemovalPresentationName(annotationId, oldAnnotation.getAnnotationType());
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoAnnotationRemovalPresentationName(annotationId, oldAnnotation.getAnnotationType());
    }

    @Override
    public boolean isPrevented() {
        return !getPreventingCause().isEmpty();
    }

    @Override
    public String getPreventingCause() {

        if (!getAnnotatedTextHandler().getAnnotationSetId(annotationId).equals(getAnnotatedTextHandler().getEditableUsersAnnotationSet().getId())) {
            // FIXME not I18N
            return "The annotation can not be removed because it does not belong to an editable Annotation Set";
        }

        List<Annotation> references = getAnnotatedTextHandler().getReferencesToAnnotation(annotationId);
        if (references.isEmpty()) {
            return null;
        } else {
            StringBuilder msg = new StringBuilder();
            // FIXME not I18N
            msg.append("The annotation can not be removed because it is still referenced by the following annotations:");
            String sep = " ";
            for (Annotation a : references) {
                msg.append(sep).append(a.getId());
                sep = ", ";
            }
            return msg.toString();
        }
    }

    @Override
    public boolean isForcible() {
        return false;
    }

    @Override
    public AnnotationCompoundEdit getForcingEdit() {
        throw new UnsupportedOperationException("Forcing this Edit is not allowed.");
    }
    
    @Override
    public Annotation getDeletedAnnotation() {
        return oldAnnotation;
    }    
}
