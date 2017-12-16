/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.user.client.Element;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.vaadin.gwtgraphics.client.DrawingArea;

/**
 *
 * @author fpapazian
 */
public class AnnotationDisplayerEngine {

    public static final String MISSINGTYPEDEF_COLOR = "silver";

    public static interface AnnotationDisplayer {

        void init(AnnotationDisplayerEngine engine);

        void onVeiledStatusRefreshed(String annotationId, boolean veiled);

        void complete();

        void reset(AnnotationDisplayerEngine engine);
    }

    public static interface PrimaryAnnotationDisplayer extends AnnotationDisplayer {

        /**
         *
         * @return true if the annotation has been rendered
         */
        boolean processAnnotation(SpecifiedAnnotation annotation, String color, List<String> markerIds, boolean veiled);
    }

    public static interface TextAnnotationDisplayer extends PrimaryAnnotationDisplayer {
    }

    public static interface SecondaryAnnotationDisplayer extends AnnotationDisplayer {

        /**
         *
         * @return true if the annotation has been rendered
         */
        boolean processAnnotation(SpecifiedAnnotation annotation, String color, boolean veiled);
    }

    public static interface GroupAnnotationDisplayer extends SecondaryAnnotationDisplayer {
    }

    public static interface RelationAnnotationDisplayer extends SecondaryAnnotationDisplayer {
    }

    public static interface AnnotationMainDisplayer {

        DocumentView.Rect getAnnotationClip(Annotation annotation);
    }

    public static interface TextAnnotationMainDisplayer extends AnnotationMainDisplayer {

        List<DocumentView.FlankingClips> getAnnotationFragmentClips(Annotation annotation);
    }

    public static interface AnnotationOverlayHandler {

        public final String PROXYANNID_ATTRNAME = "aae_prxyid";

        /**
         *
         * @return true if the specified Overlay is managed/produced by this
         * OverlayDisplayer
         */
        boolean isManagedOverlay(com.google.gwt.dom.client.Element overlayElt);
    }

    public static interface AnnotationOverlayProducer extends AnnotationOverlayHandler {
    }

    public static interface AnnotationOverlayEventsHandler extends AnnotationOverlayHandler {

        public void onAnnotationOverlayClick(ClickEvent event, String annotationId);

        public void onAnnotationOverlayMouseMove(MouseMoveEvent event, String annotationId);
    }

    //
    public static String getAnnIdFromOverlayElement(com.google.gwt.dom.client.Element targetElement) {
        return targetElement.getAttribute(AnnotationOverlayProducer.PROXYANNID_ATTRNAME);
    }
    //--------------------------------------------------------------------------
    //
    private AnnotatedTextHandler annotatedDoc;
    private AnnotationDocumentViewMapper mapper;
    //
    private DrawingArea documentCanvas;
    private Element overlayContainer;
    //
    private List<AnnotationDisplayer> allDisplayers = new ArrayList<AnnotationDisplayer>();
    private List<TextAnnotationDisplayer> textAnnotationDisplayers = new ArrayList<TextAnnotationDisplayer>();
    private List<GroupAnnotationDisplayer> groupAnnotationDisplayers = new ArrayList<GroupAnnotationDisplayer>();
    private List<RelationAnnotationDisplayer> relationAnnotationDisplayers = new ArrayList<RelationAnnotationDisplayer>();
    private List<AnnotationOverlayEventsHandler> allOverlayEventsHandler = new ArrayList<AnnotationOverlayEventsHandler>();
    private TextAnnotationMainDisplayer textAnnotationMainDisplayer = null;
    private AnnotationMainDisplayer groupAnnotationMainDisplayer = null;
    private AnnotationMainDisplayer relationAnnotationMainDisplayer = null;
    //
    private HashSet<String> renderedAnnotations = new HashSet<String>();

    public DrawingArea getDocumentCanvas() {
        return documentCanvas;
    }

    public Element getOverlayContainer() {
        return overlayContainer;
    }

