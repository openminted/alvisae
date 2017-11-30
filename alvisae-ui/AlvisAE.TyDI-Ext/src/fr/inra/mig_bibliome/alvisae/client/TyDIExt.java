/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TermAnnotationsExpositionEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.ResourceLocator;

/**
 * Entry point classes define
 * <code>onModuleLoad()</code>.
 */
public class TyDIExt implements EntryPoint {

    //
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {

        Element testContainer = Document.get().getElementById("TerminologyShapingWidgetTester");
        //testContainer is a placeholder for 
        if (testContainer != null) {

            GWT.log("Creating widget");
            TesterStructTerm appWidget = new TesterStructTerm();
            RootLayoutPanel.get().add(appWidget);

            //
            GWT.log("Preparing locator");
            //ResourceLocator locator = new ResourceLocator("http://127.0.0.1:8080/projects/10360305/");
            //ResourceLocator locator = new ResourceLocator("http://bibliome.jouy.inra.fr/test/tydiws/rest/pharmaco/projects/6/");
            //ResourceLocator locator = new ResourceLocator("http://cl30.dbcls.jp:9080/tydiws/rest/training/projects/10360305/");
            //ResourceLocator locator = new ResourceLocator("http://bibliome.jouy.inra.fr/test/tydiws/rest/ontobiodemo/projects/8725573/");
            //ResourceLocator locator = new ResourceLocator("http://bibliome.jouy.inra.fr/test/tydiws/rest/training/projects/10360305/");
 
            //ResourceLocator locator = new ResourceLocator("http://bibliome.jouy.inra.fr/test/tydiws/rest/pharmaco/projects/6/");
            //ResourceLocator locator = new ResourceLocator("https://bibliome.jouy.inra.fr/tydirws/fpa2/projects/2294/");
            ResourceLocator locator = new ResourceLocator("https://bibliome.jouy.inra.fr/tydirws/gchp2e/projects/4/");
            GWT.log("locator " + locator.getTyDIProjectId() + " " + locator.getTyDIInstanceBaseUrl());

            injector.getMainEventBus().fireEvent(new TermAnnotationsExpositionEvent(TermAnnotationsExpositionEvent.ChangeType.Available, locator));
        }
    }
}
