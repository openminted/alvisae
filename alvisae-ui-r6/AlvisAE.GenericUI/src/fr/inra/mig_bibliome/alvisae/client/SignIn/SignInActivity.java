/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SignIn;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Start.DefaultPlace;
import fr.inra.mig_bibliome.alvisae.client.data.ResultMessageDialog;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.AsyncResponseHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.AuthenticationInfo;

/**
 * Activity in charge of User Authentication
 * @author fpapazian
 */
public class SignInActivity extends AbstractActivity implements SignInView.Presenter {

    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);
    private boolean silentSignIn;

    public SignInActivity(SignInPlace place) {
        this.silentSignIn = place.isSilentSignIn();
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(final AcceptsOneWidget containerWidget, final EventBus eventBus) {
        if (silentSignIn) {
            injector.getCoreDataProvider().getRequestManager().reSignIn(
                    new AsyncResponseHandler<AuthenticationInfo>() {

                        @Override
                        public void onSuccess(AuthenticationInfo result) {
                            injector.getLoginView();
                            
                            Place nextPlace = injector.getAppActivityMapper().getIntendedPlace();
                            SignInActivity.this.goTo(nextPlace);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            startVisible(containerWidget, eventBus);
                        }
                    });

        } else {
            startVisible(containerWidget, eventBus);
        }
    }

    public void startVisible(AcceptsOneWidget containerWidget, EventBus eventBus) {
        final SignInView signInView = injector.getLoginView();

        signInView.setSuccessfulLoginCommand(new Command() {

            @Override
            public void execute() {

                injector.getCoreDataProvider().getRequestManager().signIn(signInView.getLoginName(), signInView.getPassword(), new AsyncResponseHandler<AuthenticationInfo>() {

                    @Override
                    public void onSuccess(AuthenticationInfo result) {
                        Place nextPlace = injector.getAppActivityMapper().getIntendedPlace();
                        SignInActivity.this.goTo(nextPlace);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ResultMessageDialog d = new ResultMessageDialog(ResultMessageDialog.Error, "Unable to sign-in", caught.getMessage());
                        d.show();
                        SignInActivity.this.goTo(new DefaultPlace("start"));
                    }
                });
            }
        });

        signInView.setName("");
        signInView.setPresenter(this);
        containerWidget.setWidget(signInView.asWidget());
        signInView.startView();
    }

    /**
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        injector.getLoginView().stopView();
        return null;
    }

    /**
     * Navigate to a new Place in the browser
     */
    @Override
    public void goTo(Place place) {
        injector.getPlaceController().goTo(place);
    }
}
