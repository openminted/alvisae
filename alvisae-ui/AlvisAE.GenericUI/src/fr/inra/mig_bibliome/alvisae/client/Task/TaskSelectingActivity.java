/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Task;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocOffsetTaskParams;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;

/**
 * Activity providing Task selection feature
 * @author fpapazian
 */
public class TaskSelectingActivity extends AbstractActivity implements TaskSelectingView.Presenter {

    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);
    private final BasicUserCampaignDocOffsetTaskParams params;

    public TaskSelectingActivity(TaskSelectingPlace place) {
        this.params = place.getParams();
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {
        //TODO since the Asynch loading can take some time, a wait dialog should appear on screen to make user more comfortable

        //Async loading to enable fast startup of the default activity
        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onFailure(Throwable reason) {
                Window.alert("Unable to load TaskSelecting view.");
                throw new UnsupportedOperationException("Unable to load TaskSelecting view.");
            }

            @Override
            public void onSuccess() {
                final TaskSelectingView taskSelectingView = injector.getTaskSelectingView();
                taskSelectingView.setParams(params);
                taskSelectingView.setPresenter(TaskSelectingActivity.this);
                containerWidget.setWidget(taskSelectingView.asWidget());

                //FIXME Retrieve the task list for the campaign specified in the place name
            }
        });
    }

    /**
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        //it is always allowed to quit 
        return null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Navigate to a new Place in the browser
     */
    @Override
    public void goTo(Place place) {
        injector.getPlaceController().goTo(place);
    }
}
