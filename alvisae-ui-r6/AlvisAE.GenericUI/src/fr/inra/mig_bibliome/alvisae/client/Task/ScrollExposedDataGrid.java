/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Task;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 *
 * @author fpapazian
 */
public class ScrollExposedDataGrid<T> extends DataGrid<T> {

    public ScrollPanel getScrollPanel() {
        HeaderPanel header = (HeaderPanel) getWidget();
        return (ScrollPanel) header.getContentWidget();
    }
}