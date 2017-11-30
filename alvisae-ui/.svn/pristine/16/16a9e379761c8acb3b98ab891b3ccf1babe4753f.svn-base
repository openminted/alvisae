/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data;

import fr.inra.mig_bibliome.alvisae.client.data3.CDXWS_Workflow_ResponseImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CampaignAnnotatorsListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.Basic_UserMe_Response;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSetListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.ExtendedUserInfoImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CDXWS_UserCampaignDocuments_Response;
import fr.inra.mig_bibliome.alvisae.client.data3.ExtendedUserInfoListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.DocumentNAssignedListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.TaskInstanceListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CampaignListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CDXWS_Authorizations_Response;
import fr.inra.mig_bibliome.alvisae.client.data3.CDXWS_DocInternalIdWithExternalId_Response;
import fr.inra.mig_bibliome.alvisae.client.data3.UserAuthorizationsImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AuthorizationListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.DocumentInfoListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CDXWS_UserMe_Response;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.AbstractRequestManager;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.RequestManager;
import fr.inra.mig_bibliome.alvisae.client.data3.CDWXS_ConsoBlocks_ResponseImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSet;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.AdministrativeQueries;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.CampaignQueries;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.ConsolidationQueries;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.DocumentQueries;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.WorkflowQueries;
import java.util.Collection;
import java.util.Set;

/**
 * Singleton in charge of providing data and saving changes
 *
 * @author fpapazian
 */
public class CoreDataProvider implements DocumentQueries, CampaignQueries, AdministrativeQueries, WorkflowQueries, ConsolidationQueries {

    public static String join(Collection<?> collection, String delimiter) {
        StringBuilder joined = new StringBuilder();
        for (Object id : collection) {
            joined.append(id.toString()).append(delimiter);
        }
        return joined.toString();
    }
    private final EventBus eventBus;
    private final RequestManager requestManager;

