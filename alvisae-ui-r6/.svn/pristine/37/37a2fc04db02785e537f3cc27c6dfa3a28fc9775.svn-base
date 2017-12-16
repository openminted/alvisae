/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Config.ShortCutToActionTypeMapper;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.AnnotationMainDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.AnnotationOverlayProducer;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.RelationAnnotationDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView.Rect;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.AnnotationSelections;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.RelationAnnotationSelection;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextProcessor;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * Graphically renders Relation annotations
 *
 * @author fpapazian
 */
public class RelationDisplayer extends CombinedAnnotationDisplayer implements RelationAnnotationDisplayer, AnnotationMainDisplayer, AnnotationOverlayProducer {

    private static final String PROXYANNRELKIND_ATTRNAME = "aae_prxyrel";

    public class RelationWidget extends CombinedAnnotationWidget {

        private static final int coreXSize = 4;
        private static final int coreYSize = 6;
        //
        private final double vertCoef[] = {.5, .4, .6, .3, .7, .2, .55, .45, .65, .35, .25,};
        private final double horzCoef[] = {.05, .25, .45, .65, .85, .15, .35, .55, .75};
        private final Path centerWidget;
        private final ArrayList<Path> backlines;
        private final ArrayList<Path> frontlines;
        private final ArrayList<Rectangle> shadows;
        private final ArrayList<Path> hints;
        private final Rect centerClip;
        private final String description;

