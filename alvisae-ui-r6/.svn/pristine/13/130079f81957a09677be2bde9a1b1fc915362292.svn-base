/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import fr.inra.mig_bibliome.alvisae.client.Document.Validation.ValidationResultDialog;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import fr.inra.mig_bibliome.alvisae.client.AlvisaeCore;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationSetDescriptionCell;
import fr.inra.mig_bibliome.alvisae.client.Annotation.CombinedAnnotationCreationHelper;
import fr.inra.mig_bibliome.alvisae.client.Annotation.ExplainSchemaPanel;
import fr.inra.mig_bibliome.alvisae.client.Config.ApplicationOptions;
import fr.inra.mig_bibliome.alvisae.client.Config.ApplicationOptions.PersistedOptionHandler;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Config.ShortCutToActionTypeMapper;
import fr.inra.mig_bibliome.alvisae.client.Config.ShortCutToActionTypeMapper.ShortCutTriggeredActionType;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.AnnotationDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.AnnotationOverlayEventsHandler;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.TextAnnotationDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationReificator.DroppingAnnotationCallback;
import fr.inra.mig_bibliome.alvisae.client.Document.CombinedAnnotationDisplayer.CombinedAnnotationWidget;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentUi.Styles;
import fr.inra.mig_bibliome.alvisae.client.Document.Validation.ClientFaultHTMLMessages;
import fr.inra.mig_bibliome.alvisae.client.Edit.*;
import fr.inra.mig_bibliome.alvisae.client.Edit.undo.CannotRedoException;
import fr.inra.mig_bibliome.alvisae.client.Edit.undo.CannotUndoException;
import fr.inra.mig_bibliome.alvisae.client.Edit.undo.UndoManager;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationFocusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationFocusChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationSelectionChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationStatusChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationOptionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TermAnnotationsExpositionEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourcesCheckChangesInfoEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIVersionedResourcesInfoEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIVersionedResourcesInfoEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIVersionedResourcesUnchangedInfoEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.GenericAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.InformationReleasedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.MaximizingWidgetEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.MaximizingWidgetEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.RangeSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RangeSelectionChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.AnnotationSelections;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.GenericAnnotationSelection;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.TextAnnotationSelection;
import fr.inra.mig_bibliome.alvisae.client.Events.TargetSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.WorkingDocumentChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.WorkingDocumentChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.client.data.ResultMessageDialog;
import fr.inra.mig_bibliome.alvisae.client.data3.*;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.CheckedSemClassImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.ResourceLocator;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.TyDIResRefPropValImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.TyDISemClassRefImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import static fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind.TEXT;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetInfo;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicAnnotationSchemaValidator;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicFaultListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.logging.Logger;
import org.vaadin.gwtgraphics.client.DrawingArea;

/**
 * UI that displays a formatted text with its associated Annotations
 *
 * Important Note : in order to work properly, this component needs to be
 * wrapped in Panels having RequiresResize from the root panel down
 *
 * @author fpapazian
 */
public class DocumentUi extends ResizeComposite implements DocumentView, AnnotationSelectionChangedEventHandler, EditHappenedEventHandler, AnnotationStatusChangedEventHandler, AnnotationFocusChangedEventHandler, WorkingDocumentChangedEventHandler, MaximizingWidgetEventHandler, TyDIVersionedResourcesInfoEventHandler {

    interface DocumentUiBinder extends UiBinder<Widget, DocumentUi> {
    }
    private static DocumentUiBinder uiBinder = GWT.create(DocumentUiBinder.class);
    // -------------------------------------------------------------------------
    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    private static final Logger log = Logger.getLogger(DocumentUi.class.getName());
    private static final ClientFaultHTMLMessages faultMessages = GWT.create(ClientFaultHTMLMessages.class);

    //
    // -------------------------------------------------------------------------
    public interface Styles extends CssResource {

        String SelectableText();

        String DropTargetAnnot();

        String Processing();

        String BackGroundPos();

        String ForeGroundPos();

        String SelectionMarker();
    }
    // -------------------------------------------------------------------------
    @UiField
    LayoutPanel mainLayoutPanel;
    @UiField
    FocusPanel titleBar;
    @UiField
    Label titleLabel;
    @UiField
    Anchor titleHLink;
    @UiField
    LayoutPanel layoutPanel;
    @UiField
    FlowPanel toolBar;
    @UiField
    LayoutPanel docPanel;
    @UiField
    LayoutPanel docTextPanel;
    @UiField
    Image expandCollapseImg;
    @UiField
    Panel scrollPanelContainer;
    @UiField
    ScrollPanel scrollPanel;
    @UiField
    AbsolutePanel absolutePanel;
    @UiField
    HTML overlayContainer;
    @UiField
    HTML contentHTML;
    @UiField
    RequiresResizeSpy resizeSpy;
    @UiField(provided = true)
    final DrawingArea canvas;
    @UiField
    AbsolutePanel occurencePanel;
    @UiField(provided = true)
    final DrawingArea occurenceBar;
    @UiField
    PushButton incLineSize;
    @UiField
    PushButton decLineSize;
    @UiField
    PushButton undoBtn;
    @UiField
    PushButton redoBtn;
    @UiField
    ToggleButton selectionModeBtn;
    @UiField
    FocusPanel focusPanel;
    @UiField
    SimplePanel sp;
    @UiField
    ListBox annTypeList;
    @UiField
    PushButton addAnnotButton;
    @UiField
    PushButton deleteAnnButton;
    /*
     * @UiField PushButton editAnnButton;
     */
    @UiField
    PushButton addSelectionButton;
    @UiField
    PushButton delSelectionButton;
    @UiField
    PushButton replicateAnnButton;
    @UiField
    PushButton addGroupButton;
    @UiField
    PushButton deleteGroupButton;
    @UiField
    PushButton addRelButton;
    @UiField
    PushButton deleteRelButton;
    @UiField
    PushButton explainSchemaBtn;
    @UiField
    PushButton validateButton;
    @UiField
    PushButton termRefreshBtn;
    @UiField
    MenuBar annSetsSelectionMenu;
    @UiField
    MenuItem annSetMenuItem;
    @UiField
    Image errorImage;
    /*
     * @UiField AnnotationTypeDropDownList annTypeList2;
     *
     */
    @UiField
    Styles style;
    //
    private EventBus eventBus;
    //
    private UndoManager undoManager = null;
    private PickupDragController dragController;
    private HandlerRegistration textAnnotationSelectionChangedRegistration;
    private AnnotationSelections selectedTextAnnotations = new AnnotationSelections();
    private AnnotationSelections selectedGroups = new AnnotationSelections();
    private AnnotationSelections selectedRelations = new AnnotationSelections();
    private final AnnotationDisplayerEngine dispEngine;
    private final GroupDisplayer groupDisplayer;
    private final RelationDisplayer relationDisplayer;
    private final ExtraFeatureDisplayer extraDisplayer;
    private final AnnotationMarkerManager annMarkerMgr;
    private AnnotationDocumentViewMapper mapper;
    private final ButtonEnablementManager buttonManager;
    private final TaskSchemaManager schemaManager = new TaskSchemaManager();
    private int interlineSizeIndex;
    private final PersistedOptionHandler interlineSizeHnd;
    private Options options = new Options();
    private AnnotationReificator annotationReificator = null;
    private final AnnMarkerMouseHandler mouseHandler;
    private final ToolBarExpandCollapseHandler toolBarExpandCollapseHandler;
    private final OccurenceDisplayer occurenceDisplayer;
    private boolean maxmimized = false;
    private HandlerRegistration annTypeListRegistration = null;
    private SelectAnnotationPopup selectAnnotationPopup = null;
    private final boolean withScrollpanel;
    //
    private boolean updatingAfterEditEvents = true;
    //
    //for document exposing TermAnnotations referencing TyDI resource
    private ResourceLocator locator;
    private AnnotationSchemaDefHandler schemaHandler;
    private TyDIResourceRefsRefresher tyDIResourceRefsRefresher;

    /**
     *
     */
    class AnnotationInteractionHandler {

        private Element previoustargetElement = null;
        private Boolean previousShiftKeyStatus = null;
        private ArrayList<String> temporarilyUnveiledRelationIds = new ArrayList<String>();
        private AnnotationOverlayEventsHandler relationOverlayEventHandler = new AnnotationOverlayEventsHandler() {
            @Override
            public void onAnnotationOverlayClick(ClickEvent event, String annotationId) {
                relationDisplayer.onRelationClick(event, annotationId);
            }

            @Override
            public void onAnnotationOverlayMouseMove(MouseMoveEvent event, String annotationId) {
                if (annotationId != null && !annotationId.isEmpty()) {
                    temporarilyUnveilRelation(annotationId);
                }
            }

            @Override
            public boolean isManagedOverlay(Element overlayElt) {
                return relationDisplayer.isManagedOverlay(overlayElt);
            }
        };
        private AnnotationOverlayEventsHandler groupOverlayEventHandler = new AnnotationOverlayEventsHandler() {
            @Override
            public void onAnnotationOverlayClick(ClickEvent event, String annotationId) {
                groupDisplayer.onGroupClick(event, annotationId);
            }

            @Override
            public void onAnnotationOverlayMouseMove(MouseMoveEvent event, String annotationId) {
            }

            @Override
            public boolean isManagedOverlay(Element overlayElt) {
                return groupDisplayer.isManagedOverlay(overlayElt);
            }
        };

        private void temporarilyUnveilRelation(String refRelationId) {
            if (mapper != null && mapper.isVeiled(refRelationId)) {
                temporarilyUnveiledRelationIds.add(refRelationId);

                mapper.setUnveiled(refRelationId);
                relationDisplayer.refreshVeiledStatus(refRelationId);
            }
        }

        //Show relations associated to the specified AnnotationId (e.g. when the text annotation is hovered)
        private void temporarilyUnveilReferencedRelations(String argumentId) {
            List<Annotation> references = mapper.getReferencesToAnnotation(argumentId);
            for (Annotation refRelation : references) {
                if (AnnotationKind.RELATION.equals(refRelation.getAnnotationKind())) {
                    String refRelationId = refRelation.getId();
                    temporarilyUnveilRelation(refRelationId);
                }
            }
        }

        //Hide back the relations which were temporarily displayed 
        private void veilTemporarilyUnveiledRelation() {
            Iterator<String> iterator = temporarilyUnveiledRelationIds.iterator();
            while (iterator.hasNext()) {
                String refRelationId = iterator.next();
                iterator.remove();
                mapper.setVeiled(refRelationId);
                relationDisplayer.refreshVeiledStatus(refRelationId);
            }
        }

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        private void hoveringAnnotation(Element hoveredTargetElement) {
            String markerId = hoveredTargetElement.getId();
            String annotationId = mapper.getAnnotationIdFromMakerId(markerId);

            if (annotationId != null && !mapper.isFormattingAnnotation(annotationId)) {

                //expose relations whose argument is the hovered element
                temporarilyUnveilReferencedRelations(annotationId);
            }
        }

        private void MouseMoveOverMarker(MouseMoveEvent event) {
            NativeEvent ntvEvent = event.getNativeEvent();
            EventTarget evtTarget = ntvEvent.getEventTarget();
            Element targetElement = evtTarget.cast();
            if (isNewHoverTarget(ntvEvent)) {

                previoustargetElement = targetElement;
                previousShiftKeyStatus = ntvEvent.getShiftKey();

                //hide back the relation which were exposed when one of its argument was hovered
                veilTemporarilyUnveiledRelation();

                //find the main selected range mark
                List<Element> ascendantMarkerElts = AnnotatedTextProcessor.getAscendantMarkerElements(targetElement, annMarkerMgr.getTextContainerId());
                int nbAscendantMarker = ascendantMarkerElts.size();

                if (nbAscendantMarker >= 1 && !previousShiftKeyStatus) {
                    //display veiled Relation of the Annotation at the foreground under the mouse pointer
                    Element selectedTargetElement = ascendantMarkerElts.get(0);
                    hoveringAnnotation(selectedTargetElement);

                } else if (nbAscendantMarker > 1 && previousShiftKeyStatus) {
                    //several annotations under the mouse pointer : if Shift key is pressed, then display popup with exposed annotations to allow precise selection

                    if (selectAnnotationPopup == null) {
                        selectAnnotationPopup = new SelectAnnotationPopup(annMarkerMgr.getTextContainerId(), new SelectAnnotationPopup.SelectingAnnotationCallback() {
                            @Override
                            public void annotationSelected(String markerId, String annotationId) {
                                updateMarkerSelection(markerId, annotationId, false, false);
                            }

                            @Override
                            public void annotationSelectionAborted() {
                                if (annotationReificator != null) {
                                    annotationReificator.resetWidget();
                                }
                            }
                        });
                    }

                    final Element selectedTargetElement = ascendantMarkerElts.get(0);
                    selectAnnotationPopup.setSelectableAnnotation(ascendantMarkerElts);
                    if (annotationReificator != null) {
                        annotationReificator.hideWidget();
                    }
                    selectAnnotationPopup.setPopupPositionAndShow(new PositionCallback() {
                        @Override
                        public void setPosition(int offsetWidth, int offsetHeight) {
                            selectAnnotationPopup.setPopupPosition(selectedTargetElement.getAbsoluteLeft() + selectedTargetElement.getOffsetWidth() - offsetWidth / 2, selectedTargetElement.getAbsoluteTop() + selectedTargetElement.getOffsetHeight() - offsetHeight / 2);
                        }
                    });
                }
            }
        }