    public void setDocument(AnnotatedTextHandler annotatedDoc, AnnotationDocumentViewMapper mapper) {
        this.annotatedDoc = annotatedDoc;
        this.mapper = mapper;
    }

    public void addDisplayer(AnnotationDisplayer displayer) {
        allDisplayers.add(displayer);
        if (displayer instanceof TextAnnotationDisplayer) {
            textAnnotationDisplayers.add((TextAnnotationDisplayer) displayer);
            if (displayer instanceof TextAnnotationMainDisplayer) {
                textAnnotationMainDisplayer = (TextAnnotationMainDisplayer) displayer;
            }
        }
        if (displayer instanceof GroupAnnotationDisplayer) {
            groupAnnotationDisplayers.add((GroupAnnotationDisplayer) displayer);
            if (displayer instanceof AnnotationMainDisplayer) {
                groupAnnotationMainDisplayer = (AnnotationMainDisplayer) displayer;
            }
        }
        if (displayer instanceof RelationAnnotationDisplayer) {
            relationAnnotationDisplayers.add((RelationAnnotationDisplayer) displayer);
            if (displayer instanceof AnnotationMainDisplayer) {
                relationAnnotationMainDisplayer = (AnnotationMainDisplayer) displayer;
            }
        }
    }

    public void addOverlayEventsHandler(AnnotationOverlayEventsHandler handler) {
        allOverlayEventsHandler.add(handler);
    }

    public DocumentView.Rect getAnnotationClip(Annotation annotation) {
        AnnotationMainDisplayer displayer = null;

        AnnotationKind kind = annotation.getAnnotationKind();
        if (kind.equals(AnnotationKind.TEXT)) {
            displayer = textAnnotationMainDisplayer;
        } else if (kind.equals(AnnotationKind.GROUP)) {
            displayer = groupAnnotationMainDisplayer;
        } else if (kind.equals(AnnotationKind.RELATION)) {
            displayer = relationAnnotationMainDisplayer;
        }

        return displayer != null ? displayer.getAnnotationClip(annotation) : null;
    }

    public List<DocumentView.FlankingClips> getAnnotationFragmentClips(Annotation annotation) {
        return textAnnotationMainDisplayer != null ? textAnnotationMainDisplayer.getAnnotationFragmentClips(annotation) : null;
    }

    private void processSecondaryAnnotation(List<? extends SecondaryAnnotationDisplayer> displayers, SpecifiedAnnotation annotation) {
        String annotationId = annotation.getAnnotation().getId();
        AnnotationTypeDefinition typeDef = annotatedDoc.getAnnotatedText().getAnnotationSchema().getAnnotationTypeDefinition(annotation.getAnnotation().getAnnotationType());
        String color;
        if (typeDef != null) {
            color = typeDef.getColor();
        } else {
            color = MISSINGTYPEDEF_COLOR;
        }
        boolean veiled = mapper.isVeiled(annotationId);
        for (SecondaryAnnotationDisplayer d : displayers) {
            if (d.processAnnotation(annotation, color, veiled)) {
                renderedAnnotations.add(annotationId);
            }
        }
    }

    private boolean areRendered(Collection<AnnotationReference> annotationRefererences) {
        boolean renderable = true;
        for (AnnotationReference aRef : annotationRefererences) {
            String referencedId = aRef.getAnnotationId();
            Annotation referenced = annotatedDoc.getAnnotation(referencedId);
            if (referenced == null) {
                GWT.log("Missing referenced Annotation id= " + referencedId);
            } else if (!renderedAnnotations.contains(referencedId)) {
                renderable = false;
                break;
            }
        }
        return renderable;
    }

    private boolean canBeRendered(SpecifiedAnnotation annotation) {
        AnnotationKind kind = annotation.getAnnotation().getAnnotationKind();
        if (kind.equals(AnnotationKind.GROUP)) {
            return areRendered(annotation.getAnnotation().getAnnotationGroup().getComponentRefs());
        } else if (kind.equals(AnnotationKind.RELATION)) {
            return areRendered(annotation.getAnnotation().getRelation().getRolesArguments().values());
        } else {
            return true;
        }
    }

