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
import fr.inra.mig_bibliome.alvisae.client.Config.History.CloneablePlace;
import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams.ParamsProcessor;

/**
 * Place Corresponding to the activity displaying a specific
 * campaign/document/task specified by external ids
 *
 * @author fpapazian
 */
public class DocDisplayPlace extends Place implements CloneablePlace, AuthenticationRequiredPlace {

    public static final String PlacePrefix = "docDisp";

    public static class BasicDocExtIdParams {

        public static final String DOCEXTERNALID_PARAMNAME = "DocumentID";
        private static final String TASKNAME_PARAMNAME = "TaskName";
        private static final String CAMPAIGNID_PARAMNAME = "CampaignID";

        public static BasicDocExtIdParams createFromToken(String token) {
            return new BasicDocExtIdParams(token);
        }
        private String docExternalId;
        private String taskName;
        private Integer campaignId;

        public BasicDocExtIdParams(String docExternalId, String taskName, Integer campaignId) {
            this.docExternalId = docExternalId;
            this.taskName = taskName;
            this.campaignId = campaignId;
        }

        public BasicDocExtIdParams(String token) {
            this(null, null, null);
            parseToken(token);
        }

        public String getDocExternalId() {
            return docExternalId;
        }

        public void setDocExternalId(String docExternalId) {
            this.docExternalId = docExternalId;
        }

        public Integer getCampaignId() {
            return campaignId;
        }

        public void setCampaignId(Integer CampaignId) {
            this.campaignId = CampaignId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String createToken() {
            return DOCEXTERNALID_PARAMNAME + ParamsProcessor.KEYVAL_SEP + getDocExternalId()
                    + ParamsProcessor.PARAM_SEP + CAMPAIGNID_PARAMNAME + ParamsProcessor.KEYVAL_SEP + getCampaignId()
                    + ParamsProcessor.PARAM_SEP + TASKNAME_PARAMNAME + ParamsProcessor.KEYVAL_SEP + getTaskName();
        }

        public class BasicDocExtIdParamsProcessor implements ParamsProcessor {

            @Override
            public void processParam(String key, String value) {
                if (key.toLowerCase().equals(DOCEXTERNALID_PARAMNAME.toLowerCase()) && getDocExternalId() == null) {
                    setDocExternalId(value);

                } else if (key.toLowerCase().equals(CAMPAIGNID_PARAMNAME.toLowerCase()) && getCampaignId() == null) {
                    try {
                        Integer cId = Integer.valueOf(value);
                        setCampaignId(cId);
                    } catch (NumberFormatException e) {
                    }

                } else if (key.toLowerCase().equals(TASKNAME_PARAMNAME.toLowerCase()) && getTaskName() == null) {
                    setTaskName(value);

                }
            }
        }

        protected void parseToken(String token) {
            PlaceParams.parseToken(token, new BasicDocExtIdParamsProcessor());
        }
    }
    private BasicDocExtIdParams params;

    public DocDisplayPlace(BasicDocExtIdParams params) {
        this.params = params;
    }

    public BasicDocExtIdParams getParams() {
        return params;
    }

    @Override
    public DocDisplayPlace clone() {
        return tokenizer.getPlace(tokenizer.getToken(this));
    }
    public static final Tokenizer tokenizer = new Tokenizer();

    @Prefix(PlacePrefix)
    public static class Tokenizer implements PlaceTokenizer<DocDisplayPlace> {

        @Override
        public String getToken(DocDisplayPlace place) {
            return place.getParams().createToken();
        }

        @Override
        public DocDisplayPlace getPlace(String token) {
            return new DocDisplayPlace(new BasicDocExtIdParams(token));
        }
    }

    @Override
    public boolean needAdminRole() {
        return false;
    }
}
