/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Task;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocOffsetTaskParams;

/**
 *
 * @author fpapazian
 */
public interface TaskSelectingView extends IsWidget {

    BasicUserCampaignDocOffsetTaskParams getParams();

    void setParams(BasicUserCampaignDocOffsetTaskParams params);

    void setPresenter(Presenter presenter);

    public interface Presenter {

        void goTo(Place place);
    }

}
