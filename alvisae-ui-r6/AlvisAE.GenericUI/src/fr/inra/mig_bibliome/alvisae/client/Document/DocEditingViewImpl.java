/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.http.client.Response;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationDetailsUi;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationTable;
import fr.inra.mig_bibliome.alvisae.client.Config.ApplicationOptions;
import fr.inra.mig_bibliome.alvisae.client.Config.ApplicationOptions.PersistedOptionHandler;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Events.*;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TermAnnotationsExpositionEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TermAnnotationsExpositionEventHandler;
import fr.inra.mig_bibliome.alvisae.client.SemClass.StructTermUi;
import fr.inra.mig_bibliome.alvisae.client.StaneResources;
import fr.inra.mig_bibliome.alvisae.client.Start.MainToolBar;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.DetailedAsyncResponseHandler;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.NetworkActivityDisplayer;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSetImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSetListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.JsArrayDecorator;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Main UI of the Annotation Editor, it contains - a document editor panel, - a
 * table of all annotations of the current document, - a property grid for the
 * selected annotation - a tool bar allowing to navigate through the documents
 * of the campaign
 *
 * @author fpapazian
 */
public class DocEditingViewImpl extends Composite implements DocEditingView, EditHappenedEventHandler, MaximizingWidgetEventHandler, TermAnnotationsExpositionEventHandler {

    interface DocEditingViewImplUiBinder extends UiBinder<DockLayoutPanel, DocEditingViewImpl> {
    }
    private static DocEditingViewImplUiBinder uiBinder = GWT.create(DocEditingViewImplUiBinder.class);
    private static final Logger log = Logger.getLogger(DocEditingViewImpl.class.getName());

    static class InformationPanel {

        private final HTML panel;
        private final Timer timer;

        public InformationPanel(HTML htmlPanel) {
            this.panel = htmlPanel;
            timer = new Timer() {
                @Override
                public void run() {
                    panel.getElement().setAttribute("style", "display:none;");
                }
            };
        }

        protected void cancelEffect() {
            timer.cancel();
        }

        public void slide() {
            panel.getElement().setAttribute("style", "");
        }

        public void slide(int openDuration) {
            slide();
            timer.schedule(openDuration);
        }

        /**
         *
         * @param event the event which must be displayed to the user
         */
        public void setMessage(InformationReleasedEvent event) {
            cancelEffect();
            panel.setHTML(event.getMessage());
        }
    }

    interface Styles extends CssResource {
    }
    //
    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);
    //
    // --
    @UiField
    DocEditingViewImpl.Styles style;
    // --
    @UiField
    DockLayoutPanel dockPanel;
    @UiField
    Panel toolBarHolder;
    @UiField
    HTML statusPanel;
    @UiField
    PushButton showMessagesBtn;
    @UiField
    DocumentUi documentUI;
    @UiField
    SplitLayoutPanel detailsDocSplitPanel;
    @UiField
    LayoutPanel detailsPanel;
    @UiField
    SplitLayoutPanel docTableSplitPanel;
    @UiField
    LayoutPanel annotTableLayoutPanel;
    @UiField
    AnnotationTable annotationTable;
    @UiField
    RequiresResizeSpy annotTableResizeSpy;
    @UiField
    DockLayoutPanel bottomDockPanel;
    @UiField
    LayoutPanel statusBar;
    @UiField
    NetworkActivityDisplayer networkActivityDisplayer;
    @UiField
    StructTermUi structTermUI;
    @UiField
    AnnotationDetailsUi annotationDetailsUI;
    @UiField
    SplitLayoutPanel propsSemClassTreeSplitPanel;
