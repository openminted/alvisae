/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.TextAnnotationDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView.FlankingClips;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Path;

/**
 * Graphically renders TEXT annotation extra features, such as link between
 * fragments
 */
public class ExtraFeatureDisplayer implements TextAnnotationDisplayer {

    private final HashMap<String, Group> extraFeat = new HashMap<String, Group>();
    private final List<Group> pending = new ArrayList<Group>();
    private int top;
    private int left;
    private AnnotationDisplayerEngine engine;
    private boolean refreshOptional;

    public ExtraFeatureDisplayer() {
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
    //FIXME : annotation Marker can be composed of several box due to line wrapping. This methode does not handle correctly such case

    /**
     * Perform the rendering of extra info, such as Relations, which can not be
     * displayed by standard html+css
     */
    @Override
    public boolean processAnnotation(SpecifiedAnnotation ann, String color, List<String> MrkerIds, boolean veiled) {
        if (!isRefreshOptional()) {
            List<FlankingClips> fragmentClips = engine.getAnnotationFragmentClips(ann.getAnnotation());
            if (fragmentClips != null) {


                Group annotationFeatures;
                //Render links between fragments of discountinuous annotation

                annotationFeatures = new Group();
                //do not draw links between marker that are to close one from each other (for instance successive markers of the same fragment separated just by another annotation boundary)
                int size = fragmentClips.size();
                if (size > 1) {
                    Integer fromX = null;
                    Integer fromY = null;
                    //
                    Group previouslinkGroup = null;
                    Group currentlinkGroup = null;
                    int toX = 0;
                    int toY = 0;
                    for (int i = 0; i < fragmentClips.size(); i++) {

                        FlankingClips clip = fragmentClips.get(i);
                        {
                            //top left corner of marker box : x, y
                            int x = clip.first.left;
                            int y = clip.first.top;
                            boolean tooClose = Math.abs(x - toX) < 5 && Math.abs(y - toY) < 5;
                            //top right corner of marker box : : toX, toY
                            toX = clip.last.left + clip.last.width;
                            toY = clip.last.top;
                            int r = 4;
                            int xOff = 2;
                            int yOff = 1;
                            //outbound point :
                            int obX = toX + xOff + r;
                            int obY = toY + yOff + (r / 2);
                            if (i < size - 1) {
                                currentlinkGroup = new Group();
                                //outbound arrow head
                                Path path = new Path(toX + xOff, toY + 1);
                                path.lineTo(obX, obY);
                                path.lineTo(toX + xOff, toY + yOff + r);
                                path.close();
                                path.setStrokeColor("red");
                                path.setStrokeWidth(1);
                                currentlinkGroup.add(path);
                            }
                            //inbound point :
                            int ibX = x - xOff - r;
                            int ibY = y + yOff + (r / 2);
                            if (fromX != null) {
                                //inbound arrow head
                                Path path = new Path(x - xOff - r, y + yOff);
                                path.lineTo(x - xOff, y + yOff + (r / 2));
                                path.lineTo(x - xOff - r, y + 1 + r);
                                path.close();
                                path.setStrokeColor("green");
                                path.setStrokeWidth(1);
                                previouslinkGroup.add(path);
                                //link
                                final Path link = new Path(fromX, fromY);
                                int ctrlFromX = fromX + 5;
                                int ctrltoX;
                                int ctrlFromY = fromY - clip.first.height;
                                int ctrltoY;

                                if ((ibY - fromY) < 5) {
                                    ctrltoX = ibX + 5;
                                    ctrltoY = ibY - clip.first.height;
                                } else {
                                    ctrltoX = ibX - 5;
                                    ctrltoY = ibY + clip.first.height;
                                }
                                if (ibX < fromX) {
                                    ctrlFromY = fromY + 4 * clip.first.height;
                                    ctrltoY = ibY - 2 * clip.first.height;
                                }
                                link.curveTo(ctrlFromX, ctrlFromY, ctrltoX, ctrltoY, ibX, ibY);
                                link.setStrokeColor(color);
                                link.setStrokeWidth(1);
                                link.setStrokeOpacity(.7);
                                link.setFillColor(null);
                                link.addMouseOverHandler(new MouseOverHandler() {
                                    @Override
                                    public void onMouseOver(MouseOverEvent event) {
                                        link.setStrokeWidth(3);
                                    }
                                });
                                link.addMouseOutHandler(new MouseOutHandler() {
                                    @Override
                                    public void onMouseOut(MouseOutEvent event) {
                                        link.setStrokeWidth(1);
                                    }
                                });
                                previouslinkGroup.add(link);
                                if (!tooClose) {
                                    annotationFeatures.add(previouslinkGroup);
                                    previouslinkGroup = null;
                                }
                            }
                            fromX = obX;
                            fromY = obY;
                            previouslinkGroup = currentlinkGroup;
                        }
                    }
                    annotationFeatures.setVisible(!veiled);
                    pending.add(annotationFeatures);
                    String annotationId = ann.getAnnotation().getId();
                    extraFeat.put(annotationId, annotationFeatures);
                }
            }
        }
        return false;
    }

    @Override
    public void onVeiledStatusRefreshed(String annotationId, boolean veiled) {
        Group annotationFeatures = extraFeat.get(annotationId);
        if (annotationFeatures != null) {
            annotationFeatures.setVisible(!veiled);
        }
    }

    @Override
    public void reset(AnnotationDisplayerEngine engine) {
        extraFeat.clear();
    }

    @Override
    public void init(AnnotationDisplayerEngine engine) {
        if (!isRefreshOptional()) {
            this.engine = engine;
            top = engine.getDocumentCanvas().getAbsoluteTop();
            left = engine.getDocumentCanvas().getAbsoluteLeft();
        }
    }

    @Override
    public void complete() {
        for (Group f : pending) {
            engine.getDocumentCanvas().add(f);
        }
        engine = null;
        pending.clear();
        setRefreshOptional(false);
    }
}
