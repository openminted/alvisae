/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data.Retrieve;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.StaneCoreResources;

public class NetworkActivityDisplayer extends Composite implements ApplicationStatusChangedEventHandler {

    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    interface NetworkActivityDisplayerUiBinder extends UiBinder<Widget, NetworkActivityDisplayer> {
    }
    private static NetworkActivityDisplayerUiBinder uiBinder = GWT.create(NetworkActivityDisplayerUiBinder.class);
    @UiField
    Label networkStatusLbl;
    @UiField
    Image networkActivityImg;
    @UiField
    Image networkStatusImg;
    @UiField
    Styles style;
    //
    private RequestManager requestManager = null;

    public interface Styles extends CssResource {
    }

    public NetworkActivityDisplayer() {
        initWidget(uiBinder.createAndBindUi(this));

    }

    @Override
    public void onApplicationStatusChanged(ApplicationStatusChangedEvent event) {
        if (requestManager != null && event.getRequestManager() != null && event.getRequestManager().equals(requestManager)) {
            switch (event.getStatus()) {
                case SignedIn:
                    networkStatusImg.setResource(StaneCoreResources.INSTANCE.NetworkStatusOnIcon());
                    break;
                case SignedOut:
                    networkStatusImg.setResource(StaneCoreResources.INSTANCE.NetworkStatusOffIcon());
                    break;
                case AsynchRequestSent: 
                case AsynchResponseArrived: {
                    int n = requestManager.getNbUnfinishedRequest();
                    if (n > 0) {
                        networkStatusLbl.setText("(" + n + ")");
                        networkActivityImg.setResource(StaneCoreResources.INSTANCE.NetworkActivityOnIcon());
                    } else {
                        networkStatusLbl.setText("");
                        networkActivityImg.setResource(StaneCoreResources.INSTANCE.NetworkActivityOffIcon());
                    }
                    break;
                }
                default:
            }
        }
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
        if (requestManager != null && requestManager.isSignedIn()) {
            networkStatusImg.setResource(StaneCoreResources.INSTANCE.NetworkStatusOnIcon());
        } else {
            networkStatusImg.setResource(StaneCoreResources.INSTANCE.NetworkStatusOffIcon());
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        ApplicationStatusChangedEvent.register(injector.getMainEventBus(), this);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        ApplicationStatusChangedEvent.unregister(this);
    }
}
