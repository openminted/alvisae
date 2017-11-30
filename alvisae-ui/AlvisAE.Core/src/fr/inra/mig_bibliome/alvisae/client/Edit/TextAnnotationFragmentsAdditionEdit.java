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
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import java.util.List;

/**
 * Specialized Edit corresponding to the addition of fragments from an existing Annotation.
 * @author fpapazian
 */
public class TextAnnotationFragmentsAdditionEdit extends AnnotationModificationEdit implements TextAnnotationCoverageEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final String annotationId;
    private Annotation oldAnnotation;
    private List<Fragment> deltaFragments;
    private List<Fragment> initialFragments =null;

    public TextAnnotationFragmentsAdditionEdit(AnnotatedTextHandler annotatedDoc, String annotationId, List<Fragment> fragments) {
        super(annotatedDoc);
        this.annotationId = annotationId;
        deltaFragments = fragments;
        oldAnnotation = AnnotationImpl.create((AnnotationImpl) annotatedDoc.getAnnotation(annotationId));

    }

    @Override
    protected void undoIt() {
        getMapper().setAnnotationFragment(annotationId, oldAnnotation.getTextBinding().getFragments());
        Annotation annotation = getMapper().getAnnotation(annotationId);
        getInjector().getMainEventBus().fireEvent(new TextAnnotationSelectionChangedEvent(getAnnotatedTextHandler(), annotation));
    }

    @Override
    protected boolean doIt() {
        initialFragments = getAnnotatedTextHandler().getAnnotation(annotationId).getTextBinding().getSortedFragments();
        getMapper().addAnnotationFragments(annotationId, deltaFragments);
        Annotation annotation = getMapper().getAnnotation(annotationId);
        getInjector().getMainEventBus().fireEvent(new TextAnnotationSelectionChangedEvent(getAnnotatedTextHandler(), annotation));
        return true;
    }

    @Override
    public String getPresentationName() {
        return messages.fragmentAdditionPresentationName(getCoordinates(deltaFragments), annotationId);
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoFragmentAdditionPresentationName(getCoordinates(deltaFragments), annotationId);
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoFragmentAdditionPresentationName(getCoordinates(deltaFragments), annotationId);
    }

    private String getCoordinates(List<Fragment> fragments) {
        StringBuilder coord = new StringBuilder();
        for (Fragment f : fragments) {
            coord.append("[").append(f.getStart()).append("..").append(f.getEnd()).append("] ");
        }
        return coord.toString();
    }
    
    @Override
    public Annotation getAnnotation() {
        return oldAnnotation;
    }
}
