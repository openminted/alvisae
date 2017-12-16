/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.User;

import com.google.gwt.cell.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Response;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.data.DetailedAsyncCallback;
import fr.inra.mig_bibliome.alvisae.client.data3.*;
import java.util.*;

/**
 * Main UI of the Annotation Editor, containing the DocumentView and several
 * auxiliary views
 *
 * @author fpapazian
 */
public class UserManager extends Composite implements UserManagingView {

    interface UserManagerUiBinder extends UiBinder<DockLayoutPanel, UserManager> {
    }
    private static UserManagerUiBinder uiBinder = GWT.create(UserManagerUiBinder.class);
    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static class DisableableButtonCell extends ButtonCell {

        @Override
        public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
            //do not create the button if the data is null
            if (data != null) {
                super.render(context, data, sb);
            }
        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static class CampaignAuthsInfo {

        public static ProvidesKey<CampaignAuthsInfo> KEY_PROVIDER = new ProvidesKey<CampaignAuthsInfo>() {

            @Override
            public Object getKey(CampaignAuthsInfo item) {
                return item == null ? null : item.getCampaignId();
            }
        };
        private final int campaign_id;
        private final UserAuthorizationsImpl userAuths;
        private Set<Integer> editedAuths = null;

        public CampaignAuthsInfo(int campaign_id, UserAuthorizationsImpl userAuths) {
            this.campaign_id = campaign_id;
            this.userAuths = userAuths;
        }

        public int getCampaignId() {
            return campaign_id;
        }

        public UserAuthorizationsImpl getOriginalUserAuths() {
            return userAuths;
        }

        public void changeAuth(int auths, boolean enabled) {
            if (editedAuths == null) {
                editedAuths = new HashSet<Integer>();
                editedAuths.addAll(userAuths.getAuthsByCampaign(campaign_id));
            }
            if (enabled) {
                editedAuths.add(auths);
            } else {
                editedAuths.remove(auths);
            }
        }

        public Set<Integer> getEditedUserAuths() {
            return editedAuths;
        }

        public boolean hasChanged() {
            return editedAuths != null;
        }
    }

    public interface UserAuthsTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span title='{1}'>{0}</span>")
        public SafeHtml spanWithTitle(String text, String title);
    }
    private static final UserAuthsTemplates TEMPLATES = GWT.create(UserAuthsTemplates.class);
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    interface Styles extends CssResource {
    }
    //
    private Presenter presenter;
    private Integer selectedUserId = null;
    private final List<EditableExtendedUserInfoImpl> userInfos = new ArrayList<EditableExtendedUserInfoImpl>();
    private final ListDataProvider<EditableExtendedUserInfoImpl> userInfoProvider;
    private final SingleSelectionModel<EditableExtendedUserInfoImpl> selectionModel;
    private final Set<EditableExtendedUserInfoImpl> unsavedModifiedUsers = new HashSet<EditableExtendedUserInfoImpl>();
    private EditableExtendedUserInfoImpl prevSelectedUser = null;
    private EditableExtendedUserInfoImpl currentSelectedUser = null;
    //
    private Map<Integer, String> campaigns = new HashMap<Integer, String>();
    private AuthorizationListImpl authorizations = null;
    private final ListDataProvider<CampaignAuthsInfo> authsDataProvider;
    private boolean authsEdited = false;
    //
    @UiField(provided = true)
    DataGrid<EditableExtendedUserInfoImpl> userGrid;
    @UiField
    PushButton addUserButton;
    @UiField
    PushButton cancelButton;
    @UiField(provided = true)
    DataGrid<CampaignAuthsInfo> authGrid;

