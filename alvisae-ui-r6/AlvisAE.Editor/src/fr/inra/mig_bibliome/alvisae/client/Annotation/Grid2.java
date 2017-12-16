/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Grid;

/**
 *
 * @author fpapazian
 */
public class Grid2 extends Grid {

    public Integer getRowForEvent(NativeEvent event) {

        Element td = getEventTargetCell(Event.as(event));
        if (td == null) {
            return null;
        }
        return TableRowElement.as(td.getParentElement()).getSectionRowIndex();
    }

    public Integer getColumnForEvent(NativeEvent event) {

        Element td = getEventTargetCell(Event.as(event));
        if (td == null) {
            return null;
        }
        return TableCellElement.as(td).getCellIndex();
    }
}
