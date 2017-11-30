/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;

/**
 * Main UI of the Annotation Editor, containing the DocumentView and several auxiliary views
 * @author fpapazian
 */
public class TesterStructTerm extends Composite {

    interface TesterStructTermUiBinder extends UiBinder<DockLayoutPanel, TesterStructTerm> {
    }
    private static TesterStructTerm.TesterStructTermUiBinder uiBinder = GWT.create(TesterStructTerm.TesterStructTermUiBinder.class);
    @UiField
    DockLayoutPanel dockPanel;

    
    
    interface Styles extends CssResource {
    }

    public TesterStructTerm() {
        
        initWidget(uiBinder.createAndBindUi(this));
        
    }


}