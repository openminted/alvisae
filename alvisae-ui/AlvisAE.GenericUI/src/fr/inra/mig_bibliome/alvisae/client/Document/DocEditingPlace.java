/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import fr.inra.mig_bibliome.alvisae.client.Config.History.AuthenticationRequiredPlace;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocOffsetTaskParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.CloneablePlace;

/**
 * Place Corresponding to a specific document Annotation activity
 * @author fpapazian
 */
public class DocEditingPlace extends Place implements CloneablePlace, AuthenticationRequiredPlace {

    private BasicUserCampaignDocOffsetTaskParams params;

    public DocEditingPlace(BasicUserCampaignDocOffsetTaskParams params) {
        this.params = params;
    }

    public BasicUserCampaignDocOffsetTaskParams getParams() {
        return params;
    }

    public boolean isComplete() {
        return params.getUserId() != null && params.getCampaignId() != null && params.getDocumentId() != null && params.getTaskId() != null;
    }

    @Override
    public DocEditingPlace clone() {
        return tokenizer.getPlace(tokenizer.getToken(this));
    }
    public static final Tokenizer tokenizer = new Tokenizer();

    @Prefix("docView")
    public static class Tokenizer implements PlaceTokenizer<DocEditingPlace> {

        @Override
        public String getToken(DocEditingPlace place) {
            return place.getParams().createToken();
        }

        @Override
        public DocEditingPlace getPlace(String token) {
            return new DocEditingPlace(new BasicUserCampaignDocOffsetTaskParams(token));
        }
    }

    @Override
    public boolean needAdminRole() {
        return false;
    }
}
