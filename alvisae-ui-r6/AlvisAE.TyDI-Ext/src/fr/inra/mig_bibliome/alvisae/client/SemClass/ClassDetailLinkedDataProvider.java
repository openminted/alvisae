/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import fr.inra.mig_bibliome.alvisae.client.data3.TermLinkedImpl;
import java.util.ArrayList;

/**
 *
 * @author fpapazian
 */
public class ClassDetailLinkedDataProvider extends AsyncDataProvider<TermInfo> {

    private final TermMemberInfo termMemberInfo;

    ClassDetailLinkedDataProvider(TermMemberInfo termMemberInfo) {
        this.termMemberInfo = termMemberInfo;
    }

    @Override
    protected void onRangeChanged(HasData<TermInfo> display) {
        ArrayList<TermLinkedInfo> termList = new ArrayList<TermLinkedInfo>();
        JsArray<TermLinkedImpl> linked = termMemberInfo.getTerm().getLinkedTerms();
        for (int j = 0; j < linked.length(); j++) {
            termList.add(new TermLinkedInfo(linked.get(j)));
        }
        display.setRowCount(termList.size(), true);
        display.setRowData(0, termList);
    }
}