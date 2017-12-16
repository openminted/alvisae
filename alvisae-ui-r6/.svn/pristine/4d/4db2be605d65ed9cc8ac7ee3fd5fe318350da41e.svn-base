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
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import java.util.List;

/**
 * Specialized Edit corresponding to the creation of a new Annotation.
 * @author fpapazian
 */
public class TextAnnotationCreationEdit extends AnnotationCreationEdit implements TextAnnotationCoverageEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final String annotationId;
    private final List<Fragment> targets;
    private Annotation newAnnotation;

    public TextAnnotationCreationEdit(AnnotatedTextHandler annotatedDoc, String annotationId, String annotationType, List<Fragment> targets, Properties props, List<AnnotationBackReference> backRefs) {
        super(annotatedDoc, annotationType, props, backRefs);
        this.annotationId = annotationId;
        this.targets = targets;
    }

    public TextAnnotationCreationEdit(AnnotatedTextHandler annotatedDoc, String annotationId, String annotationType, List<Fragment> targets) {
        this(annotatedDoc, annotationId, annotationType, targets, null, null);
    }
    
    @Override
    protected void undoIt() {
        getMapper().removeAnnotation(newAnnotation);
        getInjector().getMainEventBus().fireEvent(new TextAnnotationSelectionEmptiedEvent(getAnnotatedTextHandler()));

    }

    @Override
    protected boolean doIt() {
        newAnnotation = getMapper().addAnnotation(annotationId, getAnnotationType(), targets, getProperties(), getAnnotationBackReferences());
        getInjector().getMainEventBus().fireEvent(new TextAnnotationSelectionChangedEvent(getAnnotatedTextHandler(), newAnnotation));
        return true;
    }

    @Override
    public String getPresentationName() {
        return messages.annotationCreationPresentationName(annotationId);
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoAnnotationCreationPresentationName(annotationId, getAnnotationType());
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoAnnotationCreationPresentationName(annotationId, getAnnotationType());
    }
}
