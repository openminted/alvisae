/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import fr.inra.mig_bibliome.alvisae.client.Config.History.StanePlaceHistoryMapper;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Start.DefaultPlace;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskSelectingPlace;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.RequestManager;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AlvisaeGenericUI implements EntryPoint {

    private static final String VERSION_LABEL = "v0.6 (build~20140529)";
    private Place defaultPlace = new DefaultPlace("start");
    private SimplePanel appWidget;
    private ApplicationStatusChangedEventHandler applicationStatusDisplayer;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        Window.setTitle(Window.getTitle() + " - " + VERSION_LABEL);

        final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);

        ActivityManager activityManager = new ActivityManager(injector.getAppActivityMapper(), injector.getMainEventBus());
        appWidget = new SimplePanel();
        appWidget.getElement().getStyle().setPosition(Position.ABSOLUTE);
        appWidget.getElement().setId("aae_MainDisplayPanel");
        activityManager.setDisplay(appWidget);

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        StanePlaceHistoryMapper historyMapper = GWT.create(StanePlaceHistoryMapper.class);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);


        if (" DEVMODE".equals("DEVMODE")) {
            //######################################################################
            //Login disabled for dev only
            //TODO enable login dialog for prod version
            defaultPlace = new TaskSelectingPlace(null);
            injector.getCoreDataProvider().getRequestManager().setAutoSignedIn();
            //######################################################################
        } else {
            //Init the URL to access the REST web services relatively to the URL of the web application

            // Prod URL
           //
            injector.getCoreDataProvider().getRequestManager().setServerBaseUrl(GWT.getModuleBaseURL() + "../../api/");

          
                  
           // injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://localhost:8080/alvisae/quaerobiotopes/api/");
           
            //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://bibliome.jouy.inra.fr/demo/alvisae/quaerobiotopes/api/");
           
            //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://cl30.dbcls.jp:9080/alvisae/fpa/api/");
            //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://cl30.dbcls.jp:9080/alvisae/ontobiodemo/api/");

            //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://cl30.dbcls.jp:9080/alvisae/csms/api/");

            //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("https://bibliome.jouy.inra.fr/alvisae/fpa2/api/");
            
           // injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://cl30.dbcls.jp:9080/alvisae/tv05/api/");
           //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://bibdev:8580/alvisae/tv05/api/");
            //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://127.0.0.1:8080/alvisae/tv05/api/");
            //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://127.0.0.1:8080/api/");
            //injector.getRequestManager().setServerBaseUrl("http://bibweb:8580/alvisae/bi/api/");
           
              
   //    injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://bibweb:8580/alvisae/fpa2/api/");
       //    injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://bibweb:8580/alvisae/biotopes-quaero2/api/");
          //  injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("https://bibliome.jouy.inra.fr/alvisae/gchp2e/api/");
            
            //          injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://bibdev:8580/alvisae/bi2013/api/");
           
            //injector.getRequestManager().setServerBaseUrl("http://bibweb:8580/alvisae/fsov/api/");
            //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://bibweb:8580/alvisae/demo/api/");
            //injector.getRequestManager().setServerBaseUrl("http://bibweb:8580/alvisae/dialecti/api/");
            //injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("http://bibdev:8580/alvisae/fpa/api/");
            //injector.getRequestManager().setServerBaseUrl("http://bibliome.jouy.inra.fr/test/stane/dev/api/");
        }

        historyHandler.register(injector.getPlaceController(), injector.getMainEventBus(), defaultPlace);
        RootLayoutPanel.get().add(appWidget);

        //Global listener wich display a wait banner when the application is engaged is a long processing 
        final DivElement waitBanner = (DivElement) Document.get().getElementById("waitBanner");
        waitBanner.setInnerHTML("&nbsp;&nbsp; processing");

        applicationStatusDisplayer = new ApplicationStatusChangedEventHandler() {

            @Override
            public void onApplicationStatusChanged(final ApplicationStatusChangedEvent event) {
                RequestManager requestManager = injector.getCoreDataProvider().getRequestManager();
                if (requestManager != null && event.getRequestManager() != null && event.getRequestManager().equals(requestManager)) {
                    Scheduler.get().scheduleFinally(new Command() {

                        @Override
                        public void execute() {
                            if (event.getStatus().equals(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle)) {
                                //hide wait banner
                                if (waitBanner != null) {
                                    waitBanner.getStyle().setDisplay(Style.Display.NONE);
                                }
                                RootLayoutPanel.get().removeStyleName(StaneResources.INSTANCE.style().WaitCursor());
                            } else if (event.getStatus().equals(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing)) {
                                //display wait banner
                                if (waitBanner != null) {
                                    waitBanner.getStyle().clearDisplay();
                                }
                                RootLayoutPanel.get().addStyleName(StaneResources.INSTANCE.style().WaitCursor());
                            }
                        }
                    });
                }
            }
        };
        ApplicationStatusChangedEvent.register(injector.getMainEventBus(), applicationStatusDisplayer);

        // Goes to the place represented on URL else default place
        historyHandler.handleCurrentHistory();
    }
}
