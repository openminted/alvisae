/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationGroupCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationGroupEditionEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationGroupDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicAnnotationSchemaValidator;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicFaultListener;
import java.util.ArrayList;
import java.util.List;

/**
 * About dialog box to show credits and copyrights
 * @author fpapazian
 */
public class EditGroupDialog extends GenericEditDialog {

    /**
     * Start the interactive dialog that leads to Group creation
     * @param annotatedDoc the AnnotatedText where to create the group
     * @param annotationIds the list of Ids of the annotation taking part in the group by default
     */
    public static void startGroupCreation(final AnnotatedTextHandler annotatedDoc, List<String> annotationIds) {
        final EditGroupDialog dlg = new EditGroupDialog(annotatedDoc);
        dlg.setGroupToCreate(annotationIds);
        dlg.setApplyCommand(new Command() {

            @Override
            public void execute() {
                //clear argument selection
                EventBus eventBus = injector.getMainEventBus();
                eventBus.fireEvent(new TextAnnotationSelectionEmptiedEvent(annotatedDoc));
                eventBus.fireEvent(new RelationSelectionEmptiedEvent(annotatedDoc));
                eventBus.fireEvent(new GroupSelectionEmptiedEvent(annotatedDoc));

                String groupType = dlg.getSelectedAnnotationType();
                List<AnnotationReference> components = dlg.getComponents();
                AnnotationGroupCreationEdit groupCreate = new AnnotationGroupCreationEdit(annotatedDoc, groupType, components);
                groupCreate.redo();
            }
        });

        dlg.show();
        dlg.center();
    }

    public static void startGroupEdition(final AnnotatedTextHandler annotatedDoc, String annotationId) {
        final EditGroupDialog dlg = new EditGroupDialog(annotatedDoc);
        final Annotation annotation = annotatedDoc.getAnnotation(annotationId);
        dlg.setGroupToEdit(annotation);
        dlg.setApplyCommand(new Command() {

            @Override
            public void execute() {
                String groupType = dlg.getSelectedAnnotationType();
                List<AnnotationReference> components = dlg.getComponents();
                AnnotationGroupEditionEdit groupEdit = new AnnotationGroupEditionEdit(annotatedDoc, annotation, groupType, components);
                groupEdit.redo();
            }
        });

        dlg.show();
        dlg.center();
    }
    private final BasicFaultListener faultLstnr = new BasicFaultListener(faultMessages);
    private final ClickHandler checkBoxClickHandler =
            new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    validateAnnotation();
                }
            };
    private ArrayList<String> model = null;

    public EditGroupDialog(AnnotatedTextHandler annotatedText) {
        super(annotatedText, AnnotationKind.GROUP);

        //FIXME : not I18N
        annotationTypes.setTitle("Select the annotation type for the new group");

    }

    public void setGroupToCreate(List<String> annotationIds) {
        //FIXME not I18N
        setText("Creation of a new Group...");


        //when being in creation mode, determine the most relevant Group to create considering the selected annotations 
        boolean foundMatching = false;
        int typeIndex = 0;
        for (AnnotationTypeDefinition type : getAllAnnotationTypeDefs()) {
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
                //initialize the UI
                annotationTypes.setSelectedIndex(typeIndex);
                break;
            }
            typeIndex++;
        }

        setCreating(annotationIds);
    }

    private void setGroupToEdit(Annotation annotation) {
        //FIXME not I18N
        setText("Edit Group...");
        setEditing(annotation);
    }
    
    @Override
    protected void prepareArgumentGrid(List<String> annotationIds) {

        argumentsGrid.resize(1, 2);
        argumentsGrid.setText(0, 0, "Component");
        argumentsGrid.setText(0, 1, "add to group");
        //AnnotationTypeDefinition selectedType = getSelectedAnnotationTypeDef();

        argumentsGrid.resize(annotationIds.size() + 1, 2);
        int row = 1;
        Annotation editedAnnotation = getMainAnnotation();
        model = new ArrayList<String>();
        if (editedAnnotation == null) {
            for (String annId : annotationIds) {
                model.add(annId);
                argumentsGrid.setText(row, 0, getShortDesc(annId));
                CheckBox ckbox = new CheckBox();
                ckbox.addClickHandler(checkBoxClickHandler);
                ckbox.setValue(true);
                argumentsGrid.setWidget(row, 1, ckbox);
                row++;
            }
        } else {
            List<String> components = new ArrayList<String>();
            for (AnnotationReference c : editedAnnotation.getAnnotationGroup().getComponentRefs()) {
                components.add(c.getAnnotationId());
            }
            //components already in the group
            for (String annId : components) {
                model.add(annId);
                argumentsGrid.setText(row, 0, getShortDesc(annId));
                CheckBox ckbox = new CheckBox();
                ckbox.addClickHandler(checkBoxClickHandler);
                ckbox.setValue(true);
                argumentsGrid.setWidget(row, 1, ckbox);
                row++;
            }
            //other annotations that can be added
            for (String annId : annotationIds) {
                if (!components.contains(annId)) {
                    model.add(annId);
                    argumentsGrid.setText(row, 0, getShortDesc(annId));
                    CheckBox ckbox = new CheckBox();
                    ckbox.addClickHandler(checkBoxClickHandler);
                    ckbox.setValue(false);
                    argumentsGrid.setWidget(row, 1, ckbox);
                    row++;
                }
            }

        }

        //validate annotation with default values
        validateAnnotation();
    }

    @Override
    protected void validateAnnotation() {
        setWarning(null);
        setError(null);

        //1rst level check : completeness of the data to create the group
        if (checkCompleteness()) {

            //2nd level check : validity of the group relatively to its type

            AnnotatedText annotatedText = getAnnotatedTextHandler().getAnnotatedText();
            BasicAnnotationSchemaValidator validator = getAnnotationSchemaValidator();
            validator.setAnnotatedText(annotatedText);
            faultLstnr.reset();
            String typeName = getSelectedAnnotationType();
            AnnotationTypeDefinition annTypeDef = annotatedText.getAnnotationSchema().getAnnotationTypeDefinition(typeName);
            Annotation annotation = ((AnnotatedTextImpl) annotatedText).createLooseGroupAnnotation("new group", typeName, getComponents(), null, null);
            validator.validateAnnotation(faultLstnr, annTypeDef, annotation, true);
            setWarning(faultLstnr.getLastMessage());
        }
    }

    private boolean checkCompleteness() {
        boolean complete = false;
        if (getSelectedAnnotationType() == null) {
            complete = false;
            //FIXME not I18N
            setError("A Group must have a type!");
        } else {
            for (int row = 1; row < argumentsGrid.getRowCount(); row++) {
                CheckBox ckbox = (CheckBox) argumentsGrid.getWidget(row, 1);
                if (ckbox.getValue()) {
                    complete = true;
                    break;
                }
            }
            if (!complete) {
                //FIXME not I18N
                setError("The group must have at least one component!");
            }
        }
        applyModifBtn.setEnabled(complete);
        return complete;
    }

    private List<AnnotationReference> getComponents() {
        List<AnnotationReference> components = new ArrayList<AnnotationReference>();
        for (int row = 1; row < argumentsGrid.getRowCount(); row++) {
            CheckBox ckbox = (CheckBox) argumentsGrid.getWidget(row, 1);
            if (ckbox.getValue()) {
                String componentId = model.get(row - 1);
                AnnotationReference annotationRef = getAnnotatedTextHandler().getAnnotationReference(componentId);
                components.add(annotationRef);
            }
        }
        return components;
    }
}
