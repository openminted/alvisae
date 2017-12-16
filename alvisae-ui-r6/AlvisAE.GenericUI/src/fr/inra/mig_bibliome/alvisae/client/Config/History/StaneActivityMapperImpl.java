/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config.History;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import fr.inra.mig_bibliome.alvisae.client.Campaign.CampaignAssignmentActivity;
import fr.inra.mig_bibliome.alvisae.client.Campaign.CampaignAssignmentPlace;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Conso.AnnotationSetConsoActivity;
import fr.inra.mig_bibliome.alvisae.client.Conso.AnnotationSetConsoPlace;
import fr.inra.mig_bibliome.alvisae.client.Document.DocDisplayActivity;
import fr.inra.mig_bibliome.alvisae.client.Document.DocDisplayPlace;
import fr.inra.mig_bibliome.alvisae.client.Document.DocEditingActivity;
import fr.inra.mig_bibliome.alvisae.client.Document.DocEditingPlace;
import fr.inra.mig_bibliome.alvisae.client.SignIn.SignInActivity;
import fr.inra.mig_bibliome.alvisae.client.SignIn.SignInPlace;
import fr.inra.mig_bibliome.alvisae.client.Start.DefaultPlace;
import fr.inra.mig_bibliome.alvisae.client.Start.DefaultViewActivity;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskSelectingActivity;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskSelectingPlace;
import fr.inra.mig_bibliome.alvisae.client.User.UserManagingActivity;
import fr.inra.mig_bibliome.alvisae.client.User.UserManagingPlace;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.RequestManager;

/**
 * Provides the Activity corresponding to a specific Place, while checking that Authentication is performed whenever it is necessary
 * @author fpapazian
 */
public class StaneActivityMapperImpl implements StaneActivityMapper {

    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);
    private Activity currentActivity = null;
    private Place currentPlace = null;
    private Place intendedPlace = null;

    @Override
    public Activity getActivity(Place place) {
        Activity nextActivity = null;
        RequestManager requestMgr = injector.getCoreDataProvider().getRequestManager();
        boolean authenticationRequired = place instanceof AuthenticationRequiredPlace;
        boolean signedIn = requestMgr.isSignedIn();
        if (authenticationRequired && !signedIn) {
            //Jump to SignIn whenever the activity requires authentication while the user has not yet beeing authenticated

            if (place instanceof CloneablePlace) {
                //need to clone the intended place otherwise the PlaceController will not forward to it after signIn
                intendedPlace = ((CloneablePlace) place).clone();
            } else {
                intendedPlace = null;
            }
            boolean trySilentSignIn = !(place instanceof ExplicitAuthenticationRequiredPlace) && (requestMgr.canPerformReSignIn());
            nextActivity = new SignInActivity(new SignInPlace(trySilentSignIn));

        } else if (authenticationRequired && signedIn && ((AuthenticationRequiredPlace) place).needAdminRole() && !requestMgr.isCurrentUserAdmin()) {
            //prevent to access places requiring Admin role if current user does not posses this role
            intendedPlace = null;
            Window.alert("This operation requires admin role!");
            nextActivity = currentActivity;
            place = currentPlace;

        } else {
            intendedPlace = null;

            if (place instanceof SignInPlace) {
                nextActivity = new SignInActivity(new SignInPlace());
            } else {
                if (place instanceof DefaultPlace) {

                    nextActivity = new DefaultViewActivity((DefaultPlace) place);

                } else if (place instanceof DocEditingPlace) {
                    nextActivity = new DocEditingActivity((DocEditingPlace) place);
                } else if (place instanceof DocDisplayPlace) {
                    nextActivity = new DocDisplayActivity((DocDisplayPlace) place);
                } else if (place instanceof TaskSelectingPlace) {
                    nextActivity = new TaskSelectingActivity((TaskSelectingPlace) place);
                } else if (place instanceof AnnotationSetConsoPlace) {
                    nextActivity = new AnnotationSetConsoActivity((AnnotationSetConsoPlace) place);
                } else if (place instanceof CampaignAssignmentPlace) {
                    nextActivity = new CampaignAssignmentActivity((CampaignAssignmentPlace) place);
                } else if (place instanceof UserManagingPlace) {
                    nextActivity = new UserManagingActivity((UserManagingPlace) place);
                }
            }
        }

        if (nextActivity != null) {
            currentActivity = nextActivity;
        }

        currentPlace = place;
        return nextActivity;
    }

    /**
     *
     * @return the last Place that was intended to be reached, but was not
     * actually reached due to lack of authentication
     */
    @Override
    public Place getIntendedPlace() {
        return intendedPlace != null ? intendedPlace : new DefaultPlace("Start");
    }
}
