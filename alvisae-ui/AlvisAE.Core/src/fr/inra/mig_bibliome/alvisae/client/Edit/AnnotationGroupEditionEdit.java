/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Specialized Edit corresponding to the modification of an Annotation.
 * @author fpapazian
 */
public class AnnotationGroupEditionEdit extends AnnotationModificationEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final String oldType;
    private final String newType;
    private final List<AnnotationReference> oldComponents;
    private final List<AnnotationReference> newComponents;
    private final String oldGroupId;
    private Annotation newGroup;

    public AnnotationGroupEditionEdit(AnnotatedTextHandler annotatedDoc, Annotation group, String newType, List<AnnotationReference> newComponents) {
        super(annotatedDoc);
        //keep a copy of the annotation before modification
        this.newType = newType;
        this.newComponents = new ArrayList<AnnotationReference>();
        for (AnnotationReference c: newComponents) {
            this.newComponents.add(c);
        }
        oldGroupId = group.getId();
        oldType = group.getAnnotationType();
        oldComponents = new ArrayList<AnnotationReference>();
        for (AnnotationReference c: group.getAnnotationGroup().getComponentRefs()) {
            oldComponents.add(c);
        }
    }

    @Override
    protected void undoIt() {
        newGroup = getMapper().modifyGroup(newGroup.getId(), oldType, oldComponents);
    }

    @Override
    protected boolean doIt() {
        newGroup = getMapper().modifyGroup(oldGroupId, newType, newComponents);
        return true;
    }

    @Override
    public String getPresentationName() {
        return messages.annotationGroupEditionPresentationName(oldGroupId, oldType);
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoAnnotationGroupEditionPresentationName(oldGroupId, oldType, getDisplayForm(oldComponents));
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoAnnotationGroupEditionPresentationName(oldGroupId, newType, getDisplayForm(newComponents));
    }

    private String getDisplayForm(List<AnnotationReference> components) {
        StringBuilder displayForm = new StringBuilder();
        displayForm.append("{ ");
        for (AnnotationReference c : components) {
            displayForm.append(c.getAnnotationId()).append(", ");
        }
        displayForm.append(" }");
        return displayForm.toString();
    }

    @Override
    public Annotation getAnnotation() {
        return newGroup;
    }
}
