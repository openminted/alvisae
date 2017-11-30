/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Campaign;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocParams;

/**
 *
 * @author fpapazian
 */
public interface CampaignAssignmentView extends IsWidget {

    BasicUserCampaignDocParams getParams();

    void setParams(BasicUserCampaignDocParams params);

    void setPresenter(CampaignAssignmentView.Presenter presenter);

    public interface Presenter {

        void goTo(Place place);
    }

}
