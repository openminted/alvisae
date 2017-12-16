/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Start;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import fr.inra.mig_bibliome.alvisae.client.Config.History.CloneablePlace;

/**
 * Default Place of the Application = lightweight Welcoming screen
 * @author fpapazian
 */
public class DefaultPlace extends Place implements CloneablePlace {

    public DefaultPlace(String token) {
    }

    public String getDefaultName() {
        return "start";
    }

    public DefaultPlace clone() {
        return tokenizer.getPlace(tokenizer.getToken(this));
    }

    public static final Tokenizer tokenizer = new Tokenizer();

    @Prefix("start")
    public static class Tokenizer implements PlaceTokenizer<DefaultPlace> {

        @Override
        public String getToken(DefaultPlace place) {
            return place.getDefaultName();
        }

        @Override
        public DefaultPlace getPlace(String token) {
            return new DefaultPlace(token);
        }
    }
}
