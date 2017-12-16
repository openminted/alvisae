/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView.Rect;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import java.util.*;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 *
 * @author fpapazian
 */
public abstract class CombinedAnnotationDisplayer {

    static {
        StanEditorResources.INSTANCE.css().ensureInjected();
    }

    public static class Point {

        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static Point getIsoBarycentre(List<Point> points) {
        float x = 0, y = 0;
        for (Point p : points) {
            x += p.x;
            y += p.y;
        }
        int size = points.size();
        x = x / size;
        y = y / size;

        Point isoBar = new Point(Math.round(x), Math.round(y));
        return isoBar;
    }

    public abstract class CombinedAnnotationWidget extends Group {

        private final String combinedAnnotationId;
        private final ArrayList<String> annotationIds;
        private final ArrayList<Rect> annotationClips;
        private final String color;
        private final int topOffset;
        private final int leftOffset;
        private final ArrayList<Point> centers = new ArrayList<Point>();
        private final Point centerPoint;

        protected CombinedAnnotationWidget(String combinedAnnotationId, AnnotationTypeDefinition typedef, ArrayList<String> annotationIds, ArrayList<Rect> annotationClips) {
            this.combinedAnnotationId = combinedAnnotationId;
            if (typedef != null) {
                this.color = typedef.getColor();
            } else {
                this.color = AnnotationDisplayerEngine.MISSINGTYPEDEF_COLOR;
            }
            this.annotationIds = annotationIds;
            this.annotationClips = new ArrayList<Rect>();

            leftOffset = getCanvas().getAbsoluteLeft();
            topOffset = getCanvas().getAbsoluteTop();

            for (Rect annClip : annotationClips) {
                int sLeft = annClip.left;
                int sTop = annClip.top;
                int sx = sLeft + annClip.width / 2;
                int sy = sTop + annClip.height / 2;
                this.annotationClips.add(new Rect(sLeft, sTop, annClip.width, annClip.height));
                centers.add(new Point(sx, sy));
            }

            centerPoint = getIsoBarycentre(centers);

        }

        public String getAnnotationId() {
            return combinedAnnotationId;
        }

        public abstract boolean isAnnotationSelected();

        public boolean isAnnotationVeiled() {
            return getMapper().isVeiled(getAnnotationId());
        }

        public Point getCenterPoint() {
            return centerPoint;
        }

        public abstract VectorObject getCenterWidget();

        public ArrayList<Rect> getAnnotationClips() {
            return annotationClips;
        }

        public ArrayList<String> getAnnotationIds() {
            return annotationIds;
        }

        public ArrayList<Point> getCenters() {
            return centers;
        }

        public String getColor() {
            return color;
        }

        public final int getLeftOffset() {
            return leftOffset;
        }

        public final int getTopOffset() {
            return topOffset;
        }

        public void display() {
            display(mapper.isVeiled(combinedAnnotationId), isAnnotationSelected(), false);
        }

        public abstract void display(boolean veiled, boolean selected, boolean hovered);

        public abstract Rect getCenterClip();

        public Element createOverlay(Element overlayParentElt) {
            Rect widgetClip = getCenterClip();
            Element overlayElt = Document.get().createDivElement();
            String uniqueId = HTMLPanel.createUniqueId();
            overlayElt.setId(uniqueId);
            overlayElt.setAttribute(AnnotationDisplayerEngine.AnnotationOverlayProducer.PROXYANNID_ATTRNAME, getAnnotationId());
            overlayElt.getStyle().setTop(widgetClip.top, Unit.PX);
            overlayElt.getStyle().setLeft(widgetClip.left, Unit.PX);
            overlayElt.getStyle().setWidth(widgetClip.width, Unit.PX);
            overlayElt.getStyle().setHeight(widgetClip.height, Unit.PX);
            overlayElt.setClassName(StanEditorResources.INSTANCE.css().Overlay());

            overlayParentElt.appendChild(overlayElt);
            return overlayElt;
        }
    }
    //    
    //  
    private final EventBus eventBus;
    private final DrawingArea canvas;
    private final HashMap<String, CombinedAnnotationWidget> combinedAnnWidget = new HashMap<String, CombinedAnnotationWidget>();
    private AnnotatedTextHandler annotatedDoc;
    private AnnotationDocumentViewMapper mapper;
    private HashMap<Integer, Integer> interlineOccupancy = new HashMap<Integer, Integer>();
    private int interlineSize = 0;
    private final Map<String, List<String>> inboundSegmentByAnnId = new HashMap<String, List<String>>();
    private boolean refreshOptional;

