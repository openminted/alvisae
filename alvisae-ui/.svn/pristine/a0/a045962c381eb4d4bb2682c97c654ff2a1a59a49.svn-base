/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Queries;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CDWXS_ConsoBlocks_ResponseImpl;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public interface ConsolidationQueries {
    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/user/[userId]/campaign/[campaignId]/document/[documentId]/task/[taskId]/readonly
     *
     * @throws IllegalArgumentException if the specified userId, campaignId, documentId or TaskId do not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getAnnotatedDocumentForReview(int userId, int campaignId, int documentId,  int taskId, final AsyncCallback<AnnotatedTextImpl> resultCallback);

    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/campaigns/[campaignId]/annotations/diff?ids=[comma separated list of annotationSetId]
     *
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     * @throws IllegalArgumentException if the specified campaignId does not exist (400 Bad Request)
     */
    void getConsolidationBlocks(int campaignId, Set<Integer> annotationSetIds, final AsyncCallback<CDWXS_ConsoBlocks_ResponseImpl> resultCallback);

}
