/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Campaign;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.CloneablePlace;
import fr.inra.mig_bibliome.alvisae.client.Config.History.ExplicitAuthenticationRequiredPlace;

/**
 *
 * @author fpapazian
 */
public class CampaignAssignmentPlace extends Place implements CloneablePlace, ExplicitAuthenticationRequiredPlace {

    private BasicUserCampaignDocParams params;

    public CampaignAssignmentPlace(BasicUserCampaignDocParams params) {
        if (params==null) {
            params = new BasicUserCampaignDocParams("");
        }
        this.params = params;
    }

    public BasicUserCampaignDocParams getParams() {
        return params;
    }

    @Override
    public CampaignAssignmentPlace clone() {
        return tokenizer.getPlace(tokenizer.getToken(this));
    }
    public static final Tokenizer tokenizer = new Tokenizer();

    @Prefix("campaignAssgn")
    public static class Tokenizer implements PlaceTokenizer<CampaignAssignmentPlace> {

        @Override
        public String getToken(CampaignAssignmentPlace place) {
            return place.getParams().createToken();
        }

        @Override
        public CampaignAssignmentPlace getPlace(String token) {
            return new CampaignAssignmentPlace(new BasicUserCampaignDocParams(token));
        }
    }
    
    @Override
    public boolean needAdminRole() {
        return true;
    }
}