    public CoreDataProvider(EventBus eventBus) {
        this.eventBus = eventBus;
        this.requestManager = new RequestManager(eventBus, "user/me?wzauths=true");
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Override
    public void getUserList(AsyncCallback<ExtendedUserInfoListImpl> resultCallback) {
        String methodUrl = getRequestManager().getServerBaseUrl() + "user?wzauths=true";
        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<ExtendedUserInfoListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public ExtendedUserInfoListImpl decode(String responseText) {
                        ExtendedUserInfoListImpl parsedResponse = ExtendedUserInfoListImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void getAuthorizationList(AsyncCallback<AuthorizationListImpl> resultCallback) {
        String methodUrl = getRequestManager().getServerBaseUrl() + "authorizations";
        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<AuthorizationListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public AuthorizationListImpl decode(String responseText) {
                        CDXWS_Authorizations_Response parsedResponse = (CDXWS_Authorizations_Response) CDXWS_Authorizations_Response.createFromJSON(responseText);
                        return parsedResponse.getAuthorizations();
                    }
                });
    }

    @Override
    public void getUserAuthorizations(int userId, AsyncCallback<UserAuthorizationsImpl> resultCallback) {
        String methodUrl = getRequestManager().getServerBaseUrl() + "user/" + userId + "/authorizations";
        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<UserAuthorizationsImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public UserAuthorizationsImpl decode(String responseText) {
                        UserAuthorizationsImpl parsedResponse = UserAuthorizationsImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void setUserAuthorizations(final int userId, UserAuthorizationsImpl newAuths, AsyncCallback<UserAuthorizationsImpl> resultCallback) {
        String methodUrl = getRequestManager().getServerBaseUrl() + "user/" + userId + "/authorizations";
        requestManager.genericCall(methodUrl, RequestBuilder.PUT, null, newAuths.getJSON(),
                new GenericRequestCallback<UserAuthorizationsImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public UserAuthorizationsImpl decode(String responseText) {
                        UserAuthorizationsImpl parsedResponse = UserAuthorizationsImpl.createFromJSON(responseText);
                        //change authorization infos if current user was changed
                        if (getRequestManager().getCurrentUserId() == userId) {
                            getRequestManager().resetAuthenticationInfoAuths(parsedResponse);
                        }
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void createUser(String login, boolean isAdmin, String password, AsyncCallback<ExtendedUserInfoImpl> resultCallback) {
        AbstractRequestManager.Entry[] params = new AbstractRequestManager.Entry[]{
            new AbstractRequestManager.Entry("login", login),
            new AbstractRequestManager.Entry("is_admin", String.valueOf(isAdmin)),
            new AbstractRequestManager.Entry("passwd", password),};

        String methodUrl = getRequestManager().getServerBaseUrl() + "user";

        getRequestManager().genericCall(methodUrl, RequestBuilder.POST, params, null,
                new GenericRequestCallback<ExtendedUserInfoImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public ExtendedUserInfoImpl decode(String responseText) {
                        ExtendedUserInfoImpl parsedResponse = (ExtendedUserInfoImpl) Basic_UserMe_Response.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void updateUser(final int userId, String login, boolean isAdmin, AsyncCallback<ExtendedUserInfoImpl> resultCallback) {
        AbstractRequestManager.Entry[] params = new AbstractRequestManager.Entry[]{
            new AbstractRequestManager.Entry("login", login),
            new AbstractRequestManager.Entry("is_admin", String.valueOf(isAdmin)),};

        String methodUrl = getRequestManager().getServerBaseUrl() + "user/" + userId + "?" + AbstractRequestManager.buildQueryString(params);

        getRequestManager().genericCall(methodUrl, RequestBuilder.PUT, params, null,
                new GenericRequestCallback<ExtendedUserInfoImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public ExtendedUserInfoImpl decode(String responseText) {
                        ExtendedUserInfoImpl parsedResponse = (ExtendedUserInfoImpl) Basic_UserMe_Response.createFromJSON(responseText);
                        //update authorization infos if current user was changed
                        if (getRequestManager().getCurrentUserId() == userId) {
                            getRequestManager().resetAuthenticationInfos(parsedResponse.getDisplayName(), parsedResponse.isAdmin());
                        }
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void updateUserPassword(final int userId, final String newPassword, final AsyncCallback<JavaScriptObject> resultCallback) {
        AbstractRequestManager.Entry[] params = new AbstractRequestManager.Entry[]{
            new AbstractRequestManager.Entry("passwd", newPassword),};

        String methodUrl = getRequestManager().getServerBaseUrl() + "user/" + userId + "/chpasswd";

        //POST method used here to avoid transmitting the new password as an URL parameter
        getRequestManager().genericCall(methodUrl, RequestBuilder.POST, params, null,
                new GenericRequestCallback<JavaScriptObject>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public JavaScriptObject decode(String responseText) {
                        //change authorization infos if password was changed for current user
                        if (getRequestManager().getCurrentUserId() == userId) {
                            CDXWS_UserMe_Response parsedResponse = (CDXWS_UserMe_Response) Basic_UserMe_Response.createFromJSON(responseText);
                            AuthenticationInfoImpl authInfo = getRequestManager().changeAuthenticationInfo(parsedResponse, newPassword);
                        }
                        return null;
                    }
                });
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public void getCampaignList(AsyncCallback<CampaignListImpl> resultCallback) {
        String methodUrl = getRequestManager().getServerBaseUrl() + "campaigns";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<CampaignListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public CampaignListImpl decode(String responseText) {
                        CDXWS_UserMe_Response tmp = (CDXWS_UserMe_Response) Basic_UserMe_Response.createFromJSON(responseText);
                        CampaignListImpl parsedResponse = tmp.getUserCampaignList();
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void getUserCampaignList(int userId, final AsyncCallback<CampaignListImpl> resultCallback) {
        //FIXME (not yet implemented on server side)
        if (userId != requestManager.getCurrentUserId()) {
            throw new UnsupportedOperationException("Not supported yet. (retrieval of other user's annotation set)");
        }

        String methodUrl = requestManager.getServerBaseUrl() + "user/me";
        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<CampaignListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public CampaignListImpl decode(String responseText) {
                        CDXWS_UserMe_Response parsedResponse = (CDXWS_UserMe_Response) Basic_UserMe_Response.createFromJSON(responseText);
                        return parsedResponse.getUserCampaignList();
                    }
                });
    }

    @Override
    public void getCampaignAnnotators(AsyncCallback<CampaignAnnotatorsListImpl> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "campaigns/" + "annotators";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<CampaignAnnotatorsListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public CampaignAnnotatorsListImpl decode(String responseText) {
                        CampaignAnnotatorsListImpl parsedResponse = CampaignAnnotatorsListImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void updateCampaignAnnotators(int campaignId, Set<Integer> userToAdd, Set<Integer> userToDel, AsyncCallback<CampaignAnnotatorsListImpl> resultCallback) {
        StringBuilder methodUrl = new StringBuilder(requestManager.getServerBaseUrl()).append("campaigns/").append(campaignId).append("/annotators").append("?add=").append(join(userToAdd, ",")).append("&del=").append(join(userToDel, ","));

        requestManager.genericCall(methodUrl.toString(), RequestBuilder.PUT, null, null,
                new GenericRequestCallback<CampaignAnnotatorsListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public CampaignAnnotatorsListImpl decode(String responseText) {
                        CampaignAnnotatorsListImpl parsedResponse = CampaignAnnotatorsListImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void getDocumentInfoList(int userId, int campaignId, final AsyncCallback<DocumentInfoListImpl> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "user/" + userId + "/campaign/" + campaignId + "/documents";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<DocumentInfoListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public DocumentInfoListImpl decode(String responseText) {
                        CDXWS_UserCampaignDocuments_Response parsedResponse = CDXWS_UserCampaignDocuments_Response.createFromJSON(responseText);
                        return parsedResponse.getDocumentInfoList();
                    }
                });
    }

    @Override
    public void getCampaignUserDocAssignment(int campaignId, AsyncCallback<DocumentNAssignedListImpl> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "campaigns/" + campaignId + "/documents";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<DocumentNAssignedListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public DocumentNAssignedListImpl decode(String responseText) {
                        DocumentNAssignedListImpl parsedResponse = DocumentNAssignedListImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void updateCampaignUserDocAssignment(int userId, int campaignId, Set<Integer> docToAdd, Set<Integer> docToDel, AsyncCallback<DocumentInfoListImpl> resultCallback) {
        StringBuilder methodUrl = new StringBuilder(requestManager.getServerBaseUrl()).append("user/").append(userId).append("/campaign/").append(campaignId).append("/documents").append("?add=").append(join(docToAdd, ",")).append("&del=").append(join(docToDel, ","));

        requestManager.genericCall(methodUrl.toString(), RequestBuilder.PUT, null, null,
                new GenericRequestCallback<DocumentInfoListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public DocumentInfoListImpl decode(String responseText) {
                        CDXWS_UserCampaignDocuments_Response parsedResponse = CDXWS_UserCampaignDocuments_Response.createFromJSON(responseText);
                        return parsedResponse.getDocumentInfoList();
                    }
                });
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Override
    public void getAnnotatedDocument(final int userId, final int campaignId, int documentId, int taskId, final AsyncCallback<AnnotatedTextImpl> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "user/" + userId + "/campaign/" + campaignId + "/document/" + documentId + "/task/" + taskId;

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<AnnotatedTextImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public AnnotatedTextImpl decode(String responseText) {
                        AnnotatedTextImpl parsedResponse = AnnotatedTextImpl.createFromJSON(responseText);
                        //create Modification Handler
                        AnnotatedTextHandler.createHandler(userId, campaignId, parsedResponse);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void getDocumentWithExternalId(int userId, int campaignId, String docExternalId, String taskName, AsyncCallback<CDXWS_DocInternalIdWithExternalId_Response> resultCallback) {

        AbstractRequestManager.Entry[] params = new AbstractRequestManager.Entry[]{
            new AbstractRequestManager.Entry("documentid", docExternalId),
            new AbstractRequestManager.Entry("taskname", String.valueOf(taskName)),};

        String methodUrl = requestManager.getServerBaseUrl() + "user/" + userId + "/campaign/" + campaignId + "/withexternalid" + "?" + AbstractRequestManager.buildQueryString(params);

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<CDXWS_DocInternalIdWithExternalId_Response>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public CDXWS_DocInternalIdWithExternalId_Response decode(String responseText) {
                        CDXWS_DocInternalIdWithExternalId_Response parsedResponse = CDXWS_DocInternalIdWithExternalId_Response.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void getAdditionalAnnotationSet(AnnotatedTextHandler handler, Set<Integer> annotationSetIds, AsyncCallback<AnnotationSetListImpl> resultCallback) {
        StringBuilder methodUrl = new StringBuilder(requestManager.getServerBaseUrl()).append("annotation?ids=").append(join(annotationSetIds, ","));

        requestManager.genericCall(methodUrl.toString(), RequestBuilder.GET, null, null,
                new GenericRequestCallback<AnnotationSetListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public AnnotationSetListImpl decode(String responseText) {
                        AnnotationSetListImpl parsedResponse = AnnotationSetListImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    private void saveAnnotationSet(int userId, int campaignId, int documentId, AnnotationSet annotationSet, boolean publish, final AsyncCallback<JavaScriptObject> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "user/" + userId + "/campaign/" + campaignId + "/document/" + documentId + "?publish=" + String.valueOf(publish);

        requestManager.genericCall(methodUrl, RequestBuilder.PUT, null, annotationSet.getJSON(),
                new GenericRequestCallback<JavaScriptObject>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public JavaScriptObject decode(String responseText) {
                        return null;
                    }
                });
    }

    @Override
    public void saveAnnotationSet(int userId, int campaignId, int documentId, AnnotationSet annotationSet, final AsyncCallback<JavaScriptObject> resultCallback) {
        saveAnnotationSet(userId, campaignId, documentId, annotationSet, false, resultCallback);
    }

    @Override
    public void saveAndPublishAnnotationSet(int userId, int campaignId, int documentId, AnnotationSet annotationSet, final AsyncCallback<JavaScriptObject> resultCallback) {
        saveAnnotationSet(userId, campaignId, documentId, annotationSet, true, resultCallback);
    }

    // =========================================================================
    @Override
    public void getWorkflow(int campaignId, AsyncCallback<CDXWS_Workflow_ResponseImpl> resultCallback) {
        String methodUrl = getRequestManager().getServerBaseUrl() + "/campaigns/" + campaignId + "/workflow";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<CDXWS_Workflow_ResponseImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public CDXWS_Workflow_ResponseImpl decode(String responseText) {
                        CDXWS_Workflow_ResponseImpl parsedResponse = CDXWS_Workflow_ResponseImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void getTaskList(int campaignId, int userId, AsyncCallback<TaskInstanceListImpl> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "user/" + userId + "/campaign/" + campaignId + "/tasks";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<TaskInstanceListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public TaskInstanceListImpl decode(String responseText) {
                        TaskInstanceListImpl parsedResponse = TaskInstanceListImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void getAvailableTaskList(int campaignId, int userId, int taskId, AsyncCallback<TaskInstanceListImpl> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "user/" + userId + "/campaign/" + campaignId + "/task/" + taskId + "/available";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<TaskInstanceListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public TaskInstanceListImpl decode(String responseText) {
                        TaskInstanceListImpl parsedResponse = TaskInstanceListImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void getReviewableTaskInstances(int reviewerUserId, int campaignId, int documentId, int reviewTaskId, final AsyncCallback<TaskInstanceListImpl> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "user/" + reviewerUserId + "/campaign/" + campaignId + "/document/" + documentId + "/task/" + reviewTaskId + "/reviewable";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<TaskInstanceListImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public TaskInstanceListImpl decode(String responseText) {
                        TaskInstanceListImpl parsedResponse = TaskInstanceListImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void publishAnnotationTask(int userId, int campaignId, int documentId, int taskId, AsyncCallback<JavaScriptObject> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "user/" + userId + "/campaign/" + campaignId + "/document/" + documentId + "/task/" + taskId + "/publish";

        requestManager.genericCall(methodUrl, RequestBuilder.POST, null, null,
                new GenericRequestCallback<JavaScriptObject>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public JavaScriptObject decode(String responseText) {
                        return null;
                    }
                });
    }

    @Override
    public void removeAnnotationTask(int userId, int campaignId, int documentId, int taskId, AsyncCallback<JavaScriptObject> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "user/" + userId + "/campaign/" + campaignId + "/document/" + documentId + "/task/" + taskId + "/remove";

        requestManager.genericCall(methodUrl, RequestBuilder.POST, null, null,
                new GenericRequestCallback<JavaScriptObject>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public JavaScriptObject decode(String responseText) {
                        return null;
                    }
                });
    }

    // =========================================================================
    @Override
    public void getAnnotatedDocumentForReview(final int userId, final int campaignId, int documentId, int taskId, AsyncCallback<AnnotatedTextImpl> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "user/" + userId + "/campaign/" + campaignId + "/document/" + documentId + "/task/" + taskId + "/readonly";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<AnnotatedTextImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public AnnotatedTextImpl decode(String responseText) {
                        AnnotatedTextImpl parsedResponse = AnnotatedTextImpl.createFromJSON(responseText);
                        //create Modification Handler
                        AnnotatedTextHandler.createHandler(userId, campaignId, parsedResponse);
                        return parsedResponse;
                    }
                });
    }

    @Override
    public void getConsolidationBlocks(int campaignId, Set<Integer> annotationSetIds, AsyncCallback<CDWXS_ConsoBlocks_ResponseImpl> resultCallback) {
        StringBuilder methodUrl = new StringBuilder(requestManager.getServerBaseUrl())
                .append("campaigns/").append(campaignId)
                .append("/annotations/diff?ids=").append(join(annotationSetIds, ","));

        requestManager.genericCall(methodUrl.toString(), RequestBuilder.GET, null, null,
                new GenericRequestCallback<CDWXS_ConsoBlocks_ResponseImpl>(eventBus, resultCallback, getRequestManager()) {
                    @Override
                    public CDWXS_ConsoBlocks_ResponseImpl decode(String responseText) {
                        CDWXS_ConsoBlocks_ResponseImpl parsedResponse = CDWXS_ConsoBlocks_ResponseImpl.createFromJSON(responseText);
                        return parsedResponse;
                    }
                });

    }

    // =========================================================================
    public RequestManager getRequestManager() {
        return requestManager;
    }
    // =========================================================================
}
