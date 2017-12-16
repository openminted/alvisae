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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.data.AuthenticationInfoImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AlvisaeCore implements EntryPoint {

    //
    public static native void speedTracerlog(String msg) /*-{
    var logger = $wnd.console;
    if(logger && logger.markTimeline) {
    logger.markTimeline(msg); 
    }
    }-*/;
    
    //
    public static native void firebugStartTiming(String token) /*-{
    var cons = $wnd.console;
    if(cons) {
    cons.time(token); 
    }
    }-*/;

    //
    public static native void firebugEndTiming(String token) /*-{
    var cons = $wnd.console;
    if(cons) {
    cons.timeEnd(token); 
    }
    }-*/;

    public static native void firebugLog(String message) /*-{
    var cons = $wnd.console;
    if(cons) {
    cons.log(message); 
    }
    }-*/;
    
    public static native void firebugStartGroup(String token) /*-{
    var cons = $wnd.console;
    if(cons) {
    cons.group(token); 
    }
    }-*/;
    
    public static native void firebugEndGroup(String token) /*-{
    var cons = $wnd.console;
    if(cons) {
    cons.groupEnd(token); 
    }
    }-*/;
    
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        Element testContainer = Document.get().getElementById("StaneCoreTester");
        if (testContainer != null) {

            String json = StaneCoreResources.INSTANCE.jsonData3Test().getText();
            GWT.log(json);
            
            AnnotatedTextImpl newDoc = AnnotatedTextImpl.createFromJSON(json);
            newDoc.checkStructure();
                        
            final Label label = new Label("Hello, GWT!!!");
            final TextBox field = new TextBox();
            field.setText("some value");
            final Button button = new Button("encode64");
            RootPanel.get("StaneCoreTester").add(field);
            RootPanel.get("StaneCoreTester").add(button);
            RootPanel.get("StaneCoreTester").add(label);

            button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                         label.setText(AuthenticationInfoImpl.base64Encode(field.getText()));
                }
            });

        }
    }
}
