/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Queries;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.inra.mig_bibliome.alvisae.client.data3.CampaignAnnotatorsListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CampaignListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.DocumentInfoListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.DocumentNAssignedListImpl;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public interface CampaignQueries {

    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/users/[userId]/campaigns
     *
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of all Campaigns
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getCampaignList(AsyncCallback<CampaignListImpl> resultCallback);

    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/users/[userId]/campaigns
     *
     * @param userId Id of an User (might be different from current user)
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of Annotation Campaign to which the specified user can participate to
     * @throws IllegalArgumentException if the specified userId does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getUserCampaignList(int userId, AsyncCallback<CampaignListImpl> resultCallback);

    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/campaigns/annotators
     *
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of Annotation Campaigns and their associated annotators
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getCampaignAnnotators(AsyncCallback<CampaignAnnotatorsListImpl> resultCallback);
    
    /**
     *
     * <b>Rest Method & URL:</b>
     * PUT http://[baseurl]/campaigns/annotators?add=[user_ids]&del=[user_ids]
     *
     * @param campaignId Id of an Annotation Campaign
     * @param userToAdd Ids of the users to be added to the Campaign participants
     * @param userToDel Ids of the users to be removed to the Campaign participants
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of Annotation Campaigns and their associated annotators
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void updateCampaignAnnotators(int campaignId, Set<Integer> userToAdd, Set<Integer> userToDel, AsyncCallback<CampaignAnnotatorsListImpl> resultCallback);
    
    /**
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/user/[userId]/campaign/[campaignId]/documents
     *
     * @param userId Id of an User (might be different from current user)
     * @param campaignId Id of an Annotation Campaign
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of partial info about the Documents assigned to the specified user in the Campaign
     * @throws IllegalArgumentException if the specified campaignId does not exist (400 Bad Request)
     * @throws IllegalArgumentException if the specified userId  does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getDocumentInfoList(int userId, int campaignId, AsyncCallback<DocumentInfoListImpl> resultCallback);
    
    /**
     * <b>Rest Method & URL:</b>
     * PUT http://[baseurl]/user/[userId]/campaign/[campaignId]/documents?add=[doc_ids]&del=[doc_ids]
     *
     * @param userId Id of an User (might be different from current user)
     * @param campaignId Id of an Annotation Campaign
     * @param docToAdd Ids of the document to be assigned to the specified user
     * @param docToDel Ids of the document to be unassigned from the specified user
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of partial info about the Documents assigned to the specified user in the Campaign
     * @throws IllegalArgumentException if the specified campaignId does not exist (400 Bad Request)
     * @throws IllegalArgumentException if the specified userId  does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void updateCampaignUserDocAssignment(int userId, int campaignId, Set<Integer> docToAdd, Set<Integer> docToDel, AsyncCallback<DocumentInfoListImpl> resultCallback);

    /**
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]//campaign/[campaignId]/documents
     *
     * @param campaignId Id of an Annotation Campaign
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of partial info about the Documents assigned to the specified user in the Campaign
     * @throws IllegalArgumentException if the specified campaignId does not exist (400 Bad Request)
     * @throws IllegalArgumentException if the specified userId  does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getCampaignUserDocAssignment(int campaignId, AsyncCallback<DocumentNAssignedListImpl> resultCallback);
    

}
