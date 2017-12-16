/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * Simple dialog displaying the result of XML export in a textarea
 * @author fpapazian
 */
public class ExportDialog extends PopupPanel {

    interface Binder extends UiBinder<Widget, ExportDialog> {
    }
    private static final Binder binder = GWT.create(Binder.class);
    @UiField
    Button closeButton;
    @UiField
    TextArea rawTextPanel;

    public ExportDialog(String rawExport) {
        super(true);
        setSize(String.valueOf(Window.getClientWidth() / 10 * 6) + "px", String.valueOf(Window.getClientHeight() / 10 * 6) + "px");
        setWidget(binder.createAndBindUi(this));

        setAnimationEnabled(true);
        setGlassEnabled(true);
        rawTextPanel.setText(rawExport);

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
    void onSignOutClicked(ClickEvent event) {
        hide();
    }
}
