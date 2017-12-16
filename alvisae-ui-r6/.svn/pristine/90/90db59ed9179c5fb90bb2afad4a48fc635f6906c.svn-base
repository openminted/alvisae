/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Events.GenericAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationSelectionChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentUi.Styles;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView.Rect;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextProcessor;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Manage proxy widgets used in DnD actions on annotation (such as relation creation)
 * @author fpapazian
 */
public class AnnotationReificator implements AnnotationSelectionChangedEventHandler {

    private static boolean isWithinVisibleClip(int absTop, int absBottom, HTML contentHTML, ScrollPanel scrollPanel) {
        boolean visible = true;
        //do not add target for markers outside visible part of the document panel
        if (absTop - contentHTML.getAbsoluteTop() < scrollPanel.getVerticalScrollPosition()) {
            visible = false;
        }

        if (absBottom - contentHTML.getAbsoluteTop() > scrollPanel.getVerticalScrollPosition() + scrollPanel.getOffsetHeight()) {
            visible = false;
        }

        //FIXME : handle the horizontal scrolling as well 
        return visible;
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private static final int REIFICATION_DELAY = 750;
    // -------------------------------------------------------------------------
    private final DocumentView docView;
    private final Styles style;
    private final HTML contentHTML;
    private final ScrollPanel scrollPanel;
    private final AnnotationDocumentViewMapper mapper;
    private final AbsolutePanel absolutePanel;
    private DragHandlePopup draggabled =null;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    /**
     * Widget used for dragging annotation
     */
    static class DragHandlePopup extends PopupPanel {

        private Image handleImage;
        private final Label label;
        private final HorizontalPanel fp;

        public DragHandlePopup() {
            super(true, false);

            this.setStyleName("");
            fp = new HorizontalPanel();
            handleImage = new Image(StanEditorResources.INSTANCE.DragHandleIcon());
            fp.add(handleImage);
            label = new Label();
            fp.add(label);
            this.setWidget(fp);
            //dragged element must be on top
            this.getElement().getStyle().setZIndex(5);
        }

        public Label getLabel() {
            return label;
        }

        private Widget getDragHandle() {
            return handleImage;
        }

        private Widget getDragWidget() {
            return fp;
        }

        private Image getHandleImage() {
            return handleImage;
        }

        private void setText(String text) {
            if (text.length() > 25) {
                text = text.substring(0, 8) + "..." + text.substring(text.length() - 8);
            }
            label.setText(text);
        }

        private void setLabelVisible(boolean visible) {
            label.setVisible(visible);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    /*
     * Widget used for targetting annotation
     */
    static class DropTargetPanel extends SimplePanel {

        private final Element marker;
        private final String sourceAnnotationId;
        private final String targetAnnotationId;

        private DropTargetPanel(String sourceAnnotationId, String targetAnnotationId, Element marker, String annotTargetStyleName) {
            this.sourceAnnotationId = sourceAnnotationId;
            this.targetAnnotationId = targetAnnotationId;
            this.marker = marker;
            setStyleName(annotTargetStyleName);
            int width = marker.getAbsoluteRight() - marker.getAbsoluteLeft();
            int height = marker.getAbsoluteBottom() - marker.getAbsoluteTop();
            setPixelSize(width, height);
        }

        public String getSourceAnnotationId() {
            return sourceAnnotationId;
        }

        public String getTargetAnnotationId() {
            return targetAnnotationId;
        }

        public Element getMarker() {
            return marker;
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //callback trigerred when Annotation drop is performed
    public static interface DroppingAnnotationCallback {

        //An annotation has been chosen
        public void annotationDropped(String sourceAnnotationId, String targetAnnotationId, Element targetMarker);
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static class AnnotationDropController extends SimpleDropController {

        private final DroppingAnnotationCallback droppingAnnotationCallback;

        public AnnotationDropController(Widget dropTarget, DroppingAnnotationCallback droppingAnnotationCallback) {
            super(dropTarget);
            this.droppingAnnotationCallback = droppingAnnotationCallback;
        }

        @Override
        public void onDrop(DragContext context) {
            super.onDrop(context);
            DropTargetPanel dropWidget = (DropTargetPanel) context.dropController.getDropTarget();
            if (dropWidget != null) {
                if (droppingAnnotationCallback != null) {
                    droppingAnnotationCallback.annotationDropped(dropWidget.getSourceAnnotationId(), dropWidget.getTargetAnnotationId(), dropWidget.getMarker());
                }
            } else {
                GWT.log("Error: Null Drop target!");
            }
        }

        @Override
        public void onEnter(DragContext context) {
            super.onEnter(context);
            DropTargetPanel dropWidget = (DropTargetPanel) context.dropController.getDropTarget();
            dropWidget.getMarker().addClassName(GlobalStyles.HoveredTargetAnnotation);
        }

        @Override
        public void onLeave(DragContext context) {
            super.onLeave(context);
            DropTargetPanel dropWidget = (DropTargetPanel) context.dropController.getDropTarget();
            dropWidget.getMarker().removeClassName(GlobalStyles.HoveredTargetAnnotation);
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    /*
     * This class create drop targets for annotation markers
     */
    class AnnotationDragHandlerAdapter extends DragHandlerAdapter {

        private final DroppingAnnotationCallback droppingAnnotationCallback;

        public AnnotationDragHandlerAdapter(DroppingAnnotationCallback droppingAnnotationCallback) {
            this.droppingAnnotationCallback = droppingAnnotationCallback;
        }
        ArrayList<DropTargetPanel> dropTargets = new ArrayList<DropTargetPanel>();

        private void clearTargets() {
            getDragController().unregisterDropControllers();
            for (DropTargetPanel p : dropTargets) {
                p.removeFromParent();
            }
            dropTargets.clear();
        }

        private void createTargets() {
            String currentAnnId = mapper.getAnnotationIdFromMakerId(markerId);
            //prepare other markers to be drop target
            ArrayList<Element> orderedMarker = new ArrayList<Element>();

            //TODO : perform selection on the possible annotation target depending on the dragged annotation

            //1rst step : enumerate every possible marker that can be a drop target 
            for (Annotation annotation : mapper.getAnnotatedTextHandler().getReferenceableAnnotations()) {
                String annotationId = annotation.getId();
                //no target for dragged annotation
                if (currentAnnId.equals(annotationId)) {
                    continue;
                }

                //TODO : drop target of the same Annotation should be all highlighted when hovering any one of them
                for (String mId : mapper.getMarkerIdsFromAnnotationId(annotationId)) {
                    if (mId != null && !mId.equals(markerId)) {
                        Element receiver = Document.get().getElementById(mId);
                        orderedMarker.add(receiver);
                    }
                }
            }

            //2nd step : reorder Markers by creation order so the drop target will respect imbrication order
            Collections.sort(orderedMarker, COMPARATOR);

            //3rd step : actually create ordered drop target so their stack order will follow the one of the corresponding marker 
            for (Element receiver : orderedMarker) {
                String annotationId = AnnotatedTextProcessor.getAnnotationIdFromMarker(receiver);
                if (receiver != null) {
                    final DropTargetPanel p = new DropTargetPanel(currentAnnId, annotationId, receiver, style.DropTargetAnnot());
                    absolutePanel.add(p, receiver.getOffsetLeft(), receiver.getOffsetTop());
                    dropTargets.add(p);
                }
            }
        }
        private final Comparator<Element> COMPARATOR = new Comparator<Element>() {

            @Override
            public int compare(Element o1, Element o2) {

                Integer order1 = Integer.parseInt(o1.getAttribute(AnnotatedTextProcessor.MAKERORDVAL_ATTRNAME));
                Integer order2 = Integer.parseInt(o2.getAttribute(AnnotatedTextProcessor.MAKERORDVAL_ATTRNAME));
                return order1.compareTo(order2);
            }
        };

        private void showTargets() {
            for (final DropTargetPanel targetPanel : dropTargets) {
                AnnotationDropController dropController = new AnnotationDropController(targetPanel, droppingAnnotationCallback);
                getDragController().registerDropController(dropController);
            }
        }

        @Override
        public void onDragEnd(DragEndEvent event) {
            super.onDragEnd(event);
            dragHandlePopup.getLabel().setVisible(false);
            clearTargets();
        }

        @Override
        public void onDragStart(DragStartEvent event) {
            super.onDragStart(event);
            dragHandlePopup.getLabel().setVisible(true);
            createTargets();
            showTargets();
        }
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private Timer reificationDelay = new Timer() {

        @Override
        public void run() {
            doReification();
        }
    };
    private String markerId = null;
    private Annotation annotation = null;
    private Element marker = null;
    private AnnotationDragHandlerAdapter dragHandler = null;
    private DragHandlePopup dragHandlePopup = null;
    private final PickupDragController dragController;

    public AnnotationReificator(DocumentView docView, PickupDragController dragController, DroppingAnnotationCallback droppingAnnotationCallback, Styles style, HTML contentHTML, ScrollPanel scrollPanel, AbsolutePanel absolutePanel) {

        this.docView = docView;
        this.mapper = docView.getMapper();
        this.dragController = dragController;
        this.style = style;
        this.contentHTML = contentHTML;
        this.scrollPanel = scrollPanel;
        //
        this.absolutePanel = absolutePanel;

        dragHandler = new AnnotationDragHandlerAdapter(droppingAnnotationCallback);
        getDragController().addDragHandler(dragHandler);
        getDragController().unregisterDropControllers();
    }

    //Display a drag-handle associated to the selected TextAnnotation to perform DnD
    private void doReification() {
        if (markerId == null) {
            if (marker != null && dragHandlePopup != null && dragHandlePopup.equals(draggabled)) {
                getDragController().makeNotDraggable(dragHandlePopup.getDragHandle());
                draggabled = null;
            }
            marker = null;
            dragHandlePopup = null;
        } else {
            //check that Annotation is referenceable before creating handle
            if (mapper.getAnnotatedTextHandler().isReferenceableAnnotation(annotation.getId())) {
                dragHandlePopup = new DragHandlePopup();
                marker = Document.get().getElementById(markerId);
                getDragController().makeDraggable(dragHandlePopup.getDragWidget(), dragHandlePopup.getDragHandle());
                draggabled = dragHandlePopup;
            }
        }
        if (marker != null) {

            if (isWithinVisibleClip(getMarker().getAbsoluteTop(), getMarker().getAbsoluteBottom(), contentHTML, scrollPanel)) {
                dragHandlePopup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

                    @Override
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        dragHandlePopup.setLabelVisible(false);
                        dragHandlePopup.setText(AnnotatedTextProcessor.getAnnotationText(annotation));
                        Rect clip = docView.getAnnotatedTextClip(getMarker());
                        int left = clip.left - dragHandlePopup.getHandleImage().getWidth() / 2;
                        int top = clip.top - dragHandlePopup.getHandleImage().getHeight() / 2;

                        dragHandlePopup.setPopupPosition(left, top);

                    }
                });

            }
        }
    }

    //Hide the drag-handle (if visible) and prevent it to show again
    public void hideWidget() {
        reificationDelay.cancel();
        if (dragHandlePopup != null && dragHandlePopup.isShowing()) {
            dragHandlePopup.hide();
        }
    }

    //enable the drag-handle to show again
    public void resetWidget() {
        if (dragHandlePopup != null) {
            reificationDelay.schedule(REIFICATION_DELAY);
        }
    }

    private void hideAndRescheduleDragPopup() {
        hideWidget();
        resetWidget();
    }

    @Override
    public void onAnnotationSelectionChanged(GenericAnnotationSelectionChangedEvent e) {
        if (e instanceof TextAnnotationSelectionChangedEvent) {
            //after any change of TextAnnotation selection, trigger the display of the drag-handle
            TextAnnotationSelectionChangedEvent event = (TextAnnotationSelectionChangedEvent) e;
            reificationDelay.cancel();
            hideWidget();
            markerId = null;
            if ((event.getMainSelectedAnnotationMarkers() != null) && (!event.getMainSelectedAnnotationMarkers().isEmpty())) {
                markerId = event.getMainSelectedMarker();
                annotation = event.getMainSelectedAnnotation();
                if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                    reificationDelay.schedule(REIFICATION_DELAY);
                }
            }
        }
    }

    private Element getMarker() {
        return marker;
    }

    private PickupDragController getDragController() {
        return dragController;
    }

    public void resetWidgetPositions() {
        hideAndRescheduleDragPopup();
    }
}