        protected RelationWidget(String relationId, AnnotationTypeDefinition typedef, ArrayList<String> annotationIds, ArrayList<Rect> annotationClips, ArrayList<String> roles) {
            super(relationId, typedef, annotationIds, annotationClips);

            shadows = new ArrayList<Rectangle>();
            hints = new ArrayList<Path>();

            int typicalHeight = getAnnotationClips().get(0).height;
            int interlineSize = getInterlineSize();
            if (interlineSize == 0) {
                interlineSize = (int) (typicalHeight * 1.25);
            }
            int interlineSpaceAvail = interlineSize - typicalHeight / 2;

            int lowestAnnotationIdx = 0;
            int lowestAnnotationFloor = 0;
            int highestAnnotationIdx = 0;
            int highestAnnotationFloor = 999999;

            int leftAnnotationIdx = 0;
            int leftAnnotationCenter = 999999;
            int rightAnnotationIdx = 0;
            int rightAnnotationCenter = 0;

            //create shadow boxes under the TEXT annotations
            int index = 0;
            for (Rect clip : getAnnotationClips()) {
                AnnotationKind argKind =  getMapper().getAnnotation(annotationIds.get(index)).getAnnotationKind();
                if (AnnotationKind.TEXT.equals(argKind)) {
                    Rectangle sShadow = new Rectangle(clip.left, clip.top, clip.width + 2, clip.height + 2);
                    sShadow.setStrokeWidth(1);
                    sShadow.setFillOpacity(0);
                    sShadow.setStrokeColor("black");
                    shadows.add(sShadow);
                    this.add(sShadow);

                    Path relationHint = new Path(clip.left + clip.width - 2 - 2 * coreXSize, clip.top);
                    relationHint.lineRelativelyTo(coreXSize, -coreYSize);
                    relationHint.lineRelativelyTo(coreXSize, coreYSize);
                    relationHint.close();
                    relationHint.setStrokeWidth(1);
                    relationHint.setStrokeColor("grey");
                    relationHint.setFillOpacity(.5);
                    relationHint.setFillColor("silver");
                    hints.add(relationHint);
                    this.add(relationHint);
                }

                //determine the argument annotations bordering the area covered by the relation and its arguments
                int annotationFloor = clip.top + clip.height;
                if (annotationFloor > lowestAnnotationFloor) {
                    lowestAnnotationFloor = annotationFloor;
                    lowestAnnotationIdx = index;
                }
                if (annotationFloor < highestAnnotationFloor) {
                    highestAnnotationFloor = annotationFloor;
                    highestAnnotationIdx = index;
                }
                int annotationCenter = clip.left + clip.width / 2;
                if (leftAnnotationCenter > annotationCenter) {
                    leftAnnotationCenter = annotationCenter;
                    leftAnnotationIdx = index;
                }
                if (rightAnnotationCenter < annotationCenter) {
                    rightAnnotationCenter = annotationCenter;
                    rightAnnotationIdx = index;
                }

                index++;
            }


            backlines = new ArrayList<Path>();
            frontlines = new ArrayList<Path>();

            //center point of the graphical core of the relation
            Point coreCenter = getIsoBarycentre(getCenters());

            int arity = getAnnotationClips().size();
            boolean stackedAnnotations = false;

            int interlineIndex = lowestAnnotationFloor / interlineSize;
            int occupancy = getInterlineOccupancy(interlineIndex);

            int verticalOffset = (int) (interlineSpaceAvail * vertCoef[occupancy % vertCoef.length]);

            //the graphical core of the relation is drawn within the interline near the lowest annotation
            coreCenter.y = lowestAnnotationFloor + verticalOffset;
            incInterlineOccupancy(interlineIndex);
            ArrayList<Point> argumentCenters = getCenters();
            if (arity == 2) {

                //if the centers are too close, apply an horizontal shift to the center
                if ((rightAnnotationCenter - leftAnnotationCenter) < 20) {
                    coreCenter.x = getAnnotationClips().get(leftAnnotationIdx).left - 10;
                    stackedAnnotations = true;
                }
            }

            List<Point> path = new ArrayList<Point>();
            int annIndex = 0;
            for (Point center : argumentCenters) {
                String annotationId = annotationIds.get(annIndex);
                String role = roles.get(annIndex);
                String title = role + "(" + AnnotatedTextProcessor.getBriefId(annotationId) + ")";

                path.clear();

                //compute segments linking the current argument to the graphical core of the relation
                Point startingPt = new Point(center.x, center.y);

                //adjust starting point of current linking segment (based on center of arguments clip) in order to avoid overlapping with other linking segments
                int direction;
                //the horizontal offset is adjusted in the direction of the graphical core of the relation
                if (coreCenter.x < startingPt.x) {
                    direction = -1;
                } else {
                    direction = 1;
                }

                int inboundSegmentIndex = getInboundSegmentIndex(relationId, annotationId);
                Rect argClip = getAnnotationClips().get(annIndex);
                int horizontalOffset = (int) (argClip.width / 2 * horzCoef[inboundSegmentIndex % horzCoef.length]);
                startingPt.x = startingPt.x + direction * horizontalOffset;

                if (stackedAnnotations && (annIndex == highestAnnotationIdx)) {
                    path.add(new Point(coreCenter.x, center.y));
                } else {
                    path.add(new Point(startingPt.x, coreCenter.y));
                }
                path.add(new Point(coreCenter.x, coreCenter.y));

                annIndex++;

                //background segments
                Path line = new Path(startingPt.x, startingPt.y);
                for (Point p : path) {
                    line.lineTo(p.x, p.y);
                }
                line.setFillColor(null);
                line.setTitle(title);
                backlines.add(line);
                this.add(line);

                //foreground segments
                line = new Path(startingPt.x, startingPt.y);
                for (Point p : path) {
                    line.lineTo(p.x, p.y);
                }
                line.setFillColor(null);
                line.setTitle(title);
                frontlines.add(line);
                this.add(line);
            }

            //draw center
            if (arity == 2) {
                //if first argument of the binary relation is on the left side, 
                //then the core is an arrow-head directed to the right; otherwise to the left
                int director = leftAnnotationIdx == 0 ? 1 : -1;
                centerWidget = new Path(coreCenter.x - director * coreXSize, coreCenter.y - director * coreYSize);
                centerWidget.lineTo(coreCenter.x + director * coreXSize, coreCenter.y);
                centerWidget.lineTo(coreCenter.x - director * coreXSize, coreCenter.y + director * coreYSize);
                centerWidget.close();
            } else {
                centerWidget = new Path(coreCenter.x, coreCenter.y - coreYSize);
                centerWidget.lineTo(coreCenter.x + coreXSize, coreCenter.y);
                centerWidget.lineTo(coreCenter.x, coreCenter.y + coreYSize);
                centerWidget.lineTo(coreCenter.x - coreXSize, coreCenter.y);
                centerWidget.close();
            }

            if (typedef != null) {
                description = "Relation '" + typedef.getType() + "' (" + AnnotatedTextProcessor.getBriefId(relationId) + ")";
            } else {
                description = "Relation 'unknown-type' (" + AnnotatedTextProcessor.getBriefId(relationId) + ")";
            }
            centerWidget.setTitle(description);

            centerClip = new Rect(coreCenter.x - coreXSize, coreCenter.y - coreYSize, coreXSize * 2, coreYSize * 2);

            display(isAnnotationVeiled(), isAnnotationSelected(), false);

            this.add(centerWidget);
            this.addMouseOverHandler(new MouseOverHandler() {
                @Override
                public void onMouseOver(MouseOverEvent event) {
                    display(isAnnotationVeiled(), isAnnotationSelected(), true);
                }
            });

            this.addMouseOutHandler(new MouseOutHandler() {
                @Override
                public void onMouseOut(MouseOutEvent event) {
                    display(isAnnotationVeiled(), isAnnotationSelected(), false);
                }
            });

            this.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    onRelationClick(event, getAnnotationId());
                }
            });

        }

        @Override
        public boolean isAnnotationSelected() {
            return selectedRelations.isAnnotationSelected(getAnnotationId());
        }

        @Override
        public void display(boolean veiled, boolean selected, boolean hovered) {
            centerWidget.setVisible(!veiled);
            for (Rectangle shadow : shadows) {
                shadow.setVisible(!veiled);
            }
            for (Path hint : hints) {
                hint.setVisible(veiled);
            }
            for (Path fronLine : frontlines) {
                fronLine.setVisible(!veiled);
            }
            for (Path backline : backlines) {
                backline.setVisible(!veiled);
            }
            if (!veiled) {
                if (hovered) {
                    if (selected) {
                        centerWidget.setStrokeColor(GlobalStyles.HoveredSelectedRelation.getColor());
                        centerWidget.setStrokeWidth(2);
                        centerWidget.setFillColor(getColor());

                        for (Path backline : backlines) {
                            backline.setStrokeColor(GlobalStyles.HoveredSelectedRelation.getColor());
                            backline.setStrokeWidth(4);
                        }
                    } else {
                        centerWidget.setStrokeColor(GlobalStyles.HoveredRelation.getColor());
                        centerWidget.setStrokeWidth(2);
                        centerWidget.setFillColor(getColor());
                        for (Path backline : backlines) {
                            backline.setStrokeColor(GlobalStyles.HoveredRelation.getColor());
                            backline.setStrokeWidth(5);
                        }
                    }
                } else {
                    if (selected) {
                        centerWidget.setStrokeColor(GlobalStyles.SelectedRelation.getColor());
                        centerWidget.setStrokeWidth(2);
                        centerWidget.setFillColor(getColor());

                        for (Path fronLine : frontlines) {

                            fronLine.setStrokeColor(getColor());
                            fronLine.setStrokeWidth(3);
                        }

                        for (Path backline : backlines) {
                            backline.setStrokeColor(GlobalStyles.SelectedRelation.getColor());
                            backline.setStrokeWidth(4);
                        }
                    } else {
                        centerWidget.setStrokeColor("darkgrey");
                        centerWidget.setStrokeWidth(1);
                        centerWidget.setFillColor(getColor());


                        for (Path fronLine : frontlines) {
                            //FIXME the color of the segment linking the argument to the center should be specific of the role
                            fronLine.setStrokeColor(getColor());
                            fronLine.setStrokeWidth(1);
                        }

                        for (Path backline : backlines) {
                            backline.setStrokeColor("white");
                            backline.setStrokeWidth(3);
                        }
                    }
                }
            }
        }

        @Override
        public VectorObject getCenterWidget() {
            return centerWidget;
        }

        @Override
        public Rect getCenterClip() {
            return centerClip;
        }

        @Override
        public Element createOverlay(Element overlayParentElt) {
            Element overlayElt = super.createOverlay(overlayParentElt);
            overlayElt.setTitle(description);
            overlayElt.setAttribute(PROXYANNRELKIND_ATTRNAME, "true");
            return overlayElt;
        }
    }
    //

    @Override
    public boolean isManagedOverlay(Element overlayElt) {
        return !overlayElt.getAttribute(PROXYANNRELKIND_ATTRNAME).isEmpty();
    }
    //
    private final AnnotationSelections selectedRelations;
    private AnnotationDisplayerEngine engine;

    public RelationDisplayer(DrawingArea canvas, EventBus eventBus, AnnotationSelections selectedRelations) {
        super(canvas, eventBus);
        this.selectedRelations = selectedRelations;
        //clear any relation selection when canvas is clicked
        getCanvas().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                clearSelection();
            }
        });

    }

    @Override
    public void clearSelection() {
        getEventBus().fireEvent(new RelationSelectionEmptiedEvent(getAnnotatedTextHandler()));
    }

    public void onRelationClick(ClickEvent event, String relationId) {
        //[Ctrl]/[Command] key used for multiselect
        boolean multiSelectKeyDown = (!ShortCutToActionTypeMapper.isMacOs() && event.isControlKeyDown()) || (ShortCutToActionTypeMapper.isMacOs() && event.isMetaKeyDown());
        if (!multiSelectKeyDown) {
            selectedRelations.clear();
        }

        boolean selected = selectedRelations.isAnnotationSelected(relationId);
        //
        if (selected && multiSelectKeyDown) {
            //when multiselection is explicitely requested and the clicked Relation was previously selected, 
            //it must be removed from selection
            selectedRelations.removeAnnotationFromSelection(relationId);
            selected = false;
        } else {
            selectedRelations.getSelections().add(new RelationAnnotationSelection(getAnnotatedDoc().getAnnotation(relationId)));
            selected = true;
        }

        //#2452 remove TextAnnotation or Group selection if no explicit multiselection requested 
        if (!multiSelectKeyDown) {
            getEventBus().fireEvent(new TextAnnotationSelectionEmptiedEvent(getAnnotatedTextHandler()));
            getEventBus().fireEvent(new GroupSelectionEmptiedEvent(getAnnotatedTextHandler()));
        }

        if (selectedRelations.isEmpty()) {
            getEventBus().fireEvent(new RelationSelectionEmptiedEvent(getAnnotatedTextHandler()));
        } else {
            getEventBus().fireEvent(new RelationSelectionChangedEvent(getAnnotatedTextHandler(), selectedRelations));
        }
        event.stopPropagation();
    }

    @Override
    public CombinedAnnotationWidget addAnnotation(Annotation relation) {
        CombinedAnnotationWidget result = null;
        if (getAnnotatedDoc() != null) {

            ArrayList<String> annotationIds = new ArrayList<String>();
            ArrayList<String> roles = new ArrayList<String>();

            AnnotationTypeDefinition typedef = getAnnotatedDoc().getAnnotationSchema().getAnnotationTypeDefinition(relation.getAnnotationType());
            ArrayList<Rect> annotationClips = new ArrayList<Rect>();


            Collection<String> orderedRoles;
            if (typedef != null) {
                orderedRoles = typedef.getRelationDefinition().getSupportedRoles();
            } else {
                orderedRoles = relation.getRelation().getRoles();
            }

            //check that relation is well formed (has at least one member)
            boolean canBeDisplayed = !relation.getRelation().getRoles().isEmpty();

            //the annotation can be displayed if the other annotations it refers to are already (Of course, no cyclic ref allowed)
            for (String role : orderedRoles) {
                AnnotationReference aRef = relation.getRelation().getArgumentRef(role);
                //incomplete Relation have empty argument
                if (aRef == null || aRef.getAnnotationId() == null) {
                    canBeDisplayed = false;
                    break;
                }

                Annotation argument = getAnnotatedDoc().getAnnotation(aRef.getAnnotationId());
                if (argument == null) {
                    canBeDisplayed = false;
                    break;
                }
                String annotationId = argument.getId();
                annotationIds.add(annotationId);
                roles.add(role);
                Rect c = engine.getAnnotationClip(argument);
                if (c != null) {
                    annotationClips.add(c);
                } else {
                    canBeDisplayed = false;
                    break;
                }
            }

            if (canBeDisplayed) {
                RelationWidget relGroup = new RelationWidget(relation.getId(), typedef, annotationIds, annotationClips, roles);
                addWidget(relation.getId(), relGroup);
                result = relGroup;
                //add at the background
                getCanvas().insert(relGroup, 0);
            } else {
                //FIXME not I18N
                GWT.log("Relation " + relation.getId() + " can not be displayed!");
                //eventBus.fireEvent(new InformationReleasedEvent("Relation " + relation.getId() + " can not be displayed!"));
            }
        }
        return result;
    }

    @Override
    public Rect getAnnotationClip(Annotation annotation) {
        CombinedAnnotationWidget w = getWidget(annotation.getId());
        if (w != null) {
            return w.getCenterClip();
        } else {
            return null;
        }
    }

    @Override
    public boolean processAnnotation(SpecifiedAnnotation annotation, String color, boolean veiled) {
        if (!isRefreshOptional()) {
            CombinedAnnotationWidget widget = addAnnotation(annotation.getAnnotation());
            if (widget != null) {
                widget.createOverlay(engine.getOverlayContainer());
            }
            return (widget != null);
        } else {
            return true;
        }
    }

    @Override
    public void init(AnnotationDisplayerEngine engine) {
        if (!isRefreshOptional()) {
            this.engine = engine;
            clearCombinedWidget();
        }
    }

    @Override
    public void onVeiledStatusRefreshed(String annotationId, boolean veiled) {
    }

    @Override
    public void complete() {
        this.engine = engine;
        setRefreshOptional(false);

    }
}