    public UserManager() {
        userGrid = new DataGrid<EditableExtendedUserInfoImpl>();
        authGrid = new DataGrid<CampaignAuthsInfo>();
        initWidget(uiBinder.createAndBindUi(this));

        selectionModel = new SingleSelectionModel<EditableExtendedUserInfoImpl>(EditableExtendedUserInfoImpl.KEY_PROVIDER);
        userGrid.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                prevSelectedUser = currentSelectedUser;
                currentSelectedUser = selectionModel.getSelectedObject();
                refreshUserAuthslist();
            }
        });

        userInfoProvider = new ListDataProvider<EditableExtendedUserInfoImpl>(EditableExtendedUserInfoImpl.KEY_PROVIDER);
        authsDataProvider = new ListDataProvider<CampaignAuthsInfo>(CampaignAuthsInfo.KEY_PROVIDER);

        initUserTable();
        initAuthTable();
        refreshAll();

    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setParams(UserManagingParams params) {
        if (selectedUserId != null) {
            selectedUserId = params.getUserId();
        }
    }

    @Override
    public UserManagingParams getParams() {
        return new UserManagingParams(selectedUserId);
    }

    private void initUserTable() {

        userInfoProvider.addDataDisplay(userGrid);
        userGrid.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);



        Column<EditableExtendedUserInfoImpl, String> idColumn =
                new Column<EditableExtendedUserInfoImpl, String>(new TextCell()) {

                    @Override
                    public String getValue(EditableExtendedUserInfoImpl object) {
                        return String.valueOf(object.getId());
                    }
                };
        userGrid.addColumn(idColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Id")));
        // - - -

        Column<EditableExtendedUserInfoImpl, String> loginColumn =
                new Column<EditableExtendedUserInfoImpl, String>(new EditTextCell()) {

                    @Override
                    public String getValue(EditableExtendedUserInfoImpl object) {
                        return String.valueOf(object.getDisplayName());
                    }
                };

        FieldUpdater<EditableExtendedUserInfoImpl, String> loginFieldUpd = new FieldUpdater<EditableExtendedUserInfoImpl, String>() {

            @Override
            public void update(int index, EditableExtendedUserInfoImpl object, String value) {
                String newLogin = value.trim();
                if (!newLogin.isEmpty() && !newLogin.equals(object.getDisplayName())) {
                    object.setDisplayName(newLogin);
                    object.setPersisted(false);
                    unsavedModifiedUsers.add(object);
                    userGrid.redraw();
                }

            }
        };
        loginColumn.setFieldUpdater(loginFieldUpd);
        userGrid.addColumn(loginColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Login")));
        // - - -

        Column<EditableExtendedUserInfoImpl, Boolean> isAdminColumn =
                new Column<EditableExtendedUserInfoImpl, Boolean>(new CheckboxCell(true, false)) {

                    @Override
                    public Boolean getValue(EditableExtendedUserInfoImpl object) {
                        return object.isAdmin();
                    }
                };
        FieldUpdater<EditableExtendedUserInfoImpl, Boolean> isAdminFieldUpd = new FieldUpdater<EditableExtendedUserInfoImpl, Boolean>() {

            @Override
            public void update(int index, EditableExtendedUserInfoImpl object, Boolean value) {
                object.setAdmin(value);
                object.setPersisted(false);
                unsavedModifiedUsers.add(object);
                userGrid.redraw();

            }
        };
        isAdminColumn.setFieldUpdater(isAdminFieldUpd);
        userGrid.addColumn(isAdminColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("is Admin")));

        // - - -

        Column<EditableExtendedUserInfoImpl, String> saveBtnColumn = new Column<EditableExtendedUserInfoImpl, String>(new DisableableButtonCell()) {

            @Override
            public String getValue(EditableExtendedUserInfoImpl object) {
                if (object.isPersisted()) {
                    return null;
                } else {
                    return "Save changes";
                }
            }
        };
        FieldUpdater<EditableExtendedUserInfoImpl, String> saveBtnFieldUpd = new FieldUpdater<EditableExtendedUserInfoImpl, String>() {

            @Override
            public void update(final int index, final EditableExtendedUserInfoImpl object, String value) {
                if (object.getId() != -1) {

                    injector.getCoreDataProvider().updateUser(object.getId(), object.getDisplayName(), object.isAdmin(), new DetailedAsyncCallback<ExtendedUserInfoImpl>() {

                        @Override
                        public void onSuccess(ExtendedUserInfoImpl result) {
                            userInfoProvider.getList().set(index, (EditableExtendedUserInfoImpl) result);
                            userInfoProvider.flush();
                            unsavedModifiedUsers.remove(object);
                        }

                        @Override
                        public void onFailure(Response response) {
                            Window.alert(response.getStatusCode() + " : " + response.getText());
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            ;
                        }
                    });

                } else {

                    injector.getCoreDataProvider().createUser(object.getDisplayName(), object.isAdmin(), object.getPassword(), new DetailedAsyncCallback<ExtendedUserInfoImpl>() {

                        @Override
                        public void onFailure(Response response) {
                            Window.alert(response.getStatusCode() + " : " + response.getText());
                        }

                        @Override
                        public void onSuccess(ExtendedUserInfoImpl result) {
                            userInfoProvider.getList().set(index, (EditableExtendedUserInfoImpl) result);
                            userInfoProvider.flush();
                            unsavedModifiedUsers.remove(object);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            ;
                        }
                    });
                }
            }
        };

        saveBtnColumn.setFieldUpdater(saveBtnFieldUpd);
        userGrid.addColumn(saveBtnColumn);
        // - - -

        Column<EditableExtendedUserInfoImpl, String> passwdBtnColumn = new Column<EditableExtendedUserInfoImpl, String>(new DisableableButtonCell()) {

            @Override
            public String getValue(EditableExtendedUserInfoImpl object) {
                if (object.isPersisted()) {
                    return "Change password";
                } else {
                    return null;
                }

            }
        };
        FieldUpdater<EditableExtendedUserInfoImpl, String> passwdBtnFieldUpd = new FieldUpdater<EditableExtendedUserInfoImpl, String>() {

            @Override
            public void update(int index, EditableExtendedUserInfoImpl object, String value) {
                PasswordUpdater.update(object.getId());
            }
        };
        passwdBtnColumn.setFieldUpdater(passwdBtnFieldUpd);
        userGrid.addColumn(passwdBtnColumn);
        // - - -


    }

    private void refreshUserList() {
        saveModifiedUserAuths(currentSelectedUser);

        userInfoProvider.getList().clear();

        injector.getCoreDataProvider().getUserList(new AsyncCallback<ExtendedUserInfoListImpl>() {

            @Override
            public void onFailure(Throwable caught) {
                //FIXME
            }

            @Override
            public void onSuccess(ExtendedUserInfoListImpl result) {
                unsavedModifiedUsers.clear();
                userInfos.clear();

                for (int row = 0; row < result.length(); row++) {
                    userInfos.add((EditableExtendedUserInfoImpl) result.get(row));
                    userInfoProvider.getList().add(EditableExtendedUserInfoImpl.create(result.get(row)));
                }

                userGrid.setVisibleRange(0, userInfoProvider.getList().size());
            }
        });

    }

    private void initAuthTable() {
        authsDataProvider.addDataDisplay(authGrid);

        campaigns.clear();
        injector.getCoreDataProvider().getCampaignList(new AsyncCallback<CampaignListImpl>() {

            @Override
            public void onFailure(Throwable caught) {
                //FIXME
            }

            @Override
            public void onSuccess(CampaignListImpl result) {
                for (int row = 0; row < result.length(); row++) {
                    campaigns.put(result.get(row).getId(), result.get(row).getDisplayName());
                }
            }
        });

        injector.getCoreDataProvider().getAuthorizationList(new AsyncCallback<AuthorizationListImpl>() {

            @Override
            public void onFailure(Throwable caught) {
                //FIXME
            }

            @Override
            public void onSuccess(AuthorizationListImpl result) {
                authorizations = result;
                SafeHtmlHeader campaignHeader = new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Campaign"));
                Column<CampaignAuthsInfo, String> idColumn =
                        new Column<CampaignAuthsInfo, String>(new TextCell()) {

                            @Override
                            public String getValue(CampaignAuthsInfo object) {
                                return String.valueOf(object.getCampaignId());
                            }
                        };
                authGrid.addColumn(idColumn, campaignHeader);
                authGrid.setColumnWidth(idColumn, 3, Unit.EM);


                Column<CampaignAuthsInfo, String> campaignColumn =
                        new Column<CampaignAuthsInfo, String>(new TextCell()) {

                            @Override
                            public String getValue(CampaignAuthsInfo object) {
                                return campaigns.get(object.getCampaignId());
                            }
                        };
                authGrid.addColumn(campaignColumn, campaignHeader);
                authGrid.setColumnWidth(campaignColumn, 10, Unit.EM);

                for (int row = 0; row < authorizations.length(); row++) {
                    if (authorizations.get(row).isCampaignRelated()) {
                        final int currentAuthId = authorizations.get(row).getId();


                        Column<CampaignAuthsInfo, Boolean> authCol = new Column<CampaignAuthsInfo, Boolean>(new CheckboxCell(true, false)) {

                            @Override
                            public Boolean getValue(CampaignAuthsInfo object) {
                                return object.getOriginalUserAuths().getAuthsByCampaign(object.getCampaignId()).contains(currentAuthId);
                            }
                        };

                        FieldUpdater<CampaignAuthsInfo, Boolean> authColUpdater = new FieldUpdater<CampaignAuthsInfo, Boolean>() {

                            @Override
                            public void update(int index, CampaignAuthsInfo object, Boolean value) {
                                object.changeAuth(currentAuthId, value);
                                authsEdited = true;
                            }
                        };
                        authCol.setFieldUpdater(authColUpdater);


                        String head = authorizations.get(row).getDescription();
                        if (head.length() > 10) {
                            head = head.substring(0, 9) + "...";
                        }
                        String title = authorizations.get(row).getId() + " - " + authorizations.get(row).getDescription();
                        authGrid.addColumn(authCol, new SafeHtmlHeader(TEMPLATES.spanWithTitle(head, title)));
                    }
                }
            }
        });
    }

    private void saveModifiedUserAuths(EditableExtendedUserInfoImpl authOwner) {
        //check if there is any modified auths
        if (authOwner != null && authsEdited) {

            UserAuthorizationsImpl originalUserAuths = null;

            for (CampaignAuthsInfo authByCampaign : authsDataProvider.getList()) {
                int campaignId = authByCampaign.getCampaignId();

                originalUserAuths = authByCampaign.getOriginalUserAuths();
                if (authByCampaign.hasChanged()) {
                    //replace original authorization by the edited one
                    originalUserAuths.setAuthsForCampaign(campaignId, authByCampaign.getEditedUserAuths());
                } else {
                    //for campaign with unmodified authorizations, just forget about it so they zill not be send to the server
                    originalUserAuths.removeCampaignAuthInfos(campaignId);
                }
            }

            injector.getCoreDataProvider().setUserAuthorizations(authOwner.getId(), originalUserAuths,
                    new AsyncCallback<UserAuthorizationsImpl>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            //FIXME
                        }

                        @Override
                        public void onSuccess(UserAuthorizationsImpl result) {
                            authsEdited = false;
                        }
                    });

        }
    }

    private void refreshUserAuthslist() {

        saveModifiedUserAuths(prevSelectedUser);

        if (currentSelectedUser != null) {
            authsDataProvider.getList().clear();

            injector.getCoreDataProvider().getUserAuthorizations(currentSelectedUser.getId(), new AsyncCallback<UserAuthorizationsImpl>() {

                @Override
                public void onFailure(Throwable caught) {
                    //FIXME
                }

                @Override
                public void onSuccess(UserAuthorizationsImpl result) {
                    for (int campaignId : result.getCampaigns()) {
                        authsDataProvider.getList().add(new CampaignAuthsInfo(campaignId, result));
                    }
                    authGrid.setVisibleRange(0, authsDataProvider.getList().size());
                    authsDataProvider.flush();
                }
            });

        }

    }

    private void refreshAll() {
        Scheduler.get().scheduleDeferred(new Command() {

            @Override
            public void execute() {
                refreshUserList();
                refreshUserAuthslist();
            }
        });
    }

    @Override
    public boolean canCloseView() {
        //Authorizations changes are always automatically saved
        saveModifiedUserAuths(currentSelectedUser);

        //User changes are saved upon explicit user request
        return unsavedModifiedUsers.isEmpty();
    }

    @UiHandler("addUserButton")
    void handleAddUserClick(ClickEvent e) {
        EditableExtendedUserInfoImpl newUser = EditableExtendedUserInfoImpl.create();
        newUser.setPassword("#$%&'~");
        userInfoProvider.getList().add(newUser);
        unsavedModifiedUsers.add(newUser);
        userGrid.setVisibleRange(0, userInfoProvider.getList().size());
        selectionModel.setSelected(newUser, true);
    }

    @UiHandler("cancelButton")
    void handleCancelAllClick(ClickEvent e) {
        if (unsavedModifiedUsers.size() > 0) {
            if (Window.confirm("All unsaved modifications will be lost!\nAre you sure?")) {
                refreshUserList();
            }
        } else {
            refreshUserList();
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
    }
}
