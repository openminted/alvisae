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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDisplayerEngine.TextAnnotationDisplayer;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Path;

/**
 *
 * @author fpapazian
 */
public class OccurenceDisplayer implements TextAnnotationDisplayer {

    static class OccurenceMark {

        int position;
        String color;
        String annotationId;

        public OccurenceMark(int position, String color, String annotationId) {
            this.position = position;
            this.color = color;
            this.annotationId = annotationId;
        }
    }
    private static int VERTICALMARGIN = 2;
    private static int HORIZONTALMARGIN = 2;
    //
    private final DrawingArea occurenceCanvas;
    private final Widget container;
    private final ScrollPanel scrollPanel;
    private int docPanelSize = 0;
    private int offset;
    private int occurBarHeight;
    private int occurBarWidth;
    private ArrayList<OccurenceMark> pending = new ArrayList<OccurenceMark>();
    private int top;
    private int left;
    private boolean refreshOptional;

    public OccurenceDisplayer(Widget container, DrawingArea occurenceCanvas, ScrollPanel scrollPanel) {
        this.occurenceCanvas = occurenceCanvas;
        this.container = container;
        this.scrollPanel = scrollPanel;
    }

    private int getBarPosFromDocPos(int positionInDoc) {
        return offset + (positionInDoc * occurBarHeight) / docPanelSize;
    }

    public void addMarkAtPosition(final int position, String color, String annotationId) {
        pending.add(new OccurenceMark(position, color, annotationId));
    }

    private void displayPendingChanges() {
        Group marksGroup = new Group();
        int markWidth = HORIZONTALMARGIN + occurBarWidth;
        int markHalfWidth = HORIZONTALMARGIN + occurBarWidth / 2;
        for (final OccurenceMark occur : pending) {
            if (docPanelSize > 0) {
                int relPos = getBarPosFromDocPos(occur.position);
                Path mark = new Path(HORIZONTALMARGIN, relPos);
                mark.lineTo(markHalfWidth, relPos - 1);
                mark.lineTo(markWidth, relPos);
                mark.lineTo(markHalfWidth, relPos + 1);
                mark.close();
                mark.setStrokeWidth(1);
                mark.setStrokeColor(occur.color);
                mark.setStrokeOpacity(0.4);
                mark.setFillOpacity(0.7);
                mark.setStrokeOpacity(0.4);
                mark.setFillColor(occur.color);
                marksGroup.add(mark);
                if (scrollPanel != null) {
                    mark.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            scrollPanel.setVerticalScrollPosition(occur.position - (scrollPanel.getElement().getClientHeight() / 2));
                        }
                    });
                }
            }
        }
        occurenceCanvas.add(marksGroup);
        pending.clear();
    }

    private void reset(int docPanelSize) {
        pending.clear();
        if (docPanelSize == 0 || !refreshOptional) {
            this.docPanelSize = docPanelSize;
            occurenceCanvas.clear();
            occurenceCanvas.setWidth(container.getOffsetWidth());
            occurenceCanvas.setHeight(container.getOffsetHeight());
            this.offset = VERTICALMARGIN;
            this.occurBarHeight = occurenceCanvas.getHeight() - 2 * VERTICALMARGIN;
            this.occurBarWidth = occurenceCanvas.getWidth() - 2 * HORIZONTALMARGIN;
        }
    }

    @Override
    public void reset(AnnotationDisplayerEngine engine) {
        reset(0);
    }

    @Override
    public void init(AnnotationDisplayerEngine engine) {
        DrawingArea documentCanvas = engine.getDocumentCanvas();
        top = documentCanvas.getAbsoluteTop();
        left = documentCanvas.getAbsoluteLeft();

        reset(documentCanvas.getHeight());
    }

    @Override
    public void onVeiledStatusRefreshed(String annotationId, boolean veiled) {
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

    @Override
    public boolean processAnnotation(SpecifiedAnnotation annotation, String color, List<String> markerIds, boolean veiled) {
        if (!isRefreshOptional()) {
            int size = markerIds.size();
            if (size > 0) {
                String mId = markerIds.get(0);
                Element e = Document.get().getElementById(mId);
                if (e != null) {
                    int y = e.getAbsoluteTop() - top;
                    addMarkAtPosition(y, color, annotation.getAnnotation().getId());
                }
            }
        }
        return false;
    }

    @Override
    public void complete() {
        displayPendingChanges();
        setRefreshOptional(false);
    }
}
