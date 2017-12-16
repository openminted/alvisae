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
 * Extended Set of parameters for Places
 * @author fpapazian
 */
public class BasicUserCampaignDocOffsetParams extends BasicUserCampaignDocParams implements PlaceParams.UserCampaignDocOffsetParams {

    public BasicUserCampaignDocOffsetParams(Integer userId, Integer campaignId, Integer documentId, Integer offset) {
        super(userId, campaignId, documentId);
        this.offset = offset;
    }
    private Integer offset = null;

    public BasicUserCampaignDocOffsetParams(String token) {
        this(null, null, null, null);
        parseToken(token);
    }

    @Override
    public Integer getOffset() {
        return offset;
    }

    @Override
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String createToken() {
        return super.createToken() + ParamsProcessor.PARAM_SEP + "o" + ParamsProcessor.KEYVAL_SEP + getOffset();
    }

    public class BasicUserCampaignDocOffsetParamsProcessor extends BasicUserCampaignDocParamsProcessor {

        @Override
        public void processParam(String key, String value) {
            super.processParam(key, value);
            if (key.equals("o") && offset == null) {
                try {
                    offset = Integer.valueOf(value);
                    setOffset(offset);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    @Override
    protected void parseToken(String token) {
        PlaceParams.parseToken(token, new BasicUserCampaignDocOffsetParamsProcessor());
    }
}
