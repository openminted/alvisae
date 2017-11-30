/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Queries;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSetListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CDXWS_DocInternalIdWithExternalId_Response;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSet;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public interface DocumentQueries {

    // [baseurl] :  bibliome.jouy.inra.fr/test/stane/dev/api
    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/user/[userId]/campaign/[campaignId]/document/[documentId]/task/[taskId]
     *
     * @throws IllegalArgumentException if the specified userId, campaignId, documentId or TaskId do not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getAnnotatedDocument(int userId, int campaignId, int documentId,  int taskId, final AsyncCallback<AnnotatedTextImpl> resultCallback);

    /**
     * 
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/user/[userId]/campaign/[campaignId]/withexternalid?documentid=[documentExternalId]&taskname=[taskName]
     *
     * @throws IllegalArgumentException if the specified userId, campaignId, documentExternalId or taskName does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getDocumentWithExternalId(int userId, int campaignId, String docExternalId, String taskName, final AsyncCallback<CDXWS_DocInternalIdWithExternalId_Response> resultCallback);


    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/annotation?ids=[comma separated list of annotationSetId]
     *
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getAdditionalAnnotationSet(AnnotatedTextHandler handler, Set<Integer> annotationSetIds, final AsyncCallback<AnnotationSetListImpl> resultCallback);
    
    /**
     * 
     * <b>Rest Method & URL:</b>
     * PUT http://[baseurl]/user/[userId]/campaign/[campaignId]/document/[documentId]
     *
     * @throws IllegalArgumentException if the specified userId, campaignId or documentId does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void saveAnnotationSet(int userId, int campaignId, int documentId, AnnotationSet annotationSet, final AsyncCallback<JavaScriptObject> resultCallback);
    
    void saveAndPublishAnnotationSet(int userId, int campaignId, int documentId, AnnotationSet annotationSet, final AsyncCallback<JavaScriptObject> resultCallback);
    
}
