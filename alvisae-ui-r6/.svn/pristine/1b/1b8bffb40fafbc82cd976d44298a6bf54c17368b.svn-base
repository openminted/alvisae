/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationDetailsUi;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentUi;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.NetworkActivityDisplayer;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;

/**
 * Main UI of the Annotation Editor, containing the DocumentView and several auxiliary views
 * @author fpapazian
 */
public class Tester extends Composite {
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    interface TesterUiBinder extends UiBinder<DockLayoutPanel, Tester> {
    }
    
    private static TesterUiBinder uiBinder = GWT.create(TesterUiBinder.class);
    @UiField
    DockLayoutPanel dockPanel;
    @UiField
    Label statusLabel;
    @UiField
    DocumentUi documentUI;
    @UiField
    AnnotationDetailsUi annotationDetailsUI;
    @UiField
    PushButton exportButton;
    @UiField
    PushButton svgButton;
    @UiField
    NetworkActivityDisplayer networkActivityDisplayer;
    
    
    
    interface Styles extends CssResource {
    }

    public Tester() {
        
        initWidget(uiBinder.createAndBindUi(this));
        networkActivityDisplayer.setRequestManager(injector.getCoreDataProvider().getRequestManager());

        //Add parametrized global styleSheet
        Element cssElement = GlobalStyles.getInlinedStyleElement();
        cssElement.setId("aae_GlobalDynamicStyles.Block");
        Element oldCssElement = Document.get().getElementById(cssElement.getId());
        if (oldCssElement != null) {
            oldCssElement.removeFromParent();
        }
        RootLayoutPanel.get().getElement().insertFirst(cssElement);

    }

     public void setDocument(final AnnotatedTextHandler document, final DocumentView.Options options) {
        documentUI.setDocument(document, options);
        annotationDetailsUI.setRegisteredAnnotatedText(document);
    }
    
    @UiHandler("exportButton")
    void handleExportAnnotationButtonClick(ClickEvent e) {
        try {
            exportButton.setEnabled(false);
            //String export = injector.getDataProvider().getMapper(documentUI).exportAnchorMarkers();
            String export = documentUI.getDocument().getJSON();
            ExportDialog dlg = new ExportDialog(export);
            dlg.show();
            dlg.center();

        } finally {
            exportButton.setEnabled(true);
        }
    }    
    
    @UiHandler("svgButton")
    void handleSVGButtonClick(ClickEvent e) {
            documentUI.setPrintable(true);
    }    
    
}