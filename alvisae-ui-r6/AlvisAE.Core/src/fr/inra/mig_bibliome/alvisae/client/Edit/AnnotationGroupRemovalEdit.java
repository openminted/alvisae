/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.SourceAnnotations;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class AnnotationGroupRemovalEdit extends AnnotationDeletionEdit implements PreventableEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final Annotation oldGroup;
    private List<AnnotationReference> components;

    public AnnotationGroupRemovalEdit(AnnotatedTextHandler annotatedDoc, Annotation group) {
        super(annotatedDoc);

        //keep a copy of the annotation to be removed
        oldGroup = AnnotationImpl.create((AnnotationImpl) group);
        components = new ArrayList<AnnotationReference>();
        for (AnnotationReference aRef : group.getAnnotationGroup().getComponentRefs()) {
            components.add(aRef);
        }
    }

    @Override
    protected void undoIt() {
        SourceAnnotations sourceAnns = oldGroup.getSourceAnnotations();

        Annotation newAnnotationGroup = getMapper().addGroup(oldGroup.getId(), oldGroup.getAnnotationType(), components, oldGroup.getProperties(), sourceAnns == null ? null : sourceAnns.getAnnotationBackReferences());
        newAnnotationGroup.getProperties().replaceAll(oldGroup.getProperties());

        getInjector().getMainEventBus().fireEvent(new GroupSelectionChangedEvent(getAnnotatedTextHandler(), newAnnotationGroup));
    }

    @Override
    protected boolean doIt() {
        //FIXME : if necessary also recreate metadata, ....
        if (!getMapper().removeGroup(oldGroup)) {
            return false;
        } else {
            getInjector().getMainEventBus().fireEvent(new GroupSelectionEmptiedEvent(getAnnotatedTextHandler()));
            return true;
        }

    }

    @Override
    public String getPresentationName() {
        return messages.annotationGroupRemovalPresentationName(oldGroup.getId(), oldGroup.getAnnotationType());
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoAnnotationGroupRemovalPresentationName(oldGroup.getId(), oldGroup.getAnnotationType());
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoAnnotationGroupRemovalPresentationName(oldGroup.getId(), oldGroup.getAnnotationType());
    }

    @Override
    public boolean isPrevented() {
        return !getPreventingCause().isEmpty();
    }

    @Override
    public String getPreventingCause() {
        String annotationId = oldGroup.getId();
        List<Annotation> references = getAnnotatedTextHandler().getReferencesToAnnotation(annotationId);
        if (references.isEmpty()) {
            return null;
        } else {
            // FIXME not I18N
            StringBuilder msg = new StringBuilder();
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
        return oldGroup;
    }
}
