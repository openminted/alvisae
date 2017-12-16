/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.AnnotationOverlayEventsHandler;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.AnnotationOverlayProducer;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.GroupAnnotationDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.RelationAnnotationDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.TextAnnotationDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.CombinedAnnotationDisplayer.Point;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.ConsolidationStatus;
import static fr.inra.mig_bibliome.alvisae.shared.data3.ConsolidationStatus.POSTPONED;
import static fr.inra.mig_bibliome.alvisae.shared.data3.ConsolidationStatus.REJECTED;
import static fr.inra.mig_bibliome.alvisae.shared.data3.ConsolidationStatus.RESOLVED;
import static fr.inra.mig_bibliome.alvisae.shared.data3.ConsolidationStatus.SPLIT;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Ellipse;
import org.vaadin.gwtgraphics.client.shape.Path;

/**
 *
 * @author fpapazian
 */
public abstract class ConsolidationStatusDisplayer implements TextAnnotationDisplayer, GroupAnnotationDisplayer, RelationAnnotationDisplayer, AnnotationOverlayProducer, AnnotationOverlayEventsHandler {

    private static final String PROXYANNCONSOSTATUS_ATTRNAME = "aae_consostatus";
    private static ConsolidationStatus[] orderedStatus = {ConsolidationStatus.RESOLVED, ConsolidationStatus.REJECTED, ConsolidationStatus.SPLIT, ConsolidationStatus.POSTPONED};
    private static final String ConsoStatus_POSTPONED_color = "silver";
    private static final String ConsoStatus_REJECTED_color = "red";
    private static final String ConsoStatus_RESOLVED_color = "green";
    private static final String ConsoStatus_SPLIT_color = "blue";
    private static String[] statusColor;

    {
        statusColor = new String[ConsolidationStatus.values().length];
        statusColor[ConsolidationStatus.POSTPONED.toInt()] = ConsoStatus_POSTPONED_color;
        statusColor[ConsolidationStatus.REJECTED.toInt()] = ConsoStatus_REJECTED_color;
        statusColor[ConsolidationStatus.RESOLVED.toInt()] = ConsoStatus_RESOLVED_color;
        statusColor[ConsolidationStatus.SPLIT.toInt()] = ConsoStatus_SPLIT_color;
    }
    private static final String ConsoStatus_POSTPONED_label = "Postpone";
    private static final String ConsoStatus_REJECTED_label = "Reject";
    private static final String ConsoStatus_RESOLVED_label = "Accept";
    private static final String ConsoStatus_SPLIT_label = "Split";
    private static String[] statusLabel;

    {
        statusLabel = new String[ConsolidationStatus.values().length];
        statusLabel[ConsolidationStatus.POSTPONED.toInt()] = ConsoStatus_POSTPONED_label;
        statusLabel[ConsolidationStatus.REJECTED.toInt()] = ConsoStatus_REJECTED_label;
        statusLabel[ConsolidationStatus.RESOLVED.toInt()] = ConsoStatus_RESOLVED_label;
        statusLabel[ConsolidationStatus.SPLIT.toInt()] = ConsoStatus_SPLIT_label;
    }

    static {
        StanEditorResources.INSTANCE.css().ensureInjected();
    }

    public static class ConsoStatusCell extends AbstractCell<ConsolidationStatus> {

        static interface Templates extends SafeHtmlTemplates {

            @SafeHtmlTemplates.Template("<div class='{0}' title='{1}'/>")
            public SafeHtml divConsoStatus(String style, String status);
        }
        static final Templates TEMPLATES = GWT.create(Templates.class);

        @Override
        public void render(Cell.Context context, ConsolidationStatus status, SafeHtmlBuilder sb) {
            if (status != null) {
                switch (status) {
                    case RESOLVED:
                        sb.append(TEMPLATES.divConsoStatus(StanEditorResources.INSTANCE.css().csResolved(), status.name()));
                        break;

                    case REJECTED:
                        sb.append(TEMPLATES.divConsoStatus(StanEditorResources.INSTANCE.css().csRejected(), status.name()));
                        break;

                    case SPLIT:
                        sb.append(TEMPLATES.divConsoStatus(StanEditorResources.INSTANCE.css().csSplit(), status.name()));
                        break;

                    case POSTPONED:
                        sb.append(TEMPLATES.divConsoStatus(StanEditorResources.INSTANCE.css().csPostponed(), status.name()));
                        break;
                }

            }
        }
    }

