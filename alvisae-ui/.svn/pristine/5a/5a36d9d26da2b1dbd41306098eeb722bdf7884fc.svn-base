/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data;

import fr.inra.mig_bibliome.alvisae.client.data3.SemClassTreeLevelImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.ProjectInfoListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.BranchesImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.SemClassNTermsImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.SemClassListImpl;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.inra.mig_bibliome.alvisae.client.data.Queries.TermQueries;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.AbstractRequestManager;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.AbstractRequestManager.Entry;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.RequestManager;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.TyDISemClassRefImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.TyDITermRefImpl;

/**
 * Singleton in charge of providing data and saving changes
 *
 * @author fpapazian
 */
public class TermDataProvider implements TermQueries {

    private final EventBus eventBus;
    private final RequestManager requestManager;

    public TermDataProvider(EventBus eventBus) {
        this.eventBus = eventBus;
        this.requestManager = new RequestManager(eventBus, "user/me");
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    @Override
    public void getUserProjects(int userId, AsyncCallback<ProjectInfoListImpl> resultCallback) {

        String methodUrl = requestManager.getServerBaseUrl() + "/users/" + userId + "/projects";

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<ProjectInfoListImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public ProjectInfoListImpl decode(String responseText) {
                ProjectInfoListImpl parsedResponse = ProjectInfoListImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    @Override
    public void getSemanticClass(int projectId, int semClassId, AsyncCallback<SemClassTreeLevelImpl> resultCallback) {
        if (!requestManager.isSignedIn()) {
            return;
        }
        String methodUrl = requestManager.getServerBaseUrl() + "/projects/" + projectId + "/semClass/" + semClassId;

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<SemClassTreeLevelImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public SemClassTreeLevelImpl decode(String responseText) {
                SemClassTreeLevelImpl parsedResponse = SemClassTreeLevelImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    @Override
    public void getSemanticClassNTerms(int projectId, int semClassId, AsyncCallback<SemClassNTermsImpl> resultCallback) {
        String methodUrl = requestManager.getServerBaseUrl() + "/projects/" + projectId + "/semClassNTerms/" + semClassId;

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<SemClassNTermsImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public SemClassNTermsImpl decode(String responseText) {
                SemClassNTermsImpl parsedResponse = SemClassNTermsImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    @Override
    public void searchSemanticClassesByPattern(int projectId, String pattern, AsyncCallback<SemClassListImpl> resultCallback) {

        Entry[] params = new Entry[]{new AbstractRequestManager.Entry("pattern", pattern)};
        String methodUrl = requestManager.getServerBaseUrl() + "/projects/" + projectId + "/semClasses?" + AbstractRequestManager.buildQueryString(params);

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<SemClassListImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public SemClassListImpl decode(String responseText) {
                SemClassListImpl parsedResponse = SemClassListImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    @Override
    public void getBranchesBetweenClasses(int projectId, int fromSemClassId, int toSemClassId, AsyncCallback<BranchesImpl> resultCallback) {
        if (!requestManager.isSignedIn()) {
            return;
        }
        String methodUrl = requestManager.getServerBaseUrl() + "/projects/" + projectId + "/branches/fromSemClass/" + fromSemClassId + "/toSemClass/" + toSemClassId;

        requestManager.genericCall(methodUrl, RequestBuilder.GET, null, null,
                new GenericRequestCallback<BranchesImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public BranchesImpl decode(String responseText) {
                BranchesImpl parsedResponse = BranchesImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });
    }

    @Override
    public void replaceHyperonym(int projectId, int semClassId, int semClassVersion, int prevHyperSemClassId, int prevHyperSemClassVersion, int newHyperSemClassId, int newHyperSemClassVersion, DetailedAsyncCallback<SemClassTreeLevelImpl> resultCallback) {
        Entry[] params = new Entry[]{
            new AbstractRequestManager.Entry("semClassVersion", String.valueOf(semClassVersion)),
            new AbstractRequestManager.Entry("prevHyperSemClassId", String.valueOf(prevHyperSemClassId)),
            new AbstractRequestManager.Entry("prevHyperSemClassVersion", String.valueOf(prevHyperSemClassVersion)),
            new AbstractRequestManager.Entry("newHyperSemClassId", String.valueOf(newHyperSemClassId)),
            new AbstractRequestManager.Entry("newHyperSemClassVersion", String.valueOf(newHyperSemClassVersion)),};

        String methodUrl = requestManager.getServerBaseUrl() + "/projects/" + projectId + "/semClass/" + semClassId + "?" + AbstractRequestManager.buildQueryString(params);

        requestManager.genericCall(methodUrl, RequestBuilder.PUT, params, null,
                new GenericRequestCallback<SemClassTreeLevelImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public SemClassTreeLevelImpl decode(String responseText) {
                SemClassTreeLevelImpl parsedResponse = SemClassTreeLevelImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });

    }

    @Override
    public void createTermSynonym(int projectId, String surfaceForm, String lemmatizedForm, int newHyperSemClassId, int newHyperSemClassVersion, DetailedAsyncCallback<SemClassTreeLevelImpl> resultCallback) {
        Entry[] params = new Entry[]{
            new AbstractRequestManager.Entry("surfaceForm", surfaceForm),
            new AbstractRequestManager.Entry("lemmatizedForm", lemmatizedForm),
            new AbstractRequestManager.Entry("classId", String.valueOf(newHyperSemClassId)),
            new AbstractRequestManager.Entry("classVersion", String.valueOf(newHyperSemClassVersion)),};

        String methodUrl = requestManager.getServerBaseUrl() + "/projects/" + projectId + "/term";

        requestManager.genericCall(methodUrl, RequestBuilder.POST, params, null,
                new GenericRequestCallback<SemClassTreeLevelImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public SemClassTreeLevelImpl decode(String responseText) {
                SemClassTreeLevelImpl parsedResponse = SemClassTreeLevelImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });

    }

    @Override
    public void createClassAndRepresentativeTerm(int projectId, String surfaceForm, String lemmatizedForm, int hyperSemClassId, int hyperSemClassVersion, boolean force, DetailedAsyncCallback<SemClassTreeLevelImpl> resultCallback) {
        Entry[] params = new Entry[]{
            new AbstractRequestManager.Entry("surfaceForm", surfaceForm),
            new AbstractRequestManager.Entry("lemmatizedForm", lemmatizedForm),
            new AbstractRequestManager.Entry("hyperId", String.valueOf(hyperSemClassId)),
            new AbstractRequestManager.Entry("hyperVersion", String.valueOf(hyperSemClassVersion)),
            new AbstractRequestManager.Entry("force", String.valueOf(force)),};

        String methodUrl = requestManager.getServerBaseUrl() + "/projects/" + projectId + "/semClasses";

        requestManager.genericCall(methodUrl, RequestBuilder.POST, params, null,
                new GenericRequestCallback<SemClassTreeLevelImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public SemClassTreeLevelImpl decode(String responseText) {
                SemClassTreeLevelImpl parsedResponse = SemClassTreeLevelImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });

    }

    @Override
    public void addSynonymToSemanticClass(int projectId, int termId, int semClassId, int semClassVersion, AsyncCallback<SemClassNTermsImpl> resultCallback) {
        Entry[] params = new Entry[]{
            new AbstractRequestManager.Entry("termId", String.valueOf(termId)),
            new AbstractRequestManager.Entry("classVersion", String.valueOf(semClassVersion)),};

        String methodUrl = requestManager.getServerBaseUrl() + "/projects/" + projectId + "/semClassNTerms/" + semClassId;

        requestManager.genericCall(methodUrl, RequestBuilder.POST, params, null,
                new GenericRequestCallback<SemClassNTermsImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public SemClassNTermsImpl decode(String responseText) {
                SemClassNTermsImpl parsedResponse = SemClassNTermsImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });

    }

    @Override
    public void mergeClasses(int projectId, int semClassId1, int semClassVersion1, int semClassId2, int semClassVersion2, DetailedAsyncCallback<SemClassTreeLevelImpl> resultCallback) {
        Entry[] params = new Entry[]{
            new AbstractRequestManager.Entry("semClassId1", String.valueOf(semClassId1)),
            new AbstractRequestManager.Entry("semClassVersion1", String.valueOf(semClassVersion1)),
            new AbstractRequestManager.Entry("semClassId2", String.valueOf(semClassId2)),
            new AbstractRequestManager.Entry("semClassVersion2", String.valueOf(semClassVersion2)),};

        String methodUrl = requestManager.getServerBaseUrl() + "/projects/" + projectId + "/semClasses" + "/merge";

        requestManager.genericCall(methodUrl, RequestBuilder.POST, params, null,
                new GenericRequestCallback<SemClassTreeLevelImpl>(eventBus, resultCallback, getRequestManager()) {
            @Override
            public SemClassTreeLevelImpl decode(String responseText) {
                SemClassTreeLevelImpl parsedResponse = SemClassTreeLevelImpl.createFromJSON(responseText);
                return parsedResponse;
            }
        });

    }

    // =========================================================================
    public RequestManager getRequestManager() {
        return requestManager;
    }
    // =========================================================================

    public String getTermExternalId(int projectId, int termId) {
        return TyDITermRefImpl.getTermExternalId(requestManager.getServerBaseUrl(), projectId, termId);
    }

    public String getSemClassExternalId(Integer projectId, int semClassId, int canonicTermId) {
        return TyDISemClassRefImpl.getSemClassFullExternalId(requestManager.getServerBaseUrl(), projectId, semClassId, canonicTermId);
    }
}
