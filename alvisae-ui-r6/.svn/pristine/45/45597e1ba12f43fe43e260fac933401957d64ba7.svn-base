/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class CellListSelectPopupPanel<C> extends PopupPanel {

    private final CellList<C> cellList;
    private final SingleSelectionModel<C> selectionModel;
    private ValueUpdater<C> valueUpdater = null;

    public CellListSelectPopupPanel(Cell<C> cell, List<C> values) {
        super(true, true);
        this.cellList = new CellList<C>(cell);

        selectionModel = new SingleSelectionModel<C>();
        cellList.setSelectionModel(selectionModel);
        
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (CellListSelectPopupPanel.this.valueUpdater != null) {
                    C selected = selectionModel.getSelectedObject();
                    if (selected != null) {
                        CellListSelectPopupPanel.this.valueUpdater.update(selected);
                    }
                }
                hide();
            }
        });

        //disable pagination
        cellList.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {

            @Override
            public void onRowCountChange(RowCountChangeEvent event) {
                 cellList.setVisibleRange(new Range(0, event.getNewRowCount()));
            }
        });
                
        
        add(new ScrollPanel(cellList));

        cellList.setRowCount(values.size(), true);
        cellList.setRowData(0, values);
    }

    public void display(int left, int top, String preferredWidth, ValueUpdater<C> valueUpdater) {
        this.valueUpdater = valueUpdater;
        if (cellList.getRowCount() != 0) {

            C selected = selectionModel.getSelectedObject();
            if (selected != null) {
                selectionModel.setSelected(selected, false);
            }

            if (cellList.getRowCount() > 10) {
                setHeight("12em");
            } else {
                setHeight("");
            }
            setPopupPosition(left, top);
            if (preferredWidth != null) {
                setWidth(preferredWidth);
            }
            show();
        }
    }
}
