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
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentUIWrapper;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;

/**
 * Entry point classes define
 * <code>onModuleLoad()</code>.
 */
public class AlvisaeEditor implements EntryPoint {

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        DocumentUIWrapper.exportMethods();
        StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

        /* ------------------------------------------------------------------ */
        Element testContainer = Document.get().getElementById("StanEditorTester");
        if (testContainer != null) {

             injector.getCoreDataProvider().getRequestManager().setAutoSignedIn();
             Tester testerview = new Tester();
            
             //
             String json = StaneCoreResources.INSTANCE.jsonData3Test().getText();
             AnnotatedTextImpl newDoc = AnnotatedTextImpl.createFromJSON(json);
            
             //create Modification Handler
             AnnotatedTextHandler handler = null;
             handler = AnnotatedTextHandler.createHandler(0, 0, newDoc);
            
             testerview.setDocument(handler, new DocumentView.Options(false, false, false, false, false, true, null, "6f211019-45e4-4b48-ac43-90000dc09cac"));

            // RootPanel.get("StanEditorTester").add(testerview);
            RootLayoutPanel.get().add(testerview);
        }


        /* ------------------------------------------------------------------ */
//        final AnnSetCompare testerview = new AnnSetCompare();
//        RootLayoutPanel.get().add(testerview);
//        injector.getCoreDataProvider().getRequestManager().setServerBaseUrl("https://bibliome.jouy.inra.fr/alvisae/biotopes-quaero/api/");
//        injector.getCoreDataProvider().getRequestManager().signIn("claire", "demo", new AsyncResponseHandler<AuthenticationInfo>() {
//            @Override
//            public void onSuccess(AuthenticationInfo result) {
//                GWT.log("Signed in as " + result.toString());
//                List<Integer> userIds = new ArrayList<Integer>();
//                userIds.add(4);
//                userIds.add(5);
//                testerview.setParams(new BasicTaskReviewParams(2, 11, 4688, 0, 27, userIds, 26));
//                testerview.proceed();
//            }
//
//            @Override
//            public void onFailure(Throwable caught) {
//                ResultMessageDialog d = new ResultMessageDialog(ResultMessageDialog.Error, "Unable to sign-in", caught.getMessage());
//                d.show();
//                GWT.log("Could not Signin");
//            }
//        });

        /* ------------------------------------------------------------------ */


    }
}
