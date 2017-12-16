/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextProcessor;
import java.util.List;

/**
 * Popup dialog used to help selection of embedded annotations
 * @author fpapazian
 */
public class SelectAnnotationPopup extends PopupPanel {

    protected static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    interface Binder extends UiBinder<Widget, SelectAnnotationPopup> {
    }
    private static final Binder binder = GWT.create(Binder.class);
    private static final String PROXYFOR_ATTRNAME = "proxyfor";

    public static interface SelectAnnotationPopupTemplates extends SafeHtmlTemplates {

        @Template("&nbsp;<span class='{0}' " + AnnotatedTextProcessor.ANNOTATONID_ATTRNAME + "='{1}' " + GlobalStyles.SelectedAttr + "='{2}' " + PROXYFOR_ATTRNAME + "='{3}' title='{4}'>{5}</span>&nbsp;<br>")
        public SafeHtml annotationProxy(String className, String annotationId, String selected, String markerId, String title, String text);
    }
    private static final SelectAnnotationPopupTemplates TEMPLATES = GWT.create(SelectAnnotationPopupTemplates.class);

    //callback trigerred when Annotation selection is performed
    public static interface SelectingAnnotationCallback {

        //An annotation has been chosen
        public void annotationSelected(String markerId, String annotationId);

        //Selection has been aborted
        public void annotationSelectionAborted();
    }

    interface Styles extends CssResource {
    }
    
    @UiField
    HTML annotationProxy;
    @UiField
    Styles style;
    
    private final SelectingAnnotationCallback selectingAnnotationCallback;
    //set to false as long as the selection as not been propagated to the callback
    private boolean selectionPropagated = true;

    public SelectAnnotationPopup(final String textContainerId, final SelectingAnnotationCallback selectingAnnotationCallback) {
        super(true, true);
        setWidget(binder.createAndBindUi(this));

        setAnimationEnabled(true);
        setGlassEnabled(false);

        this.selectingAnnotationCallback = selectingAnnotationCallback;

        annotationProxy.addClickHandler(new ClickHandler() {

            private Element previoustargetElement = null;

            @Override
            public void onClick(ClickEvent event) {
                NativeEvent ntvEvent = event.getNativeEvent();
                EventTarget evtTarget = ntvEvent.getEventTarget();
                Element targetElement = evtTarget.cast();
                if (!targetElement.equals(previoustargetElement)) {
                    previoustargetElement = targetElement;

                    //find the main selected range mark
                    Element selectedTargetElement = AnnotatedTextProcessor.getFirstEnclosingMarkerElement(targetElement, textContainerId);
                    if (selectedTargetElement != null) {
                        String annotationId = AnnotatedTextProcessor.getAnnotationIdFromMarker(selectedTargetElement);
                        String markerId = selectedTargetElement.getAttribute(PROXYFOR_ATTRNAME);
                
                        //trigger callback
                        selectionPropagated = true;
                        selectingAnnotationCallback.annotationSelected(markerId, annotationId);
                        //close this Annotation Selection popup
                        hide();
                    }
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        selectionPropagated = false;
    }

    @Override
    public void hide(boolean autoClosed) {
        super.hide(autoClosed);
        
        if (!selectionPropagated) {
            //trigger callback if no annotation has been selection 
            selectionPropagated = true;
            selectingAnnotationCallback.annotationSelectionAborted();
        }
    }

    public void setSelectableAnnotation(List<Element> markerAscendant) {
        //display a list of selectable annotation (one by line)
        SafeHtmlBuilder html = new SafeHtmlBuilder();
        for (Element e : markerAscendant) {
            String annotationId = e.getAttribute(AnnotatedTextProcessor.ANNOTATONID_ATTRNAME);
            html.append(TEMPLATES.annotationProxy(e.getClassName(), annotationId, e.getAttribute(GlobalStyles.SelectedAttr), e.getId(), AnnotatedTextProcessor.getBriefId(annotationId), e.getInnerText()));
        }
        annotationProxy.setHTML(html.toSafeHtml());
    }

    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent preview) {
        super.onPreviewNativeEvent(preview);
        NativeEvent evt = preview.getNativeEvent();
        if (evt.getType().equals("keydown")) {
            switch (evt.getKeyCode()) {
                case KeyCodes.KEY_ENTER:
                case KeyCodes.KEY_ESCAPE:
                    hide();
                    break;
            }
        }
    }
}
