/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationSchemaCell;
import fr.inra.mig_bibliome.alvisae.client.Widgets.CellListSelectPopupPanel;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSchemaDefHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition;
import java.util.List;

/**
 * Cell used to display Annotation Kind/Id/Type and to popup an Annotation Type selection dialog when clicked
 * @author fpapazian
 */
public class AnnotationIdNTypeSelectCell extends AnnotationIdNTypeCell {

    public static class AnnotationNamedTypeCell extends AbstractCell<String> {

        @Override
        public void render(Cell.Context context, String annotationType, SafeHtmlBuilder sb) {
            if (annotationType != null) {
                AnnotationSchemaCell.renderType(annotationType, sb);
            }
        }
    }
    
    private ValueUpdater<String> annotationTypeUpdater = null;
    //no static because may change with Annotated Document
    private CellListSelectPopupPanel<String>[] selectionPopupPanels;
    private AnnotatedText previousAnnotatedText = null;
    private boolean readonly = true;

    public AnnotationIdNTypeSelectCell() {
        super(null, "<br>", "click");
        resetAnnotationTypesPopupPanel();
    }
    
    public void setReadonly(boolean readonly) {
        this.readonly = annotationTypeUpdater == null || readonly;
    }

    private ValueUpdater<String> getAnnotationTypeUpdater() {
        return this.annotationTypeUpdater;
    }

    public void setAnnotationTypeUpdater(ValueUpdater<String> annotationTypeUpdater) {
        this.annotationTypeUpdater = annotationTypeUpdater;
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, Annotation annotation, NativeEvent event, ValueUpdater<Annotation> valueUpdater) {
        super.onBrowserEvent(context, parent, annotation, event, valueUpdater);
        if (!readonly) {
            String eventType = event.getType();
            if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                EventTarget evtTarget = event.getEventTarget();
                Element targetElement = evtTarget.cast();
                Element targetParent = targetElement.getParentElement();
                if (targetParent.getAttribute(PropertiesTree.CHANGETYPEBTN_CLASSVAL).equals("true")) {
                    String preferredWidth = targetParent.getParentElement().getClientWidth() + "px";
                    CellListSelectPopupPanel<String> selectionPopupPanel = getAnnotationTypesPopupPanel(annotation);
                    selectionPopupPanel.display(parent.getAbsoluteLeft(), parent.getAbsoluteTop(), preferredWidth, getAnnotationTypeUpdater());
                }
            }
        }
    }

    @SuppressWarnings(value = {"unchecked"})
    private void resetAnnotationTypesPopupPanel() {
        CellListSelectPopupPanel<String>[] apanel = new CellListSelectPopupPanel[]{null, null, null};
        selectionPopupPanels = apanel;
    }

    private CellListSelectPopupPanel<String> getAnnotationTypesPopupPanel(Annotation annotation) {
        if (annotation == null) {
            return null;
        }
        AnnotationKind kind = annotation.getAnnotationKind();
        //Annotated document or task changed
        if (!annotation.getAnnotatedText().equals(previousAnnotatedText)) {
            //force to reset panels
            resetAnnotationTypesPopupPanel();
        }
        CellListSelectPopupPanel<String> selectionPopupPanel = selectionPopupPanels[kind.toInt()];
        //reset panel when needed
        if (selectionPopupPanel == null) {
            previousAnnotatedText = annotation.getAnnotatedText();
            TaskDefinition taskDef = previousAnnotatedText.getEditedTask();
            //retrieve only Annotation types editable in this Task
            List<String> annotationTypes = AnnotationSchemaDefHandler.getEditableAnnotationTypes(previousAnnotatedText.getAnnotationSchema(), kind, taskDef);
            selectionPopupPanel = new CellListSelectPopupPanel<String>(new AnnotationNamedTypeCell(), annotationTypes);
            selectionPopupPanels[kind.toInt()] = selectionPopupPanel;
        }
        return selectionPopupPanel;
    }
}
