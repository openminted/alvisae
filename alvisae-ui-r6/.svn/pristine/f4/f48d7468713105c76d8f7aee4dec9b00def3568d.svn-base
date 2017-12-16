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
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import java.util.List;

/**
 * Specialized Edit corresponding to the creation of a new Annotation.
 * @author fpapazian
 */
public class AnnotationGroupCreationEdit extends AnnotationCreationEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final List<AnnotationReference> components;
    private Annotation newAnnotationGroup = null;

    public AnnotationGroupCreationEdit(AnnotatedTextHandler annotatedDoc, String groupType, List<AnnotationReference> components,  Properties props, List<AnnotationBackReference> backRefs) {
        super(annotatedDoc, groupType, props, backRefs);
        this.components = components;
    }

    public AnnotationGroupCreationEdit(AnnotatedTextHandler annotatedDoc, String groupType, List<AnnotationReference> components) {
        this(annotatedDoc, groupType, components, null, null);
    }

    @Override
    protected void undoIt() {
        //FIXME : gather any information useful for later redo : such as metadata, ...
        getMapper().removeGroup(newAnnotationGroup);
        getInjector().getMainEventBus().fireEvent(new GroupSelectionEmptiedEvent(getAnnotatedTextHandler()));
    }

    @Override
    protected boolean doIt() {
        newAnnotationGroup = getMapper().addGroup(getAnnotationType(), components, getProperties(), getAnnotationBackReferences());
        //FIXME : if necessary also recreate metadata, ....

        getInjector().getMainEventBus().fireEvent(new GroupSelectionChangedEvent(getAnnotatedTextHandler(), newAnnotationGroup));
        return true;
    }

    @Override
    public String getPresentationName() {
        return messages.annotationGroupCreationPresentationName(getAnnotationType(), getDisplayForm());
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoAnnotationGroupCreationPresentationName(getAnnotationType(), getDisplayForm());
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoAnnotationGroupCreationPresentationName(getAnnotationType(), getDisplayForm());
    }

    public Annotation getCreatedGroup() {
        return newAnnotationGroup;
    }

    private String getDisplayForm() {
        StringBuilder displayForm = new StringBuilder();
        displayForm.append("{ ");
        for (AnnotationReference a : components) {
            displayForm.append(a.getAnnotationId()).append(", ");
        }
        displayForm.append(" }");
        return displayForm.toString();
    }
}
