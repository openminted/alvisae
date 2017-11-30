/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Start;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;
import fr.inra.mig_bibliome.alvisae.client.About.AboutStaneDialog;
import fr.inra.mig_bibliome.alvisae.client.Campaign.CampaignAssignmentPlace;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocParams;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.StaneCoreResources;
import fr.inra.mig_bibliome.alvisae.client.StaneResources;
import fr.inra.mig_bibliome.alvisae.client.User.PasswordUpdater;
import fr.inra.mig_bibliome.alvisae.client.User.UserManagingParams;
import fr.inra.mig_bibliome.alvisae.client.User.UserManagingPlace;

/**
 *
 * @author fpapazian
 */
public class MainToolBar extends Composite {

    private static MainToolBarUiBinder uiBinder = GWT.create(MainToolBarUiBinder.class);
    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);

    static interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<div title='{0}'>{1}</div>")
        public SafeHtml aboutLink(String title, SafeHtml icon);

        @SafeHtmlTemplates.Template("<a href='{0}' target='_blank' title='{1}'>{2}</a> ")
        public SafeHtml helpLink(String url, String title, SafeHtml icon);
    }
    static final Templates TEMPLATES = GWT.create(Templates.class);

    interface MainToolBarUiBinder extends UiBinder<Widget, MainToolBar> {
    }
    @UiField
    SimplePanel embeddedWidgetHolder;
    @UiField
    MenuBar mainMenu;
    @UiField
    MenuItem userActionMenuItem;
    @UiField
    MenuItem helpMenuItem;
    @UiField
    MenuItem aboutMenuItem;

    //
    public MainToolBar(String helpPageUrl) {
        initWidget(uiBinder.createAndBindUi(this));


        SafeHtmlBuilder shbuilder = new SafeHtmlBuilder();
        shbuilder.append(SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneCoreResources.INSTANCE.DropDownIcon()).getHTML()));
        shbuilder.appendHtmlConstant("&nbsp;");
        shbuilder.appendEscaped(injector.getCoreDataProvider().getRequestManager().getCurrentUserName());

        userActionMenuItem.setHTML(shbuilder.toSafeHtml());
        MenuBar subMenuBar = new MenuBar(true);

        shbuilder = new SafeHtmlBuilder();
        shbuilder.append(SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneCoreResources.INSTANCE.SigningOutIcon()).getHTML())).appendEscaped("  Sign-out");
        MenuItem item = subMenuBar.addItem(shbuilder.toSafeHtml(), new Command() {
            @Override
            public void execute() {
                signOut();
            }
        });

        shbuilder = new SafeHtmlBuilder();
        shbuilder.append(SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneCoreResources.INSTANCE.PasswordIcon()).getHTML())).appendEscaped("  Change password");
        item = subMenuBar.addItem(shbuilder.toSafeHtml(), new Command() {
            @Override
            public void execute() {
                changeUserPassword();
            }
        });

        shbuilder = new SafeHtmlBuilder();
        shbuilder.append(SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneCoreResources.INSTANCE.ManageUsersIcon()).getHTML())).appendEscaped("  Manage users");
        item = subMenuBar.addItem(shbuilder.toSafeHtml(), new Command() {
            @Override
            public void execute() {
                manageUsers();
            }
        });

        shbuilder = new SafeHtmlBuilder();
        shbuilder.append(SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.AssignDocIcon()).getHTML())).appendEscaped("  Assignments");
        item = subMenuBar.addItem(shbuilder.toSafeHtml(), new Command() {
            @Override
            public void execute() {
                manageAssignments();
            }
        });
        userActionMenuItem.setSubMenu(subMenuBar);


        aboutMenuItem.setHTML(TEMPLATES.aboutLink("About AlvisAE", SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.AboutIcon()).getHTML())));
        aboutMenuItem.setCommand(new Command() {
            @Override
            public void execute() {
                AboutStaneDialog dlg = new AboutStaneDialog();
                dlg.show();
                dlg.center();
            }
        });

        helpMenuItem.setHTML(TEMPLATES.helpLink(helpPageUrl, "Help", SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.HelpIcon()).getHTML())));

    }

    public void addWidget(Widget embedded) {
        embeddedWidgetHolder.add(embedded);
    }

    private void signOut() {
        injector.getCoreDataProvider().getRequestManager().signOut(null);
        mainMenu.setTitle("");
        injector.getPlaceController().goTo(new DefaultPlace(null));
    }

    private void changeUserPassword() {
        PasswordUpdater.update(injector.getCoreDataProvider().getRequestManager().getCurrentUserId());
    }

    private void manageUsers() {
        injector.getPlaceController().goTo(new UserManagingPlace(new UserManagingParams(injector.getCoreDataProvider().getRequestManager().getCurrentUserId())));
    }

    private void manageAssignments() {
        injector.getPlaceController().goTo(new CampaignAssignmentPlace(new BasicUserCampaignDocParams(injector.getCoreDataProvider().getRequestManager().getCurrentUserId(), null, null)));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        //FIXME: not I18N
        userActionMenuItem.setTitle("logged as: " + injector.getCoreDataProvider().getRequestManager().getCurrentUserName());
    }
}
