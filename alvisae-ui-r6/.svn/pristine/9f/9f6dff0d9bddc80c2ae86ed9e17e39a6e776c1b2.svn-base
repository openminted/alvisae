/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientExtGinInjector;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.AsyncResponseHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.SemClassNTermsImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.TermMemberImpl;
import java.util.ArrayList;

/**
 *
 * @author fpapazian
 */
public class ClassDetailMembersDataProvider extends AsyncDataProvider<TermInfo> {

    private static final StaneClientExtGinInjector termInjector = GWT.create(StaneClientExtGinInjector.class);

    public static void reloadClassDetails(int projectId, int semClassId) {
        ClassDetailMembersDataProvider detailProvider = ProviderStore.forProject(projectId).getDetailsProvider(semClassId);
        if (detailProvider != null) {
            detailProvider.reloadData(null);
        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private final int projectId;
    private final SemClassInfo semClassInfo;
    private boolean loaded = false;
    private ArrayList<TermMemberInfo> data = new ArrayList<TermMemberInfo>();

    protected ClassDetailMembersDataProvider(ProvidesKey<TermInfo> KEY_PROVIDER, int projectId, SemClassInfo semClassInfo) {
        super(KEY_PROVIDER);
        this.projectId = projectId;
        this.semClassInfo = semClassInfo;
    }

    void reloadData(final Command executeWhenReloaded) {
        loaded = false;

        loadData(new Command() {

            @Override
            public void execute() {
                updateRowCount(data.size(), true);
                ArrayList<TermInfo> dataInRange = new ArrayList<TermInfo>(data);
                updateRowData(0, dataInRange);
                if (executeWhenReloaded != null) {
                    executeWhenReloaded.execute();
                }
            }
        });
    }

    private void loadData(final Command executeWhenLoaded) {


        termInjector.getTermDataProvider().getSemanticClassNTerms(projectId, semClassInfo.getId(), new AsyncResponseHandler<SemClassNTermsImpl>() {

            @Override
            public void onSuccess(SemClassNTermsImpl result) {
                loaded = true;

                ProviderStore.forProject(projectId).cacheSemClassNTerms(result);
                data.clear();

                JsArray<TermMemberImpl> members = result.getTermMembers();
                for (int i = 0; i < members.length(); i++) {
                    TermMemberImpl t = members.get(i);
                    data.add(new TermMemberInfo(t));
                }
                if (executeWhenLoaded != null) {
                    executeWhenLoaded.execute();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                data.clear();
            }
        });

    }

    @Override
    protected void onRangeChanged(final HasData<TermInfo> display) {
        Scheduler.get().scheduleDeferred(new Command() {

            @Override
            public void execute() {

                if (!loaded) {
                    loadData(new Command() {

                        @Override
                        public void execute() {
                            display.setRowData(0, data);
                        }
                    });
                } else {
                    display.setRowData(0, data);
                }

            }
        });

    }
}
