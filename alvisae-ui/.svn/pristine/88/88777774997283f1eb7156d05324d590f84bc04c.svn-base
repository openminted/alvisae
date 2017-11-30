/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Response;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocOffsetTaskParams;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Document.DocDisplayPlace.BasicDocExtIdParams;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.WorkingDocumentChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.DetailedAsyncResponseHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.CDXWS_DocInternalIdWithExternalId_Response;

/**
 * Activity providing Document Annotation Editing feature
 *
 * @author fpapazian
 */
public class DocDisplayActivity extends AbstractActivity implements DocEditingView.Presenter {

    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);
    private final DocDisplayPlace place;

    public DocDisplayActivity(DocDisplayPlace place) {
        this.place = place;
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(final AcceptsOneWidget containerWidget, final EventBus eventBus) {

        //TODO since the Asynch loading can take some time, a wait dialog should appear on screen to make user more comfortable
        eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing, null));

        //Async loading to enable fast startup of the default activity
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, null));
                throw new UnsupportedOperationException("Unable to load AlvisAE DocDisplay view - Please reload page.");
            }

            @Override
            public void onSuccess() {
                Scheduler.get().scheduleDeferred(new Command() {
                    @Override
                    public void execute() {

                        if (place != null) {

                            final BasicDocExtIdParams params = place.getParams();

                            if (params.getCampaignId() == null) {
                                Window.alert("A campaign must be specified");
                            } else {

                                injector.getMainEventBus().fireEvent(new WorkingDocumentChangedEvent(null, null, WorkingDocumentChangedEvent.ChangeType.Unloaded));
                                final int userId = injector.getCoreDataProvider().getRequestManager().getCurrentUserId();

                                injector.getCoreDataProvider().getDocumentWithExternalId(userId, params.getCampaignId(), params.getDocExternalId(), params.getTaskName(), new DetailedAsyncResponseHandler<CDXWS_DocInternalIdWithExternalId_Response>() {
                                    @Override
                                    public void onSuccess(CDXWS_DocInternalIdWithExternalId_Response result) {

                                        int campaignId = result.getCampaignId();
                                        int documentId = result.getDocumentId();
                                        int taskId = result.getTaskId();

                                        //"redirect" to actual doc editing activity
                                        BasicUserCampaignDocOffsetTaskParams actualParams = new BasicUserCampaignDocOffsetTaskParams(userId, campaignId, documentId, null, taskId);
                                        goTo(new DocEditingPlace(actualParams));
                                    }

                                    @Override
                                    public void onFailure(Response response) {
                                        Window.alert("Document could not be loaded:\n[" + response.getStatusCode() + "] - "+ response.getStatusText());
                                    }
                                });
                            }

                        } else {
                            Window.alert("Could not proceed with no argument specified");
                        }
                        eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, null));
                    }
                });

            }
        });
    }

    /**
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        return null;
    }

    @Override
    public void onStop() {
        super.onStop();
        //empty the view
    }

    /**
     * Navigate to a new Place in the browser
     */
    @Override
    public void goTo(Place place) {
        injector.getPlaceController().goTo(place);
    }
}
