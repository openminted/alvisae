/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document.Validation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;

/**
 * @author fpapazian
 */
public class ValidationResultDialog extends PopupPanel {

    public static interface AnnotationSelectionHandler {

        public void annotationSelected(String annotationId);
    }

    interface Binder extends UiBinder<Widget, ValidationResultDialog> {
    }
    private static final Binder binder = GWT.create(Binder.class);
    @UiField
    Button closeButton;
    @UiField
    HTML htmlPanel;
    private final AnnotationSelectionHandler selectHandler;
    private final ClickHandler clickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            NativeEvent nevent = event.getNativeEvent();
            EventTarget evtTarget = nevent.getEventTarget();
            Element targetElement = evtTarget.cast();
            if (targetElement.getClassName().contains(ClientFaultHTMLMessages.ANNIDREF_CLASSVAL)) {
                if (selectHandler != null) {
                    String annotationId = targetElement.getInnerText();
                    hide();
                    selectHandler.annotationSelected(annotationId);
                }
            }
        }
    };

    public ValidationResultDialog(AnnotationSelectionHandler selectHandler, List<SafeHtml> messages) {
        super(true);
        this.selectHandler = selectHandler;
        setSize(String.valueOf(Window.getClientWidth() / 10 * 6) + "px", String.valueOf(Window.getClientHeight() / 10 * 6) + "px");
        setWidget(binder.createAndBindUi(this));

        setAnimationEnabled(true);
        setGlassEnabled(true);

        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        for (SafeHtml m : messages) {
            sb.appendHtmlConstant("<div>").append(m).appendHtmlConstant("<br></div>");
        }
        htmlPanel.addClickHandler(clickHandler);

        htmlPanel.setHTML(sb.toSafeHtml());
    }

    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent preview) {
        super.onPreviewNativeEvent(preview);

        NativeEvent evt = preview.getNativeEvent();
        if (evt.getType().equals("keydown")) {
            // Use the popup's key preview hooks to close the dialog when either
            // enter or escape is pressed.
            switch (evt.getKeyCode()) {
                case KeyCodes.KEY_ENTER:
                case KeyCodes.KEY_ESCAPE:
                    hide();
                    break;
            }
        }
    }

    @UiHandler("closeButton")
    void oncloseButtonClicked(ClickEvent event) {
        hide();
    }
}
