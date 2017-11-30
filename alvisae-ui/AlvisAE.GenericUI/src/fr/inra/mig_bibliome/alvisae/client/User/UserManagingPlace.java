/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.User;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import fr.inra.mig_bibliome.alvisae.client.Config.History.AuthenticationRequiredPlace;
import fr.inra.mig_bibliome.alvisae.client.Config.History.CloneablePlace;

/**
 * Place Corresponding to a specific document Annotation activity
 * @author fpapazian
 */
public class UserManagingPlace extends Place implements CloneablePlace, AuthenticationRequiredPlace {

    private UserManagingParams params;

    public UserManagingPlace(UserManagingParams params) {
        this.params = params;
    }

    public UserManagingParams getParams() {
        return params;
    }

    @Override
    public UserManagingPlace clone() {
        return tokenizer.getPlace(tokenizer.getToken(this));
    }
    public static final Tokenizer tokenizer = new Tokenizer();

    @Prefix("userManage")
    public static class Tokenizer implements PlaceTokenizer<UserManagingPlace> {

        @Override
        public String getToken(UserManagingPlace place) {
            return place.getParams().createToken();
        }

        @Override
        public UserManagingPlace getPlace(String token) {
            return new UserManagingPlace(new UserManagingParams(token));
        }
    }
    
    @Override
    public boolean needAdminRole() {
        return true;
    }
    
}
