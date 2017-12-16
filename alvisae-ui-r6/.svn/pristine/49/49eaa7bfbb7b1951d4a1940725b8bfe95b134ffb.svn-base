/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import fr.inra.mig_bibliome.alvisae.client.Events.RangeSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RangeSelectionChangedEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.data3.DOMRange;
import java.util.ArrayList;

/**
 *
 * @author fpapazian
 */
public class RangeListUi extends ResizeComposite implements RangeListView, RangeSelectionChangedEventHandler {

    interface RangeListUiBinder extends UiBinder<Widget, RangeListUi> {
    }
    private static RangeListUiBinder uiBinder = GWT.create(RangeListUiBinder.class);
    //
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    interface Styles extends CssResource {
    }
    //
    @UiField
    Grid rangeGrid;
    //
    @UiField
    Styles style;

    public RangeListUi() {
        initWidget(uiBinder.createAndBindUi(this));

        rangeGrid.resize(1, 4);
        rangeGrid.setText(0, 0, "Element Id");
        rangeGrid.setText(0, 1, "relative Start offset");
        rangeGrid.setText(0, 2, "relative End offset");
        rangeGrid.setText(0, 3, "Text");
    }

    private void displayRangeSelection(ArrayList<DOMRange> ranges) {
        if (ranges == null) {
            rangeGrid.resize(1, 4);
        } else {
            rangeGrid.resize(1 + ranges.size(), 4);
            int i = 0;
            for (DOMRange r : ranges) {
                i++;
                rangeGrid.setText(i, 0, r.getStartContainerId());
                rangeGrid.setText(i, 1, String.valueOf(r.getStartOffset()));
                rangeGrid.setText(i, 2, String.valueOf(r.getEndOffset()));
                rangeGrid.setText(i, 3, r.getText());
            }
        }
    }

    @Override
    public void onRangeSelectionChanged(RangeSelectionChangedEvent event) {
        displayRangeSelection(event.getRangeSelection());
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        RangeSelectionChangedEvent.register(injector.getMainEventBus(), this);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        RangeSelectionChangedEvent.unregister(this);
    }
}
