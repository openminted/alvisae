/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Queries;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.inra.mig_bibliome.alvisae.client.data3.CDXWS_Workflow_ResponseImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.TaskInstanceListImpl;

/**
 *
 * @author fpapazian
 */
public interface WorkflowQueries {

    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/campaigns/[userId]/workflow
     *
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of all Task definition and the annotation schema of the specified campaign
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getWorkflow(int campaignId, AsyncCallback<CDXWS_Workflow_ResponseImpl> resultCallback);

    
    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/user/[userId]/campaign/[campaignId]/tasks
     *
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of all Task instances for the specified campaign and user 
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getTaskList(int campaignId, int userId, AsyncCallback<TaskInstanceListImpl> resultCallback);
    
    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/user/[userId]/campaign/[campaignId]/task/[taskId]/available
     *
     * @param result asynchronous callback which will be called when the query ends (either with a response or on failure)
     *
     * @return list of all available Task instances (i.e. document that could be annotated) for the specified campaign, user and task
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getAvailableTaskList(int campaignId, int userId, int taskId, AsyncCallback<TaskInstanceListImpl> resultCallback);
    
    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/user/[userId]/campaign/[campaignId]/document/[documentId]/task/[taskId]/reviewable
     *
     * @return list of all Task instances that can be reviewed by the user/document/task/campaign
     * @throws IllegalArgumentException if the specified userId, campaignId, documentId or TaskId do not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getReviewableTaskInstances(int reviewerUserId, int campaignId, int documentId, int reviewTaskId, final AsyncCallback<TaskInstanceListImpl> resultCallback);
    
    /**
     * 
     * <b>Rest Method & URL:</b>
     * POST http://[baseurl]/user/[userId]/campaign/[campaignId]/document/[documentId]/task/[taskId]/publish
     *
     * @throws IllegalArgumentException if the specified userId, campaignId, documentId or taskId does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void publishAnnotationTask(int userId, int campaignId, int documentId, int taskId, AsyncCallback<JavaScriptObject> resultCallback);
    

    /**
     * 
     * <b>Rest Method & URL:</b>
     * POST http://[baseurl]/user/[userId]/campaign/[campaignId]/document/[documentId]/task/[taskId]/remove
     *
     * @throws IllegalArgumentException if the specified userId, campaignId, documentId or taskId does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void removeAnnotationTask(int userId, int campaignId, int documentId, int taskId, AsyncCallback<JavaScriptObject> resultCallback);
}
