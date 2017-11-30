/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Campaign.CampaignAssignmentView;
import fr.inra.mig_bibliome.alvisae.client.Config.History.StaneActivityMapper;
import fr.inra.mig_bibliome.alvisae.client.Conso.AnnotationSetConsoViewImpl;
import fr.inra.mig_bibliome.alvisae.client.Document.DocEditingView;
import fr.inra.mig_bibliome.alvisae.client.SignIn.SignInView;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskSelectingView;
import fr.inra.mig_bibliome.alvisae.client.User.UserManagingView;
import fr.inra.mig_bibliome.alvisae.client.data.CoreDataProvider;

/**
 * Gin Injector used to retrieve "Global" instances
 *
 * @author fpapazian
 */
@GinModules(StaneClientGinModule.class)
public interface StaneClientGinInjector extends Ginjector {

    public CoreDataProvider getCoreDataProvider();

    public EventBus getMainEventBus();

    public PlaceController getPlaceController();

    public StaneActivityMapper getAppActivityMapper();

    public SignInView getLoginView();

    public DocEditingView getDocEditingView();

    public UserManagingView getUserManagingView();
    
    public TaskSelectingView getTaskSelectingView();

    public CampaignAssignmentView getCampaignAssignmentView();
    
}