        private void MouseClickOnMarker(ClickEvent event) {
            NativeEvent ntvEvent = event.getNativeEvent();
            EventTarget evtTarget = ntvEvent.getEventTarget();
            Element targetElement = evtTarget.cast();
            //find the main selected range mark
            targetElement = AnnotatedTextProcessor.getFirstEnclosingMarkerElement(targetElement, annMarkerMgr.getTextContainerId());
            if (targetElement != null) {
                String markerId = targetElement.getId();
                String annotationId = getMapper().getAnnotationIdFromMakerId(markerId);

                boolean multiSelectKeyDown = (!ShortCutToActionTypeMapper.isMacOs() && event.isControlKeyDown()) || (ShortCutToActionTypeMapper.isMacOs() && event.isMetaKeyDown());
                boolean removeIfPreviouslySelected = event.isControlKeyDown();

                updateMarkerSelection(targetElement.getId(), annotationId, multiSelectKeyDown, removeIfPreviouslySelected);
            }
        }

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        private void MouseClickOnOverlay(ClickEvent event) {
            dispEngine.onOverlayClick(event);
        }

        private void MouseMoveOverOverlay(MouseMoveEvent event) {
            NativeEvent ntvEvent = event.getNativeEvent();
            EventTarget evtTarget = ntvEvent.getEventTarget();
            Element targetElement = evtTarget.cast();
            if (isNewHoverTarget(ntvEvent)) {

                previoustargetElement = targetElement;
                previousShiftKeyStatus = ntvEvent.getShiftKey();

                //hide back the relations which were exposed when one of its argument was hovered
                veilTemporarilyUnveiledRelation();
                dispEngine.onMouseMove(event);
            }
        }

        private boolean isNewHoverTarget(NativeEvent ntvEvent) {
            EventTarget evtTarget = ntvEvent.getEventTarget();
            Element targetElement = evtTarget.cast();
            //to avoid unecessary processing, first check that hovered element is different or shift key status has changed 
            return (!targetElement.equals(previoustargetElement) || previousShiftKeyStatus != ntvEvent.getShiftKey());
        }

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        private AnnotationOverlayEventsHandler getGroupOverlayEventHandler() {
            return groupOverlayEventHandler;
        }

        private AnnotationOverlayEventsHandler getRelationOverlayEventHandler() {
            return relationOverlayEventHandler;
        }
    }

    class AnnMarkerMouseHandler implements ClickHandler, MouseMoveHandler, MouseUpHandler {

        private final AnnotationInteractionHandler interactionHandler;

        AnnMarkerMouseHandler(AnnotationInteractionHandler interactionHandler) {
            this.interactionHandler = interactionHandler;
        }

        @Override
        public void onClick(ClickEvent event) {
            interactionHandler.MouseClickOnMarker(event);
        }

        @Override
        public void onMouseUp(MouseUpEvent event) {
            //FIXME : implement "snap to boundary" constraint
            textSelectionChanged(event.getClientX(), event.getClientY());
        }

        @Override
        public void onMouseMove(MouseMoveEvent event) {
            interactionHandler.MouseMoveOverMarker(event);
        }
    }

    static class OverlayMouseHandler implements ClickHandler, MouseMoveHandler {

        private final AnnotationInteractionHandler interactionHandler;

        OverlayMouseHandler(AnnotationInteractionHandler interactionHandler) {
            this.interactionHandler = interactionHandler;
        }

        @Override
        public void onClick(ClickEvent event) {
            interactionHandler.MouseClickOnOverlay(event);
        }

        @Override
        public void onMouseMove(MouseMoveEvent event) {
            interactionHandler.MouseMoveOverOverlay(event);
        }
    };

    //displayer used only to compute Text Annotation clips (optionnaly with correction when spanning over several lines)
    private class AnnTextClipEvaluatorDisplayer implements TextAnnotationDisplayer, AnnotationDisplayerEngine.TextAnnotationMainDisplayer {

        private Map<String, List<DocumentView.FlankingClips>> flankingClipsByAnnotationId = new HashMap<String, List<DocumentView.FlankingClips>>();

        @Override
        public boolean processAnnotation(SpecifiedAnnotation annotation, String color, List<String> markerIds, boolean veiled) {
            return true;
        }

        @Override
        public void init(AnnotationDisplayerEngine engine) {
        }

        @Override
        public void onVeiledStatusRefreshed(String annotationId, boolean veiled) {
        }

        @Override
        public void complete() {
        }

        @Override
        public void reset(AnnotationDisplayerEngine engine) {
            flankingClipsByAnnotationId.clear();
        }

        @Override
        public Rect getAnnotationClip(Annotation annotation) {
            DocumentView.Rect clip = null;
            if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                clip = getAnnotatedTextClip(annotation.getId());
                if (clip != null) {
                    clip.left -= canvas.getAbsoluteLeft();
                    clip.top -= canvas.getAbsoluteTop();
                }
            }
            return clip;
        }

        @Override
        public List<DocumentView.FlankingClips> getAnnotationFragmentClips(Annotation annotation) {
            List<DocumentView.FlankingClips> clips = flankingClipsByAnnotationId.get(annotation.getId());
            if (clips == null) {
                clips = _getAnnotationFragmentClips(annotation);
                flankingClipsByAnnotationId.put(annotation.getId(), clips);
            }
            return clips;
        }

