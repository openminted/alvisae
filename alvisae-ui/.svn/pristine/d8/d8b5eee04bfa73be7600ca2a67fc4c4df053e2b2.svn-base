/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.inject.Provides;
import fr.inra.mig_bibliome.alvisae.client.Campaign.CampaignAssignment;
import fr.inra.mig_bibliome.alvisae.client.Campaign.CampaignAssignmentView;
import fr.inra.mig_bibliome.alvisae.client.Config.History.StaneActivityMapper;
import fr.inra.mig_bibliome.alvisae.client.Config.History.StaneActivityMapperImpl;
import fr.inra.mig_bibliome.alvisae.client.Conso.AnnSetCompare;
import fr.inra.mig_bibliome.alvisae.client.Conso.AnnotationSetConsoViewImpl;
import fr.inra.mig_bibliome.alvisae.client.Document.DocEditingView;
import fr.inra.mig_bibliome.alvisae.client.Document.DocEditingViewImpl;
import fr.inra.mig_bibliome.alvisae.client.SignIn.SignInDialog;
import fr.inra.mig_bibliome.alvisae.client.SignIn.SignInView;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskInstancesList;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskSelectingView;
import fr.inra.mig_bibliome.alvisae.client.User.UserManager;
import fr.inra.mig_bibliome.alvisae.client.User.UserManagingView;

/**
 * Binding Gin module - provides implementations for several global interface
 * instances
 *
 * @author fpapazian
 */
public class StaneClientGinModule extends StaneClientBaseGinModule {

    private static SignInView loginView = null;
    private static StaneActivityMapperImpl activityMapper = null;
    private static DocEditingView docEditingView = null;
    private static TaskSelectingView taskSelectingView = null;
    private static UserManagingView userManagingView = null;
    private static CampaignAssignmentView campaignAssignmentView = null;

    @Override
    protected void configure() {
    }

    @Provides
    public SignInView getLoginView() {
        if (loginView == null) {
            loginView = new SignInDialog();
        }
        // when singIn is performed, it may be necessary to reset other views in case of connection with a new user profile
        taskSelectingView = null;
        userManagingView = null;
        campaignAssignmentView = null;
        docEditingView = null;
        
        return loginView;
    }

    @Provides
    public DocEditingView getDocEditingView() {
        if (docEditingView == null) {
            docEditingView = new DocEditingViewImpl();
        }
        return docEditingView;
    }

    @Provides
    public StaneActivityMapper getStaneActivityMapper() {
        if (activityMapper == null) {
            activityMapper = new StaneActivityMapperImpl();
        }
        return activityMapper;
    }

    @Provides
    public TaskSelectingView getTaskSelectingView() {
        if (taskSelectingView == null) {
            taskSelectingView = new TaskInstancesList();
        }
        return taskSelectingView;
    }

    @Provides
    public UserManagingView getUserManagingView() {
        if (userManagingView == null) {
            userManagingView = new UserManager();
        }
        return userManagingView;
    }

    @Provides
    public CampaignAssignmentView getCampaignAssignmentView() {
        if (campaignAssignmentView == null) {
            campaignAssignmentView = new CampaignAssignment();
        }
        return campaignAssignmentView;
    }

}
