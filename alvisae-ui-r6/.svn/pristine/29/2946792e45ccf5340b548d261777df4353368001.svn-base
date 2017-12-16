/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SignIn;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * Place for User Authentication screen
 * @author fpapazian
 */
public class SignInPlace extends Place {

    private final boolean silentSignIn;

    public SignInPlace(boolean silentSignIn) {
        this.silentSignIn = silentSignIn;
    }

    public SignInPlace() {
        this(false);
    }

    public static String getName(boolean silentSignIn) {
        return silentSignIn ? "silentSignIn" : "signIn";
    }

    boolean isSilentSignIn() {
        return silentSignIn;
    }

    @Prefix("signIn")
    public static class Tokenizer implements PlaceTokenizer<SignInPlace> {

        @Override
        public String getToken(SignInPlace place) {
            return getName(place.isSilentSignIn());
        }

        @Override
        public SignInPlace getPlace(String token) {
            return new SignInPlace(token.equals(getName(true)));
        }
    }
}