    public static abstract class ConsoStatusColumn<T> extends Column<T, ConsolidationStatus> {
        public ConsoStatusColumn() {
            super(new ConsoStatusCell());
        }
    };

    /**
     * Popup menu used to change Consolidation Status when click on the
     * ConsoStatus widget
     */
    public static class SelectConsoStatusPopup extends PopupPanel {

        private static final String CONSOSTATUSID_ATTRNAME = "aae_cstatusid";

        static interface SelectConsoStatusPopupTemplates extends SafeHtmlTemplates {

            @SafeHtmlTemplates.Template("<style type='text/css'> .mnuElt{cursor:pointer; padding:3px; margin:2px;} .mnuElt:hover{background-color:#D0E4F6;}</style>")
            public SafeHtml style();

            @SafeHtmlTemplates.Template("<span " + CONSOSTATUSID_ATTRNAME + "='" + ConsoStatus_POSTPONED_label + "' style='background-color:" + ConsoStatus_POSTPONED_color + "'>&nbsp</span>")
            public SafeHtml spanStatusPOSTPONED();

            @SafeHtmlTemplates.Template("<span " + CONSOSTATUSID_ATTRNAME + "='" + ConsoStatus_REJECTED_label + "' style='background-color:" + ConsoStatus_REJECTED_color + "'>&nbsp</span>")
            public SafeHtml spanStatusREJECTED();

            @SafeHtmlTemplates.Template("<span " + CONSOSTATUSID_ATTRNAME + "='" + ConsoStatus_RESOLVED_label + "' style='background-color:" + ConsoStatus_RESOLVED_color + "'>&nbsp</span>")
            public SafeHtml spanStatusRESOLVED();

            @SafeHtmlTemplates.Template("<span " + CONSOSTATUSID_ATTRNAME + "='" + ConsoStatus_SPLIT_label + "' style='background-color:" + ConsoStatus_SPLIT_color + "'>&nbsp</span>")
            public SafeHtml spanStatusSPLIT();

            @SafeHtmlTemplates.Template("<div class='mnuElt' " + CONSOSTATUSID_ATTRNAME + "='{0}'>{2}&nbsp{1}</div>")
            public SafeHtml consoStatusProxy(int statusId, String statusLabel, SafeHtml spanStatus);

            @SafeHtmlTemplates.Template("<b><div class='mnuElt' " + CONSOSTATUSID_ATTRNAME + "='{0}'>{2}&nbsp{1}</div></b>")
            public SafeHtml selectedConsoStatusProxy(int statusId, String statusLabel, SafeHtml spanStatus);
        }
        static final SelectConsoStatusPopupTemplates TEMPLATES = GWT.create(SelectConsoStatusPopupTemplates.class);
        private static SafeHtml[] statusSpans;

        {
            statusSpans = new SafeHtml[ConsolidationStatus.values().length];
            statusSpans[ConsolidationStatus.POSTPONED.toInt()] = TEMPLATES.spanStatusPOSTPONED();
            statusSpans[ConsolidationStatus.REJECTED.toInt()] = TEMPLATES.spanStatusREJECTED();
            statusSpans[ConsolidationStatus.RESOLVED.toInt()] = TEMPLATES.spanStatusRESOLVED();
            statusSpans[ConsolidationStatus.SPLIT.toInt()] = TEMPLATES.spanStatusSPLIT();
        }

        public static interface SelectingConsoStatusCallback {

            public void statusSelected(String annotationId, ConsolidationStatus selectedStatus);
        }

