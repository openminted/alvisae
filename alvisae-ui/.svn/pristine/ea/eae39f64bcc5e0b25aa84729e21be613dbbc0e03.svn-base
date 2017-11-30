/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data.Queries;

import fr.inra.mig_bibliome.alvisae.client.data3.SemClassTreeLevelImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.ProjectInfoListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.BranchesImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.SemClassNTermsImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.SemClassListImpl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.inra.mig_bibliome.alvisae.client.data.DetailedAsyncCallback;

/**
 *
 * @author fpapazian
 */
public interface TermQueries {

    // [baseurl] :  bibliome.jouy.inra.fr/tydiws/[instance_name]/
    void getUserProjects(int userId, AsyncCallback<ProjectInfoListImpl> resultCallback);

    /**
     * Retrieve the detail of a Semantic Class and its direct hyponyms
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/projects/[projectId]/semClass/[semClassId]
     *
     * @param projectId Id of a Termino Project
     * @param semClassId Id of a SemanticClass (0 to get the virtual root class)
     *
     * @throws IllegalArgumentException if the specified projectId or semClassId does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getSemanticClass(int projectId, int semClassId, final AsyncCallback<SemClassTreeLevelImpl> resultCallback);

    void getSemanticClassNTerms(int projectId, int semClassId, AsyncCallback<SemClassNTermsImpl> resultCallback);

    void searchSemanticClassesByPattern(int projectId, String pattern, AsyncCallback<SemClassListImpl> resultCallback);

    void getBranchesBetweenClasses(int projectId, int fromSemClassId, int toSemClassId, AsyncCallback<BranchesImpl> resultCallback);

    void replaceHyperonym(int projectId, int semClassId, int semClassVersion, int prevHyperSemClassId, int prevHyperSemClassVersion, int newHyperSemClassId, int newHyperSemClassVersion, DetailedAsyncCallback<SemClassTreeLevelImpl> resultCallback);

    void createClassAndRepresentativeTerm(int projectId, String surfaceForm, String lemmatizedForm, int newHyperSemClassId, int newHyperSemClassVersion, boolean force, DetailedAsyncCallback<SemClassTreeLevelImpl> resultCallback);

    void createTermSynonym(int projectId, String surfaceForm, String lemmatizedForm, int hyperSemClassId, int hyperSemClassVersion, DetailedAsyncCallback<SemClassTreeLevelImpl> resultCallback);

    void addSynonymToSemanticClass(int projectId, int termId, int semClassId, int semClassVersion, AsyncCallback<SemClassNTermsImpl> resultCallback);

    void mergeClasses(int projectId, int semClassId1, int semClassVersion1, int semClassId2, int semClassVersion2, DetailedAsyncCallback<SemClassTreeLevelImpl> resultCallback);
}