    public CombinedAnnotationDisplayer(DrawingArea canvas, final EventBus eventBus) {
        this.canvas = canvas;
        this.eventBus = eventBus;
    }

    public void setDocument(AnnotatedTextHandler annotatedDoc, AnnotationDocumentViewMapper mapper, CombinedAnnotationDisplayer otherDisplayer) {
        this.annotatedDoc = annotatedDoc;
        this.mapper = mapper;
    }

    public Set<String> getAnnotationIds() {
        return combinedAnnWidget.keySet();
    }

    public CombinedAnnotationWidget getWidget(String combinedAnnotationId) {
        return combinedAnnWidget.get(combinedAnnotationId);
    }

    protected void addWidget(String combinedAnnotationId, CombinedAnnotationWidget widget) {
        combinedAnnWidget.put(combinedAnnotationId, widget);
    }

    public void setRefreshOptional() {
        setRefreshOptional(true);
    }

    protected void setRefreshOptional(boolean optionalRefresh) {
        this.refreshOptional = optionalRefresh;
    }

    protected boolean isRefreshOptional() {
        return refreshOptional;
    }

    public void refresh() {
        for (CombinedAnnotationWidget w : combinedAnnWidget.values()) {
            w.display();
        }
    }

    public abstract void clearSelection();

    public void reset(AnnotationDisplayerEngine engine) {
        annotatedDoc = null;
        clearCombinedWidget();
    }

    public void clearCombinedWidget() {
        combinedAnnWidget.clear();
        interlineOccupancy.clear();
        inboundSegmentByAnnId.clear();
    }

    /**
     * Tell to the displayer the document interline size to help the layouting
     * of the graphic elements
     *
     * @param interlineSize
     */
    public void setInterlineSize(int interlineSize) {
        this.interlineSize = interlineSize;
    }

    protected int getInterlineSize() {
        return interlineSize;
    }

    protected int getInterlineOccupancy(int interlineIndex) {
        Integer occupancy = interlineOccupancy.get(interlineIndex);
        return occupancy == null ? 0 : occupancy;
    }

    protected void incInterlineOccupancy(int interlineIndex) {
        Integer previousOccupancy = interlineOccupancy.get(interlineIndex);
        if (previousOccupancy == null) {
            previousOccupancy = new Integer(0);
        }
        previousOccupancy++;
        interlineOccupancy.put(interlineIndex, previousOccupancy);

    }

    protected int getInboundSegmentIndex(String combinedAnnotationId, String referencedAnnotationId) {
        List<String> inboundSegments = inboundSegmentByAnnId.get(referencedAnnotationId);
        if (inboundSegments == null) {
            inboundSegments = new ArrayList<String>();
            inboundSegmentByAnnId.put(referencedAnnotationId, inboundSegments);
            inboundSegments.add(combinedAnnotationId);
        }
        int idx = inboundSegments.indexOf(combinedAnnotationId);
        if (idx == -1) {
            inboundSegments.add(combinedAnnotationId);
            idx = inboundSegments.indexOf(combinedAnnotationId);
        }
        return idx;
    }

    public abstract CombinedAnnotationWidget addAnnotation(Annotation annotation);

    public AnnotatedText getAnnotatedDoc() {
        return annotatedDoc == null ? null : annotatedDoc.getAnnotatedText();
    }

    public AnnotatedTextHandler getAnnotatedTextHandler() {
        return annotatedDoc;
    }

    public DrawingArea getCanvas() {
        return canvas;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public AnnotationDocumentViewMapper getMapper() {
        return mapper;
    }

    public void refreshVeiledStatus(String annotationId) {
        CombinedAnnotationWidget w = combinedAnnWidget.get(annotationId);
        if (w != null) {
            w.display();
        }
    }
}