//
    private Presenter presenter;
    private final InformationReleasedEventHandler informationDisplayer;
    private final InformationPanel messagePanel;
    private Integer userId;
    private Integer campaignId;
    private Integer documentId;
    private Integer offset;
    private Integer taskId;
    private boolean modified = false;
    private String waterMark;
    //
    private int prevDetailsPanelSize;
    private int prevBottomDockPanelSize;
    private int prevMessageBarSize;
    private int prevStatusBarSize;
    private final MainToolBar mainToolBar;
    private final DocEditingToolBar docEditingToolBar;
    private final PersistedOptionHandler detailPanelWidthHnd = new ApplicationOptions.PersistedOptionHandler("docediting.proppanelwidth");
    private final PersistedOptionHandler annotTableHeightHnd = new ApplicationOptions.PersistedOptionHandler("docediting.annottableheight");

    public DocEditingViewImpl() {

        initWidget(uiBinder.createAndBindUi(this));

        mainToolBar = new MainToolBar("help/AaeUserGuide.html#Editing-view");
        docEditingToolBar = new DocEditingToolBar(this);
        mainToolBar.addWidget(docEditingToolBar);
        toolBarHolder.add(mainToolBar);

        networkActivityDisplayer.setRequestManager(injector.getCoreDataProvider().getRequestManager());

        //Add parametrized global styleSheet
        Element cssElement = GlobalStyles.getInlinedStyleElement();
        cssElement.setId("aae_GlobalDynamicStyles.Block");
        Element oldCssElement = Document.get().getElementById(cssElement.getId());
        if (oldCssElement != null) {
            oldCssElement.removeFromParent();
        }
        RootLayoutPanel.get().getElement().insertFirst(cssElement);

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        messagePanel = new InformationPanel(statusPanel);
        informationDisplayer = new InformationReleasedEventHandler() {
            @Override
            public void onInformationReleased(InformationReleasedEvent event) {
                messagePanel.setMessage(event);
                messagePanel.slide(3000);
            }
        };

        showMessagesBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                messagePanel.slide(1500);
            }
        });
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        //force the split separator to be always visible, even when the view is zoomed
        docTableSplitPanel.setWidgetMinSize(bottomDockPanel, 2);

        //restore panel sizes as set by the user                
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                {
                    Integer detailPanelWidth = detailPanelWidthHnd.getValue();
                    if (detailPanelWidth != null) {
                        detailPanelWidth = Math.min(detailPanelWidth, detailsDocSplitPanel.getElement().getClientWidth() - 20);
                        detailsDocSplitPanel.setWidgetSize(detailsPanel, detailPanelWidth);
                    }
                }
                {
                    Integer annotTableHeight = annotTableHeightHnd.getValue();
                    if (annotTableHeight != null) {
                        annotTableHeight = Math.min(annotTableHeight, docTableSplitPanel.getElement().getClientWidth() - 20);
                        docTableSplitPanel.setWidgetSize(annotTableLayoutPanel, annotTableHeight);
                    }
                }

            }
        });


        annotTableResizeSpy.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                detailPanelWidthHnd.persistValue(detailsPanel.getElement().getClientWidth());
                annotTableHeightHnd.persistValue(event.getHeight());
            }
        });


        RootLayoutPanel.get().removeStyleName(StaneResources.INSTANCE.style().WaitCursor());
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void setDocument(final AnnotatedTextHandler document, boolean readOnly) {

        docEditingToolBar.setDocUrl(null);

        modified = false;
        docEditingToolBar.saveAnnotationsButton.setEnabled(modified);

        docEditingToolBar.nextDocButton.setEnabled(false);
        docEditingToolBar.prevDocButton.setEnabled(false);
        annotationDetailsUI.clearDisplay();
        annotationDetailsUI.setReadOnly(readOnly);
        annotationDetailsUI.setRegisteredAnnotatedText(document);
        annotationTable.setRegisteredAnnotatedText(document);
        getDocumentView().setDocument(document, readOnly);

        if (document != null) {
            //FIXME there must be a way to let GWT compute the actual URL associated to the place including its fragment identifier 

            Properties props = document.getAnnotatedText().getDocument().getProperties();
            String taskName = document.getAnnotatedText().getEditedTask() != null ? document.getAnnotatedText().getEditedTask().getName() : null;
            //Conveniently, the url parameter and the property key are identical !!
            if (taskName != null && props != null && props.getKeys().contains(DocDisplayPlace.BasicDocExtIdParams.DOCEXTERNALID_PARAMNAME)) {

                String token = "#" + DocDisplayPlace.PlacePrefix + ":" + new DocDisplayPlace.BasicDocExtIdParams(props.getValues(DocDisplayPlace.BasicDocExtIdParams.DOCEXTERNALID_PARAMNAME).get(0),
                        taskName,
                        campaignId).createToken();

                docEditingToolBar.setDocUrl(GWT.getModuleBaseURL().replaceFirst("/" + GWT.getModuleName(), "") + token);
            }

            document.setAdditionalAnnotationSetRequestHandler(new AnnotatedTextHandler.AdditionalAnnotationSetRequestHandler() {
                @Override
                public void requestAdditionalAnnotationSet(final AnnotatedTextHandler annotatedTextHandler, int annotationSetId) {

                    HashSet<Integer> annSetIds = new HashSet<Integer>();
                    annSetIds.add(annotationSetId);
                    AsyncCallback<AnnotationSetListImpl> resultCallback = new AsyncCallback<AnnotationSetListImpl>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            log.severe("Problem while requesting addtionnal AnnotationSet!");
                            injector.getMainEventBus().fireEvent(new InformationReleasedEvent("<span style='color:red;'>Problem while requesting addtionnal AnnotationSet!</span>"));
                        }

                        @Override
                        public void onSuccess(AnnotationSetListImpl result) {
                            for (AnnotationSetImpl as : new JsArrayDecorator<AnnotationSetImpl>(result)) {
                                if (!annotatedTextHandler.getLoadedAnnotationSetIds().contains(as.getId())) {
                                    annotatedTextHandler.addAdditionalAnnotationSet(as);
                                }
                            }
                            injector.getMainEventBus().fireEvent(new WorkingDocumentChangedEvent(document, documentUI, WorkingDocumentChangedEvent.ChangeType.AdditionalAnnotationSetLoaded));
                        }
                    };

                    injector.getCoreDataProvider().getAdditionalAnnotationSet(document, annSetIds, resultCallback);
                }
            });
        }

        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                waterMark = getDocumentView().getUndoManager() != null ? getDocumentView().getUndoManager().getWaterMark() : "";
                Integer docId;
                if (document != null) {
                    docId = document.getAnnotatedText().getDocument().getId();
                } else {
                    //stay on previously edited document
                    docId = documentId;
                }
                docEditingToolBar.addDocumentToHistory(documentId);
                docEditingToolBar.resetPrevDocumentId();
                docEditingToolBar.resetNextDocumentId();
            }
        });
    }

    void saveDocument(boolean publish) {
        AnnotatedTextHandler hnd = getDocumentView().getAnnotatedTextHandler();

        docEditingToolBar.saveAnnotationsButton.setEnabled(modified);
        docEditingToolBar.saveAnnotationsButton.setVisible(false);
        docEditingToolBar.finalizeDocButton.setVisible(false);
        AnnotationSetImpl usersAnnotationSet = hnd.getEditableUsersAnnotationSet();
        DetailedAsyncResponseHandler<JavaScriptObject> callback = new DetailedAsyncResponseHandler<JavaScriptObject>() {
            @Override
            public void onFailure(Response response) {
                int statusCode = response.getStatusCode();
                String responseText = response.getText();
                if (statusCode == 422) {
                    Window.alert("Could not save/publish AnnotationSet\n" + responseText);
                } else {
                    log.severe("Problem while saving AnnotationSet!");
                    //FIXME :escape message
                    injector.getMainEventBus().fireEvent(new InformationReleasedEvent("<span style='color:red;'>Problem while saving AnnotationSet!</span>&nbsp;<span>" + responseText + "</span>"));
                }
                docEditingToolBar.saveAnnotationsButton.setVisible(true);
                docEditingToolBar.finalizeDocButton.setVisible(true);
            }

            @Override
            public void onSuccess(JavaScriptObject result) {
                log.info("AnnotationSet Saved!");
                injector.getMainEventBus().fireEvent(new InformationReleasedEvent("Save performed!"));
                docEditingToolBar.saveAnnotationsButton.setVisible(true);
                docEditingToolBar.finalizeDocButton.setVisible(true);
                waterMark = documentUI.getUndoManager().getWaterMark();
                modified = false;
            }
        };

        if (publish) {
            injector.getCoreDataProvider().saveAndPublishAnnotationSet(userId, campaignId, documentId, usersAnnotationSet, callback);
        } else {
            injector.getCoreDataProvider().saveAnnotationSet(userId, campaignId, documentId, usersAnnotationSet, callback);
        }

    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    @Override
    public void setParameters(Integer userId, Integer campaignId, Integer documentId, Integer offset) {
        this.userId = userId;
        this.campaignId = campaignId;
        this.documentId = documentId;
        this.offset = offset;
    }

    @Override
    public void setParameters(Integer userId, Integer campaignId, Integer documentId, Integer offset, Integer taskId) {
        setParameters(userId, campaignId, documentId, offset);
        if (this.taskId != taskId) {
            docEditingToolBar.clearDocumentHistory();
        }
        this.taskId = taskId;
    }

    public DocumentView getDocumentView() {
        return documentUI;
    }

    @Override
    public boolean canCloseView() {
        return !modified;
    }

    @Override
    public void onEditHappened(EditHappenedEvent event) {

        if (event.getEdit().getAnnotatedTextHandler().getAnnotatedText().getDocument().getId() == documentId) {
            //deferred to leave time to the Undo manager to handle the current edit
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    modified = (!waterMark.equals(documentUI.getUndoManager().getWaterMark()));
                    docEditingToolBar.saveAnnotationsButton.setEnabled(modified);
                }
            });
        }
    }

    @Override
    public void onMaximizingWidget(MaximizingWidgetEvent event) {
        if (event.getWidget().equals(documentUI)) {
            if (event.isMaximizing()) {
                //Hide everything except documentUI...

                prevDetailsPanelSize = detailsPanel.getElement().getOffsetWidth();
                prevBottomDockPanelSize = bottomDockPanel.getElement().getOffsetHeight();

                //FIXME : dockPanel unit is EM, but there is no (?) simple way to retrieve size in this unit,
                //BAD :   so use hard coded values instead (they are not likely to change)....
                prevMessageBarSize = 2;
                prevStatusBarSize = 2;
                detailsDocSplitPanel.setWidgetSize(detailsPanel, 0);
                docTableSplitPanel.setWidgetSize(bottomDockPanel, 0);
                dockPanel.setWidgetSize(toolBarHolder, 0);
                dockPanel.setWidgetSize(statusBar, 0);
            } else {
                detailsDocSplitPanel.setWidgetSize(detailsPanel, prevDetailsPanelSize);
                docTableSplitPanel.setWidgetSize(bottomDockPanel, prevBottomDockPanelSize);
                dockPanel.setWidgetSize(toolBarHolder, prevMessageBarSize);
                dockPanel.setWidgetSize(statusBar, prevStatusBarSize);
            }
        }
    }

    @Override
    public void onTermAnnotationsExpositionChanged(TermAnnotationsExpositionEvent event) {
        if (TermAnnotationsExpositionEvent.ChangeType.Available.equals(event.getChangeType())) {
            //give space to the Terminology tree widget if the document can reference external term ressources
            int h = propsSemClassTreeSplitPanel.getElement().getClientHeight() - 120;
            propsSemClassTreeSplitPanel.setWidgetSize(structTermUI, h);
        } else {
            propsSemClassTreeSplitPanel.setWidgetSize(structTermUI, 0);
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        EventBus eventBus = injector.getMainEventBus();
        InformationReleasedEvent.register(eventBus, informationDisplayer);
        EditHappenedEvent.register(eventBus, this);
        MaximizingWidgetEvent.register(eventBus, this);
        TermAnnotationsExpositionEvent.register(eventBus, this);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        InformationReleasedEvent.unregister(informationDisplayer);
        EditHappenedEvent.unregister(this);
        MaximizingWidgetEvent.unregister(this);
        TermAnnotationsExpositionEvent.unregister(this);
    }
}
