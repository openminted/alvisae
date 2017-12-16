/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import java.util.ArrayList;

/**
 *
 * @author fpapazian
 */
public class ClassDetailRootDataProvider extends AsyncDataProvider<SemClassInfo> {

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private final SemClassInfo semClassInfo;

    public ClassDetailRootDataProvider(ProvidesKey<SemClassInfo> KEY_PROVIDER, SemClassInfo semClassInfo) {
        super(KEY_PROVIDER);
        this.semClassInfo = semClassInfo;
    }

    @Override
    protected void onRangeChanged(final HasData<SemClassInfo> display) {
        if (semClassInfo != null) {
            ArrayList<SemClassInfo> dataInRange;
            dataInRange = new ArrayList<SemClassInfo>();
            dataInRange.add(semClassInfo);
            display.setRowCount(dataInRange.size(), true);
            display.setRowData(0, dataInRange);
        }
    }
}
