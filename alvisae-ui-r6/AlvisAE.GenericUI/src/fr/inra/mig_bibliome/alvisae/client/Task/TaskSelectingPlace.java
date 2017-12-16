/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Task;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocOffsetTaskParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.CloneablePlace;
import fr.inra.mig_bibliome.alvisae.client.Config.History.ExplicitAuthenticationRequiredPlace;

/**
 *
 * @author fpapazian
 */
public class TaskSelectingPlace extends Place implements CloneablePlace, ExplicitAuthenticationRequiredPlace {

    private BasicUserCampaignDocOffsetTaskParams params;

    public TaskSelectingPlace(BasicUserCampaignDocOffsetTaskParams params) {
        if (params==null) {
            params = new BasicUserCampaignDocOffsetTaskParams("");
        }
        this.params = params;
    }

    public BasicUserCampaignDocOffsetTaskParams getParams() {
        return params;
    }

    @Override
    public TaskSelectingPlace clone() {
        return tokenizer.getPlace(tokenizer.getToken(this));
    }
    public static final TaskSelectingPlace.Tokenizer tokenizer = new TaskSelectingPlace.Tokenizer();

    @Prefix("taskSelect")
    public static class Tokenizer implements PlaceTokenizer<TaskSelectingPlace> {

        @Override
        public String getToken(TaskSelectingPlace place) {
            return place.getParams().createToken();
        }

        @Override
        public TaskSelectingPlace getPlace(String token) {
            return new TaskSelectingPlace(new BasicUserCampaignDocOffsetTaskParams(token));
        }
    }
    
    @Override
    public boolean needAdminRole() {
        return false;
    }
}
