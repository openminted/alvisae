/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config.History;

import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams.ParamsProcessor;

/**
 * Set of basic parameters for Places, and the parsing mechanism when the are used in PlaceTokenizer
 * @author fpapazian
 */
public class BasicUserCampaignDocParams implements PlaceParams.UserCampaignDocParams {

    public static BasicUserCampaignDocParams createFromToken(String token) {
        return new BasicUserCampaignDocParams(token);
    }
    private Integer userId = null;
    private Integer campaignId = null;
    private Integer documentId = null;

    public BasicUserCampaignDocParams(Integer userId, Integer campaignId, Integer documentId) {
        this.userId = userId;
        this.campaignId = campaignId;
        this.documentId = documentId;
    }

    public BasicUserCampaignDocParams(String token) {
        parseToken(token);
    }

    @Override
    public Integer getCampaignId() {
        return campaignId;
    }

    @Override
    public Integer getDocumentId() {
        return documentId;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    @Override
    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    @Override
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String createToken() {
        return "u" + ParamsProcessor.KEYVAL_SEP + getUserId() + ParamsProcessor.PARAM_SEP + "c" + ParamsProcessor.KEYVAL_SEP + getCampaignId() + ParamsProcessor.PARAM_SEP + "d" + ParamsProcessor.KEYVAL_SEP + getDocumentId();
    }

    public class BasicUserCampaignDocParamsProcessor implements ParamsProcessor {

        @Override
        public void processParam(String key, String value) {
            if (key.equals("u") && userId == null) {
                try {
                    userId = Integer.valueOf(value);
                    setUserId(userId);
                } catch (NumberFormatException e) {
                }
            }
            if (key.equals("c") && campaignId == null) {
                try {
                    campaignId = Integer.valueOf(value);
                    setCampaignId(campaignId);
                } catch (NumberFormatException e) {
                }
            }
            if (key.equals("d") && documentId == null) {
                try {
                    documentId = Integer.valueOf(value);
                    setDocumentId(documentId);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    protected void parseToken(String token) {
        PlaceParams.parseToken(token, new BasicUserCampaignDocParamsProcessor());
    }
}
