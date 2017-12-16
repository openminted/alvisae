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
import fr.inra.mig_bibliome.alvisae.client.data.ResultMessageDialog;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.SourceAnnotations;
import java.util.List;

/**
 * Specialized Edit corresponding to the removal of fragments from an existing
 * Annotation.
 *
 * @author fpapazian
 */
public class TextAnnotationFragmentsRemovalEdit extends AnnotationModificationEdit implements TextAnnotationCoverageEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final String annotationId;
    private Annotation oldAnnotation;
    private List<Fragment> deltaFragments;

    public TextAnnotationFragmentsRemovalEdit(AnnotatedTextHandler annotatedDoc, String annotationId, List<Fragment> fragments) {
        super(annotatedDoc);

        this.annotationId = annotationId;
        deltaFragments = fragments;
        Annotation annotation = getMapper().getAnnotation(annotationId);
        oldAnnotation = AnnotationImpl.create((AnnotationImpl) annotation);
    }

    @Override
    protected void undoIt() {
        if (getMapper().getAnnotation(annotationId) == null) {
            SourceAnnotations sourceAnns = oldAnnotation.getSourceAnnotations();
            Annotation newAnnotation = getMapper().addAnnotation(annotationId, oldAnnotation.getAnnotationType(), oldAnnotation.getTextBinding().getFragments(), oldAnnotation.getProperties(), sourceAnns == null ? null : sourceAnns.getAnnotationBackReferences());
            newAnnotation.getProperties().replaceAll(oldAnnotation.getProperties());

        } else {
            getMapper().setAnnotationFragment(annotationId, oldAnnotation.getTextBinding().getFragments());
        }
        Annotation annotation = getMapper().getAnnotation(annotationId);
        getInjector().getMainEventBus().fireEvent(new TextAnnotationSelectionChangedEvent(getAnnotatedTextHandler(), annotation));
    }

    @Override
    protected boolean doIt() {

        //FIXME : gather any information useful for later redo : such as annotation metadata, relations to other annotation, ...
        if (!getMapper().removeAnnotationFragment(annotationId, deltaFragments)) {
            if (!getMapper().removeAnnotation(oldAnnotation)) {
                // FIXME not I18N
                List<Annotation> references = getAnnotatedTextHandler().getReferencesToAnnotation(annotationId);
                StringBuilder msg = new StringBuilder();
                msg.append("The annotation can not be removed because it is still referenced by the following annotations:");
                String sep = " ";
                for (Annotation a : references) {
                    msg.append(sep).append(a.getId());
                    sep = ", ";
                }
                ResultMessageDialog d = new ResultMessageDialog(ResultMessageDialog.Error, "Can not remove annotation " + annotationId, msg.toString());
                d.show();
                return false;
            } else {
                getInjector().getMainEventBus().fireEvent(new TextAnnotationSelectionEmptiedEvent(getAnnotatedTextHandler()));
            }
        } else {
            Annotation annotation = getMapper().getAnnotation(annotationId);
            getInjector().getMainEventBus().fireEvent(new TextAnnotationSelectionChangedEvent(getAnnotatedTextHandler(), annotation));
        }
        return true;
    }

    @Override
    public String getPresentationName() {
        return messages.fragmentRemovalPresentationName(getCoordinates(deltaFragments), annotationId);
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoFragmentRemovalPresentationName(getCoordinates(deltaFragments), annotationId);
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoFragmentRemovalPresentationName(getCoordinates(deltaFragments), annotationId);
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
