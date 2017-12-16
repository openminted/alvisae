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
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.GroupAnnotationDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView.Rect;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.AnnotationSelections;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.GroupAnnotationSelection;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextProcessor;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import java.util.ArrayList;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * Graphically renders Group annotations
 *
 * @author fpapazian
 */
public class GroupDisplayer extends CombinedAnnotationDisplayer implements GroupAnnotationDisplayer, AnnotationMainDisplayer, AnnotationOverlayProducer {

    private static final String PROXYANNGRPKIND_ATTRNAME = "aae_prxygrp";

    public class GroupWidget extends CombinedAnnotationWidget {

        private static final int coreRadius = 4;
        //
        private final Circle centerWidget;
        private final ArrayList<Line> backlines;
        private final ArrayList<Line> frontlines;
        private final ArrayList<Rectangle> shadows;
        private final ArrayList<Path> hints;
        private final Rect centerClip;
        private final String description;

        private GroupWidget(String groupId, AnnotationTypeDefinition typedef, ArrayList<String> annotationIds, ArrayList<Rect> annotationClips) {
            super(groupId, typedef, annotationIds, annotationClips);

            shadows = new ArrayList<Rectangle>();
            hints = new ArrayList<Path>();

            int index = 0;
            //create "shadow" of the referenced Annotation
            for (Rect clip : getAnnotationClips()) {
                if (getMapper().getAnnotation(annotationIds.get(index)).getAnnotationKind().equals(AnnotationKind.TEXT)) {
                    Rectangle sShadow = new Rectangle(clip.left, clip.top, clip.width + 2, clip.height + 2);
                    sShadow.setStrokeWidth(1);
                    sShadow.setFillOpacity(0);
                    sShadow.setStrokeColor("black");
                    shadows.add(sShadow);
                    this.add(sShadow);

                    Path groupHint = new Path(clip.left + clip.width - 2 - 2 * coreRadius, clip.top + clip.height + 2);
                    groupHint.arc(coreRadius, coreRadius, 0, true, false, clip.left + clip.width - 2, clip.top + clip.height + 2);
                    groupHint.close();
                    groupHint.setStrokeWidth(1);
                    groupHint.setStrokeColor("grey");
                    groupHint.setFillOpacity(.5);
                    groupHint.setFillColor("silver");
                    groupHint.setVisible(false);
                    hints.add(groupHint);
                    this.add(groupHint);

                }
                index++;
            }


            Point isoBar = getIsoBarycentre(getCenters());
            //apply a vertical shift if the center is probable on the same line as the referenced annotation (avoid to collide with the text)
            isoBar.x -= 12;
            isoBar.y += 12;

            backlines = new ArrayList<Line>();
            int annIndex = 0;
            for (Point center : getCenters()) {
                Line backline = new Line(center.x, center.y, isoBar.x, isoBar.y);
                backline.setStrokeOpacity(0.5);
                backlines.add(backline);
                String annotationId = annotationIds.get(annIndex);
//                    String role = roles.get(annIndex);
                //                  backline.setTitle(role + "(" + annotationId + ")");
                this.add(backline);
                annIndex++;
            }

            frontlines = new ArrayList<Line>();
            annIndex = 0;
            for (Point center : getCenters()) {
                Line frontline = new Line(center.x, center.y, isoBar.x, isoBar.y);
                frontline.setStrokeOpacity(0.5);
                frontlines.add(frontline);
                String annotationId = annotationIds.get(annIndex);
//                    String role = roles.get(annIndex);
//                    frontline.setTitle(role + "(" + annotationId + ")");
                this.add(frontline);
                annIndex++;
            }

            //draw center
            centerWidget = new Circle(isoBar.x, isoBar.y, coreRadius);
            centerWidget.setStrokeOpacity(0.6);
            centerWidget.setFillOpacity(0.4);

            if (typedef != null) {
                description = "Group '" + typedef.getType() + "' (" + AnnotatedTextProcessor.getBriefId(groupId) + ")";
            } else {
                description = "Group 'unknown-type' (" + AnnotatedTextProcessor.getBriefId(groupId) + ")";
            }
            centerWidget.setTitle(groupId);

            centerClip = new Rect(isoBar.x - coreRadius, isoBar.y - coreRadius, coreRadius * 2, coreRadius * 2);

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
                    onGroupClick(event, getAnnotationId());
                }
            });

        }

        @Override
        public boolean isAnnotationSelected() {
            return selectedGroups.isAnnotationSelected(getAnnotationId());
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
            for (Line fronLine : frontlines) {
                fronLine.setVisible(!veiled);
            }
            for (Line backline : backlines) {
                backline.setVisible(!veiled);
            }
            if (!veiled) {
                if (hovered) {
                    if (selected) {
                        centerWidget.setStrokeColor(GlobalStyles.HoveredSelectedRelation.getColor());
                        centerWidget.setStrokeWidth(2);
                        centerWidget.setFillColor(getColor());

                        for (Line backline : backlines) {
                            backline.setStrokeColor(GlobalStyles.HoveredSelectedRelation.getColor());
                            backline.setStrokeWidth(5);
                        }
                    } else {
                        centerWidget.setStrokeColor(GlobalStyles.HoveredRelation.getColor());
                        centerWidget.setStrokeWidth(2);
                        centerWidget.setFillColor(getColor());
                        for (Line backline : backlines) {
                            backline.setStrokeColor(GlobalStyles.HoveredRelation.getColor());
                            backline.setStrokeWidth(5);
                        }
                    }
                } else {
                    if (selected) {
                        centerWidget.setStrokeColor(GlobalStyles.SelectedRelation.getColor());
                        centerWidget.setStrokeWidth(2);
                        centerWidget.setFillColor(getColor());

                        for (Line fronLine : frontlines) {

                            fronLine.setStrokeColor("silver");
                            fronLine.setStrokeWidth(1);
                        }

                        for (Line backline : backlines) {
                            backline.setStrokeColor(GlobalStyles.SelectedRelation.getColor());
                            backline.setStrokeWidth(3);
                        }
                    } else {
                        centerWidget.setStrokeColor("darkgrey");
                        centerWidget.setStrokeWidth(1);
                        centerWidget.setFillColor(getColor());


                        for (Line fronLine : frontlines) {
                            fronLine.setStrokeColor("#B8860B");
                            fronLine.setStrokeWidth(1);
                        }

                        for (Line backline : backlines) {
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
            overlayElt.setAttribute(PROXYANNGRPKIND_ATTRNAME, "true");
            return overlayElt;
        }
    }
    //

    @Override
    public boolean isManagedOverlay(Element overlayElt) {
        return !overlayElt.getAttribute(PROXYANNGRPKIND_ATTRNAME).isEmpty();
    }
    //    
    private final AnnotationSelections selectedGroups;
    private AnnotationDisplayerEngine engine;

    public GroupDisplayer(DrawingArea canvas, final EventBus eventBus, AnnotationSelections selectedGroups) {
        super(canvas, eventBus);
        this.selectedGroups = selectedGroups;
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
        getEventBus().fireEvent(new GroupSelectionEmptiedEvent(getAnnotatedTextHandler()));
    }

    public void onGroupClick(ClickEvent event, String annotationId) {
        //[Ctrl]/[Command] key used for multiselect
        boolean multiSelectKeyDown = (!ShortCutToActionTypeMapper.isMacOs() && event.isControlKeyDown()) || (ShortCutToActionTypeMapper.isMacOs() && event.isMetaKeyDown());
        if (!multiSelectKeyDown) {
            selectedGroups.clear();
        }

        boolean selected = selectedGroups.isAnnotationSelected(annotationId);
        if (selected && multiSelectKeyDown) {
            //when multiselection is explicitely requested and the clicked Group was previously selected, 
            //it must be removed from selection
            selectedGroups.removeAnnotationFromSelection(annotationId);
            selected = false;
        } else {
            selectedGroups.getSelections().add(new GroupAnnotationSelection(getAnnotatedDoc().getAnnotation(annotationId)));
            selected = true;
        }

        //#2452 remove TextAnnotation or Relation selection if no explicit multiselection requested 
        if (!multiSelectKeyDown) {
            getEventBus().fireEvent(new TextAnnotationSelectionEmptiedEvent(getAnnotatedTextHandler()));
            getEventBus().fireEvent(new RelationSelectionEmptiedEvent(getAnnotatedTextHandler()));
        }

        if (selectedGroups.isEmpty()) {
            getEventBus().fireEvent(new GroupSelectionEmptiedEvent(getAnnotatedTextHandler()));
        } else {
            getEventBus().fireEvent(new GroupSelectionChangedEvent(getAnnotatedTextHandler(), selectedGroups));
        }
        event.stopPropagation();
    }

    @Override
    public CombinedAnnotationWidget addAnnotation(Annotation group) {
        CombinedAnnotationWidget result = null;
        if (getAnnotatedDoc() != null) {

            ArrayList<String> annotationIds = new ArrayList<String>();

            AnnotationTypeDefinition typedef = getAnnotatedDoc().getAnnotationSchema().getAnnotationTypeDefinition(group.getAnnotationType());
            ArrayList<Rect> annotationClips = new ArrayList<Rect>();

            boolean canBeDisplayed = !group.getAnnotationGroup().getComponentRefs().isEmpty();
            for (AnnotationReference aRef : group.getAnnotationGroup().getComponentRefs()) {
                Annotation component = getAnnotatedDoc().getAnnotation(aRef.getAnnotationId());
                if (component == null) {
                    canBeDisplayed = false;
                    break;
                }
                String annotationId = component.getId();
                annotationIds.add(annotationId);
                Rect c = engine.getAnnotationClip(component);
                if (c != null) {
                    annotationClips.add(c);
                } else {
                    canBeDisplayed = false;
                    break;
                }
            }

            if (canBeDisplayed) {
                GroupWidget relGroup = new GroupWidget(group.getId(), typedef, annotationIds, annotationClips);
                addWidget(group.getId(), relGroup);
                result = relGroup;
                //add at the background
                getCanvas().insert(relGroup, 0);
            } else {
                //FIXME not I18N
                GWT.log("Group " + group.getId() + " can not be displayed!");
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
        engine = null;
        setRefreshOptional(false);
    }
}
