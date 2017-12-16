/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import fr.inra.mig_bibliome.alvisae.client.Events.TargetSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TargetSelectionChangedEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class TargetListUi extends ResizeComposite implements TargetListView, TargetSelectionChangedEventHandler {

    interface TargetListUiBinder extends UiBinder<Widget, TargetListUi> {
    }
    private static TargetListUiBinder uiBinder = GWT.create(TargetListUiBinder.class);
    //
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    interface Styles extends CssResource {
    }
    //
    @UiField
    Grid targetGrid;
    //
    @UiField
    Styles style;

    public TargetListUi() {
        initWidget(uiBinder.createAndBindUi(this));

        targetGrid.resize(1, 2);
        targetGrid.setText(0, 0, "Start offset");
        targetGrid.setText(0, 1, "End offset");
    }

    private void displayTargetSelection(List<Fragment> targets) {
        if (targets == null) {
            targetGrid.resize(1, 2);
        } else {
            targetGrid.resize(1 + targets.size(), 2);
            int i = 0;
            for (Fragment t : targets) {
                i++;
                targetGrid.setText(i, 0, String.valueOf(t.getStart()));
                targetGrid.setText(i, 1, String.valueOf(t.getEnd()));
            }
        }
    }

    public void onTargetSelectionChanged(TargetSelectionChangedEvent event) {
        displayTargetSelection(event.getTargetSelection());
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        TargetSelectionChangedEvent.register(injector.getMainEventBus(), this);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        TargetSelectionChangedEvent.unregister(this);
    }
}
