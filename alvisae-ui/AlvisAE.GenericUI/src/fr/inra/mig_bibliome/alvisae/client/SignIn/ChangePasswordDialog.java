/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SignIn;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import java.util.ArrayList;

/**
 * Dialog used to collect user login name and password for authentication
 * purposes
 *
 * @author fpapazian
 */
public class ChangePasswordDialog extends DialogBox implements ChangeHandler {

    public static interface ChangePasswordCommand {
        void changePassword(String newPassword);
    }
    
    interface Binder extends UiBinder<Widget, ChangePasswordDialog> {
    }
    private static final Binder binder = GWT.create(Binder.class);
    @UiField
    public PasswordTextBox password_1;
    @UiField
    public PasswordTextBox password_2;
    @UiField
    Button changeButton;
    private ChangePasswordCommand applyCommand;
    private ArrayList<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

    public ChangePasswordDialog(ChangePasswordCommand applyCommand) {
        super(true, true);
        setText("Change password...");
        setWidget(binder.createAndBindUi(this));
        this.applyCommand = applyCommand;
        
        handlers.add(password_1.addChangeHandler(this));
        handlers.add(password_2.addChangeHandler(this));
        password_1.setFocus(true);
        checkValidPassword();
    }

    @Override
    public void onChange(ChangeEvent event) {
        checkValidPassword();
    }

    private boolean checkValidPassword() {
        boolean valid = !password_1.getText().isEmpty() && !password_2.getText().isEmpty() && password_1.getText().equals(password_2.getText());
        changeButton.setEnabled(valid);
        return valid;
    }

    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent preview) {
        super.onPreviewNativeEvent(preview);

        NativeEvent evt = preview.getNativeEvent();
        if (evt.getType().equals("keydown")) {
            switch (evt.getKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    onChangeButtonClicked(null);
                    break;
                case KeyCodes.KEY_ESCAPE:
                    break;
            }
        }
    }

    @UiHandler("changeButton")
    void onChangeButtonClicked(ClickEvent event) {
        if (checkValidPassword()) {
            hide();
            for (HandlerRegistration h : handlers) {
                h.removeHandler();
            }
            handlers.clear();
            applyCommand.changePassword(password_1.getText());
        }
    }
}
