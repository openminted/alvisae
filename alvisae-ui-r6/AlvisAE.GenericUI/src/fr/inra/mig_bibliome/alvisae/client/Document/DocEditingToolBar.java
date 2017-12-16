/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocOffsetTaskParams;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.StaneResources;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskSelectingPlace;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.RequestManager;
import fr.inra.mig_bibliome.alvisae.client.data3.TaskInstanceListImpl;
import java.util.LinkedList;

/**
 *
 * @author fpapazian
 */
public class DocEditingToolBar extends Composite implements ApplicationStatusChangedEventHandler {

    private static DocEditingToolBarUiBinder uiBinder = GWT.create(DocEditingToolBarUiBinder.class);

    interface DocEditingToolBarUiBinder extends UiBinder<Widget, DocEditingToolBar> {
    }
    //
    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);

    interface Styles extends CssResource {

        String BackGroundPos();

        String ForeGroundPos();
    }
    //
    // --
    @UiField
    DocEditingToolBar.Styles style;
    // --
    @UiField
    Panel glassPanel;
    @UiField
    LayoutPanel messageBar;
    @UiField
    PushButton saveAnnotationsButton;
    @UiField
    PushButton finalizeDocButton;
    @UiField
    PushButton nextDocButton;
    @UiField
    PushButton prevDocButton;
    @UiField
    PushButton promptDocLink;
    //
    private final DocEditingViewImpl docEditView;
    private final LinkedList<Integer> documentHistory = new LinkedList<Integer>();
    private Integer nextDocId;
    private Integer prevDocId;
    private String docUrl = null;

    public DocEditingToolBar(DocEditingViewImpl docEditView) {
        initWidget(uiBinder.createAndBindUi(this));
        this.docEditView = docEditView;
        promptDocLink.setVisible(docUrl != null);
    }

    void clearDocumentHistory() {
        documentHistory.clear();
    }

    void resetNextDocumentId() {
        if (docEditView.getDocumentId() != null) {
            Integer currentDocumentIndex = documentHistory.indexOf(docEditView.getDocumentId());
            if (currentDocumentIndex + 1 < documentHistory.size()) {
                nextDocId = documentHistory.get(currentDocumentIndex + 1);
            } else {

                injector.getCoreDataProvider().
                        getAvailableTaskList(
                        docEditView.getCampaignId(),
                        docEditView.getUserId(),
                        docEditView.getTaskId(),
                        new AsyncCallback<TaskInstanceListImpl>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        nextDocId = null;
                        nextDocButton.setEnabled(nextDocId != null);
                    }

                    @Override
                    public void onSuccess(TaskInstanceListImpl result) {
                        //get the first document that is not already in document history
                        for (int row = 0; row < result.length(); row++) {
                            int dId = result.get(row).getDocumentId();
                            if (!documentHistory.contains(dId)) {
                                nextDocId = dId;
                                break;
                            }
                        }
                        nextDocButton.setEnabled(nextDocId != null);
                    }
                });

            }
        }

    }

    void resetPrevDocumentId() {
        if (docEditView.getDocumentId() != null) {
            Integer currentDocumentIndex = documentHistory.indexOf(docEditView.getDocumentId());
            prevDocId = (currentDocumentIndex > 0) ? documentHistory.get(currentDocumentIndex - 1) : null;
            prevDocButton.setEnabled(prevDocId != null);
        }
    }

    boolean addDocumentToHistory(Integer documentId) {
        if (documentId != null && !documentHistory.contains(documentId)) {
            documentHistory.addLast(documentId);
            return true;
        } else {
            return false;
        }
    }

    @UiHandler("gotoDocListButton")
    void handleGotoDocListButtonClick(ClickEvent e) {
        docEditView.getPresenter().goTo(new TaskSelectingPlace(new BasicUserCampaignDocOffsetTaskParams(docEditView.getUserId(), docEditView.getCampaignId(), docEditView.getDocumentId(), 0, docEditView.getTaskId())));
    }

    @UiHandler("promptDocLink")
    void handlePromptDocLinkClick(ClickEvent e) {
        if (docUrl != null) {
            Window.prompt("Copy URL to clipboard ([Ctrl]+C or [Cmd]+C), then close this box", docUrl);
        }
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl == null || docUrl.trim().isEmpty() ? null : docUrl.trim();
        promptDocLink.setVisible(docUrl != null);
    }

    @UiHandler("nextDocButton")
    void handleNextDocButtonClick(ClickEvent e) {
        if (nextDocId != null) {
            glassPanel.addStyleName(style.ForeGroundPos());
            glassPanel.removeStyleName(style.BackGroundPos());
            docEditView.getPresenter().goTo(new DocEditingPlace(new BasicUserCampaignDocOffsetTaskParams(docEditView.getUserId(), docEditView.getCampaignId(), nextDocId, 0, docEditView.getTaskId())));
        }
    }

    @UiHandler("prevDocButton")
    void handlePrevDocButtonClick(ClickEvent e) {
        if (prevDocId != null) {
            glassPanel.addStyleName(style.ForeGroundPos());
            glassPanel.removeStyleName(style.BackGroundPos());
            docEditView.getPresenter().goTo(new DocEditingPlace(new BasicUserCampaignDocOffsetTaskParams(docEditView.getUserId(), docEditView.getCampaignId(), prevDocId, 0, docEditView.getTaskId())));
        }
    }

    @UiHandler("saveAnnotationsButton")
    void handleSaveDocButtonClick(ClickEvent e) {
        docEditView.saveDocument(false);
    }

    @UiHandler("finalizeDocButton")
    void handleFinalizeButtonClick(ClickEvent e) {
        docEditView.saveDocument(true);
    }

    @Override
    public void onApplicationStatusChanged(final ApplicationStatusChangedEvent event) {
        RequestManager requestManager = injector.getCoreDataProvider().getRequestManager();
        if (requestManager != null && event.getRequestManager() != null && event.getRequestManager().equals(requestManager)) {
            Scheduler.get().scheduleFinally(new Command() {
                @Override
                public void execute() {
                    if (event.getStatus().equals(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle)) {
                        glassPanel.addStyleName(style.BackGroundPos());
                        glassPanel.removeStyleName(style.ForeGroundPos());
                    } else if (event.getStatus().equals(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing)) {
                        glassPanel.addStyleName(style.ForeGroundPos());
                        glassPanel.removeStyleName(style.BackGroundPos());
                    }
                }
            });
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        EventBus eventBus = injector.getMainEventBus();
        ApplicationStatusChangedEvent.register(eventBus, this);

    }

    @Override
    protected void onDetach() {
        super.onDetach();
        ApplicationStatusChangedEvent.unregister(this);
    }
}
