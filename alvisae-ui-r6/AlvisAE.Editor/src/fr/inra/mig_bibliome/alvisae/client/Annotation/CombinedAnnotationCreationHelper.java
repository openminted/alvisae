/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationGroupCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationRelationCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSchemaDefHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.RelationBase;
import fr.inra.mig_bibliome.alvisae.client.data3.validation.ClientFaultMessages;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationGroupDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicAnnotationSchemaValidator;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.RelationDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class CombinedAnnotationCreationHelper {

    protected static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    protected static final ClientFaultMessages faultMessages = GWT.create(ClientFaultMessages.class);
    private static final EventBus eventBus = injector.getMainEventBus();
    private static final BasicAnnotationSchemaValidator validator = new BasicAnnotationSchemaValidator();

    /**
     *
     * @param annotatedDoc the AnnotatedText where to create the group
     * @param annotationIds the list of Ids of the annotation taking part in the
     * group
     */
    public static void createGroup(AnnotatedTextHandler annotatedDoc, List<String> annotationIds) {
        new CombinedAnnotationCreationHelper(annotatedDoc, AnnotationKind.GROUP).createGroup(annotationIds);
    }

    /**
     *
     *
     * @param annotatedDoc the AnnotatedText where to create the relation
     * @param annotationIds the list of Ids of the annotation taking part in the
     * relation
     */
    public static void CreateRelation(AnnotatedTextHandler annotatedDoc, List<String> annotationIds) {
        new CombinedAnnotationCreationHelper(annotatedDoc, AnnotationKind.RELATION).createRelation(annotationIds);
    }

    public static Map<String, AnnotationTypeDefinition> getEditableAnnotationTypeDefs(AnnotatedTextHandler annotatedText, AnnotationKind kind) {
        TaskDefinition taskDef = annotatedText.getAnnotatedText().getEditedTask();
        Set<String> editedTypes = null;
        if (taskDef != null) {
            editedTypes = new HashSet<String>(taskDef.getEditedAnnotationTypes());
        }

        List<AnnotationTypeDefinition> annTypeDefs = annotatedText.getAnnotatedText().getAnnotationSchema().getAnnotationTypeDefinition(kind);
        Map<String, AnnotationTypeDefinition> editableAnnTypeDefs = new LinkedHashMap<String, AnnotationTypeDefinition>();
        for (AnnotationTypeDefinition type : annTypeDefs) {
            if ((editedTypes == null) || editedTypes.contains(type.getType())) {
                editableAnnTypeDefs.put(type.getType(), type);
            }
        }
        return editableAnnTypeDefs;
    }

    private static BasicAnnotationSchemaValidator getAnnotationSchemaValidator() {
        return validator;
    }
    //
    private final Map<String, AnnotationTypeDefinition> editableAnnTypeDefs;
    private final AnnotatedTextHandler annotatedText;
    private AnnotationSchemaDefHandler schemaHandler;
    private Annotation editedAnnotation = null;

    public CombinedAnnotationCreationHelper(AnnotatedTextHandler annotatedText, AnnotationKind kind) {
        if (annotatedText == null) {
            throw new IllegalArgumentException("annotatedText must be specified");
        }
        this.annotatedText = annotatedText;

        //if the edition is associated to a TaskDefinition, then the available Annotation types must be limited to the one of the Task
        editableAnnTypeDefs = getEditableAnnotationTypeDefs(annotatedText, kind);
        if (editableAnnTypeDefs.isEmpty()) {
            throw new IllegalArgumentException("No available annotation type to edit for this task");
        }

        schemaHandler = new AnnotationSchemaDefHandler(annotatedText.getAnnotatedText().getAnnotationSchema());
    }

    protected void setEditing(Annotation editedAnnotation) {
        List<String> annIds = new ArrayList<String>();
        for (Annotation a : getAnnotatedTextHandler().getReferenceableAnnotations()) {
            String otherAnnotationId = a.getId();
            //filter out Annotation referencing the main annotation being edited
            if (!getAnnotatedTextHandler().isEqualToOrReferencedBy(editedAnnotation.getId(), otherAnnotationId)) {
                annIds.add(otherAnnotationId);
            }
        }

        //this.annotationIds = annIds;
        this.editedAnnotation = editedAnnotation;

    }

    private AnnotatedTextHandler getAnnotatedTextHandler() {
        return annotatedText;
    }

    private Annotation getEditedAnnotation() {
        return editedAnnotation;
    }

    private List<AnnotationTypeDefinition> getEditableAnnotationTypeDefs() {
        return new ArrayList<AnnotationTypeDefinition>(editableAnnTypeDefs.values());
    }

    private AnnotationSchemaDefHandler getSchemaHandler() {
        return schemaHandler;
    }

    //------------------------------------------------------------
    private void createGroup(List<String> annotationIds) {
        AnnotationTypeDefinition choosenAnnotationType = null;

        //when being in creation mode, determine the most relevant Group to create considering the selected annotations 
        boolean foundMatching = false;
        List<AnnotationTypeDefinition> editableTypes = getEditableAnnotationTypeDefs();
        for (AnnotationTypeDefinition type : editableTypes) {
            AnnotationGroupDefinition grpDef = type.getAnnotationGroupDefinition();
            List<String> supportedTypes = grpDef.getComponentsTypes();
            foundMatching = true;
            String previousType = null;
            for (String compId : annotationIds) {
                Annotation compAnnotation = getAnnotatedTextHandler().getAnnotation(compId);
                String currentType = compAnnotation.getAnnotationType();
                if (!supportedTypes.contains(currentType)) {
                    //type mismatch, check next possible group type
                    foundMatching = false;
                    break;
                } else if (grpDef.isHomogeneous() && previousType != null && !currentType.equals(previousType)) {
                    //un-homogenous types, check next possible group type
                    foundMatching = false;
                    break;
                }
                previousType = compAnnotation.getAnnotationType();
            }
            if (foundMatching) {
                choosenAnnotationType = type;
                break;
            }
        }
        //if no relevant Group type found, just take the first one...
        choosenAnnotationType = editableTypes.get(0);

        //clear annotation selection
        AnnotatedTextHandler annotatedDoc = getAnnotatedTextHandler();
        eventBus.fireEvent(new TextAnnotationSelectionEmptiedEvent(annotatedDoc));
        eventBus.fireEvent(new RelationSelectionEmptiedEvent(annotatedDoc));
        eventBus.fireEvent(new GroupSelectionEmptiedEvent(annotatedDoc));

        //eventually create the Group
        String groupType = choosenAnnotationType.getType();
        List<AnnotationReference> components = new ArrayList<AnnotationReference>();
        for (String componentId : annotationIds) {
            AnnotationReference annotationRef = getAnnotatedTextHandler().getAnnotationReference(componentId);
            components.add(annotationRef);
        }

        AnnotationGroupCreationEdit groupCreate = new AnnotationGroupCreationEdit(annotatedDoc, groupType, components);
        groupCreate.redo();

    }

    public RelationBase computeRelevantRelation(String relationTypeName, List<String> annotationIds) {
        if (!editableAnnTypeDefs.containsKey(relationTypeName)) {
            throw new IllegalArgumentException(relationTypeName + " is not an editable type");
        }
        List<AnnotationTypeDefinition> possibleAnnotationTypeDefs = new ArrayList<AnnotationTypeDefinition>();
        possibleAnnotationTypeDefs.add(editableAnnTypeDefs.get(relationTypeName));
        return computeRelevantRelation(possibleAnnotationTypeDefs, annotationIds);
    }

    /**
     * Find the most relevant Relation to create with the provided arguments
     */
    private RelationBase computeRelevantRelation(List<AnnotationTypeDefinition> possibleAnnotationTypeDefs, List<String> annotationIds) {
        if (possibleAnnotationTypeDefs.isEmpty()) {
            throw new IllegalArgumentException("At least one possible type must be provided!");
        }
        AnnotationTypeDefinition choosenAnnotationType = null;

        Map<String, AnnotationReference> argumentRoleMap = new HashMap<String, AnnotationReference>();

        //when being in creation mode, determine the most relevant relation to create considering the selected annotations 
        int nbAnnotations = annotationIds.size();

        String[] permAnnIds = annotationIds.toArray(new String[0]);
        ArrayPermuter<String> permutations = new ArrayPermuter<String>(permAnnIds);
        boolean foundMatching = false;

        while (!foundMatching && permutations.next()) {

            //The first permutation is identity : search for exact match : number of argument, type of arguments in the selected order, 
            //then, with the other permutations : search for close match : number of argument, type of arguments in the any order,

            for (AnnotationTypeDefinition type : possibleAnnotationTypeDefs) {

                RelationDefinition relDef = type.getRelationDefinition();
                foundMatching = true;

                argumentRoleMap.clear();

                List<String> supportedRoles = relDef.getSupportedRoles();
                if (supportedRoles.size() == nbAnnotations) {
                    int argumentOrdNum = 0;
                    for (String r : supportedRoles) {
                        String argId = permAnnIds[argumentOrdNum++];
                        Annotation argAnnotation = getAnnotatedTextHandler().getAnnotation(argId);
                        if (!type.getRelationDefinition().getArgumentTypes(r).contains(argAnnotation.getAnnotationType())) {
                            foundMatching = false;
                            break;
                        }
                        argumentRoleMap.put(r, getAnnotatedTextHandler().getAnnotationReference(argId));
                    }
                } else {
                    foundMatching = false;
                }

                if (foundMatching) {
                    choosenAnnotationType = type;
                    break;
                }
            }
        }

        //Search for partial match if the above did not result in any matching
        if (choosenAnnotationType == null) {
            argumentRoleMap.clear();

            //No relevant annotation combination found : use first available Type which can hold the closest number of arguments, and keeps arguments in order

            int nbRolespOfSecondChoiceType = 0;
            AnnotationTypeDefinition secondChoiceType = null;

            for (AnnotationTypeDefinition type : possibleAnnotationTypeDefs) {
                RelationDefinition relDef = type.getRelationDefinition();

                int currentTypeNbRoles = relDef.getSupportedRoles().size();
                if (currentTypeNbRoles == nbAnnotations) {
                    //current type can hold exactly the number of provided arguments
                    secondChoiceType = type;
                    break;
                } else if (currentTypeNbRoles > nbAnnotations && (currentTypeNbRoles < nbRolespOfSecondChoiceType || nbRolespOfSecondChoiceType < nbAnnotations)) {
                    //current type can hold more arguments that those provided, but less than the previously found one
                    //or the previous one could not hold ll arguments
                    secondChoiceType = type;
                    nbRolespOfSecondChoiceType = relDef.getSupportedRoles().size();
                } else if (currentTypeNbRoles < nbAnnotations && currentTypeNbRoles > nbRolespOfSecondChoiceType) {
                    //current type can not hold all the provided arguments, but more than the previously found one
                    secondChoiceType = type;
                    nbRolespOfSecondChoiceType = relDef.getSupportedRoles().size();
                }
            }

            choosenAnnotationType = secondChoiceType;

            RelationDefinition relDef = choosenAnnotationType.getRelationDefinition();
            List<String> supportedRoles = relDef.getSupportedRoles();
            int argumentOrdNum = 0;
            for (String r : supportedRoles) {
                //the choosen type may required a different number of arguments than the selected annotations
                if (argumentOrdNum >= annotationIds.size()) {
                    break;
                } else {
                    String argId = annotationIds.get(argumentOrdNum++);
                    argumentRoleMap.put(r, getAnnotatedTextHandler().getAnnotationReference(argId));
                }
            }
        }

        String relationType = choosenAnnotationType.getType();
        return new RelationBase(relationType, argumentRoleMap);
    }

    private void createRelation(List<String> annotationIds) {

        AnnotatedTextHandler annotatedDoc = getAnnotatedTextHandler();
        RelationBase relBase = computeRelevantRelation(getEditableAnnotationTypeDefs(), annotationIds);
        AnnotationRelationCreationEdit relCreate = new AnnotationRelationCreationEdit(annotatedDoc, relBase.getType(), relBase.getArgumentRoleMap());

        //clear annotation selection
        eventBus.fireEvent(new TextAnnotationSelectionEmptiedEvent(annotatedDoc));
        eventBus.fireEvent(new RelationSelectionEmptiedEvent(annotatedDoc));
        eventBus.fireEvent(new GroupSelectionEmptiedEvent(annotatedDoc));

        //eventually create the Relation
        relCreate.redo();

    }
}
