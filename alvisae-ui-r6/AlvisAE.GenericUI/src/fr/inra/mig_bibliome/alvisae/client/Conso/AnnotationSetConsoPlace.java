/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Conso;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicTaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.CloneablePlace;
import fr.inra.mig_bibliome.alvisae.client.Config.History.ExplicitAuthenticationRequiredPlace;
import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams.TaskReviewParams;

/**
 *
 * @author fpapazian
 */
public class AnnotationSetConsoPlace extends Place implements CloneablePlace, ExplicitAuthenticationRequiredPlace {

    private BasicTaskReviewParams params;

    public AnnotationSetConsoPlace(BasicTaskReviewParams params) {
        if (params==null) {
            params = new BasicTaskReviewParams("");
        }
        this.params = params;
    }

    public TaskReviewParams getParams() {
        return params;
    }

    @Override
    public AnnotationSetConsoPlace clone() {
        return tokenizer.getPlace(tokenizer.getToken(this));
    }
    
    public static final AnnotationSetConsoPlace.Tokenizer tokenizer = new AnnotationSetConsoPlace.Tokenizer();

    @Prefix("annSetReview")
    public static class Tokenizer implements PlaceTokenizer<AnnotationSetConsoPlace> {

        @Override
        public String getToken(AnnotationSetConsoPlace place) {
            return place.getParams().createToken();
        }

        @Override
        public AnnotationSetConsoPlace getPlace(String token) {
            return new AnnotationSetConsoPlace(new BasicTaskReviewParams(token));
        }
    }
    
    @Override
    public boolean needAdminRole() {
        return false;
    }
}
