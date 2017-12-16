/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config.History;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import fr.inra.mig_bibliome.alvisae.client.Campaign.CampaignAssignmentPlace;
import fr.inra.mig_bibliome.alvisae.client.Conso.AnnotationSetConsoPlace;
import fr.inra.mig_bibliome.alvisae.client.Document.DocDisplayPlace;
import fr.inra.mig_bibliome.alvisae.client.Document.DocEditingPlace;
import fr.inra.mig_bibliome.alvisae.client.SignIn.SignInPlace;
import fr.inra.mig_bibliome.alvisae.client.Start.DefaultPlace;
import fr.inra.mig_bibliome.alvisae.client.Task.TaskSelectingPlace;
import fr.inra.mig_bibliome.alvisae.client.User.UserManagingPlace;

/**
 *
 * @author fpapazian
 */
/**
 * PlaceHistoryMapper interface is used to attach all places which the
 * PlaceHistoryHandler should be aware of. This is done via the
 *
 * @WithTokenizers annotation or by extending PlaceHistoryMapperWithFactory and
 * creating a separate TokenizerFactory.
 */
@WithTokenizers({
    DefaultPlace.Tokenizer.class,
    SignInPlace.Tokenizer.class,
    TaskSelectingPlace.Tokenizer.class,
    CampaignAssignmentPlace.Tokenizer.class,
    UserManagingPlace.Tokenizer.class,
    DocEditingPlace.Tokenizer.class,
    DocDisplayPlace.Tokenizer.class,
    AnnotationSetConsoPlace.Tokenizer.class})
public interface StanePlaceHistoryMapper extends PlaceHistoryMapper {
}
