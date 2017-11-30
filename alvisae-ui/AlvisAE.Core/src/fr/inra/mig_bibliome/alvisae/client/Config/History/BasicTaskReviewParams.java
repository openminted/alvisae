/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config.History;

import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams.TaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.data.CoreDataProvider;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class BasicTaskReviewParams extends BasicUserCampaignDocOffsetTaskParams implements TaskReviewParams {

    public static BasicTaskReviewParams createFromToken(String token) {
        return new BasicTaskReviewParams(token);
    }

    private List<Integer> reviewedUserIds = null;
    private Integer reviewedTaskId = null;
    
    public BasicTaskReviewParams(Integer userId, Integer campaignId, Integer documentId, Integer offset, Integer taskId, List<Integer> reviewedUserIds, Integer reviewedTaskId) {
        super(userId, campaignId, documentId, offset, taskId);
        this.reviewedUserIds = new ArrayList<Integer>();
        if (reviewedUserIds!=null) {
            this.reviewedUserIds.addAll(reviewedUserIds);
        }
        this.reviewedTaskId = reviewedTaskId;
    }

    public BasicTaskReviewParams(String token) {
        this(null, null, null, null, null, null, null);
        parseToken(token);
    }
    
    @Override
    public List<Integer> getReviewedUserIds() {
        return reviewedUserIds;
    }

    @Override
    public void setReviewedUserIds(List<Integer> userIds) {
        this.reviewedUserIds = new ArrayList<Integer>(userIds);
    }

    @Override
    public Integer getReviewedTaskId() {
        return reviewedTaskId;
    }

    @Override
    public void setReviewedTaskId(Integer reviewedTaskId) {
        this.reviewedTaskId = reviewedTaskId;
    }

    
    @Override
    public String createToken() {
        return super.createToken() + PlaceParams.ParamsProcessor.PARAM_SEP 
                + "r" + PlaceParams.ParamsProcessor.KEYVAL_SEP + reviewedTaskId + PlaceParams.ParamsProcessor.PARAM_SEP 
                + "uids" + PlaceParams.ParamsProcessor.KEYVAL_SEP + CoreDataProvider.join(reviewedUserIds, PlaceParams.ParamsProcessor.VALUES_SEP);
    }

    public class BasicUserCampaignDocOffsetTaskAnnSetIdsParamsProcessor extends BasicUserCampaignDocOffsetTaskParamsProcessor {

        @Override
        public void processParam(String key, String value) {
            super.processParam(key, value);
            if (key.equals("uids") && (reviewedUserIds == null || reviewedUserIds.isEmpty())) {
                try {
                    ArrayList<Integer> result = new ArrayList<Integer>();
                    for (String sid : value.split(PlaceParams.ParamsProcessor.VALUES_SEP)) {
                        result.add(Integer.valueOf(sid));
                    }
                    reviewedUserIds = result;
                } catch (NumberFormatException e) {
                }
            } else
            if (key.equals("r") && reviewedTaskId == null) {
                try {
                    reviewedTaskId = Integer.valueOf(value);
                } catch (NumberFormatException e) {
                }
            }            
        }
    }

   
    @Override
    protected void parseToken(String token) {
        PlaceParams.parseToken(token, new BasicUserCampaignDocOffsetTaskAnnSetIdsParamsProcessor());
    }
    
}
