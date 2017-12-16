/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/**
 *
 * @author fpapazian
 */
public interface StaneCoreResources extends ClientBundle {

    public static final StaneCoreResources INSTANCE = GWT.create(StaneCoreResources.class);

    @Source("images/tick-octagon.png")
    ImageResource OkMessageIcon();

    @Source("images/exclamation-octagon-frame.png")
    ImageResource WarnMessageIcon();

    @Source("images/cross-octagon.png")
    ImageResource ErrorMessageIcon();

    @Source("images/information-octagon-frame.png")
    ImageResource InfoMessageIcon();

    @Source("images/tick-small.png")
    ImageResource ApplyIcon();

    @Source("images/cross-small.png")
    ImageResource CancelIcon();

    @Source("images/network-status.png")
    ImageResource NetworkStatusOnIcon();

    @Source("images/network-status-offline.png")
    ImageResource NetworkStatusOffIcon();

    @Source("images/ui-progress-round.gif")
    ImageResource NetworkActivityOnIcon();

    @Source("images/ui-progress-none.png")
    ImageResource NetworkActivityOffIcon();

    @Source("images/moveUp.png")
    ImageResource MoveUpIcon();

    @Source("images/moveDown.png")
    ImageResource MoveDownIcon();
    
    @Source("images/user-silhouette.png")
    ImageResource SignedInIcon();

    @Source("images/user-silhouette-question.png")
    ImageResource SignedOutIcon();

    @Source("images/signOut.png")
    ImageResource SigningOutIcon();
    
    @Source("images/key-solid.png")
    ImageResource PasswordIcon();

    @Source("images/users.png")
    ImageResource ManageUsersIcon();

    @Source("images/103.png")
    ImageResource AddIcon();

    @Source("images/104.png")
    ImageResource RemoveIcon();

    @Source("images/101.png")
    ImageResource CancelAllIcon();

    @Source("images/102.png")
    ImageResource ValidateAllIcon();

    @Source("images/book-open-text.png")
    ImageResource GuidelinesIcon();
    
    @Source("images/133.png")
    ImageResource DropDownIcon();
    
    @Source("images/question-small.png")
    ImageResource AboutIcon();

    static interface StaneCoreCSS extends CssResource {

        String AboutImageBackground();
    }
    
    @Source("images/StaneCore.css")
    StaneCoreCSS css();
    
    @Source("data3/serialized_example.json")
    public TextResource jsonData3Test();
}
