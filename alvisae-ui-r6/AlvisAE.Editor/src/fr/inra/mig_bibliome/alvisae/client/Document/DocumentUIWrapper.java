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
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView.Options;
import fr.inra.mig_bibliome.alvisae.client.Events.WorkingDocumentChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.WorkingDocumentChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.StaneCoreResources;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;

/**
 *
 * @author fpapazian
 */
public class DocumentUIWrapper {

	private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

	//exporte les methodes static dans des variables globales accessibles depuis Javascript
	public static native void exportMethods() /*-{
	 $wnd._Stane_createDocumentUI =
	 $entry(@fr.inra.mig_bibliome.alvisae.client.Document.DocumentUIWrapper::createDocumentUI(Ljava/lang/String;Z));
	 $wnd._Stane_loadDocument =
	 $entry(@fr.inra.mig_bibliome.alvisae.client.Document.DocumentUIWrapper::loadDocument(Lfr/inra/mig_bibliome/alvisae/client/Document/DocumentUi;));
	 $wnd._Stane_setDocument =
	 $entry(@fr.inra.mig_bibliome.alvisae.client.Document.DocumentUIWrapper::setDocument(Lfr/inra/mig_bibliome/alvisae/client/Document/DocumentUi;Ljava/lang/String;Ljava/lang/String;));
	 $wnd._Stane_getDocument =
	 $entry(@fr.inra.mig_bibliome.alvisae.client.Document.DocumentUIWrapper::getDocument(Lfr/inra/mig_bibliome/alvisae/client/Document/DocumentUi;));
	 $wnd._Stane_getDocumentHeight = 
	 $entry(@fr.inra.mig_bibliome.alvisae.client.Document.DocumentUIWrapper::getDocumentHeight(Lfr/inra/mig_bibliome/alvisae/client/Document/DocumentUi;));
	 $wnd._Stane_registerDocLoadedEventHandler = 
	 $entry(@fr.inra.mig_bibliome.alvisae.client.Document.DocumentUIWrapper::registerDocLoadedEventHandler(Lfr/inra/mig_bibliome/alvisae/client/Document/DocumentUi;Lcom/google/gwt/core/client/JavaScriptObject;));
	 $wnd._Stane_unregisterDocLoadedEventHandler = 
	 $entry(@fr.inra.mig_bibliome.alvisae.client.Document.DocumentUIWrapper::unregisterDocLoadedEventHandler(Lfr/inra/mig_bibliome/alvisae/client/Events/WorkingDocumentChangedEventHandler;));
	 }-*/;

	private static void prepareDocumentUI() {
		Element e = Document.get().getBody();
		Element cssElement = GlobalStyles.getInlinedStyleElement();
		cssElement.setId("aae_GlobalDynamicStyles.Block");
		Element oldCssElement = Document.get().getElementById(cssElement.getId());
		if (oldCssElement != null) {
			oldCssElement.removeFromParent();
		}
		e.insertFirst(cssElement);
	}

	static final native void invokeNativeHandler(JavaScriptObject nativeHandler, WorkingDocumentChangedEvent event) /*-{
	 nativeHandler(event);
	 }-*/;

	public static WorkingDocumentChangedEventHandler registerDocLoadedEventHandler(final DocumentUi docUI, final JavaScriptObject nativeHandler) {

		WorkingDocumentChangedEventHandler handler = new WorkingDocumentChangedEventHandler() {
			@Override
			public void onWorkingDocumentChanged(WorkingDocumentChangedEvent event) {
				if (event.getDocView()==docUI && WorkingDocumentChangedEvent.ChangeType.Loaded.equals(event.getChangeType())) {
					invokeNativeHandler(nativeHandler, event);
				}
			}
		};
		WorkingDocumentChangedEvent.register(injector.getMainEventBus(), handler);
		return handler;
	}

	public static void unregisterDocLoadedEventHandler(WorkingDocumentChangedEventHandler handler) {
		WorkingDocumentChangedEvent.unregister(handler);
	}

	//
	public static DocumentUi createDocumentUI(String parentNodeId, boolean withScrollpanel) {
		Element e = Document.get().getElementById(parentNodeId);
		if (e != null && e.hasTagName("DIV")) {
			prepareDocumentUI();

			DocumentUi docUI = new DocumentUi(withScrollpanel);
			RootPanel.get(parentNodeId).add(docUI);
			return docUI;
		} else {
			throw new IllegalArgumentException("Unable to instanciate DocumentUI: DIV '" + parentNodeId + "' not found!");
		}
	}

	@Deprecated
	public static void loadDocument(DocumentUi docUI) {
		String json = StaneCoreResources.INSTANCE.jsonData3Test().getText();
		AnnotatedTextImpl newDoc = AnnotatedTextImpl.createFromJSON(json);
		//create Modification Handler
		AnnotatedTextHandler hnd = AnnotatedTextHandler.createHandler(0, 0, newDoc);
		docUI.setDocument(hnd, false);
	}

	public static boolean setDocument(DocumentUi docUI, String jsonStr, String optionStr) {
		boolean result = false;
		if (docUI != null) {
			try {
				Options options = new DocumentUi.Options(optionStr);
				docUI.setDocument(jsonStr, options);
				result = true;
			} catch (Exception e) {
				//error while parsing options
			}
		}
		return result;
	}

	public static String getDocument(DocumentUi docUI) {
		if (docUI != null) {
			AnnotatedText doc = docUI.getDocument();
			return doc.getJSON();
		} else {
			return null;
		}
	}

	public static int getDocumentHeight(DocumentUi docUI) {
		if (docUI != null) {
			return docUI.getDocContainerHeight();
		} else {
			return 0;
		}
	}
}
