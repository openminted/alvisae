/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 *
 * @author fpapazian
 */
public interface StaneResources extends ClientBundle {

    public static final StaneResources INSTANCE = GWT.create(StaneResources.class);

    public interface Style extends CssResource {

        String DefaultCursor();

        String WaitCursor();

        String DetailGrid();

        String DetailGridHeader();

        String DetailGridFirstRow();

        String ToolBar();

        String SmallButton();

        String SmallerButton();

        String MiniPopButton();
    }

    @Source("../public/StaneUIGlobal.css")
    public Style style();

    @Source("images/AlvisAE_background.png")
    ImageResource LogoImage();

    @Source("images/question-white.png")
    ImageResource HelpIcon();
            
    @Source("images/AlvisAE-small.png")
    ImageResource AboutIcon();
    
    @Source("images/notebook--arrow.png")
    ImageResource ExportIcon();

    @Source("images/information-balloon.png")
    ImageResource ShowMessagesIcon();

    @Source("images/folder-open-document-text.png")
    ImageResource OpenDocIcon();

    @Source("images/blueprint--plus.png")
    ImageResource UploadDocIcon();

    @Source("images/tick-small-11.png")
    ImageResource ApplySmallIcon();

    @Source("images/cross-small-11.png")
    ImageResource CancelSmallIcon();

    @Source("images/plus-small-11.png")
    ImageResource AddSmallIcon();

    @Source("images/minus-small-11.png")
    ImageResource DelSmallIcon();

    @Source("images/fill.png")
    ImageResource NextDocumentIcon();

    @Source("images/fill-180.png")
    ImageResource PrevDocumentIcon();

    @Source("images/inbox--arrow.png")
    ImageResource GoToDocListIcon();

    @Source("images/table--pencil.png")
    ImageResource StartDocAnnotationIcon();

    @Source("images/table--minus.png")
    ImageResource RemoveDocAnnotationIcon();

    @Source("images/tables.png")
    ImageResource CompareAnnotationSetsIcon();
            
    @Source("images/price-tag--exclamation.png")
    ImageResource InvalidatedAnnotationSetIcon();

    @Source("images/price-tag--plus.png")
    ImageResource CanEditAnnotationIcon();
    
    @Source("images/price-tag--arrow.png")
    ImageResource CanReferenceAnnotationIcon();

    @Source("images/burn.png")
    ImageResource testIcon();
    
    @Source("images/view-refresh.png")
    ImageResource refreshIcon();
    
    @Source("images/docAssign.png")
    ImageResource AssignDocIcon();
    
    @Source("images/box.png")
    ImageResource TaskToDoIcon();

    @Source("images/hard-hat.png")
    ImageResource TaskPendingIcon();
    
    @Source("images/medal.png")
    ImageResource TaskDoneIcon();

    @Source("images/clock-select-remain.png")
    ImageResource TaskUpcomingIcon();
    
    @Source("images/safe.png")
    ImageResource TaskNADoneIcon();
    
    @Source("images/chain.png")
    ImageResource UrlLinkIcon();
    
    @Source("images/blue-document-page-next.png")
    ImageResource TaskInstNextIcon();

    @Source("images/blue-document-page-last.png")
    ImageResource TaskInstLastIcon();

    @Source("images/ui-progress-bar-indeterminate.gif")
    ImageResource ProgressBarImage();
}