        public SelectConsoStatusPopup(final String annotationId, ConsolidationStatus currentStatus, final SelectingConsoStatusCallback selectingStatusCallback) {
            super(true, true);

            setAnimationEnabled(true);
            setGlassEnabled(false);

            SafeHtmlBuilder sb = new SafeHtmlBuilder();
            sb.append(TEMPLATES.style());
            for (ConsolidationStatus status : orderedStatus) {
                if (status.equals(currentStatus)) {
                    sb.append(TEMPLATES.selectedConsoStatusProxy(status.toInt(), statusLabel[status.toInt()], statusSpans[status.toInt()]));
                } else {
                    sb.append(TEMPLATES.consoStatusProxy(status.toInt(), statusLabel[status.toInt()], statusSpans[status.toInt()]));
                }

            }
            HTML content = new HTML(sb.toSafeHtml());
            add(content);

            content.addClickHandler(new ClickHandler() {
                private Element previoustargetElement = null;

                @Override
                public void onClick(ClickEvent event) {
                    NativeEvent ntvEvent = event.getNativeEvent();
                    EventTarget evtTarget = ntvEvent.getEventTarget();
                    Element targetElement = evtTarget.cast();
                    if (!targetElement.equals(previoustargetElement)) {
                        previoustargetElement = targetElement;
                        if (selectingStatusCallback != null) {
                            String statusId = targetElement.getAttribute(CONSOSTATUSID_ATTRNAME);
                            if (!statusId.isEmpty()) {
                                try {
                                    ConsolidationStatus selectedStatus = ConsolidationStatus.withId(Integer.valueOf(statusId));
                                    selectingStatusCallback.statusSelected(annotationId, selectedStatus);

                                } catch (Exception ex) {
                                    GWT.log("Error when selection ConsoStatus\n" + ex.getMessage());
                                }
                            }
                        }
                        hide();
                    }
                }
            });
        }

        @Override
        protected void onPreviewNativeEvent(Event.NativePreviewEvent preview) {
            super.onPreviewNativeEvent(preview);
            NativeEvent evt = preview.getNativeEvent();
            if (evt.getType().equals("keydown")) {
                switch (evt.getKeyCode()) {
                    case KeyCodes.KEY_ENTER:
                    case KeyCodes.KEY_ESCAPE:
                        hide();
                        break;
                }
            }
        }
    }
    //--------------------------------------------------------------------------
    protected AnnotationDisplayerEngine engine;
    private int top;
    private int left;
    private Map<String, Path> beadsByAnnId = new HashMap<String, Path>();
    private List<String> overlaysId = new ArrayList<String>();
    private List<VectorObject> widgets = new ArrayList<VectorObject>();
    private List<Point> origins = new ArrayList<Point>();

    @Override
    public void onVeiledStatusRefreshed(String annotationId, boolean veiled) {
    }

    @Override
    public void reset(AnnotationDisplayerEngine engine) {
        beadsByAnnId.clear();
        for (String id : overlaysId) {
            Element e = Document.get().getElementById(id);
            if (e != null) {
                e.removeFromParent();
            }
        }
        for (VectorObject w : widgets) {
            engine.getDocumentCanvas().remove(w);
        }
        widgets.clear();
        origins.clear();
    }

    @Override
    public void init(AnnotationDisplayerEngine engine) {
        this.engine = engine;
        top = engine.getDocumentCanvas().getAbsoluteTop();
        left = engine.getDocumentCanvas().getAbsoluteLeft();
        reset(engine);
    }

    @Override
    public void complete() {
        engine = null;
    }

