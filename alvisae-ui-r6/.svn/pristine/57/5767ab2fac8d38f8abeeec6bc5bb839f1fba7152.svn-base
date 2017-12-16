/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;


/**
 *
 * @author fpapazian
 */
@Deprecated
public class CellDropDownList<T> extends Composite {
    
    interface CellDropDownListUiBinder extends UiBinder<Widget, CellDropDownList> {
    }
    private static CellDropDownListUiBinder uiBinder = GWT.create(CellDropDownListUiBinder.class);

    @UiField
    HTML selectedPanel;

    private final Cell<T> cell;

    public CellDropDownList(Cell<T> cell) {
        initWidget(uiBinder.createAndBindUi(this));
        this.cell = cell;
        selectedPanel.setHTML("<i>empty</i>");
    }

}