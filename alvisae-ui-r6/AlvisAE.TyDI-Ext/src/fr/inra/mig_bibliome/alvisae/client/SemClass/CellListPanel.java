/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class CellListPanel<C> extends PopupPanel {

    public static interface Trigger extends HasClickHandlers {

        public void setEnabled(boolean enabled);

        public int getPopupLeft();

        public int getPopupTop();

        public String getPreferredWidth();

        public void onBeforeShowPopUp();

        public void onAfterHidePopUp();
    }
    
    public static abstract class TriggerHandler implements Trigger {

        @Override
        public String getPreferredWidth() {
            return null;
        }

        @Override
        public void onBeforeShowPopUp() {
        }
        
        @Override
        public void onAfterHidePopUp() {
        }
        
    }
    
    
    private final CellList<C> cellList;
    private final Trigger trigger;
    private int nbResult = 0;
    private final SingleSelectionModel<C> selectionModel;

    public CellListPanel(final Trigger trigger, Cell<C> cell, ValueUpdater<C> valueUpdater) {
        super(true, true);
        this.trigger = trigger;
        this.cellList = new CellList<C>(cell);
        selectionModel = new SingleSelectionModel<C>();
        cellList.setSelectionModel(selectionModel);
        cellList.setValueUpdater(valueUpdater);

        trigger.setEnabled(nbResult != 0);

        trigger.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                trigger.onBeforeShowPopUp();
                display();
            }
        });
        add(new ScrollPanel(cellList));
    }

    @Override
    public void hide(boolean autoClosed) {
        super.hide(autoClosed);
        trigger.onAfterHidePopUp();
    }

    private void display() {
        if (nbResult != 0) {
            if (nbResult > 10) {
                setHeight("12em");
            } else {
                setHeight("");
            }
            setPopupPosition(trigger.getPopupLeft(), trigger.getPopupTop());
            String preferredWidth = trigger.getPreferredWidth();
            if (preferredWidth!=null) {
                setWidth(preferredWidth);
            }
            show();
        }
    }

    public void reset() {
        hide();
        nbResult = 0;
        trigger.setEnabled(nbResult != 0);
        cellList.setRowCount(nbResult, true);
    }

    public void prepareForDisplay(List<C> rlist) {
        nbResult = rlist.size();
        trigger.setEnabled(nbResult != 0);
        cellList.setRowCount(nbResult, true);
        cellList.setRowData(0, rlist);
        display();
    }

    public SingleSelectionModel<C> getSelectionModel() {
        return selectionModel;
    }

}
