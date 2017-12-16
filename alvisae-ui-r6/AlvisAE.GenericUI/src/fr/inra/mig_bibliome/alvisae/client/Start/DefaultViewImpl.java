/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Start;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskSelectingPlace;

/**
 * Default View that displays a lightweight screen containing link to start actual Annotation Editor
 * <br/>
 * (this screen should provide info about the Stane instance)
 * @author fpapazian
 */
public class DefaultViewImpl extends Composite implements DefaultView {

    interface DefaultViewImplUiBinder extends UiBinder<FlowPanel, DefaultViewImpl> {
    }
    private static DefaultViewImplUiBinder uiBinder = GWT.create(DefaultViewImplUiBinder.class);
    //
    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);
    //
    @UiField
    Button start;
    //
    private Presenter presenter;

    public DefaultViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        Element waitBanner = Document.get().getElementById("waitBanner");
        if (waitBanner != null) {
            waitBanner.setAttribute("style", "display:none;");
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setName(String name) {
    }

    @UiHandler("start")
    void onClickStart(ClickEvent e) {
        presenter.goTo(new TaskSelectingPlace(null));
    }
}
