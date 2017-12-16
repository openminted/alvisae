/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.SourceAnnotations;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author fpapazian
 */
public class AnnotationRelationRemovalEdit extends AnnotationDeletionEdit implements PreventableEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final Annotation oldRelation;
    private final Map<String, AnnotationReference> rolesArguments;

    public AnnotationRelationRemovalEdit(AnnotatedTextHandler annotatedDoc, Annotation relation) {
        super(annotatedDoc);

        //keep a copy of the annotation to be removed
        oldRelation = AnnotationImpl.create((AnnotationImpl) relation);
        rolesArguments = new HashMap<String, AnnotationReference>();

        for (Entry<String, AnnotationReference> e : relation.getRelation().getRolesArguments().entrySet()) {
            rolesArguments.put(e.getKey(), e.getValue());
        }
    }

    @Override
    protected void undoIt() {
        SourceAnnotations sourceAnns = oldRelation.getSourceAnnotations();
        Annotation newAnnotationRelation = getMapper().addRelation(oldRelation.getId(), oldRelation.getAnnotationType(), rolesArguments, oldRelation.getProperties(), sourceAnns == null ? null : sourceAnns.getAnnotationBackReferences());
        newAnnotationRelation.getProperties().replaceAll(oldRelation.getProperties());

        //select the newly created relation
        getInjector().getMainEventBus().fireEvent(new RelationSelectionChangedEvent(getAnnotatedTextHandler(), newAnnotationRelation));
    }

    @Override
    protected boolean doIt() {
        String annotationId = oldRelation.getId();
        //FIXME : if necessary also recreate metadata, ....
        if (!getMapper().removeRelation(oldRelation)) {
            return false;
        } else {
            getInjector().getMainEventBus().fireEvent(new RelationSelectionEmptiedEvent(getAnnotatedTextHandler()));
            return true;
        }
    }

    @Override
    public String getPresentationName() {
        return messages.annotationRelationRemovalPresentationName(oldRelation.getId(), oldRelation.getAnnotationType());
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoAnnotationRelationRemovalPresentationName(oldRelation.getId(), oldRelation.getAnnotationType());
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoAnnotationRelationRemovalPresentationName(oldRelation.getId(), oldRelation.getAnnotationType());
    }

    @Override
    public boolean isPrevented() {
        return !getPreventingCause().isEmpty();
    }

    @Override
    public String getPreventingCause() {
        String annotationId = oldRelation.getId();
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
        return oldRelation;
    }    
}
