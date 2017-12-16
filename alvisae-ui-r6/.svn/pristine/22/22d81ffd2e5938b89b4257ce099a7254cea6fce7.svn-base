/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SignIn;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Start.DefaultPlace;
import fr.inra.mig_bibliome.alvisae.client.StaneResources;

/**
 * Dialog used to collect user login name and password for authentication purposes
 * @author fpapazian
 */
public class SignInDialog extends DialogBox implements SignInView {

    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);

    interface Binder extends UiBinder<Widget, SignInDialog> {
    }
    private static final Binder binder = GWT.create(Binder.class);
    @UiField
    TextBox loginName;
    @UiField
    PasswordTextBox password;
    @UiField
    Button loginButton;
    private Command successFullLoginCommand;
    private String name;
    private Presenter presenter;

    public SignInDialog() {
        super(false, false);
        setText("Sign-in...");
        setWidget(binder.createAndBindUi(this));
        
        /* Tried autocomplete=on to enable the browser to save password, but it it not enough as explained there :
        http://groups.google.com/group/google-web-toolkit/browse_frm/thread/2b2ce0b6aaa82461/088048aa1040a5ee?lnk=gst&q=login+form#088048aa1040a5ee
        password.getElement().setAttribute("autocomplete", "on");
        */
                
        String lastLoginName =  injector.getCoreDataProvider().getRequestManager().getApplicationOptions().getLastLoginName();
        if (lastLoginName != null && !lastLoginName.isEmpty()) {
            loginName.setText(lastLoginName);
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setSuccessfulLoginCommand(Command successFullLoginCommand) {
        this.successFullLoginCommand = successFullLoginCommand;
    }

    @Override
    public void startView() {
        center();
        loginName.setEnabled(true);
        password.setEnabled(true);
        loginButton.setEnabled(true);
        if (loginName.getText().isEmpty()) {
            loginName.setFocus(true);
        } else {
            password.setFocus(true);
        }

        RootLayoutPanel.get().addStyleName("DefaultCursor");
    }

    @Override
    public void stopView() {
        hide();
    }

    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent preview) {
        super.onPreviewNativeEvent(preview);

        NativeEvent evt = preview.getNativeEvent();
        if (evt.getType().equals("keydown")) {
            switch (evt.getKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    onloginClicked(null);
                    break;
                case KeyCodes.KEY_ESCAPE:
                    presenter.goTo(new DefaultPlace("start"));
                    break;
            }
        }
    }

    @UiHandler("loginButton")
    void onloginClicked(ClickEvent event) {
        if (loginName.getText().isEmpty()) {
            loginName.setFocus(true);
        } else if (password.getText().isEmpty()) {
            password.setFocus(true);
        } else {
            loginName.setEnabled(false);
            password.setEnabled(false);
            loginButton.setEnabled(false);
            RootLayoutPanel.get().addStyleName(StaneResources.INSTANCE.style().WaitCursor());
            stopView();
            Scheduler.get().scheduleDeferred(successFullLoginCommand);
        }
    }

    @Override
    public String getLoginName() {
        return loginName.getText();
    }

    @Override
    public String getPassword() {
        return password.getText();
    }
}
