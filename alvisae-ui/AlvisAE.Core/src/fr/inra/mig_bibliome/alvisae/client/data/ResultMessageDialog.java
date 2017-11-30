/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import fr.inra.mig_bibliome.alvisae.client.StaneCoreResources;

/**
 *
 * @author fpapazian
 */
public class ResultMessageDialog extends DialogBox {

    interface Binder extends UiBinder<Widget, ResultMessageDialog> {
    }
    private static final Binder binder = GWT.create(Binder.class);
    public static final int Ok = 0;
    public static final int Warn = 1;
    public static final int Error = 2;

    interface Styles extends CssResource {
       
    }

    @UiField
    PushButton OkBtn;
    @UiField
    Image icon;
    @UiField
    Label panel;

    public ResultMessageDialog(int dialogType, String title, String message) {
        super(false, true);
        setWidget(binder.createAndBindUi(this));

        setAnimationEnabled(true);
        setGlassEnabled(true);

        switch (dialogType) {
            case Ok:
                icon.setResource(StaneCoreResources.INSTANCE.OkMessageIcon());
                break;
            case Warn:
                icon.setResource(StaneCoreResources.INSTANCE.WarnMessageIcon());
                break;
            case Error:
                icon.setResource(StaneCoreResources.INSTANCE.ErrorMessageIcon());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported dialog type argument:" + dialogType);

        }

        setText(title);
        panel.setText(message);
        panel.setWordWrap(true);
    }

    @Override
    public void show() {
        super.show();
        center();
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

    @UiHandler("OkBtn")
    void onSignOutClicked(ClickEvent event) {
        hide();
    }
}
