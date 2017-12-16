/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import fr.inra.mig_bibliome.alvisae.client.StaneCoreResources;

/**
 *
 * @author fpapazian
 */
public class ToolBarExpandCollapseHandler implements ClickHandler {
    boolean expanded = true;
    private final Image triggerImg;
    private final LayoutPanel layoutPanel;
    private final Widget toolbar;
    private final Widget panel;
    private final int toolbarTop;
    private final int toolbarHeight;

    public ToolBarExpandCollapseHandler(Image triggerImg, LayoutPanel layoutPanel, Widget toolbar, int toolbarTop, int toolbarHeight, Widget panel) {
        this.triggerImg = triggerImg;
        this.layoutPanel = layoutPanel;
        this.toolbar = toolbar;
        //FIXME compute toolbar top and height at runtime
        this.toolbarTop = toolbarTop;
        this.toolbarHeight = toolbarHeight;
        this.panel = panel;
    }

    @Override
    public void onClick(ClickEvent event) {
        setExpanded(!expanded);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        if (expanded) {
            layoutPanel.setWidgetTopHeight(toolbar, toolbarTop, Unit.PX, toolbarHeight, Unit.PX);
            layoutPanel.setWidgetTopBottom(panel, toolbarTop+toolbarHeight, Unit.PX, 0, Unit.PX);
            triggerImg.setResource(StaneCoreResources.INSTANCE.MoveUpIcon());
        } else {
            layoutPanel.setWidgetTopHeight(toolbar, toolbarTop, Unit.PX, 0, Unit.PX);
            layoutPanel.setWidgetTopBottom(panel, toolbarTop, Unit.PX, 0, Unit.PX);
            triggerImg.setResource(StaneCoreResources.INSTANCE.MoveDownIcon());
        }
    }
    
}
