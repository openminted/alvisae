/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Conso;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams.TaskReviewParams;

/**
 *
 * @author fpapazian
 */
public interface AnnotationSetConsoView extends IsWidget {

    TaskReviewParams getParams();

    void setParams(TaskReviewParams params);

    void setPresenter(AnnotationSetConsoView.Presenter presenter);

    public interface Presenter {

        void goTo(Place place);
    }

    void proceed();
    
    public boolean canCloseView();    
}
