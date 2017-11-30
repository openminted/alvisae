/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ListBox;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Annotation.EditRelationDialog.ExclusiveSelection.ExclusiveChangeHandler;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationRelationCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationRelationEditionEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicAnnotationSchemaValidator;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicFaultListener;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.RelationDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * About dialog box to show credits and copyrights
 * @author fpapazian
 */
public class EditRelationDialog extends GenericEditDialog {

    /**
     * Start the interactive dialog that leads to Relation creation
     * @param annotatedDoc the AnnotatedText where to create the relation
     * @param annotationIds the list of Ids of the annotation taking part in the relation
     */
    public static void startRelationCreation(final AnnotatedTextHandler annotatedDoc, List<String> annotationIds) {
        final EditRelationDialog dlg = new EditRelationDialog(annotatedDoc);
        dlg.setRelationToCreate(annotationIds);
        dlg.setApplyCommand(new Command() {

            @Override
            public void execute() {
                //clear argument selection
                EventBus eventBus = injector.getMainEventBus();
                eventBus.fireEvent(new TextAnnotationSelectionEmptiedEvent(annotatedDoc));
                eventBus.fireEvent(new RelationSelectionEmptiedEvent(annotatedDoc));
                eventBus.fireEvent(new GroupSelectionEmptiedEvent(annotatedDoc));

                String relType = dlg.getSelectedAnnotationType();
                Map<String, AnnotationReference> argumentRoleMap = dlg.getArgumentRoleMap();
                AnnotationRelationCreationEdit relCreate = new AnnotationRelationCreationEdit(annotatedDoc, relType, argumentRoleMap);
                relCreate.redo();
            }
        });

        dlg.show();
        dlg.center();
    }

    /**
     * Start the interactive dialog used to edit a preexisting Relation
     * @param annotatedDoc the AnnotatedText to which the edited relation belongs
     * @param annotationId the Id of the edited relation
     */
    public static void startRelationEdition(final AnnotatedTextHandler annotatedDoc, String annotationId) {
        final EditRelationDialog dlg = new EditRelationDialog(annotatedDoc);
        final Annotation annotation = annotatedDoc.getAnnotation(annotationId);
        dlg.setRelationToEdit(annotation);
        dlg.setApplyCommand(new Command() {

            @Override
            public void execute() {
                String relType = dlg.getSelectedAnnotationType();
                Map<String, AnnotationReference> argumentRoleMap = dlg.getArgumentRoleMap();
                AnnotationRelationEditionEdit relCreate = new AnnotationRelationEditionEdit(annotatedDoc, annotation, relType, argumentRoleMap);
                relCreate.redo();
            }
        });

        dlg.show();
        dlg.center();
    }
    private ExclusiveSelection exclusiveSelection = new ExclusiveSelection();
    private final BasicFaultListener faultLstnr = new BasicFaultListener(faultMessages);

    public EditRelationDialog(AnnotatedTextHandler annotatedText) {
        super(annotatedText, AnnotationKind.RELATION);
        //FIXME : not I18N
        annotationTypes.setTitle("Select the annotation type for the new annotation");

    }

    public class ArrayPermuter<T> {

        private int[] state = null;
        private T[] data = null;
        private int permutationOrdNum = 0;

        public ArrayPermuter(T[] data) {
            this.data = data;
            this.state = new int[data.length];
            this.permutationOrdNum = 0;
        }

        public boolean next() {
            if (permutationOrdNum == 0) {
                permutationOrdNum++;
                return true;
            }
            while (permutationOrdNum < state.length) {
                if (state[permutationOrdNum] < permutationOrdNum) {
                    int index = (permutationOrdNum % 2) == 0 ? 0 : state[permutationOrdNum];
                    swap(permutationOrdNum, index);
                    state[permutationOrdNum]++;
                    permutationOrdNum = 1;
                    return true;
                } else {
                    state[permutationOrdNum] = 0;
                    permutationOrdNum++;
                }
            }
            return false;
        }

        private void swap(int i, int j) {
            T tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }
    }

