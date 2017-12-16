/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Events.InformationReleasedEvent;
import fr.inra.mig_bibliome.alvisae.client.SignIn.ChangePasswordDialog;
import fr.inra.mig_bibliome.alvisae.client.StaneCoreResources;

/**
 *
 * @author fpapazian
 */
public class PasswordUpdater {

    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);

    public static DialogBox alertDialog(final String header, final String content, final Image image) {
        final DialogBox box = new DialogBox();
        final VerticalPanel vpanel = new VerticalPanel();
        box.setText(header);
        final HorizontalPanel hpanel = new HorizontalPanel();
        hpanel.add(image);
        hpanel.add(new Label(" "));
        hpanel.add(new Label(content));
        vpanel.add(hpanel);
        final Button buttonClose = new Button("Close", new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                box.hide();
            }
        });
        // few empty labels to make widget larger
        final Label emptyLabel = new Label("");
        emptyLabel.setSize("auto", "25px");
        vpanel.add(emptyLabel);
        vpanel.add(emptyLabel);
        buttonClose.setWidth("5em");
        vpanel.add(buttonClose);
        vpanel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_CENTER);
        box.add(vpanel);
        return box;
    }

    public static void update(final int userId) {
        ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(new ChangePasswordDialog.ChangePasswordCommand() {

            @Override
            public void changePassword(final String newPassword) {
                Scheduler.get().scheduleDeferred(new Command() {

                    @Override
                    public void execute() {
                        injector.getCoreDataProvider().updateUserPassword(userId, newPassword, new AsyncCallback<JavaScriptObject>() {

                            Image ErrorImage = new Image(StaneCoreResources.INSTANCE.ErrorMessageIcon());
                            Image OkImage = new Image(StaneCoreResources.INSTANCE.OkMessageIcon());

                            @Override
                            public void onFailure(Throwable caught) {
                                injector.getMainEventBus().fireEvent(new InformationReleasedEvent("Unable to change password!"));
                                alertDialog("Changing password", "Unable to change password!", ErrorImage).center();
                            }

                            @Override
                            public void onSuccess(JavaScriptObject result) {
                                injector.getMainEventBus().fireEvent(new InformationReleasedEvent("Password successfully changed!"));
                                alertDialog("Changing password", "Password successfully changed!", OkImage).center();
                            }
                        });
                    }
                });
            }
        });

        changePasswordDialog.center();
    }
}