        private List<FlankingClips> _getAnnotationFragmentClips(Annotation annotation) {
            List<FlankingClips> fragmentClips = null;
            if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                fragmentClips = getAnnotatedTextFragmentClips(annotation.getId());
                if (fragmentClips != null) {
                    int canvasALeft = canvas.getAbsoluteLeft();
                    int canvasATop = canvas.getAbsoluteTop();

                    for (FlankingClips flanking : fragmentClips) {
                        //adjust to SVG coord
                        flanking.first.left -= canvasALeft;
                        flanking.first.top -= canvasATop;

                        if (flanking.last != flanking.first) {
                            flanking.last.left -= canvasALeft;
                            flanking.last.top -= canvasATop;
                        }
                    }
                }
            }
            return fragmentClips;
        }
    }

    public DocumentUi() {
        this(true);
    }

    public DocumentUi(boolean withScrollpanel) {
        canvas = new DrawingArea(10, 10);
        occurenceBar = new DrawingArea(10, 10);

        initWidget(uiBinder.createAndBindUi(this));
        this.withScrollpanel = withScrollpanel;

        if (!withScrollpanel) {
            Widget w = scrollPanel.getWidget();
            scrollPanelContainer.remove(scrollPanel);
            scrollPanelContainer.add(w);
            scrollPanel.remove(w);
        }
        canvas.setWidth(absolutePanel.getOffsetWidth());
        canvas.setHeight(absolutePanel.getOffsetHeight());

        eventBus = injector.getMainEventBus();

        groupDisplayer = new GroupDisplayer(canvas, eventBus, selectedGroups);
        relationDisplayer = new RelationDisplayer(canvas, eventBus, selectedRelations);
        occurenceDisplayer = new OccurenceDisplayer(occurencePanel, occurenceBar, withScrollpanel ? scrollPanel : null);
        extraDisplayer = new ExtraFeatureDisplayer();

        dispEngine = new AnnotationDisplayerEngine();
        dispEngine.addDisplayer(new AnnTextClipEvaluatorDisplayer());
        dispEngine.addDisplayer(groupDisplayer);
        dispEngine.addDisplayer(relationDisplayer);
        dispEngine.addDisplayer(extraDisplayer);
        dispEngine.addDisplayer(occurenceDisplayer);

        AnnotationInteractionHandler interactionHandler = new AnnotationInteractionHandler();
        dispEngine.addOverlayEventsHandler(interactionHandler.getGroupOverlayEventHandler());
        dispEngine.addOverlayEventsHandler(interactionHandler.getRelationOverlayEventHandler());

        annMarkerMgr = new AnnotationMarkerManager();
        contentHTML.getElement().setId(annMarkerMgr.getDocContainerId());
        //container of the SVG used to display Groups and Relations
        canvas.getElement().setId(annMarkerMgr.getDocumentCanvasId());
        //container of all absolute positioned div that are used as proxy to select underlying Group and Relation
        overlayContainer.getElement().setId(annMarkerMgr.getOverlayContainerId());

        annSetMenuItem.setHTML(SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.AnnotationSetsIcon()).getHTML()));

        interlineSizeHnd = new ApplicationOptions.PersistedOptionHandler("docui.interlinesize") {
            @Override
            public void valueChanged(Integer oldValue, Integer newValue) {
               performUpdateLineSize(oldValue==null ? 0 :oldValue, newValue);
            }
        };
        
        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        incLineSize.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                requestUpdateLineSize(+1);
            }
        });

        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        decLineSize.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                requestUpdateLineSize(-1);
            }
        });

        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        undoBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                undoEdit();
            }
        });

        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        redoBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                redoEdit();
            }
        });
        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        selectionModeBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            private boolean relationSelectionMode = false;

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                relationSelectionMode = !relationSelectionMode;
                if (relationSelectionMode) {
                    canvas.removeStyleName(style.BackGroundPos());
                    canvas.addStyleName(style.ForeGroundPos());
                } else {
                    canvas.removeStyleName(style.ForeGroundPos());
                    canvas.addStyleName(style.BackGroundPos());
                }
            }
        });
        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Catch key event for keyboard shortcuts
        focusPanel.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                keypressed(event);
            }
        });

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        mouseHandler = new AnnMarkerMouseHandler(interactionHandler);
        // Triggers event to respond to text selection changes
        contentHTML.addClickHandler(mouseHandler);
        contentHTML.addMouseUpHandler(mouseHandler);
        contentHTML.addMouseMoveHandler(mouseHandler);

        OverlayMouseHandler overlayMouseHandler = new OverlayMouseHandler(interactionHandler);
        overlayContainer.addClickHandler(overlayMouseHandler);
        overlayContainer.addMouseMoveHandler(overlayMouseHandler);
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        ResizeHandler resizeHandler = new ResizeHandler() {
            private boolean widthChanged = false;
            private boolean heightChanged = false;
            int prevWidth = -1;
            int prevHeight = -1;
            Timer drawExtraTimer = new Timer() {
                @Override
                public void run() {
                    _drawExtra(false, widthChanged, heightChanged);
                    widthChanged = false;
                    heightChanged = false;
                }
            };

            @Override
            public void onResize(ResizeEvent event) {
                int newWidth = event.getWidth();
                widthChanged = widthChanged || prevWidth != newWidth;
                prevWidth = newWidth;
                int newHeight = event.getHeight();
                heightChanged = heightChanged || prevHeight != newHeight;
                prevHeight = newHeight;
                if (widthChanged) {
                    clearCombinedAnnotationCanvas();
                }
                drawExtraTimer.schedule(200);
            }
        };

        // Redraw extra features (SVG and overlays) when the Document panel is resized
        resizeSpy.addResizeHandler(resizeHandler);

        // Redraw extra features (SVG and overlays) when the Browser Window is resized
        Window.addResizeHandler(resizeHandler);

        if (withScrollpanel) {
            //relocate Drag and Drap widgets after scrolling of the document
            scrollPanel.addScrollHandler(new ScrollHandler() {
                @Override
                public void onScroll(ScrollEvent event) {
                    if (annotationReificator != null) {
                        annotationReificator.resetWidgetPositions();
                    }
                }
            });
        }
        //
        titleBar.addDoubleClickHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                eventBus.fireEvent(new MaximizingWidgetEvent(DocumentUi.this, !maxmimized));
            }
        });
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        buttonManager = new ButtonEnablementManager();

        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //FIXME compute toolbar top and height at runtime
        toolBarExpandCollapseHandler = new ToolBarExpandCollapseHandler(expandCollapseImg, layoutPanel, toolBar, 0, 25, docPanel);
        expandCollapseImg.addClickHandler(toolBarExpandCollapseHandler);

        //disable Annotation editing since no doc is loaded yet
        setReadOnly(true);

    }
    // ~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~ //

    // ~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~ //
    private String getCurrentAnnotationType() {
        int index = annTypeList.getSelectedIndex();
        return index > -1 ? annTypeList.getItemText(index) : null;
    }

    //Annotation-type drop-down list decoration
    private void updatePanelAspect(Integer index) {
        if (index != null) {
            annTypeList.setSelectedIndex(index);
        }
        AnnotationSchemaDefinition schema = getDocument().getAnnotationSchema();

        if (getCurrentAnnotationType() != null) {
            String inlineStyle = "outline: " + schema.getAnnotationTypeDefinition(getCurrentAnnotationType()).getColor() + " solid 2px;";
            sp.getElement().setAttribute("style", inlineStyle);
        }
    }

    private void upTypeSelection() {
        int currentIndex = annTypeList.getSelectedIndex();
        int newIndex = currentIndex - 1;
        if (newIndex >= 0) {
            updatePanelAspect(newIndex);
        }

    }

    private void downTypeSelection() {
        int currentIndex = annTypeList.getSelectedIndex();
        int newIndex = currentIndex + 1;
        if (newIndex < annTypeList.getItemCount()) {
            updatePanelAspect(newIndex);
        }
    }

    private void prepareAnnotationTypeList() {
        if (annTypeListRegistration != null) {
            annTypeListRegistration.removeHandler();
        }

        int itemIndex = 0;
        AnnotationSchemaDefinition schema = getDocument().getAnnotationSchema();
        for (String annType : schemaManager.getEditedTextTypes()) {
            AnnotationTypeDefinition annTypeDef = schema.getAnnotationTypeDefinition(annType);
            annTypeList.addItem(annType);
            String optionStyle = "background-color:" + annTypeDef.getColor() + ";";
            annTypeList.getElement().getElementsByTagName("option").getItem(itemIndex).setAttribute("style", optionStyle);
            itemIndex++;
        }
        boolean NoEditableTextType = schemaManager.getEditedTextTypes().isEmpty();
        if (NoEditableTextType) {
            annTypeList.setVisible(false);
        } else {
            annTypeList.setVisible(true);

            annTypeListRegistration = annTypeList.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    updatePanelAspect(null);
                }
            });

            //change list selection when mousewheel is used
            annTypeList.addMouseWheelHandler(new MouseWheelHandler() {
                @Override
                public void onMouseWheel(MouseWheelEvent event) {
                    if (event.isNorth()) {
                        upTypeSelection();
                    } else {
                        downTypeSelection();
                    }
                    event.preventDefault();
                }
            });
            updatePanelAspect(0);
        }
    }

    @UiHandler("addSelectionButton")
    void addSelectionPopupButtonClickHandler(ClickEvent event) {
        ArrayList<DOMRange> ranges = annMarkerMgr.getSelectedRanges();
        extendSelectedAnnotationWithRange(ranges);
    }

    @UiHandler("delSelectionButton")
    void delSelectionPopupButtonClickHandler(ClickEvent event) {
        ArrayList<DOMRange> ranges = annMarkerMgr.getSelectedRanges();
        pruneRangeFromSelectedAnnotation(ranges);
    }
    // ~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~・~ //

    private void undoEdit() {
        if (undoManager != null) {
            String presName = undoManager.getUndoPresentationName();
            try {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                    eventBus.fireEvent(new InformationReleasedEvent(messages.undoing(presName)));
                } else {
                    eventBus.fireEvent(new InformationReleasedEvent(messages.nothingToUndo()));
                }
            } catch (CannotUndoException cannotUndoException) {
                //FIXME : should be displayed in dialogbox
                eventBus.fireEvent(new InformationReleasedEvent(messages.cannotUndo(cannotUndoException.getLocalizedMessage())));
            }
        }
    }

    private void redoEdit() {
        if (undoManager != null) {
            String presName = undoManager.getRedoPresentationName();
            try {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                    eventBus.fireEvent(new InformationReleasedEvent(messages.redoing(presName)));
                } else {
                    eventBus.fireEvent(new InformationReleasedEvent(messages.nothingToRedo()));
                }
            } catch (CannotRedoException cannotRedoException) {
                //FIXME : should be displayed in dialogbox
                eventBus.fireEvent(new InformationReleasedEvent(messages.cannotRedo(cannotRedoException.getLocalizedMessage())));
            }
        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     *
     * @return true is the point specified by its (x,y) coordinates is within
     * this DocumentUi area
     */
    @Override
    public boolean isPointInside(int x, int y) {
        boolean inside = true;
        Element elmt = this.getElement();
        if ((y < elmt.getAbsoluteTop()) || (y > elmt.getAbsoluteBottom())) {
            inside = false;
        } else {
            if ((x < elmt.getAbsoluteLeft()) || (x > elmt.getAbsoluteRight())) {
                inside = false;
            }
        }
        return inside;
    }

    /**
     * Increase or reduce the interline space
     *
     * @param step - the number of logical steps to move from current position
     * (-1 or 1). 0 to reset to minimum size.
     */
    private void requestUpdateLineSize(int step) {
        int newInterlineSizeIndex = interlineSizeIndex;
        if (step == 0) {
            newInterlineSizeIndex = 0;
        } else {
            newInterlineSizeIndex += step;
        }
        if (newInterlineSizeIndex <= 0) {
            newInterlineSizeIndex = 0;
        } else if (newInterlineSizeIndex >= GlobalStyles.getInterlineSizePx().length) {
            newInterlineSizeIndex = GlobalStyles.getInterlineSizePx().length - 1;
        }
        interlineSizeHnd.persistValue(newInterlineSizeIndex);
    }
    
    private void performUpdateLineSize(int currentInterlineSizeIndex, int newInterlineSizeIndex) {
        
        int prevScrollPos = scrollPanel.getVerticalScrollPosition();
        int prevMaxScrollPos = scrollPanel.getMaximumVerticalScrollPosition();
        
        contentHTML.removeStyleName(GlobalStyles.getInterlineStyleName(currentInterlineSizeIndex));

        interlineSizeIndex = newInterlineSizeIndex;
        contentHTML.addStyleName(GlobalStyles.getInterlineStyleName(interlineSizeIndex));
        
        //reset scrolling in order to show the part of text that was visible before resizing the line
        setScrollPositionAtRatio(prevScrollPos, prevMaxScrollPos);

        buttonManager.updateButtonStatuses();
        drawExtra();

    }

    private boolean canReduceLineSize() {
        return interlineSizeIndex > 0;
    }

    private boolean canIncreaseLineSize() {
        return interlineSizeIndex < GlobalStyles.getInterlineSizePx().length - 1;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Override
    public void onAnnotationSelectionChanged(GenericAnnotationSelectionChangedEvent event) {

        //TODO should someday simplify the selection scheme and does not distinguish 
        // the selection depending on annotation kind (see AnnotationTable.updateSelection() )

        boolean refreshNeeded = (getDocument() != null) && getDocument().equals(event.getAnnotatedTextHandler().getAnnotatedText());
        if (!refreshNeeded) {
            return;
        }
        if (event instanceof TextAnnotationSelectionChangedEvent) {

            hideCurrentlySelectedAnnotations();
            clearCurrentlySelectedAnnotations();
            if (!(event instanceof TextAnnotationSelectionEmptiedEvent)) {
                replaceCurrentlySelectedAnnotations(event.getAnnotationSelection());
                displayCurrentlySelectedAnnotations();
            } else {
                //GWT.log("TextAnnotationSelectionEmptiedEvent ");
            }
        }
        if (event instanceof RelationSelectionChangedEvent) {
            if (event instanceof RelationSelectionEmptiedEvent) {
                selectedRelations.clear();
            } else {
                selectedRelations.replaceSelection(event.getAnnotationSelection());
            }
            relationDisplayer.refresh();
        }
        if (event instanceof GroupSelectionChangedEvent) {
            if (event instanceof GroupSelectionEmptiedEvent) {
                selectedGroups.clear();
            } else {
                selectedGroups.replaceSelection(event.getAnnotationSelection());
            }
            groupDisplayer.refresh();
        }
    }

    /*
     * Enable DnD Relation creation
     */
    private void initDragController(boolean readOnly) {
        //init DnD Relation creation only if Relation creation is allowed
        if (readOnly || !schemaManager.canEditSomeRelationAnnotation()) {
            dragController = null;
            //this event handler instance correspond to the Annotation Reificator
            if (textAnnotationSelectionChangedRegistration != null) {
                textAnnotationSelectionChangedRegistration.removeHandler();
                textAnnotationSelectionChangedRegistration = null;
            }
            annotationReificator = null;
        } else {
            //enable DnD support

            // workaround for GWT issue 1813
            // http://code.google.com/p/google-web-toolkit/issues/detail?id=1813
            //RootPanel.get().getElement().getStyle().setProperty("position", "relative");

            if (dragController == null) {
                //by default, it is not allowed to drag a term outside the document view
                AbsolutePanel boundaryPanel = absolutePanel;

                dragController = new PickupDragController(boundaryPanel, false);

                dragController.setBehaviorConstrainedToBoundaryPanel(true);
            }
            if (annotationReificator == null) {

                final DroppingAnnotationCallback droppingAnnotationCallback = new DroppingAnnotationCallback() {
                    @Override
                    public void annotationDropped(final String sourceAnnotationId, String targetAnnotationId, Element targetElement) {


                        List<Element> ascendantMarkerElts = AnnotatedTextProcessor.getAscendantMarkerElements(targetElement, getTextContainerId());
                        int nbAscendantMarker = ascendantMarkerElts.size();

                        //filter-out markers corresponding to un-referenceable annotations
                        {
                            Iterator<Element> iterator = ascendantMarkerElts.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                String annId = AnnotatedTextProcessor.getAnnotationIdFromMarker(e);
                                if (!getAnnotatedTextHandler().isReferenceableAnnotation(annId)) {
                                    iterator.remove();
                                }
                            }
                        }

                        if (nbAscendantMarker > 1) {
                            final SelectAnnotationPopup selectAnnotationPopup = new SelectAnnotationPopup(getTextContainerId(), new SelectAnnotationPopup.SelectingAnnotationCallback() {
                                @Override
                                public void annotationSelected(String markerId, String annotationId) {
                                    ArrayList<String> annotationIds = new ArrayList<String>();
                                    annotationIds.add(sourceAnnotationId);
                                    annotationIds.add(annotationId);
                                    CombinedAnnotationCreationHelper.CreateRelation(getAnnotatedTextHandler(), annotationIds);
                                }

                                @Override
                                public void annotationSelectionAborted() {
                                }
                            });

                            final Element selectedTargetElement = ascendantMarkerElts.get(0);

                            //filter-out marker corresponding to the dragged annotation
                            Iterator<Element> iterator = ascendantMarkerElts.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                if (sourceAnnotationId.equals(AnnotatedTextProcessor.getAnnotationIdFromMarker(e))) {
                                    iterator.remove();
                                }
                            }

                            selectAnnotationPopup.setSelectableAnnotation(ascendantMarkerElts);
                            selectAnnotationPopup.setPopupPositionAndShow(new PositionCallback() {
                                @Override
                                public void setPosition(int offsetWidth, int offsetHeight) {
                                    selectAnnotationPopup.setPopupPosition(selectedTargetElement.getAbsoluteLeft() + selectedTargetElement.getOffsetWidth() - offsetWidth / 2, selectedTargetElement.getAbsoluteTop() + selectedTargetElement.getOffsetHeight() - offsetHeight / 2);
                                }
                            });


                        } else if (nbAscendantMarker == 1) {
                            ArrayList<String> annotationIds = new ArrayList<String>();
                            //GWT.log("Direct Relation : " + sourceAnnotationId + " <-> " + targetAnnotationId);

                            annotationIds.add(sourceAnnotationId);
                            annotationIds.add(targetAnnotationId);
                            CombinedAnnotationCreationHelper.CreateRelation(getAnnotatedTextHandler(), annotationIds);
                        }
                    }
                };

                annotationReificator = new AnnotationReificator(this, dragController, droppingAnnotationCallback, style, contentHTML, scrollPanel, absolutePanel);
            }
            textAnnotationSelectionChangedRegistration = TextAnnotationSelectionChangedEvent.register(eventBus, annotationReificator);
        }
    }

    @Override
    public AnnotatedText getDocument() {
        return mapper == null ? null : mapper.getAnnotatedText();
    }

    @Override
    public AnnotatedTextHandler getAnnotatedTextHandler() {
        return mapper == null ? null : mapper.getAnnotatedTextHandler();
    }

    @Override
    public UndoManager getUndoManager() {
        return undoManager;
    }

    /**
     * Display a document specified by string containing the serialized JSON
     * representation
     */
    public void setDocument(String json, Options options) {
        AnnotatedTextHandler handler = null;
        AnnotatedTextImpl newDoc = null;
        try {
            newDoc = AnnotatedTextImpl.createFromJSON(json);
            //create Modification Handler
            handler = AnnotatedTextHandler.createHandler(0, 0, newDoc);
        } catch (Exception e) {
            AlvisaeCore.firebugLog(e.getMessage());
        }
        setDocument(handler, options);
    }

    @Override
    public void setDocument(final AnnotatedTextHandler annotatedDoc, final boolean readOnly) {
        this.options.setReadOnly(readOnly);
        setDocument(annotatedDoc, options);
    }

    @Override
    public void setDocument(final AnnotatedTextHandler annotatedDoc, final Options options) {
        this.options = new Options(options);

        //reset toolbar visibility & collapsed state
        toolBarExpandCollapseHandler.setExpanded(!options.isHiddenToolbar() && !options.isCollapsedToolbar());
        expandCollapseImg.setVisible(!options.isHiddenToolbar());

        //reset TitleBar visibility
        if (options.isHiddenTitlebar()) {
            mainLayoutPanel.setWidgetTopHeight(titleBar, 0, Unit.PX, 0, Unit.PX);
            mainLayoutPanel.setWidgetTopBottom(layoutPanel, 0, Unit.PX, 0, Unit.PX);
        } else {
            mainLayoutPanel.setWidgetTopHeight(titleBar, 0, Unit.PX, 25, Unit.PX);
            mainLayoutPanel.setWidgetTopBottom(layoutPanel, 25, Unit.PX, 0, Unit.PX);
        }

        //reset OccurenceBar visibility
        if (options.isHiddenOccurencebar()) {
            docPanel.setWidgetRightWidth(occurencePanel, 0, Unit.PX, 0, Unit.PX);
            docPanel.setWidgetLeftRight(docTextPanel, 0, Unit.PX, 0, Unit.PX);
        } else {
            docPanel.setWidgetRightWidth(occurencePanel, 0, Unit.PX, 1.2, Unit.EM);
            docPanel.setWidgetLeftRight(docTextPanel, 0, Unit.PX, 1.2, Unit.EM);
        }


        final int interlineIndex = options.getInterlineSizeIndex() != null ? options.getInterlineSizeIndex() : interlineSizeHnd.getValue(3);


        //int title bar with optionnal hyperlink (e.g. link to a pdf file)
        if (annotatedDoc != null) {
            Properties props = annotatedDoc.getAnnotatedText().getDocument().getProperties();
            String description = annotatedDoc.getAnnotatedText().getDocument().getDescription();
            String taskName;
            String idsHint = "";
            if (annotatedDoc.getEditableUsersAnnotationSet() != null) {
                idsHint = annotatedDoc.getEditableUsersAnnotationSet().getId() + ":  [" + annotatedDoc.getEditableUsersAnnotationSet().getOwner();
            }
            TaskDefinition task = annotatedDoc.getAnnotatedText().getEditedTask();
            if (task != null) {
                taskName = task.getName() + " : ";
                idsHint += "@" + task.getId();
            } else {
                taskName = "";
            }
            if (!idsHint.isEmpty()) {
                idsHint += "]";
                titleLabel.getElement().setAttribute("title", idsHint);
            } else {
                titleLabel.getElement().removeAttribute("title");
            }
            if (props != null && props.getKeys().contains("hlink")) {
                titleLabel.setVisible(!taskName.isEmpty());
                titleLabel.setText(taskName);
                titleHLink.setVisible(true);
                titleHLink.setHref(props.getValues("hlink").get(0));
                titleHLink.setText(description);
            } else {
                titleLabel.setVisible(true);
                titleLabel.setText(taskName + description);
                titleHLink.setVisible(false);
                titleHLink.setText("");
            }
        } else {
            titleLabel.setVisible(false);
            titleLabel.setText("");
            titleLabel.getElement().removeAttribute("title");
            titleHLink.setVisible(false);
            titleHLink.setText("");
        }

        //remove any remaining TextAnnotation marker (from previous document if any)
        clearAnchorMarkerSelection();
        contentHTML.addStyleName(style.Processing());

        //inform other components that a long processing is about to start (=>wait banner will be displayed)
        eventBus.fireEvent(
                new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing, null));

        signalTermExposition(annotatedDoc);

        //long processing is splitted into several command to avoid screen freezing
        Scheduler.get()
                .scheduleIncremental(new RepeatingCommand() {
            ArrayList<Command> cmds = new ArrayList<Command>();

            {
                cmds.add(new Command() {
                    @Override
                    public void execute() {
                        buttonManager.setButtonsEnabled(false);
                        schemaManager.clear();
                        //drop undoable edits from previous document 
                        removeUndoManager();
                        //clear any remaining data related to SVG widget 
                        dispEngine.reset();

                        //clear SVG 
                        canvas.clear();
                        canvas.addStyleName(style.Processing());
                        //remove remaining text of the previous document
                        contentHTML.setHTML("");
                        //reset size of the SVG canvas to the size of the document panel
                        canvas.setWidth(absolutePanel.getOffsetWidth());
                        canvas.setHeight(absolutePanel.getOffsetHeight());
                        //canvas.setWidth(contentHTML.getOffsetWidth());
                        //canvas.setHeight(contentHTML.getOffsetHeight());

                        //clear toolbar's TextAnnotation type list from previous document schema
                        annTypeList.clear();
                        sp.getElement().removeAttribute("style");

                        //reset AnnotationSet selection menu
                        resetAnnSetSelectionMenu();

                        //reset interline size
                        requestUpdateLineSize(0);
                        requestUpdateLineSize(interlineIndex);

                        //temporarily switch to readonly during init phase
                        setReadOnly(true);

                        //inform other components that this DocumentUI widget is clear of any document
                        eventBus.fireEvent(new WorkingDocumentChangedEvent(null, DocumentUi.this, WorkingDocumentChangedEvent.ChangeType.Unloaded));
                    }
                });

                final AnnotatedTextHandler freshenedDoc = annotatedDoc;

                if (annotatedDoc != null) {
                    //processings to perform only if a new Document is being displayed
                    cmds.add(new Command() {
                        @Override
                        public void execute() {

                            //create StyleSheet corresponding to the Document Annotation Schema 
                            Element inLinedStyle = Document.get().createStyleElement();
                            inLinedStyle.setInnerText(AnnotatedTextProcessor.getSampleCStyleSheet(freshenedDoc.getAnnotatedText().getAnnotationSchema()));
                            inLinedStyle.setAttribute("type", "text/css");
                            contentHTML.getElement().insertFirst(inLinedStyle);
                            //FIXME casting 
                            mapper = AnnotationDocumentViewMapper.newMapper(freshenedDoc, DocumentUi.this);
                            annMarkerMgr.reset(freshenedDoc);

                            //TODO implement https://migale.jouy.inra.fr/redmine/issues/1594 here!!
                            //Veiled all Relations
                            if (options.isVeiledRelations()) {
                                for (Annotation annotation : getDocument().getAnnotations(AnnotationKind.RELATION)) {
                                    mapper.setVeiled(annotation.getId());
                                }
                            }

                        }
                    });

                    cmds.add(new Command() {
                        @Override
                        public void execute() {
                            schemaManager.setTaskSchema(getDocument().getEditedTask(), getDocument().getAnnotationSchema());
                            //fill toolbar's TextAnnotation type list with current document annotation schema
                            prepareAnnotationTypeList();
                            //reinitialize AnnotationSet selection menu
                            reinitAnnSetSelectionMenu();
                            //regenerate HTML contening the document text, it's formatting and the TextAnnotation boxes 
                            annMarkerMgr.refreshDocument();
                        }
                    });

                } else {
                    //processing to perform if no Document is being displayed

                    cmds.add(new Command() {
                        @Override
                        public void execute() {
                            //remove remaining text of the previous document
                            contentHTML.setHTML("");
                            annMarkerMgr.reset(null);
                        }
                    });
                }

                cmds.add(new Command() {
                    @Override
                    public void execute() {
                        //set the intended readOnly status
                        DocumentUi.this.setReadOnly(options.isReadOnly());

                        //init agent in charge of displaying Relation, Group & occurences SVG representation 
                        groupDisplayer.setDocument(freshenedDoc, mapper, relationDisplayer);
                        relationDisplayer.setDocument(freshenedDoc, mapper, groupDisplayer);
                        dispEngine.setDocument(freshenedDoc, mapper);
                        //actually draw SVG representation 
                        drawExtra();
                        //
                        canvas.removeStyleName(style.Processing());
                        contentHTML.removeStyleName(style.Processing());

                        //inform other components that this DocumentUI widget is now displaying the specified document
                        eventBus.fireEvent(new WorkingDocumentChangedEvent(freshenedDoc, DocumentUi.this, WorkingDocumentChangedEvent.ChangeType.Loaded));
                        //inform other components that application long processing is finished (=>wait banner will be removed)
                        eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, null));
                        buttonManager.setButtonsEnabled(true);
                    }
                });

                //scroll into view the annotation if any specified
                final String scrolledAnnotationId = options.getScrolledAnnotation();
                if (scrolledAnnotationId != null) {

                    cmds.add(new Command() {
                        @Override
                        public void execute() {
                            setScrollPositionAtAnnotation(scrolledAnnotationId);
                        }
                    });
                }

            }

            @Override
            public boolean execute() {
                //actually execute the commands initialized above
                if (cmds.size() > 0) {
                    Command nextCmd = cmds.remove(0);
                    nextCmd.execute();
                }
                return cmds.size() > 0;
            }
        });

    }

    // -------------------------------------------------------------------------
    @Override
    public Rect getAnnotatedTextClip(String annotationId) {
        ArrayList<String> mkrIds = mapper.getMarkerIdsFromAnnotationId(annotationId);
        if (!mkrIds.isEmpty()) {
            String markId = mkrIds.get(0);
            Element srcElt = Document.get().getElementById(markId);
            return getAnnotatedTextClip(srcElt);
        } else {
            return null;
        }
    }

    //retrieve clips of the first and the last box of each annotation fragments of the specified text annotation
    public List<FlankingClips> getAnnotatedTextFragmentClips(String annotationId) {
        ArrayList<String> mkrIds = mapper.getMarkerIdsFromAnnotationId(annotationId);
        if (!mkrIds.isEmpty()) {
            ArrayList<FlankingClips> fragmentsClips = new ArrayList<FlankingClips>();
            for (String markId : mkrIds) {
                Element srcElt = Document.get().getElementById(markId);
                fragmentsClips.add(getRectifiedAnnotationTextFragmentClips(srcElt, true));
            }
            return fragmentsClips;
        } else {
            return null;
        }
    }

    @Override
    public Rect getAnnotatedTextClip(Element srcElt) {
        FlankingClips rectified = getRectifiedAnnotationTextFragmentClips(srcElt, false);
        return rectified == null ? null : rectified.first;
    }

    public FlankingClips getRectifiedAnnotationTextFragmentClips(Element srcElt, boolean rectifyLast) {

        if (srcElt != null) {
            Rect lastClip = null;

            final int aLeft = srcElt.getAbsoluteLeft();
            final int aRight = srcElt.getAbsoluteRight();
            final int aTop = srcElt.getAbsoluteTop();
            final int aBottom = srcElt.getAbsoluteBottom();


            int firstboxLeft = aLeft;
            int firstboxTop = aTop;
            int firstboxBottom = aBottom;
            //if the Annotation Box is taller than the line-height, it surely means that the Annotation spans over 2 or more lines
            if ((aBottom - aTop) > GlobalStyles.getInterlineSizePx()[interlineSizeIndex]) {

                //Note : the inlined element used to show Text Annotation can be spanned over several lines.
                //       Thus, the value returned by Element.getAbsoluteLeft() correspond to the much larger 
                //       rectangle enclosing the several boxes created by the browser to display the annotation
                //
                //       A temporary unbreakable element created just before the annotation must have the same 
                //       left coordinate, otherwise it means that the annotation spans over 2 or more lines.
                //       
                Element tempElement = Document.get().createSpanElement();
                Element parentElement = srcElt.getParentElement();
                parentElement.insertBefore(tempElement, srcElt);
                tempElement.setInnerText("");

                if (tempElement.getAbsoluteLeft() != aLeft) {
                    //determine the size of the box on the first line

                    //keep the right coordinate of the source element (=the exact right coordinate of the box on the first line)
                    firstboxLeft = tempElement.getAbsoluteLeft();
                    firstboxTop = tempElement.getAbsoluteTop();
                    firstboxBottom = tempElement.getAbsoluteBottom();

                }
                tempElement.removeFromParent();

                if (rectifyLast) {
                    //determine the size of the box on the last line

                    //simple, but approximate method : add a sibling just after. Does not work if new sibling is moved to following line
                    Element probeElement2 = Document.get().createSpanElement();
                    parentElement.insertAfter(probeElement2, srcElt);
                    probeElement2.setInnerText("");

                    int lastboxLeft = aLeft;
                    int lastboxTop = probeElement2.getAbsoluteTop();
                    //the right side of the box is at the left side of the proging element
                    int lastboxWidth = probeElement2.getAbsoluteLeft() - lastboxLeft;
                    int lastboxHeight = lastboxTop - aBottom;
                    lastClip = new Rect(lastboxLeft, lastboxTop, lastboxWidth, lastboxHeight);

                    probeElement2.removeFromParent();
                }

            }

            int sWidth = aRight - firstboxLeft;
            int sHeight = firstboxBottom - firstboxTop;
            return new FlankingClips(new Rect(firstboxLeft, firstboxTop, sWidth, sHeight), lastClip);
        } else {
            return null;
        }
    }

    // -------------------------------------------------------------------------
    private void textSelectionChanged(final int x, final int y) {

        annMarkerMgr.clearLastSelection();

        eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing, null));
        Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
            @Override
            public boolean execute() {
                ArrayList<DOMRange> ranges = annMarkerMgr.getSelectedRanges();

                RangeSelectionChangedEvent rangeSelectChangedEvent = new RangeSelectionChangedEvent(getAnnotatedTextHandler(), ranges);
                eventBus.fireEvent(rangeSelectChangedEvent);

                List<Fragment> targets = annMarkerMgr.computeTargets(ranges);
                TargetSelectionChangedEvent targetSelectChangedEvent = new TargetSelectionChangedEvent(targets);
                eventBus.fireEvent(targetSelectChangedEvent);

                eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, null));
                return false;
            }
        }, 1);
    }
    // -------------------------------------------------------------------------

    // Process keyboard shortcuts
    private void keypressed(KeyPressEvent event) {
        //FIXME : check that this document UI is the active one

        ShortCutTriggeredActionType shortcut = ShortCutToActionTypeMapper.getHappenedActionType(event.getCharCode(), event.getNativeEvent().getKeyCode(), event.isControlKeyDown(), event.isAltKeyDown(), event.isMetaKeyDown(), event.isShiftKeyDown());
        if (shortcut != null) {
            switch (shortcut) {
                case INCREASELINESIZE:
                    requestUpdateLineSize(+1);
                    break;
                case DEREASELINESIZE:
                    requestUpdateLineSize(-1);
                    break;
                case TOGGLESELECTONMODE:
                    selectionModeBtn.setValue(!selectionModeBtn.getValue(), true);
                    break;
                default:
                    if (!isReadOnly()) {
                        switch (shortcut) {
                            case UNDO:
                                undoEdit();
                                break;
                            case REDO:
                                redoEdit();
                                break;
                            case CREATEANNOTATION:
                                createAnchorMarkersFromSelectedRanges();
                                break;
                            case REMOVEANNOTATION:
                                removeSelectedTextAnnotations();
                                break;
                        }
                    }
            }
        }
    }

    // -------------------------------------------------------------------------
    private void refreshAllVeiledStatus() {
        Set<String> veiledAnnotations = getMapper().getVeiledAnnotationIds();
        for (String annotationId : getMapper().getAnnotationIds()) {
            refreshElementVeiledStatus(annotationId, veiledAnnotations.contains(annotationId));
        }
    }

    private void refreshElementVeiledStatus(String annotationId, boolean veiled) {
        Annotation annotation = getMapper().getAnnotation(annotationId);
        if (annotation != null) {
            if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                ArrayList<String> markerIds = getMapper().getMarkerIdsFromAnnotationId(annotationId);
                for (String mId : markerIds) {
                    Element e = Document.get().getElementById(mId);
                    if (e != null) {
                        if (veiled) {
                            e.setAttribute(GlobalStyles.VeiledAttr, "true");
                        } else {
                            e.removeAttribute(GlobalStyles.VeiledAttr);
                        }
                    }
                }
                dispEngine.onVeiledStatusRefreshed(annotationId, veiled);
            } else if (annotation.getAnnotationKind().equals(AnnotationKind.GROUP)) {

                groupDisplayer.refreshVeiledStatus(annotationId);
            } else if (annotation.getAnnotationKind().equals(AnnotationKind.RELATION)) {

                relationDisplayer.refreshVeiledStatus(annotationId);
            }
        }
    }

    private void refreshElementsVeiledStatus(List<String> annotationIds) {
        if (getMapper() != null) {
            for (String annotationId : annotationIds) {
                refreshElementVeiledStatus(annotationId, getMapper().isVeiled(annotationId));
            }
        }
    }
    // -------------------------------------------------------------------------

    private void drawExtra() {
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                _drawExtra();
            }
        });
    }

    public void _drawExtra() {
        _drawExtra(true, false, false);
    }

    private void clearCombinedAnnotationCanvas() {
        canvas.clear();
        canvas.setWidth(absolutePanel.getOffsetWidth());
        canvas.setHeight(absolutePanel.getOffsetHeight());
        overlayContainer.setHTML("");
    }

    private void _drawExtra(boolean forced, boolean widthChanged, boolean heightChanged) {

        AlvisaeCore.speedTracerlog("DrawExtra - Start...");
        //if width did not change, Relation/Group do not need to be draw again 
        if (forced || widthChanged) {
            clearCombinedAnnotationCanvas();
        } else {
            groupDisplayer.setRefreshOptional();
            relationDisplayer.setRefreshOptional();
            extraDisplayer.setRefreshOptional();
        }
        //if height did not change, the occurance bar does not need to be draw again
        if (!forced && !heightChanged) {
            occurenceDisplayer.setRefreshOptional();
        }
        int interlineSizePx = GlobalStyles.getInterlineSizePx()[interlineSizeIndex];
        relationDisplayer.setInterlineSize(interlineSizePx);
        dispEngine.proceed(canvas, overlayContainer.getElement());

        AlvisaeCore.speedTracerlog("DrawExtra - End.");
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // -------------------------------------------------------------------------
    @Override
    public void clearTextSelection(boolean setFocus) {
        annMarkerMgr.clearLastSelection();
        AnnotationMarkerManager.clearNativeSelection();
        if (setFocus && !isReadOnly()) {
            focusPanel.setFocus(true);
        }
    }

    private void clearCurrentlySelectedAnnotations() {
        selectedTextAnnotations.clear();
    }

    private void replaceCurrentlySelectedAnnotations(AnnotationSelections annotationSelection) {
        selectedTextAnnotations.replaceSelection(annotationSelection);
    }

    private GenericAnnotationSelection changeCurrentlySelectedAnnotations(Annotation annotation, String mainSelectedMark, boolean removeIfPreviouslySelected) {
        GenericAnnotationSelection selection = null;
        if (annotation != null) {
            if (removeIfPreviouslySelected && selectedTextAnnotations.isAnnotationSelected(annotation.getId())) {
                selectedTextAnnotations.removeAnnotationFromSelection(annotation.getId());
            } else {
                selection = selectedTextAnnotations.addAnnotationSelection(annotation, mainSelectedMark, getMapper().getMarkerIdsFromAnnotationId(annotation.getId()));
            }
        }
        return selection;
    }

    @Override
    public void clearAnchorMarkerSelection() {
        clearCurrentlySelectedAnnotations();
        //fire event to inform that annotation selection has changed
        AnnotatedTextHandler annotatedTextHandler = getAnnotatedTextHandler();
        if (annotatedTextHandler != null) {
            eventBus.fireEvent(new TextAnnotationSelectionEmptiedEvent(annotatedTextHandler));
        }
    }

    private void updateMarkerSelection(String markerId, String annotationId, boolean multiSelectKeyDown, boolean removeIfPreviouslySelected) {

        hideCurrentlySelectedAnnotations();
        //[Ctrl]/[Command] key used for multiselect
        if (!multiSelectKeyDown) {
            //clear previous selection
            clearAnchorMarkerSelection();
        }

        if (annotationId != null) {
            Annotation annotation = getMapper().getAnnotation(annotationId);
            changeCurrentlySelectedAnnotations(annotation, markerId, removeIfPreviouslySelected);

            //#2452 remove Group or Relation selection if no explicit multiselection requested 
            if (!multiSelectKeyDown) {
                eventBus.fireEvent(new GroupSelectionEmptiedEvent(getAnnotatedTextHandler()));
                eventBus.fireEvent(new RelationSelectionEmptiedEvent(getAnnotatedTextHandler()));
            }

            //fire event to inform that annotation selection has changed
            if (selectedTextAnnotations.isEmpty()) {
                eventBus.fireEvent(new TextAnnotationSelectionEmptiedEvent(getAnnotatedTextHandler()));
            } else {
                eventBus.fireEvent(new TextAnnotationSelectionChangedEvent(getAnnotatedTextHandler(), selectedTextAnnotations));
            }
        }
    }

    /**
     * Remove graphical hints used to show that some annotation/marker is
     * selected
     */
    private void hideCurrentlySelectedAnnotations() {
        for (GenericAnnotationSelection selectedAnn : selectedTextAnnotations.getTextAnnotationSelection()) {
            if (selectedAnn instanceof TextAnnotationSelection) {
                ArrayList<String> markerIds = ((TextAnnotationSelection) selectedAnn).getSelectedMarkers();
                for (String selectId : markerIds) {
                    Element e = Document.get().getElementById(selectId);
                    if (e != null) {
                        e.removeClassName(GlobalStyles.SelectedAnnotation);
                        e.removeClassName(GlobalStyles.MainSelectedAnnotation);
                        e.setAttribute(GlobalStyles.SelectedAttr, "false");
                    }
                }
            }
        }
    }

    /**
     * Graphically highlight the current annotations/markers selection
     */
    private void displayCurrentlySelectedAnnotations() {
        Scheduler.get().scheduleFinally(new Command() {
            @Override
            public void execute() {
                boolean highlightMainMarker = true;
                for (int i = selectedTextAnnotations.getSelections().size() - 1; i >= 0; i--) {
                    GenericAnnotationSelection selectedAnn = selectedTextAnnotations.getSelections().get(i);
                    if (selectedAnn instanceof TextAnnotationSelection) {
                        ArrayList<String> markerIds = ((TextAnnotationSelection) selectedAnn).getSelectedMarkers();
                        //selection of the entire TEXT annotation
                        if (markerIds.isEmpty()) {
                            ArrayList<String> newMarkers = getMapper().getMarkerIdsFromAnnotationId(selectedAnn.getAnnotation().getId());
                            markerIds.addAll(newMarkers);
                        }
                        for (String mId : markerIds) {
                            Element e = Document.get().getElementById(mId);
                            if (e != null) {
                                if (highlightMainMarker) {
                                    e.addClassName(GlobalStyles.MainSelectedAnnotation);
                                    highlightMainMarker = false;
                                } else {
                                    e.addClassName(GlobalStyles.SelectedAnnotation);
                                }
                                e.setAttribute(GlobalStyles.SelectedAttr, "true");
                            }
                        }
                    }
                }
            }
        });
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Override
    public void extendSelectedAnnotationWithRange(final List<DOMRange> ranges) {
        if (isReadOnly()) {
            throw new UnsupportedOperationException("Modification not allowed on read-only Document!");
        }

        if (ranges != null && !ranges.isEmpty()) {
            final Annotation annotation = selectedTextAnnotations.getMainSelectedTextAnnotation();
            clearAnchorMarkerSelection();

            if (annotation != null) {
                eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing, null));

                Scheduler.get().scheduleDeferred(new Command() {
                    @Override
                    public void execute() {
                        List<Fragment> targets = annMarkerMgr.computeTargets(ranges);
                        addFragmentToAnnotation(annotation.getId(), targets);
                        eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, null));
                    }
                });
            }
        }
    }

    @Override
    public void pruneRangeFromSelectedAnnotation(final List<DOMRange> ranges) {
        if (isReadOnly()) {
            throw new UnsupportedOperationException("Modification not allowed on read-only Document!");
        }

        if (ranges != null && !ranges.isEmpty()) {
            final Annotation annotation = selectedTextAnnotations.getMainSelectedTextAnnotation();
            clearAnchorMarkerSelection();

            if (annotation != null) {
                eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing, null));

                Scheduler.get().scheduleDeferred(new Command() {
                    @Override
                    public void execute() {
                        List<Fragment> targets = annMarkerMgr.computeTargets(ranges);
                        removeFragmentFromAnnotation(annotation.getId(), targets);
                        eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, null));
                    }
                });
            }
        }
    }

    @Override
    public void createAnchorMarkersFromSelectedRanges() {
        if (annotationReificator != null) {
            annotationReificator.hideWidget();
        }
        ArrayList<DOMRange> ranges = annMarkerMgr.getSelectedRanges();
        createAnchorMarkersFromRanges(getCurrentAnnotationType(), ranges);
    }
    private static RegExp regexStartingSpaces = RegExp.compile("^(\\s*)");
    private static RegExp regexEndingSpaces = RegExp.compile("(\\s*)$");

    @Override
    public void createAnchorMarkersFromRanges(final String newAnnotationType, final List<DOMRange> ranges) {
        if (!schemaManager.canEditSomeTextAnnotation()) {
            return;
        }

        if (isReadOnly()) {
            throw new UnsupportedOperationException("Modification not allowed on read-only Document!");
        }

        if (ranges != null && !ranges.isEmpty()) {
            clearAnchorMarkerSelection();

            eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing, null));


            Scheduler.get().scheduleDeferred(new Command() {
                @Override
                public void execute() {

                    List<Fragment> targets = annMarkerMgr.computeTargets(ranges);

                    //Check if Fragments include bordering whitespaces
                    List<Fragment> trimmedTargets = new ArrayList<Fragment>();
                    boolean someHaveBeenTrimmed = false;
                    int i = 0;
                    List<String> txtFrags = getDocument().getDocument().getFragmentsText(targets);
                    for (String txtFrag : txtFrags) {
                        String newTxtFrag = regexStartingSpaces.replace(txtFrag, "");
                        int startOffset = txtFrag.length() - newTxtFrag.length();

                        newTxtFrag = regexEndingSpaces.replace(newTxtFrag, "");
                        int newLength = newTxtFrag.length();
                        Fragment srcTarget = targets.get(i);
                        int newStart = srcTarget.getStart() + startOffset;
                        int newEnd = newStart + newLength;

                        if (newStart != srcTarget.getStart() || newEnd != srcTarget.getEnd()) {
                            someHaveBeenTrimmed = true;
                        }
                        //GWT.log("[" + srcTarget.getStart() + "," + srcTarget.getEnd() + "]" + " >> " + "[" + newStart + "," + newEnd + "]");
                        if (newLength != 0) {
                            trimmedTargets.add(FragmentImpl.create(newStart, newEnd));
                        }
                        i++;
                    }
                    if (someHaveBeenTrimmed) {
                        //FIXME: Not I18N
                        if (Window.confirm("Would you like to remove whitespace(s) bordering the text selection?")) {
                            targets = trimmedTargets;
                            if (trimmedTargets.isEmpty()) {
                                Window.alert("Text selection is empty after whitespace(s) removal!");
                                return;
                            }
                        }
                    }

                    checkAndCreateTextAnnotation(newAnnotationType, targets, null, true);
                    eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, null));

                }
            });
        }

    }

    private boolean checkAndCreateTextAnnotation(String newAnnotationType, List<Fragment> targets, Properties props, boolean verbose) {
        boolean result;

        AnnotationSchemaDefinition params = getDocument().getAnnotationSchema();
        //Check boundary constraints
        Annotation annotation = ((AnnotatedTextImpl) getDocument()).createLooseTextAnnotation("new textAnnotation", newAnnotationType, targets, null, null);
        BasicAnnotationSchemaValidator validator = new BasicAnnotationSchemaValidator();
        BasicFaultListener<SafeHtml> faultLstnr = new BasicFaultListener<SafeHtml>(faultMessages);
        faultLstnr.reset();
        validator.setAnnotatedText(getDocument());
        if (validator.checkBoundaries(faultLstnr, annotation, true)) {
            annMarkerMgr.createMarkersForTargets(getAnnotatedTextHandler(), params, newAnnotationType, targets, props);
            result = true;
        } else {
            if (verbose) {
                //display error image in the margin of the line containing the conflicting annotation
                docTextPanel.setWidgetTopHeight(errorImage, 10, Unit.PX, errorImage.getHeight(), Unit.PX);
                docTextPanel.setWidgetLeftWidth(errorImage, 10, Unit.PX, errorImage.getWidth(), Unit.PX);
                Annotation a = faultLstnr.getConflictingAnnotation(faultLstnr.getMessages().size() - 1);
                ArrayList<String> markerIds = mapper.getMarkerIdsFromAnnotationId(a.getId());

                Element e = Document.get().getElementById(markerIds.get(0));
                if (e != null) {
                    docTextPanel.setWidgetTopHeight(errorImage, e.getOffsetTop() - scrollPanel.getVerticalScrollPosition(), Unit.PX, 25, Unit.PX);
                }
                errorImage.setVisible(true);
                new Blinker(errorImage, new int[]{120, 40, 100, 40, 100, 1200, 10}).start();

                injector.getMainEventBus().fireEvent(new InformationReleasedEvent("<span style='color:red;'>" + faultLstnr.getLastMessage() + "</span>"));
            }
            result = false;
        }
        validator.setAnnotatedText(null);
        return result;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @UiHandler("addAnnotButton")
    void handleAddAnnButtonClick(ClickEvent e) {
        try {
            addAnnotButton.setEnabled(false);
            createAnchorMarkersFromSelectedRanges();
            clearAnchorMarkerSelection();
            clearTextSelection(true);
        } finally {
            buttonManager.setButtonsEnabled(true);
        }
    }

    @UiHandler("deleteAnnButton")
    void handleDeleteMarkButtonClick(ClickEvent e) {
        try {
            deleteAnnButton.setEnabled(false);
            removeSelectedTextAnnotations();
        } finally {
            buttonManager.setButtonsEnabled(true);
        }
    }

    /*
     * @UiHandler("editAnnButton") void handleEditMarkButtonClick(ClickEvent e)
     * { try { editAnnButton.setEnabled(false); // } finally {
     * buttonManager.setButtonsEnabled(true); } }
     *
     */
    public void removeSelectedTextAnnotations() {
        if (isReadOnly()) {
            throw new UnsupportedOperationException("Modification not allowed on read-only Document!");
        }
        List<String> annotationIds;
        if (!selectedTextAnnotations.isEmpty()) {
            if (selectedTextAnnotations.getSelections().size() > 1) {
                annotationIds = selectedTextAnnotations.getSelectedTextAnnotationIds();
                removeTextAnnotations(annotationIds);
            } else {
                Annotation annotation = selectedTextAnnotations.getMainSelectedTextAnnotation();
                removeAnnotation(annotation.getId());


            }
        }
    }

    private static class ReplicateAnnotationDialog extends DialogBox {

        private final Label progressLabel;

        public ReplicateAnnotationDialog() {
            setText("Replicating annotation...");

            setGlassEnabled(true);
            setAnimationEnabled(true);

            VerticalPanel vpanel = new VerticalPanel();
            vpanel.add(new Label("Progress:"));
            progressLabel = new Label("");
            HorizontalPanel hpanel = new HorizontalPanel();
            hpanel.add(progressLabel);
            vpanel.add(hpanel);
            setWidget(vpanel);
        }
    }

    private void replicateCurrentlySelectedTextAnnotation() {

        Annotation srcAnnotation = selectedTextAnnotations.getMainSelectedTextAnnotation();
        if (srcAnnotation != null) {
            String occurrencePattern = srcAnnotation.getAnnotationText("");

            //limit replication to selected text, if any.
            Fragment selectionBoundariesFragment = annMarkerMgr.getSelectionBoundariesFragment(getAnnotatedTextHandler());
            final String coverageType = selectionBoundariesFragment == null ? "document" : "selected text";
            //Important note: assumed that we are handling only single fragment occurrences
            final List<Fragment> occurrences = AnnotatedTextProcessor.getOccurences(getAnnotatedTextHandler(), selectionBoundariesFragment, occurrencePattern);

            if (occurrences.isEmpty()) {
                Window.confirm("No occurrences of '" + occurrencePattern + "' found in the " + coverageType);
            } else {
                final ReplicateAnnotationDialog dialog = new ReplicateAnnotationDialog();

                final String annType = srcAnnotation.getAnnotationType();
                final Properties props = srcAnnotation.getProperties();

                final List<Fragment> prexistingFragOfSameType = new ArrayList<Fragment>();
                for (Annotation prexistingAnnOfSameType : getDocument().getAnnotations(annType)) {
                    prexistingFragOfSameType.addAll(prexistingAnnOfSameType.getTextBinding().getFragments());
                }


                Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
                    int nbTotal = occurrences.size();
                    int nbProcessed = 0;
                    int nbSkipped = 0;
                    int nbConflict = 0;
                    int nbAdded = 0;
                    Iterator<Fragment> i = occurrences.iterator();

                    @Override
                    public boolean execute() {

                        setUpdatingAfterEditEvents(false);

                        if (!dialog.isShowing()) {
                            dialog.center();
                        }

                        int yetToBeprocessedInGroup = 100;
                        while (i.hasNext() && yetToBeprocessedInGroup > 0) {
                            yetToBeprocessedInGroup--;
                            nbProcessed++;

                            dialog.progressLabel.setText(nbProcessed + " / " + nbTotal + " (skipped: " + nbSkipped + ")");

                            if (i.hasNext()) {
                                Fragment fragForReplicate = i.next();

                                for (Fragment fragOfPrexistingAnnotation : prexistingFragOfSameType) {
                                    //create replicate only if the fragment for the replicate is not identical to (one of) the fragment(s) of an existing Annotation of same type
                                    if (fragForReplicate.isSame(fragOfPrexistingAnnotation)) {
                                        nbSkipped++;
                                        return true;
                                    }
                                }
                                List<Fragment> newFrags = new ArrayList<Fragment>();
                                newFrags.add(fragForReplicate);
                                if (checkAndCreateTextAnnotation(annType, newFrags, props, false)) {
                                    nbAdded++;
                                } else {
                                    nbConflict++;
                                }

                            }
                        }

                        if (i.hasNext()) {
                            return true;
                        } else {
                            setUpdatingAfterEditEvents(true);
                            dialog.hide();
                            String message = nbTotal + " occurrence(s) found in the " + coverageType + ":\n"
                                    + "*\t" + nbAdded + " new annotation(s) created\n"
                                    + "*\t" + nbSkipped + " occurrence(s) skipped (already existing annotation)\n";
                            if (nbConflict > 0) {
                                message += "*\t" + nbConflict + " annotation(s) NOT created because of conflicting boundaries\n";

                            }
                            Window.alert(message);
                            return false;
                        }
                    }
                });
            }
        }

    }

    @UiHandler("replicateAnnButton")
    void handleReplicateAnnButtonClick(ClickEvent e) {
        try {
            replicateAnnButton.setEnabled(false);
            replicateCurrentlySelectedTextAnnotation();
        } finally {
            buttonManager.setButtonsEnabled(true);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @UiHandler("addGroupButton")
    void handleAddGroupButtonClick(ClickEvent e) {
        try {
            addGroupButton.setEnabled(false);
            createGroup();
        } finally {
            buttonManager.setButtonsEnabled(true);
        }
    }

    private void createGroup() {
        if (isReadOnly()) {
            throw new UnsupportedOperationException("Modification not allowed on read-only Document!");
        }
        ArrayList<GenericAnnotationSelection> selected = new ArrayList<GenericAnnotationSelection>();
        selected.addAll(selectedTextAnnotations.getSelections());
        selected.addAll(selectedGroups.getSelections());
        selected.addAll(selectedRelations.getSelections());

        if (selected != null && !selected.isEmpty()) {
            ArrayList<String> annotationIds = new ArrayList<String>();
            for (GenericAnnotationSelection s : selected) {
                if (getAnnotatedTextHandler().isReferenceableAnnotation(s.getAnnotation().getId())) {
                    annotationIds.add(s.getAnnotation().getId());
                }
            }
            if (annotationIds.isEmpty()) {
                //FIXME not I18N
                Window.alert("The selection does not contain any referenceable annotation");
            } else {
                CombinedAnnotationCreationHelper.createGroup(getAnnotatedTextHandler(), annotationIds);
            }
        }
    }

    @UiHandler("deleteGroupButton")
    void handleDeleteGroupButtonClick(ClickEvent e) {
        try {
            deleteGroupButton.setEnabled(false);
            removeMainSelectedGroup();
        } finally {
            buttonManager.setButtonsEnabled(true);
        }
    }

    private void removeMainSelectedGroup() {
        if (isReadOnly()) {
            throw new UnsupportedOperationException("Modification not allowed on read-only Document!");
        }
        Annotation selected = selectedGroups.getMainSelectedGroupAnnotation();
        if (selected != null) {
            removeGroup(selected);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @UiHandler("addRelButton")
    void handleAddRelButtonClick(ClickEvent e) {
        try {
            addRelButton.setEnabled(false);
            createRelation();
        } finally {
            buttonManager.setButtonsEnabled(true);
        }
    }

    private void createRelation() {
        if (isReadOnly()) {
            throw new UnsupportedOperationException("Modification not allowed on read-only Document!");
        }
        ArrayList<GenericAnnotationSelection> selected = new ArrayList<GenericAnnotationSelection>();
        selected.addAll(selectedTextAnnotations.getSelections());
        selected.addAll(selectedGroups.getSelections());
        selected.addAll(selectedRelations.getSelections());

        if (selected != null && !selected.isEmpty()) {
            ArrayList<String> annotationIds = new ArrayList<String>();
            for (GenericAnnotationSelection s : selected) {
                if (getAnnotatedTextHandler().isReferenceableAnnotation(s.getAnnotation().getId())) {
                    annotationIds.add(s.getAnnotation().getId());
                }
            }
            if (annotationIds.isEmpty()) {
                //FIXME not I18N
                Window.alert("The selection does not contain any referenceable annotation");
            } else {
                CombinedAnnotationCreationHelper.CreateRelation(getAnnotatedTextHandler(), annotationIds);
            }
        }
    }

    @UiHandler("deleteRelButton")
    void handleDeleteRelButtonClick(ClickEvent e) {
        try {
            if (isReadOnly()) {
                throw new UnsupportedOperationException("Modification not allowed on read-only Document!");
            }
            deleteRelButton.setEnabled(false);
            removeRelations(selectedRelations.getSelectedAnnotations());
        } finally {
            buttonManager.setButtonsEnabled(true);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private void removeFragmentFromAnnotation(String annotationId, List<Fragment> fragments) {
        TextAnnotationFragmentsRemovalEdit edit = new TextAnnotationFragmentsRemovalEdit(getAnnotatedTextHandler(), annotationId, fragments);
        edit.redo();
    }

    private void addFragmentToAnnotation(String annotationId, List<Fragment> fragments) {
        TextAnnotationFragmentsAdditionEdit edit = new TextAnnotationFragmentsAdditionEdit(getAnnotatedTextHandler(), annotationId, fragments);
        edit.redo();
    }

    // -------------------------------------------------------------------------
    private void removeAnnotation(String annotationId) {
        TextAnnotationRemovalEdit edit = new TextAnnotationRemovalEdit(getAnnotatedTextHandler(), annotationId);
        String msg = edit.getPreventingCause();
        if (msg != null) {
            ResultMessageDialog d = new ResultMessageDialog(ResultMessageDialog.Error, "Can not remove annotation " + annotationId, msg);
            d.show();
        } else {
            edit.redo();
        }
    }

    private void removeTextAnnotations(List<String> annotationIds) {
        AnnotationCompoundEdit main = new AnnotationCompoundEdit();
        for (String annotationId : annotationIds) {
            TextAnnotationRemovalEdit edit = new TextAnnotationRemovalEdit(getAnnotatedTextHandler(), annotationId);
            String msg = edit.getPreventingCause();
            if (msg != null) {
                ResultMessageDialog d = new ResultMessageDialog(ResultMessageDialog.Error, "Can not remove annotation " + annotationId, msg);
                d.show();
                main = null;
                break;
            }
            main.addEdit(edit);
        }
        if (main != null) {
            clearAnchorMarkerSelection();
            main.redo();
        }
    }

    private void removeGroup(Annotation group) {
        AnnotationGroupRemovalEdit edit = new AnnotationGroupRemovalEdit(getAnnotatedTextHandler(), group);
        String msg = edit.getPreventingCause();
        if (msg != null) {
            ResultMessageDialog d = new ResultMessageDialog(ResultMessageDialog.Error, "Can not remove Group " + group.getId(), msg);
            d.show();
        } else {
            edit.redo();
        }
    }

    // -------------------------------------------------------------------------
    private void removeRelations(List<Annotation> relations) {
        AnnotationCompoundEdit main = new AnnotationCompoundEdit();
        for (Annotation rel : relations) {
            AnnotationRelationRemovalEdit edit = new AnnotationRelationRemovalEdit(getAnnotatedTextHandler(), rel);
            String msg = edit.getPreventingCause();
            if (msg != null) {
                ResultMessageDialog d = new ResultMessageDialog(ResultMessageDialog.Error, "Can not remove relation " + rel.getId(), msg);
                d.show();
                main = null;
                break;
            }
            main.addEdit(edit);
        }
        if (main != null) {
            clearAnchorMarkerSelection();
            main.redo();
        }
    }

    // =========================================================================
    @UiHandler("explainSchemaBtn")
    void handleExplainSchemaButtonClick(ClickEvent e) {
        if (getDocument() != null) {
            final DialogBox box = new DialogBox(true, true);
            box.setText("Annotation Schema");
            box.add(new ExplainSchemaPanel(getDocument().getAnnotationSchema(), null));
            box.center();
        }
    }

    // =========================================================================
    public void displayValidationResult(BasicFaultListener<SafeHtml> faultLstnr) {
        ValidationResultDialog dlg = new ValidationResultDialog(new ValidationResultDialog.AnnotationSelectionHandler() {
            @Override
            public void annotationSelected(String annotationId) {
                if (annotationId != null) {
                    Annotation annotation = getMapper().getAnnotation(annotationId);
                    if (annotation != null) {
                        eventBus.fireEvent(new GroupSelectionEmptiedEvent(getAnnotatedTextHandler()));
                        eventBus.fireEvent(new RelationSelectionEmptiedEvent(getAnnotatedTextHandler()));
                        eventBus.fireEvent(new TextAnnotationSelectionEmptiedEvent(getAnnotatedTextHandler()));
                        switch (annotation.getAnnotationKind()) {
                            case TEXT:
                                eventBus.fireEvent(new TextAnnotationSelectionChangedEvent(getAnnotatedTextHandler(), annotation));
                                break;
                            case GROUP:
                                eventBus.fireEvent(new GroupSelectionChangedEvent(getAnnotatedTextHandler(), annotation));
                                break;
                            case RELATION:
                                eventBus.fireEvent(new RelationSelectionChangedEvent(getAnnotatedTextHandler(), annotation));
                                break;
                        }
                    }
                }
            }
        }, faultLstnr.getMessages());
        dlg.show();
        dlg.center();
    }

    @UiHandler("validateButton")
    void handleValidateButtonClick(ClickEvent e) {
        try {
            validateButton.setEnabled(false);
            BasicFaultListener<SafeHtml> faultLstnr = new BasicFaultListener<SafeHtml>(faultMessages);
            BasicAnnotationSchemaValidator.validateAnnotatedText(getDocument(), faultLstnr);
            if (faultLstnr.getMessages().isEmpty()) {
                Window.alert("Validation completed without error!");
            } else {
                displayValidationResult(faultLstnr);
            }
        } finally {
            buttonManager.setButtonsEnabled(true);
        }
    }

    // =========================================================================
    private void signalTermExposition(AnnotatedTextHandler annotatedDoc) {

        TermAnnotationsExpositionEvent event = null;
        if (annotatedDoc != null) {
            schemaHandler = new AnnotationSchemaDefHandler(annotatedDoc.getAnnotatedText().getAnnotationSchema());

            //does the new schema include TermAnnotation referencing TyDI resource?
            if (schemaHandler.enableTyDIResourceRef()) {

                Set<String> urls = schemaHandler.getTyDIResourceBaseURLs();
                if (urls.size() > 1) {
                    IllegalArgumentException ex = new IllegalArgumentException("A schema should reference only one TyDI ressource location!");
                    Window.alert(ex.getMessage());
                    throw ex;
                }
                //inform Structured Terminology widget that it should start displaying the specified Terminology resource
                try {
                    locator = new ResourceLocator(urls.iterator().next());
                    event = new TermAnnotationsExpositionEvent(TermAnnotationsExpositionEvent.ChangeType.Available, locator);
                } catch (IllegalArgumentException ex) {
                    Window.alert(ex.getMessage());
                }
            }
        } else {
            //if the editor was previously exposing TermAnnotation referencing TyDI resource
            if (schemaHandler != null && schemaHandler.enableTyDIResourceRef()) {
                //inform Structured Terminology widget that it should stop displaying the Terminology resource
                event = new TermAnnotationsExpositionEvent(TermAnnotationsExpositionEvent.ChangeType.Unavailable, locator);
            }
            schemaHandler = null;
        }
        if (event != null) {
            eventBus.fireEvent(event);
        }
    }

    private static class TyDIResourceRefsRefresher {

        private final AnnotationSchemaDefHandler schemaHandler;
        private final AnnotatedTextHandler annotatedDoc;
        private final List<CheckedSemClassImpl> referencedSemClass = new ArrayList<CheckedSemClassImpl>();
        private final Map<String, List<String>> propNamesByAnnType = new HashMap<String, List<String>>();

        public TyDIResourceRefsRefresher(AnnotatedTextHandler annotatedDoc, AnnotationSchemaDefHandler schemaHandler) {
            this.annotatedDoc = annotatedDoc;
            this.schemaHandler = schemaHandler;
        }

        private boolean initTyDIResourceRefresh() {
            if (schemaHandler != null) {

                if (schemaHandler.enableTyDIResourceRef()) {

                    //Retrieve all Annotations referencing TyDI resources
                    for (AnnotationImpl annotation : annotatedDoc.getEditableUsersAnnotationSet().getAnnotations()) {
                        String annotationType = annotation.getAnnotationType();
                        if (schemaHandler.isTyDIResReferencingType(annotationType)) {


                            if (!propNamesByAnnType.containsKey(annotationType)) {
                                List<String> semClassOrConceptRefPropNames = new ArrayList<String>();
                                propNamesByAnnType.put(annotationType, semClassOrConceptRefPropNames);

                                String semClassRefPropName = schemaHandler.getTyDISemClassRefPropName(annotationType);
                                if (semClassRefPropName != null) {
                                    semClassOrConceptRefPropNames.add(semClassRefPropName);
                                }
                                semClassOrConceptRefPropNames.addAll(schemaHandler.getTyDIConceptRefPropNames(annotationType));
                            }

                            for (String propName : propNamesByAnnType.get(annotationType)) {
                                List<String> values = annotation.getProperties().getValues(propName);
                                if (values != null) {
                                    for (String semClassReference : values) {
                                        TyDIResRefPropValImpl resRefPropVal = TyDIResRefPropValImpl.createFromUrlWithFragment(semClassReference);
                                        if (resRefPropVal != null) {
                                            referencedSemClass.add(
                                                    CheckedSemClassImpl.create(
                                                    TyDISemClassRefImpl.getSemClassIdFromSemClassExternalId(resRefPropVal.getResourceRef()),
                                                    resRefPropVal.getVersionNumber()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (referencedSemClass.isEmpty()) {
                        return false;
                    } else {
                        injector.getMainEventBus().fireEvent(new TyDIResourcesCheckChangesInfoEvent(referencedSemClass));
                        return true;
                    }
                }
            }
            return false;
        }

        private void performRefresh(List<CheckedSemClassImpl> unaffectedSemClass) {

            //update version number of the Semantic classes that were unchanged since the version stored in the URL
            //Note : these changes are not undoable.
            Map<Integer, Integer> newVersionBySemClassId = new HashMap<Integer, Integer>();
            for (CheckedSemClassImpl r : unaffectedSemClass) {
                newVersionBySemClassId.put(r.getSemClassId(), r.getVersionNum());
            }

            for (AnnotationImpl annotation : annotatedDoc.getEditableUsersAnnotationSet().getAnnotations()) {
                String annotationType = annotation.getAnnotationType();

                if (!propNamesByAnnType.containsKey(annotationType)) {
                    continue;
                }

                for (String propName : propNamesByAnnType.get(annotationType)) {
                    List<String> values = annotation.getProperties().getValues(propName);
                    if (values != null) {
                        int index = 0;
                        for (String semClassReference : values) {
                            TyDIResRefPropValImpl resRefPropVal = TyDIResRefPropValImpl.createFromUrlWithFragment(semClassReference);
                            if (resRefPropVal != null) {
                                int semClassId = TyDISemClassRefImpl.getSemClassIdFromSemClassExternalId(resRefPropVal.getResourceRef());
                                if (newVersionBySemClassId.containsKey(semClassId)) {
                                    int newVersion = newVersionBySemClassId.get(semClassId);
                                    TyDIResRefPropValImpl updatedReference = TyDIResRefPropValImpl.create(resRefPropVal.getResourceRef(), resRefPropVal.getLabel(), newVersion);
                                    annotation.getProperties().replaceValue(propName, index, updatedReference.toUrlWithFragment());
                                }
                            }
                            index++;
                        }
                    }
                }
            }

        }
    }

    @Override
    public void onTyDIVersionedResourceInfoEvent(TyDIVersionedResourcesInfoEvent event) {
        if (event instanceof TyDIVersionedResourcesUnchangedInfoEvent) {
            if (tyDIResourceRefsRefresher != null) {
                tyDIResourceRefsRefresher.performRefresh(event.getTyDIResourcesRef());
                tyDIResourceRefsRefresher = null;
            }
        }
    }

    @UiHandler("termRefreshBtn")
    void handleRefreshTyDIResourcesButtonClick(ClickEvent e) {
        if (tyDIResourceRefsRefresher == null) {
            tyDIResourceRefsRefresher = new TyDIResourceRefsRefresher(getAnnotatedTextHandler(), schemaHandler);
            if (!tyDIResourceRefsRefresher.initTyDIResourceRefresh()) {
                tyDIResourceRefsRefresher = null;
            }
        }
    }
    // =========================================================================
    SafeHtml checkedIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.CheckedIcon()).getHTML());

    private void resetAnnSetSelectionMenu() {
        annSetMenuItem.setSubMenu(null);
    }

    private void reinitAnnSetSelectionMenu() {
        annSetMenuItem.setSubMenu(null);
        MenuBar annSetMenuBar = new MenuBar(true);
        if (getDocument() != null) {
            for (AnnotationSetInfo asi : getDocument().getAnnotationSetInfoList()) {
                Command command;
                SafeHtmlBuilder shbuilder = new SafeHtmlBuilder();

                if (getAnnotatedTextHandler().getLoadedAnnotationSetIds().contains(asi.getId())) {
                    command = null;
                    shbuilder.append(checkedIcon).appendEscaped("  ");
                } else {
                    final int annSetId = asi.getId();
                    command = new Command() {
                        @Override
                        public void execute() {
                            getAnnotatedTextHandler().requestAdditionalAnnotationSet(annSetId);
                        }
                    };
                }
                AnnotationSetDescriptionCell.render(asi, shbuilder);
                MenuItem item = new MenuItem(shbuilder.toSafeHtml(), command);
                item.setEnabled(command != null);
                annSetMenuBar.addItem(item);
            }
        }
        annSetMenuItem.setSubMenu(annSetMenuBar);


    }
    // =========================================================================

    class TaskSchemaManager {

        private final Set<String> editedTextTypes = new LinkedHashSet<String>();
        private final Set<String> editedGroupTypes = new LinkedHashSet<String>();
        private final Set<String> editedRelationTypes = new LinkedHashSet<String>();

        public Set<String> getEditedGroupTypes() {
            return editedGroupTypes;
        }

        public Set<String> getEditedRelationTypes() {
            return editedRelationTypes;
        }

        public Set<String> getEditedTextTypes() {
            return editedTextTypes;
        }

        private void setTaskSchema(TaskDefinition taskDef, AnnotationSchemaDefinition schema) {
            clear();

            //if the edition is associated to a TaskDefinition, then the available Annotation types must be limited to the one of the Task
            Set<String> editedTypes = null;
            if (taskDef != null) {
                editedTypes = new HashSet<String>(taskDef.getEditedAnnotationTypes());
            }

            if (schema != null) {
                for (String annType : schema.getAnnotationTypes()) {
                    if ((editedTypes == null) || editedTypes.contains(annType)) {
                        AnnotationTypeDefinition annTypeDef = schema.getAnnotationTypeDefinition(annType);
                        if (annTypeDef.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                            editedTextTypes.add(annType);
                        } else if (annTypeDef.getAnnotationKind().equals(AnnotationKind.GROUP)) {
                            editedGroupTypes.add(annType);
                        }
                        if (annTypeDef.getAnnotationKind().equals(AnnotationKind.RELATION)) {
                            editedRelationTypes.add(annType);
                        }
                    }
                }
            }
        }

        private void clear() {
            editedTextTypes.clear();
            editedGroupTypes.clear();
            editedRelationTypes.clear();
        }

        boolean canEditSomeTextAnnotation() {
            return getEditedTextTypes().size() > 0;
        }

        boolean canEditSomeGroupAnnotation() {
            return getEditedGroupTypes().size() > 0;
        }

        boolean canEditSomeRelationAnnotation() {
            return getEditedRelationTypes().size() > 0;
        }
    }

    class ButtonEnablementManager implements AnnotationSelectionChangedEventHandler, EditHappenedEventHandler, RangeSelectionChangedEventHandler {

        boolean globalEnabledDirective = true;

        public void setButtonsEnabled(boolean enabled) {
            globalEnabledDirective = enabled;
            updateButtonStatuses();
        }

        private void updateButtonStatuses() {
            Scheduler.get().scheduleFinally(new Command() {
                @Override
                public void execute() {
                    incLineSize.setEnabled(globalEnabledDirective && canIncreaseLineSize());
                    decLineSize.setEnabled(globalEnabledDirective && canReduceLineSize());

                    boolean readOnly = isReadOnly();
                    undoBtn.setEnabled(globalEnabledDirective && !readOnly && undoManager != null && undoManager.canUndo());
                    redoBtn.setEnabled(globalEnabledDirective && !readOnly && undoManager != null && undoManager.canRedo());

                    selectionModeBtn.setEnabled(globalEnabledDirective);

                    boolean someTextSelected = !annMarkerMgr.getSelectedRanges().isEmpty();

                    boolean someTextAnnSelected = false;
                    boolean someGroupSelected = false;
                    boolean someRelationSelected = false;

                    int nbModifiableTextAnnSelected = 0;
                    int nbModifiableGroupSelected = 0;
                    int nbModifiableRelationSelected = 0;

                    AnnotatedTextHandler handler = getAnnotatedTextHandler();
                    if (handler != null) {
                        int formattingAnnSetId = handler.getFormattingAnnotationSet() != null ? handler.getFormattingAnnotationSet().getId() : -1;
                        int userAnnSetId = handler.getEditableUsersAnnotationSet() != null ? handler.getEditableUsersAnnotationSet().getId() : -1;

                        // - Only the Annotations belonging to the user's Annotation Set can be modified
                        // - Annotation of any other Annotation Set (excepted Formatting AnnotationSet) can be referenced
                        // - Formatting Annotation can not be modified or referenced

                        for (GenericAnnotationSelection a : selectedTextAnnotations.getSelections()) {
                            int annAnnSetId = handler.getAnnotationSetId(a.getAnnotation().getId());
                            if (userAnnSetId == annAnnSetId) {
                                nbModifiableTextAnnSelected++;
                                if (nbModifiableTextAnnSelected > 1) {
                                    break;
                                }
                            } else if (formattingAnnSetId != annAnnSetId) {
                                someTextAnnSelected = true;
                            }
                        }
                        someTextAnnSelected = someTextAnnSelected || nbModifiableTextAnnSelected > 0;

                        //
                        for (GenericAnnotationSelection a : selectedGroups.getSelections()) {
                            Integer annAnnSetId = handler.getAnnotationSetId(a.getAnnotation().getId());
                            if (annAnnSetId != null) {
                                if (userAnnSetId == annAnnSetId) {
                                    nbModifiableGroupSelected++;
                                    if (nbModifiableGroupSelected > 1) {
                                        break;
                                    }
                                } else if (formattingAnnSetId != annAnnSetId) {
                                    someGroupSelected = true;
                                }
                            }
                        }
                        someGroupSelected = someGroupSelected || nbModifiableGroupSelected > 0;

                        //
                        for (GenericAnnotationSelection a : selectedRelations.getSelections()) {
                            Integer annAnnSetId = handler.getAnnotationSetId(a.getAnnotation().getId());
                            if (annAnnSetId != null) {
                                if (userAnnSetId == annAnnSetId) {
                                    nbModifiableRelationSelected++;
                                    if (nbModifiableRelationSelected > 1) {
                                        break;
                                    }
                                } else if (formattingAnnSetId != annAnnSetId) {
                                    someRelationSelected = true;
                                }
                            }
                        }
                        someRelationSelected = someRelationSelected || nbModifiableRelationSelected > 0;

                    }

                    boolean someAnnSelected = someTextAnnSelected || someGroupSelected || someRelationSelected;

                    boolean canEditsomeTextType = schemaManager.canEditSomeTextAnnotation();
                    boolean canEditsomeGroupType = schemaManager.canEditSomeGroupAnnotation();
                    boolean CanEditsomeRelationType = schemaManager.canEditSomeRelationAnnotation();

                    annTypeList.setEnabled(globalEnabledDirective && !readOnly);
                    addAnnotButton.setEnabled(globalEnabledDirective && !readOnly && canEditsomeTextType && someTextSelected);
                    deleteAnnButton.setEnabled(globalEnabledDirective && !readOnly && canEditsomeTextType && nbModifiableTextAnnSelected > 0);
                    replicateAnnButton.setEnabled(globalEnabledDirective && !readOnly && canEditsomeTextType && nbModifiableTextAnnSelected > 0);
                    //editAnnButton.setEnabled(globalEnabledDirective && !readOnly && someEditableTextType && nbModifiableTextAnnSelected == 1);
                    addSelectionButton.setEnabled(globalEnabledDirective && !readOnly && canEditsomeTextType && someTextSelected && nbModifiableTextAnnSelected == 1);
                    delSelectionButton.setEnabled(globalEnabledDirective && !readOnly && canEditsomeTextType && someTextSelected && nbModifiableTextAnnSelected == 1);

                    addGroupButton.setEnabled(globalEnabledDirective && !readOnly && canEditsomeGroupType && someAnnSelected);
                    deleteGroupButton.setEnabled(globalEnabledDirective && !readOnly && canEditsomeGroupType && nbModifiableGroupSelected > 0);

                    addRelButton.setEnabled(globalEnabledDirective && !readOnly && CanEditsomeRelationType && someAnnSelected);
                    deleteRelButton.setEnabled(globalEnabledDirective && !readOnly && CanEditsomeRelationType && nbModifiableRelationSelected > 0);

                    validateButton.setEnabled(globalEnabledDirective && !readOnly);

                    termRefreshBtn.setEnabled(globalEnabledDirective && !readOnly && schemaHandler != null && schemaHandler.enableTyDIResourceRef());
                }
            });
        }

        @Override
        public void onAnnotationSelectionChanged(GenericAnnotationSelectionChangedEvent event) {
            if ((getDocument() != null) && getDocument().equals(event.getAnnotatedTextHandler().getAnnotatedText())) {
                updateButtonStatuses();
            }
        }

        @Override
        public void onEditHappened(EditHappenedEvent event) {
            if ((getDocument() != null) && getDocument().equals(event.getEdit().getAnnotatedTextHandler().getAnnotatedText())) {
                updateButtonStatuses();
            }
        }

        @Override
        public void onRangeSelectionChanged(RangeSelectionChangedEvent event) {
            if ((getDocument() != null) && getDocument().equals(event.getAnnotatedTextHandler().getAnnotatedText())) {
                updateButtonStatuses();
            }
        }
    }
// =========================================================================

    @Override
    public void setReadOnly(boolean readOnly) {
        if (!readOnly) {
            //deny read/write for MSIE browser
            float v = AnnotationMarkerManager.getInternetExplorerVersion();
            if (v >= 0 && v < 9) {
                readOnly = true;
                Window.alert("Only readOnly mode is supported by MS Internet Explorer");
            }
        }
        this.options.setReadOnly(readOnly);
        undoBtn.setVisible(!readOnly);
        redoBtn.setVisible(!readOnly);
        if (readOnly) {
            removeUndoManager();
            contentHTML.removeStyleName(style.SelectableText());
        } else {
            contentHTML.addStyleName(style.SelectableText());
            addUndoManager();
        }
        initDragController(readOnly);
    }

    private void removeUndoManager() {
        if (undoManager != null) {
            undoManager.discardAllEdits();
            EditHappenedEvent.unregister(undoManager);
            undoManager = null;
        }
    }

    private void addUndoManager() {
        if (!isReadOnly()) {
            undoManager = new UndoManager(getDocument());
            EditHappenedEvent.register(eventBus, undoManager);
        }
    }

    public void doRefresh(boolean includingTextAnnotation) {
        if (includingTextAnnotation) {
            annMarkerMgr.refreshDocument();
            displayCurrentlySelectedAnnotations();
        }
        refreshAllVeiledStatus();
        drawExtra();
    }

    @Override
    public void onEditHappened(EditHappenedEvent event) {

        if (isUpdatingAfterEditEvents()) {
            boolean refreshNeeded = (getDocument() != null) && getDocument().equals(event.getEdit().getAnnotatedTextHandler().getAnnotatedText());
            if (!refreshNeeded) {
                return;
            }

            boolean includingTextAnnotation = false;
            if (event.getEdit() instanceof TextAnnotationCoverageEdit) {
                includingTextAnnotation = true;
            } else if (event.getEdit() instanceof AnnotationChangeTypeEdit) {
                //Refresh Text Annotation only if modified annotation type is a Text one
                AnnotationChangeTypeEdit edit = (AnnotationChangeTypeEdit) event.getEdit();
                includingTextAnnotation = edit.getAnnotation().getAnnotationKind().equals(AnnotationKind.TEXT);
            }
            doRefresh(includingTextAnnotation);
        }
    }

    public boolean isUpdatingAfterEditEvents() {
        return updatingAfterEditEvents;
    }

    public void setUpdatingAfterEditEvents(boolean nowUpdatingAfterEditEvents) {
        boolean wasUpdatingAfterEditEvents = updatingAfterEditEvents;
        updatingAfterEditEvents = nowUpdatingAfterEditEvents;
        if (!wasUpdatingAfterEditEvents && nowUpdatingAfterEditEvents) {
            doRefresh(true);
        }
    }

    @Override
    public void onAnnotationStatusChanged(AnnotationStatusChangedEvent event) {
        refreshElementsVeiledStatus(event.getAnnotationIds());
    }

    @Override
    public void onAnnotationFocusChanged(AnnotationFocusChangedEvent event) {
        //set scroll position to place the focused Annotation in the center of document panel
        if (withScrollpanel && scrollPanel.getMaximumVerticalScrollPosition() > 0) {
            boolean refreshNeeded = (getDocument() != null) && getDocument().equals(event.getAnnotatedTextHandler().getAnnotatedText());
            if (refreshNeeded) {
                Annotation annotation = event.getAnnotation();
                String annotationId = annotation.getId();
                setScrollPositionAtAnnotation(annotationId, annotation.getAnnotationKind());
            }
        }
    }

    @Override
    public void setScrollPositionAtAnnotation(String annotationId) {
        setScrollPositionAtAnnotation(annotationId, null);
    }

    private void setScrollPositionAtAnnotation(String annotationId, AnnotationKind kind) {
        if (withScrollpanel) {
            CombinedAnnotationWidget w;
            Integer pointToDisplay = null;
            if (kind == null) {
                Annotation annotation = mapper.getAnnotatedText().getAnnotation(annotationId);
                if (annotation != null) {
                    kind = annotation.getAnnotationKind();
                }
            }

            if (AnnotationKind.TEXT.equals(kind)) {
                ArrayList<String> markerIds = mapper.getMarkerIdsFromAnnotationId(annotationId);
                if (!markerIds.isEmpty()) {
                    Element e = Document.get().getElementById(markerIds.get(0));
                    if (e != null) {
                        pointToDisplay = e.getOffsetTop() + (e.getOffsetHeight() / 2);
                    }
                }
            } else if (AnnotationKind.GROUP.equals(kind)) {
                w = groupDisplayer.getWidget(annotationId);
                if (w != null) {
                    pointToDisplay = w.getCenterClip().top;
                }
            } else if (AnnotationKind.RELATION.equals(kind)) {
                w = relationDisplayer.getWidget(annotationId);
                if (w != null) {
                    pointToDisplay = w.getCenterClip().top;
                }
            }

            if (pointToDisplay != null) {
                Integer centeredScrollPos = pointToDisplay - (scrollPanel.getElement().getClientHeight() / 2);
                scrollPanel.setVerticalScrollPosition(centeredScrollPos);
            }
        }
    }

    private void setScrollPositionAtRatio(int prevScrollPos, int prevMaxScrollPos) {
        if (withScrollpanel) {
            int newMaxScrollPos = scrollPanel.getMaximumVerticalScrollPosition();
            if (prevMaxScrollPos > 0 && newMaxScrollPos > 0) {
                int newScrollPos = prevScrollPos * newMaxScrollPos / prevMaxScrollPos;
                scrollPanel.setVerticalScrollPosition(newScrollPos);
            }
        }
    }

    public void setVerticalScrollPosition(int position) {
        scrollPanel.setVerticalScrollPosition(position);
    }

    public int getVerticalScrollPosition() {
        return scrollPanel.getVerticalScrollPosition();
    }

    public HandlerRegistration addScrollHandler(ScrollHandler scrollHandler) {
        return scrollPanel.addScrollHandler(scrollHandler);
    }
    // -------------------------------------------------------------------------

    public void addAnnotationDisplayer(AnnotationDisplayer annotationDisplayer) {
        dispEngine.addDisplayer(annotationDisplayer);
    }

    public void addOverlayEventsHandler(AnnotationOverlayEventsHandler annotationOverlayEventsHandler) {
        dispEngine.addOverlayEventsHandler(annotationOverlayEventsHandler);
    }

    public void refreshSecondaryDisplayer(AnnotationDisplayer annotationDisplayer) {
        dispEngine.refreshSecondaryDisplayer(canvas, overlayContainer.getElement(), annotationDisplayer);
    }
    // -------------------------------------------------------------------------

    @Override
    public void onWorkingDocumentChanged(WorkingDocumentChangedEvent event) {
        AnnotatedTextHandler handler = getAnnotatedTextHandler();
        if (handler != null && handler.equals(event.getWorkingDocument()) && WorkingDocumentChangedEvent.ChangeType.AdditionalAnnotationSetLoaded.equals(event.getChangeType())) {
            //
            injector.getMainEventBus().fireEvent(new InformationReleasedEvent("<span style='color:green;'>Addtionnal AnnotationSet loaded!</span>"));
            reinitAnnSetSelectionMenu();
            doRefresh(true);
        }
    }

    @Override
    public void onMaximizingWidget(MaximizingWidgetEvent event) {


        if (event.getWidget().equals(this)) {
            final int prevScrollPos = scrollPanel.getVerticalScrollPosition();
            final int prevmaxScrollPos = scrollPanel.getMaximumVerticalScrollPosition();

            maxmimized = event.isMaximizing();
            //expand/collapse toolbar
            toolBarExpandCollapseHandler.setExpanded(!maxmimized);

            //perform scrolling after that other components reacted to this event
            Scheduler.get().scheduleDeferred(new Command() {
                @Override
                public void execute() {
                    //reset scrolling in order to show the part of text that was visible before (un)maximization
                    setScrollPositionAtRatio(prevScrollPos, prevmaxScrollPos);
                }
            });
        }
        //regenerate SVG after widget resizing
        drawExtra();
    }

    @Override
    public boolean isReadOnly() {
        return options.isReadOnly();
    }

    @Override
    public AnnotationDocumentViewMapper getMapper() {
        return mapper;
    }

    @Override
    public String getTextContainerId() {
        return annMarkerMgr.getTextContainerId();
    }

    @Override
    public void setTitleText(String title) {
        if (title != null && !title.isEmpty()) {
            titleLabel.setVisible(true);
            titleLabel.setText(title);
        }
    }

    @Override
    public String getTitleText() {
        return titleLabel.getText();
    }

    public int getDocContainerHeight() {
        return contentHTML.getOffsetHeight();
    }

    @Deprecated
    public void setPrintable(boolean printable) {
        if (printable) {
            Element el = contentHTML.getElement().getParentElement();
            String sizeStyle1 = "top: " + el.getOffsetTop() + "px; left: " + el.getOffsetLeft() + "px; width: " + el.getClientWidth() + "px; height: " + el.getClientHeight() + "px;";

            //remove padding 
            int realClientWidth = el.getClientWidth() - 39 - 13;
            int realClientHeight = el.getClientHeight() - 13 - 26;

            Element el2 = contentHTML.getElement();
            String sizeStyle2 = "top: " + el2.getOffsetTop() + "px; left: " + el2.getOffsetLeft() + "px; width: " + realClientWidth + "px; height: " + realClientHeight + "px;";

            Element el3 = canvas.getElement();
            String sizeStyle3 = "top: " + el3.getOffsetTop() + "px; left: " + el3.getOffsetLeft() + "px; width: 100%; height: 100%;";

            el.setAttribute("style", "position: fixed; " + sizeStyle1);
            canvas.getElement().setAttribute("style", "background-color: white; position: fixed; z-index:1; " + sizeStyle3);
            el2.setAttribute("style", "z-index:2; position: fixed; " + sizeStyle2);

            //contentHTML.getElement().getParentElement().addClassName(compId);
        } else {
            //contentHTML.getElement().getParentElement().removeClassName(compId);
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        TextAnnotationSelectionChangedEvent.register(eventBus, DocumentUi.this);
        RelationSelectionChangedEvent.register(eventBus, DocumentUi.this);
        EditHappenedEvent.register(eventBus, DocumentUi.this);
        AnnotationStatusChangedEvent.register(eventBus, DocumentUi.this);
        AnnotationFocusChangedEvent.register(eventBus, DocumentUi.this);
        WorkingDocumentChangedEvent.register(eventBus, DocumentUi.this);
        MaximizingWidgetEvent.register(eventBus, DocumentUi.this);
        TyDIVersionedResourcesUnchangedInfoEvent.register(eventBus, this);

        GenericAnnotationSelectionChangedEvent.register(eventBus, buttonManager);
        EditHappenedEvent.register(eventBus, buttonManager);
        RangeSelectionChangedEvent.register(eventBus, buttonManager);
        
        ApplicationOptionChangedEvent.register(eventBus, interlineSizeHnd);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        TextAnnotationSelectionChangedEvent.unregister(this);
        RelationSelectionChangedEvent.unregister(this);
        EditHappenedEvent.unregister(this);
        AnnotationStatusChangedEvent.unregister(this);
        AnnotationFocusChangedEvent.unregister(this);
        WorkingDocumentChangedEvent.unregister(this);
        MaximizingWidgetEvent.unregister(this);
        TyDIVersionedResourcesUnchangedInfoEvent.unregister(this);

        GenericAnnotationSelectionChangedEvent.unregister(buttonManager);
        EditHappenedEvent.unregister(buttonManager);
        RangeSelectionChangedEvent.unregister(buttonManager);
        
         ApplicationOptionChangedEvent.unregister(interlineSizeHnd);
    }
}
