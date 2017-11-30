/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config.History;

/**
 * 
 * @author fpapazian
 */
public class BasicUserCampaignDocOffsetTaskParams extends BasicUserCampaignDocOffsetParams implements PlaceParams.UserCampaignDocOffsetTaskParams {

    public BasicUserCampaignDocOffsetTaskParams(Integer userId, Integer campaignId, Integer documentId, Integer offset, Integer taskId) {
        super(userId, campaignId, documentId, offset);
        this.taskId = taskId;
    }
    private Integer taskId = null;

    public BasicUserCampaignDocOffsetTaskParams(String token) {
        this(null, null, null, null, null);
        parseToken(token);
    }

    @Override
    public Integer getTaskId() {
        return taskId;
    }

    @Override
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Override
    public String createToken() {
        return super.createToken() + PlaceParams.ParamsProcessor.PARAM_SEP + "t" + PlaceParams.ParamsProcessor.KEYVAL_SEP + getTaskId();
    }

    public class BasicUserCampaignDocOffsetTaskParamsProcessor extends BasicUserCampaignDocOffsetParamsProcessor {

        @Override
        public void processParam(String key, String value) {
            super.processParam(key, value);
            if (key.equals("t") && taskId == null) {
                try {
                    taskId = Integer.valueOf(value);
                    setTaskId(taskId);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    @Override
    protected void parseToken(String token) {
        PlaceParams.parseToken(token, new BasicUserCampaignDocOffsetTaskParamsProcessor());
    }
}
