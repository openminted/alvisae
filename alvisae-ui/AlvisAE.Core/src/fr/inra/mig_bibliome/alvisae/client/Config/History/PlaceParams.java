/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config.History;

import java.util.List;

/**
 *
 * @author fpapazian
 */
public class PlaceParams {

    /**
     * Parameters for Places
     */
    public static interface UserCampaignDocParams {
        
        public String createToken();

        public Integer getCampaignId();

        public Integer getDocumentId();

        public Integer getUserId();

        public void setCampaignId(Integer campaignId);

        public void setDocumentId(Integer documentId);

        public void setUserId(Integer userId);
    }

    public static interface UserCampaignDocOffsetParams extends UserCampaignDocParams {

        public Integer getOffset();

        public void setOffset(Integer offset);
    }

    public static interface UserCampaignDocOffsetTaskParams extends UserCampaignDocOffsetParams {

        public Integer getTaskId();

        public void setTaskId(Integer taskId);
    }

    public static interface TaskReviewParams extends UserCampaignDocOffsetTaskParams {

        /**
         * 
         * @return List of user's Id whose AnnotationSets are to be reviewed
         */
        public List<Integer> getReviewedUserIds();
        
        public void setReviewedUserIds(List<Integer> userIds);
        
        /**
         * 
         * @return the identifier of the Task to be reviewed
         */
        public Integer getReviewedTaskId();

        public void setReviewedTaskId(Integer reviewedTaskId);
    }

    /**
     * Simple methods to parse Place parameters encoded within URL fragment
     */
    public static interface ParamsProcessor {

        public static String PARAM_SEP = "&";
        public static String KEYVAL_SEP = "=";
        public static String VALUES_SEP = "!";

        void processParam(String key, String value);
    }

    public static void parseToken(String token, ParamsProcessor paramsProc) {
        if (token != null) {
            for (String param : token.split(ParamsProcessor.PARAM_SEP)) {
                String[] kv = param.split(ParamsProcessor.KEYVAL_SEP, 2);
                if (kv.length == 2) {
                    String key = kv[0];
                    String val = kv[1];
                    paramsProc.processParam(key, val);
                }
            }
        }
    }
}