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
public interface StructTermResources extends ClientBundle {

    public static final StructTermResources INSTANCE = GWT.create(StructTermResources.class);

    @Source("images/asterisk_orange.png")
    ImageResource RootSemClassNodeIcon();

    @Source("images/_rootedNleaf.png")
    ImageResource RootedLeafNodeIcon();

    @Source("images/_rooted.png")
    ImageResource RootedNodeIcon();

    @Source("images/_hypo.png")
    ImageResource HypoNodeIcon();

    @Source("images/_hypoNhyper.png")
    ImageResource HypoNHyperNodeIcon();

    @Source("images/_synonym.png")
    ImageResource LinkSynonymIcon();

    @Source("images/_antonym.png")
    ImageResource LinkAntonymIcon();

    @Source("images/_hypo.png")
    ImageResource LinkHyponymIcon();

    @Source("images/_isa_left.png")
    ImageResource LinkHyponymInvIcon();

    @Source("images/_typovar.png")
    ImageResource LinkTypoVarIcon();

    @Source("images/_canonic.png")
    ImageResource LinkCanonicIcon();

    @Source("images/_acronym_left.png")
    ImageResource LinkAcronymIcon();

    @Source("images/_acronym_right.png")
    ImageResource LinkAcronymInvIcon();

    @Source("images/_fastrvar_left.png")
    ImageResource LinkVariantIcon();

    @Source("images/_fastrvar_right.png")
    ImageResource LinkVariantInvIcon();

    @Source("images/_quasisynonym.png")
    ImageResource LinkQuasiSynIcon();

    @Source("images/_antonym.png")
    ImageResource LinkTranslationIcon();
    
    @Source("images/_isa_delete.png")
    ImageResource LinkHyponymRemoveIcon();

    @Source("images/arrow_refresh_small.png")
    ImageResource RefreshTreeIcon();

    @Source("images/go-down.png")
    ImageResource SearchTreeIcon();

    @Source("images/go-down-anim.gif")
    ImageResource SearchExpandTreeIcon();

    @Source("images/tick-small-11.png")
    ImageResource ConnectIcon();

    @Source("images/tick-small-11.png")
    ImageResource ApplyIcon();
    
    @Source("images/cross-small-grey.png")
    ImageResource CancelIcon();
    
    @Source("images/ellipsis.png")
    ImageResource EllipsisIcon();
    
    @Source("images/cross-small-11.png")
    ImageResource DisconnectIcon();

    @Source("images/ui-accordion.png")
    ImageResource SearchResult();

    @Source("images/160.png")
    ImageResource SemClassDragHandleIcon();

    @Source("images/156.png")
    ImageResource SemClassAltDragHandleIcon();
    
    @Source("images/eye--pencil.png")
    ImageResource SeeChangesIcon();
    
    static interface StructTermGlobalCSS extends CssResource {

        String DefaultCursor();

        String WaitCursor();

        String DroppableHover();
        
        String UnDroppableHover();
                
        String DragHelper();
        
        String FlatButton();
        
        String TermDroppableHover();
        
        String ScrollableContent();
    }

    @Source("images/StructTermGlobal.css")
    StructTermGlobalCSS css();
    
}