    public void refreshSecondaryDisplayer(DrawingArea documentCanvas, Element overlayContainer, AnnotationDisplayer displayer) {
        this.documentCanvas = documentCanvas;
        this.overlayContainer = overlayContainer;
        displayer.reset(this);
        List<TextAnnotationDisplayer> textAnnotationDisps = new ArrayList<TextAnnotationDisplayer>();
        if (displayer instanceof TextAnnotationDisplayer) {
            textAnnotationDisps.add((TextAnnotationDisplayer) displayer);
        }
        List<GroupAnnotationDisplayer> groupAnnotationDisps = new ArrayList<GroupAnnotationDisplayer>();
        if (displayer instanceof GroupAnnotationDisplayer) {
            groupAnnotationDisps.add((GroupAnnotationDisplayer) displayer);
        }
        List<RelationAnnotationDisplayer> relationAnnotationDisps = new ArrayList<RelationAnnotationDisplayer>();
        if (displayer instanceof RelationAnnotationDisplayer) {
            relationAnnotationDisps.add((RelationAnnotationDisplayer) displayer);
        }
        proceed(documentCanvas, overlayContainer, textAnnotationDisps, groupAnnotationDisps, relationAnnotationDisps, true);
    }

    public void proceed(DrawingArea documentCanvas, Element overlayContainer) {
        renderedAnnotations.clear();
        proceed(documentCanvas, overlayContainer, textAnnotationDisplayers, groupAnnotationDisplayers, relationAnnotationDisplayers, false);
        renderedAnnotations.clear();
    }

