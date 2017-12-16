/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.User;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;

/**
 * Activity providing Document Annotation Editing feature
 * @author fpapazian
 */
public class UserManagingActivity extends AbstractActivity implements UserManagingView.Presenter {

    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);
    private final UserManagingParams params;

    public UserManagingActivity(UserManagingPlace place) {
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
                throw new UnsupportedOperationException("Unable to load UserManaging view.");
            }

            @Override
            public void onSuccess() {
                final UserManagingView userManagingView = injector.getUserManagingView();
                userManagingView.setParams(params);
                userManagingView.setPresenter(UserManagingActivity.this);
                containerWidget.setWidget(userManagingView.asWidget());
            }
        });
    }

    /**
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        return !injector.getUserManagingView().canCloseView() ? "Do you want to leave the User management and loose unsaved modifications?" : null;
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
