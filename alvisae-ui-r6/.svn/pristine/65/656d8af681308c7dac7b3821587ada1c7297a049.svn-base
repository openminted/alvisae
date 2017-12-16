/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Conso;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicTaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocOffsetTaskParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams.TaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Conso.AnnotationSetConsoView.Presenter;
import fr.inra.mig_bibliome.alvisae.client.Start.MainToolBar;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskSelectingPlace;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.NetworkActivityDisplayer;

/**
 *
 * @author fpapazian
 */
public class AnnotationSetConsoViewImpl extends Composite {

    interface AnnotationSetConsoViewImplUiBinder extends UiBinder<DockLayoutPanel, AnnotationSetConsoViewImpl> {
    }
    private static AnnotationSetConsoViewImplUiBinder uiBinder = GWT.create(AnnotationSetConsoViewImplUiBinder.class);
    
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    
    @UiField
    Panel toolBarHolder;
    @UiField
    AnnSetCompare annSetCompare;
    @UiField
    NetworkActivityDisplayer networkActivityDisplayer;
    
    //
    private Presenter presenter;

    public AnnotationSetConsoViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        toolBarHolder.add(new MainToolBar("help/AaeUserGuide.html#Annotations-Consolidation-Review"));
        networkActivityDisplayer.setRequestManager(injector.getCoreDataProvider().getRequestManager());
    }

    void setParams(TaskReviewParams params) {
        annSetCompare.setParams(params);
    }

    void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    void proceed() {
        annSetCompare.proceed();
    }

    boolean canCloseView() {
        return annSetCompare.canCloseView();
    }

    @UiHandler("gotoDocListButton")
    void handleGotoDocListButtonClick(ClickEvent e) {
        BasicTaskReviewParams p = annSetCompare.getParams();
        presenter.goTo(new TaskSelectingPlace(new BasicUserCampaignDocOffsetTaskParams(p.getUserId(), p.getCampaignId(), p.getDocumentId(), 0, p.getTaskId())));
    }
}
