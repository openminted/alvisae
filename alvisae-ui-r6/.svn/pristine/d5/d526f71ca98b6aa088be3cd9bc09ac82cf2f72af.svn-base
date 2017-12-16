/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A sliding panel that deploys over another widget for a brief period of time, and hides back after a certain duration.
 * @author fpapazian
 */
public class SlidingCoverPanel extends PopupPanel {

    private HorizontalPanel panel;
    private final Element coveredElement;
    private final int elementBorderAndMarging;
    private int expandedHeight = 50;
    private PositionCallback postionCallback = new PositionCallback() {

        @Override
        public void setPosition(int offsetWidth, int offsetHeight) {
            setSize(coveredElement.getClientWidth() - elementBorderAndMarging, getExpandedHeight());
            setPopupPosition(coveredElement.getAbsoluteLeft() - coveredElement.getOffsetLeft(), coveredElement.getAbsoluteTop());
        }
    };
    private final Timer timer;

    public SlidingCoverPanel(Element coveredElement, int elementBorderAndMarging) {

        super(true);
        this.coveredElement = coveredElement;
        this.elementBorderAndMarging = elementBorderAndMarging;
        panel = new HorizontalPanel();
        setWidget(panel);
        timer = new Timer() {
            @Override
            public void run() {
                SlidingCoverPanel.this.setVisible(false);
            }
        };
    }

    private void setSize(int width, int height) {
        panel.setSize(String.valueOf(width) + "px", String.valueOf(height) + "px");
    }

    protected void cancelEffect() {
        timer.cancel();
    }

    public void slide() {
        this.setPopupPositionAndShow(postionCallback);
    }

    public void slide(int openDuration) {
        slide();
        timer.schedule(openDuration);
    }

    public HorizontalPanel getPanel() {
        return panel;
    }

    public int getExpandedHeight() {
        return expandedHeight;
    }

    public void setExpandedHeight(int expandedHeight) {
        this.expandedHeight = expandedHeight;
    }
}