    private void createWidget(Annotation annotation, String typeColor, String statusColor) {
        int coreRadius = 5;
        int offX = 4;
        int offY = 8;
        DocumentView.Rect clip = engine.getAnnotationClip(annotation);
        if (clip == null) {
            GWT.log("(!) No clip for annotation " + annotation.getId());
        } else {
            Group markGroup = new Group();

            int leftOrig = clip.left;
            int topOrig = clip.top;

            //FIXME
            //Quickfix to handle overlapping ConsolidationStatus widget
            int checkForCollisionRepeat = 3;
            CollisionCheck:
            while (checkForCollisionRepeat-- > 0) {
                for (Point o : origins) {
                    if ((leftOrig - o.x) < coreRadius) {
                        leftOrig = leftOrig + coreRadius * 2 + 1;
                        continue CollisionCheck;
                    }
                }
                checkForCollisionRepeat = 0;
            }

            Path pin = new Path(leftOrig + coreRadius - offX - 1, topOrig - offY);
            pin.lineTo(leftOrig + 1, topOrig + 1);
            pin.lineTo(leftOrig - offX, topOrig - offY + coreRadius - 1);
            pin.close();
            pin.setStrokeWidth(1);
            pin.setStrokeColor("orange");
            pin.setFillColor(typeColor);
            markGroup.add(pin);

            Path backbead = new Path(leftOrig + coreRadius - offX, topOrig - offY);
            backbead.arc(coreRadius, coreRadius, 0, true, false, leftOrig - offX, topOrig - offY + coreRadius);
            backbead.close();
            backbead.setStrokeWidth(2);
            backbead.setStrokeColor("magenta");
            backbead.setFillOpacity(.0);
            markGroup.add(backbead);
            
            Path bead = new Path(leftOrig + coreRadius - offX, topOrig - offY);
            bead.arc(coreRadius, coreRadius, 0, true, false, leftOrig - offX, topOrig - offY + coreRadius);
            bead.close();
            bead.setStrokeWidth(1);
            bead.setStrokeColor("grey");
            bead.setFillOpacity(.5);
            bead.setFillColor(statusColor);
            beadsByAnnId.put(annotation.getId(), bead);
            markGroup.add(bead);

            Ellipse spark = new Ellipse(leftOrig - 3, topOrig - offY - 3, 1, 2);
            spark.setRotation(-45);
            spark.setStrokeWidth(0);
            spark.setFillColor("white");
            spark.setFillOpacity(.7);
            markGroup.add(spark);

            engine.getDocumentCanvas().add(markGroup);
            widgets.add(markGroup);
            origins.add(new Point(leftOrig, topOrig));


            Element overlayElt = Document.get().createDivElement();
            String uniqueId = HTMLPanel.createUniqueId();
            overlayElt.setId(uniqueId);
            overlayElt.setAttribute(AnnotationDisplayerEngine.AnnotationOverlayProducer.PROXYANNID_ATTRNAME, annotation.getId());
            overlayElt.setAttribute(PROXYANNCONSOSTATUS_ATTRNAME, "true");
            overlayElt.getStyle().setTop(topOrig - coreRadius - offY, Style.Unit.PX);
            overlayElt.getStyle().setLeft(leftOrig - coreRadius - offX, Style.Unit.PX);
            overlayElt.getStyle().setWidth(coreRadius * 2, Style.Unit.PX);
            overlayElt.getStyle().setHeight(coreRadius * 2, Style.Unit.PX);
            overlayElt.setClassName(StanEditorResources.INSTANCE.css().Overlay());

            engine.getOverlayContainer().appendChild(overlayElt);
            overlaysId.add(uniqueId);
        }
    }

    public boolean processAnnotation(SpecifiedAnnotation annotation, String color, List<String> markerIds, boolean veiled, ConsolidationStatus status) {
        String c = statusColor[status.toInt()];
        createWidget(annotation.getAnnotation(), color, c);
        return false;
    }

    public boolean processAnnotation(SpecifiedAnnotation annotation, String color, boolean veiled, ConsolidationStatus status) {
        String c = statusColor[status.toInt()];
        createWidget(annotation.getAnnotation(), color, c);
        return false;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public abstract void onConsolidationStatusChanged(String annotationId, ConsolidationStatus status);

    private void changeConsolidationStatus(String annotationId, ConsolidationStatus status) {
        Path bead = beadsByAnnId.get(annotationId);
        if (bead != null) {
            bead.setFillColor(statusColor[status.toInt()]);
        }
        onConsolidationStatusChanged(annotationId, status);
    }

    @Override
    public boolean isManagedOverlay(Element overlayElt) {
        return "true".equals(overlayElt.getAttribute(PROXYANNCONSOSTATUS_ATTRNAME));
    }

    @Override
    public void onAnnotationOverlayClick(ClickEvent event, String annotationId) {
        ConsolidationStatus currentStatus = ConsolidationStatus.POSTPONED;
        ConsolidationStatusDisplayer.SelectConsoStatusPopup contextMenu = new ConsolidationStatusDisplayer.SelectConsoStatusPopup(annotationId, currentStatus, new ConsolidationStatusDisplayer.SelectConsoStatusPopup.SelectingConsoStatusCallback() {
            @Override
            public void statusSelected(String annotationId, ConsolidationStatus selectedStatus) {
                //GWT.log("Status " + selectedStatus.toString() + " for " + annotationId);
                changeConsolidationStatus(annotationId, selectedStatus);
            }
        });
        contextMenu.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
        contextMenu.show();
    }

    @Override
    public void onAnnotationOverlayMouseMove(MouseMoveEvent event, String annotationId) {
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}
