/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SignIn;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * User Authentication view
 * @author fpapazian
 */
public interface SignInView extends IsWidget {

    public void setSuccessfulLoginCommand(Command successFullLoginCommand);

    void setName(String docName);

    void setPresenter(Presenter presenter);

    public String getLoginName();

    public String getPassword();

    public void startView();
    
    public void stopView();

    public interface Presenter {

        void goTo(Place place);
    }
}
