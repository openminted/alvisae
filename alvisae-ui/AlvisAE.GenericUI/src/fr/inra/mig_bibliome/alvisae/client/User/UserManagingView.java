/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.User;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

/**
 *
 * @author fpapazian
 */
public interface UserManagingView extends IsWidget {

    UserManagingParams getParams();

    void setParams(UserManagingParams params);

    void setPresenter(Presenter presenter);
    
    public boolean canCloseView();
    
    public interface Presenter {

        void goTo(Place place);
    }
}