    private void setRelationToCreate(List<String> annotationIds) {
        //FIXME not I18N
        setText("Creation of a new Relation...");

        //when being in creation mode, determine the most relevant relation to create considering the selected annotations 

        int nbAnnotations = annotationIds.size();
        
        String[] permAnnIds = annotationIds.toArray(new String[0]);
        ArrayPermuter<String> permutations = new ArrayPermuter<String>(permAnnIds);
        boolean foundMatching = false;
        
        while (!foundMatching && permutations.next()) {

            //The first permutation is identity : search for exact match : number of argument, type of arguments in the selected order, 
            //then, with the other permutations : search for close match : number of argument, type of arguments in the any order,
            
            //FIXME : search for partial match if the above did not result in any matching
            
            int typeIndex = 0;
            for (AnnotationTypeDefinition type : getAllAnnotationTypeDefs()) {
                RelationDefinition relDef = type.getRelationDefinition();
                foundMatching = true;
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
                    }
                } else {
                    foundMatching = false;
                }
                if (foundMatching) {
                    //initialize the UI
                    annotationTypes.setSelectedIndex(typeIndex);
                    annotationIds.clear();
                    int argumentOrdNum = 0;
                    for (String r : supportedRoles) {
                        annotationIds.add( permAnnIds[argumentOrdNum++]);
                    }
                }
                typeIndex++;
            }
        }

        setCreating(annotationIds);
    }

    private void setRelationToEdit(Annotation annotation) {
        //FIXME not I18N
        setText("Edit Relation...");
        setEditing(annotation);
    }

    public static class ExclusiveSelection {

        public static class ExclusiveChangeHandler implements ChangeHandler {

            private ListBox listBox;
            private final ExclusiveSelection exclusiveSelection;

            public ExclusiveChangeHandler(ExclusiveSelection exclusiveSelection, ListBox listbox) {
                this.listBox = listbox;
                this.exclusiveSelection = exclusiveSelection;
            }

            @Override
            public void onChange(ChangeEvent event) {
                if (listBox != null) {
                    exclusiveSelection.publishSelection(this, listBox.getSelectedIndex());
                }
            }

            private void informSelection(int selectedIndex) {
                if (listBox != null && listBox.isItemSelected(selectedIndex)) {
                    //first value is "no-value"
                    listBox.setSelectedIndex(0);
                }
            }

            private void releaseReference() {
                listBox = null;
            }
        }
        private ArrayList<ExclusiveChangeHandler> handlers = new ArrayList<ExclusiveChangeHandler>();

        private void publishSelection(ExclusiveChangeHandler source, int selectedIndex) {
            for (ExclusiveChangeHandler h : handlers) {
                if (!source.equals(h)) {
                    h.informSelection(selectedIndex);
                }
            }
        }

        private void registerChangeHandler(ExclusiveChangeHandler hnd) {
            handlers.add(hnd);
        }

        public void reset() {
            for (ExclusiveChangeHandler h : handlers) {
                h.releaseReference();
            }
            handlers.clear();
        }
    }

    @Override
    protected void prepareArgumentGrid(List<String> annotationIds) {
        exclusiveSelection.reset();

        argumentsGrid.resize(1, 2);
        //FIXME not I18N
        argumentsGrid.setText(0, 0, "Role");
        argumentsGrid.setText(0, 1, "Argument");
        AnnotationTypeDefinition selectedType = getSelectedAnnotationTypeDef();
        List<String> roles;
        if (selectedType != null) {
            roles = selectedType.getRelationDefinition().getSupportedRoles();
        } else {
            roles = new ArrayList<String>();
        }

        argumentsGrid.resize(roles.size() + 1, 2);
        int row = 1;
        for (String role : roles) {
            argumentsGrid.setText(row, 0, role);
            ListBox lbarg = new ListBox(false);
            lbarg.addStyleName(style.Width100pct());
            // "no-value"
            lbarg.addItem("", "");
            for (String r : annotationIds) {
                lbarg.addItem(getShortDesc(r), r);
            }

            ExclusiveChangeHandler hnd = new ExclusiveChangeHandler(exclusiveSelection, lbarg) {

                @Override
                public void onChange(ChangeEvent event) {
                    super.onChange(event);
                    validateAnnotation();
                }
            };
            exclusiveSelection.registerChangeHandler(hnd);

            lbarg.addChangeHandler(hnd);
            //Creation mode 
            Annotation editedAnnotation = getMainAnnotation();
            if (editedAnnotation == null) {
                if (row <= annotationIds.size()) {
                    lbarg.setSelectedIndex(row);
                } else {
                    lbarg.setSelectedIndex(0);
                }
            } //Modification mode 
            else {
                AnnotationReference argument = editedAnnotation.getRelation().getArgumentRef(role);
                if (argument != null) {
                    int index = annotationIds.indexOf(argument.getAnnotationId());
                    lbarg.setSelectedIndex(index + 1);
                } else {
                    lbarg.setSelectedIndex(0);
                }
            }
            argumentsGrid.setWidget(row, 1, lbarg);
            row++;
        }

        //validate annotation with default values
        validateAnnotation();
    }

    @Override
    protected void validateAnnotation() {
        setWarning(null);
        setError(null);

        //1rst level check : completeness of the data to create the relation
        if (checkCompleteness()) {
            //2nd level check : validity of the relation relatively to its type
            AnnotatedText annotatedText = getAnnotatedTextHandler().getAnnotatedText();
            BasicAnnotationSchemaValidator validator = getAnnotationSchemaValidator();

            validator.setAnnotatedText(annotatedText);
            faultLstnr.reset();
            String typeName = getSelectedAnnotationType();
            AnnotationTypeDefinition annTypeDef = annotatedText.getAnnotationSchema().getAnnotationTypeDefinition(typeName);
            Annotation annotation = ((AnnotatedTextImpl) annotatedText).createLooseRelationAnnotation("new relation", typeName, getArgumentRoleMap(), null, null);
            validator.validateAnnotation(faultLstnr, annTypeDef, annotation, true);

            setWarning(faultLstnr.getLastMessage());
        }
    }

    private boolean checkCompleteness() {
        boolean complete = true;
        if (getSelectedAnnotationType() == null) {
            complete = false;
            //FIXME not I18N
            setError("A Relation must have a type!");
        } else {
            for (int row = 1; row < argumentsGrid.getRowCount(); row++) {
                String role = argumentsGrid.getText(row, 0);
                ListBox lb = (ListBox) argumentsGrid.getWidget(row, 1);
                Annotation annotation = getAnnotatedTextHandler().getAnnotation(lb.getValue(lb.getSelectedIndex()));
                if (annotation == null) {
                    complete = false;
                    //FIXME not I18N
                    setError("Every role must have an argument!");
                    break;
                }
            }
        }

        applyModifBtn.setEnabled(complete);
        return complete;
    }

    private Map<String, AnnotationReference> getArgumentRoleMap() {
        Map<String, AnnotationReference> argumentRole = new HashMap<String, AnnotationReference>();
        for (int row = 1; row < argumentsGrid.getRowCount(); row++) {
            String role = argumentsGrid.getText(row, 0);
            ListBox lb = (ListBox) argumentsGrid.getWidget(row, 1);
            String argumentId = lb.getValue(lb.getSelectedIndex());
            argumentRole.put(role, getAnnotatedTextHandler().getAnnotationReference(argumentId));
        }
        return argumentRole;
    }
}