    private void proceed(DrawingArea documentCanvas, Element overlayContainer,
            List<TextAnnotationDisplayer> textAnnotationDisplayers,
            List<GroupAnnotationDisplayer> groupAnnotationDisplayers,
            List<RelationAnnotationDisplayer> relationAnnotationDisplayers,
            boolean force) {

        Set<AnnotationDisplayer> allConcernedDisplayers = new HashSet<AnnotationDisplayer>();
        allConcernedDisplayers.addAll(textAnnotationDisplayers);
        allConcernedDisplayers.addAll(groupAnnotationDisplayers);
        allConcernedDisplayers.addAll(relationAnnotationDisplayers);

        if (mapper != null && annotatedDoc != null) {
            this.documentCanvas = documentCanvas;
            this.overlayContainer = overlayContainer;
            try {
                for (AnnotationDisplayer d : allConcernedDisplayers) {
                    d.init(this);
                }

                //1- process primary annotation (i.e. Text bound annotations)
                for (SpecifiedAnnotation annotation : annotatedDoc.getAnnotatedText().getSpecifiedAnnotations(AnnotationKind.TEXT)) {
                    String annotationId = annotation.getAnnotation().getId();
                    if (!mapper.isFormattingAnnotation(annotationId)) {
                        AnnotationTypeDefinition typeDef = annotatedDoc.getAnnotatedText().getAnnotationSchema().getAnnotationTypeDefinition(annotation.getAnnotation().getAnnotationType());
                        String color;
                        if (typeDef != null) {
                            color = typeDef.getColor();
                        } else {
                            color = MISSINGTYPEDEF_COLOR;
                        }
                        List<String> markerIds = mapper.getMarkerIdsFromAnnotationId(annotationId);
                        boolean veiled = mapper.isVeiled(annotationId);
                        for (TextAnnotationDisplayer d : textAnnotationDisplayers) {
                            if (d.processAnnotation(annotation, color, markerIds, veiled)) {
                                renderedAnnotations.add(annotationId);
                            }
                        }
                    }
                }

                //2- process secondary annotation
                List<SpecifiedAnnotation> groups = annotatedDoc.getAnnotatedText().getSpecifiedAnnotations(AnnotationKind.GROUP);
                List<SpecifiedAnnotation> relations = annotatedDoc.getAnnotatedText().getSpecifiedAnnotations(AnnotationKind.RELATION);
                List<SpecifiedAnnotation> allSecondaryAnnotations = new ArrayList<SpecifiedAnnotation>();
                if (groups != null) {
                    allSecondaryAnnotations.addAll(groups);
                }
                if (relations != null) {
                    allSecondaryAnnotations.addAll(relations);
                }

                /*
                 * Since Relations and Groups refer to other annotations, these
                 * referenced annotations must be drawn before. Thus, Annotations
                 * can be organized by levels, TEXT annotation being on the Level 0
                 * (because they do not reference any other annotations)
                 */

                //Max number of levels that will be explored (deepness)
                //FIXME : hard coded value, but in pratice high deepness surely means corrupted data 
                int iterationTreshold = 50;
                boolean moreAnnotationRendered = true;
                while (!allSecondaryAnnotations.isEmpty() && moreAnnotationRendered && --iterationTreshold > 0) {

                    moreAnnotationRendered = false;
                    List<SpecifiedAnnotation> toAdd = new ArrayList<SpecifiedAnnotation>();
                    Iterator<SpecifiedAnnotation> i2 = allSecondaryAnnotations.iterator();
                    while (i2.hasNext()) {
                        SpecifiedAnnotation annotation = i2.next();
                        if (force || canBeRendered(annotation)) {
                            moreAnnotationRendered = true;
                            i2.remove();
                            toAdd.add(annotation);
                        }
                    }

                    for (SpecifiedAnnotation annotation : toAdd) {
                        AnnotationKind kind = annotation.getAnnotation().getAnnotationKind();
                        if (kind.equals(AnnotationKind.GROUP)) {
                            processSecondaryAnnotation(groupAnnotationDisplayers, annotation);
                        } else if (kind.equals(AnnotationKind.RELATION)) {
                            processSecondaryAnnotation(relationAnnotationDisplayers, annotation);
                        }
                    }
                }

            } finally {
                for (AnnotationDisplayer d : allConcernedDisplayers) {
                    d.complete();
                }
                this.documentCanvas = null;
                this.overlayContainer = null;
            }
        }
    }

    public void onVeiledStatusRefreshed(String annotationId, boolean veiled) {
        for (AnnotationDisplayer d : allDisplayers) {
            d.onVeiledStatusRefreshed(annotationId, veiled);
        }
    }

    public void reset() {
        for (AnnotationDisplayer d : allDisplayers) {
            d.reset(this);
        }
    }

    void onOverlayClick(ClickEvent event) {
        NativeEvent ntvEvent = event.getNativeEvent();
        EventTarget evtTarget = ntvEvent.getEventTarget();
        com.google.gwt.dom.client.Element targetElement = evtTarget.cast();

        String annotationId = AnnotationDisplayerEngine.getAnnIdFromOverlayElement(targetElement);
        if (annotationId != null) {
            for (AnnotationOverlayEventsHandler handler : allOverlayEventsHandler) {
                if (handler.isManagedOverlay(targetElement)) {
                    handler.onAnnotationOverlayClick(event, annotationId);
                }
            }
        }
    }

    void onMouseMove(MouseMoveEvent event) {
        NativeEvent ntvEvent = event.getNativeEvent();
        EventTarget evtTarget = ntvEvent.getEventTarget();
        com.google.gwt.dom.client.Element targetElement = evtTarget.cast();

        String annotationId = AnnotationDisplayerEngine.getAnnIdFromOverlayElement(targetElement);
        if (annotationId != null) {
            for (AnnotationOverlayEventsHandler handler : allOverlayEventsHandler) {
                if (handler.isManagedOverlay(targetElement)) {
                    handler.onAnnotationOverlayMouseMove(event, annotationId);
                }
            }
        }
    }
}
