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
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Specialized Edit corresponding to the creation of a new Annotation.
 *
 * @author fpapazian
 */
public class AnnotationRelationCreationEdit extends AnnotationCreationEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final Map<String, AnnotationReference> argumentRoleMap;
    private Annotation newAnnotationRelation = null;

    public AnnotationRelationCreationEdit(AnnotatedTextHandler annotatedDoc, String relationType, Map<String, AnnotationReference> argumentRoleMap, Properties props, List<AnnotationBackReference> backRefs) {
        super(annotatedDoc, relationType, props, backRefs);
        this.argumentRoleMap = argumentRoleMap;
    }

    public AnnotationRelationCreationEdit(AnnotatedTextHandler annotatedDoc, String relationType, Map<String, AnnotationReference> argumentRoleMap) {
        this(annotatedDoc, relationType, argumentRoleMap, null, null);
    }

    @Override
    protected void undoIt() {
        //FIXME : gather any information useful for later redo : such as metadata, ...
        getMapper().removeRelation(newAnnotationRelation);
        getInjector().getMainEventBus().fireEvent(new RelationSelectionEmptiedEvent(getAnnotatedTextHandler()));
    }

    @Override
    protected boolean doIt() {
        newAnnotationRelation = getMapper().addRelation(getAnnotationType(), argumentRoleMap, getProperties(), getAnnotationBackReferences());
        getInjector().getMainEventBus().fireEvent(new RelationSelectionChangedEvent(getAnnotatedTextHandler(), newAnnotationRelation));

        //FIXME : if necessary also recreate metadata, ....
        return true;
    }

    @Override
    public String getPresentationName() {
        return messages.annotationRelationCreationPresentationName(getAnnotationType(), getDisplayForm());
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoAnnotationRelationCreationPresentationName(getAnnotationType(), getDisplayForm());
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoAnnotationRelationCreationPresentationName(getAnnotationType(), getDisplayForm());
    }

    public Annotation getCreatedRelation() {
        return newAnnotationRelation;
    }
    
    private String getDisplayForm() {
        StringBuilder displayForm = new StringBuilder();
        for (Entry<String, AnnotationReference> e : argumentRoleMap.entrySet()) {
            displayForm.append(e.getKey()).append("(").append(e.getValue().getAnnotationId()).append(") ");
        }
        return displayForm.toString();
    }
}
