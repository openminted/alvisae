/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;

/**
 *
 * @author fpapazian
 */
public interface DocEditingView extends IsWidget {


    void setPresenter(Presenter presenter);

    public interface Presenter {
        void goTo(Place place);
    }

    public void setDocument(AnnotatedTextHandler document, boolean readOnly);
    
    public void setParameters(Integer userId, Integer campaignId, Integer documentId, Integer offset);

    public void setParameters(Integer userId, Integer campaignId, Integer documentId, Integer offset, Integer taskId);

    public boolean canCloseView();
}
