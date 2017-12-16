/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientExtGinInjector;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.AsyncResponseHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.VersionedSemClassTreeLevelImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClass;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 *
 * @author fpapazian
 */
public class SemClassDataProvider extends AsyncDataProvider<SemClassInfo> {

    private static final StaneClientExtGinInjector termInjector = GWT.create(StaneClientExtGinInjector.class);
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    // Refresh every nodes corresponding to the specified Semantic Class id (without reloading data from DB)
    public static void refreshSemClassNodes(int projectId, int semClassId) {
        //GWT.log("Refreshing (nodes of) " + semClassId);
        HashSet<SemClassDataProvider> parentProviders = ProviderStore.forProject(projectId).getParentProviders(semClassId);
        if (parentProviders != null) {
            for (SemClassDataProvider p : parentProviders) {
                //GWT.log("         refreshing " +  ProviderStore.forProject(projectId).getCacheSemClass(p.getParentId()).getCanonicLabel());
                p.refreshRowData(semClassId);
            }
        }
    }

    // Reload from DB the details of every Hyperonym Class of the specified Semantic Class id
    public static void reloadSemClassParents(int projectId, int semClassId) {
        //GWT.log("Reloading (parents of) " + semClassId);
        HashSet<SemClassDataProvider> parentProviders = ProviderStore.forProject(projectId).getParentProviders(semClassId);
        if (parentProviders != null) {
            parentProviders = new HashSet<SemClassDataProvider>(parentProviders);
            for (SemClassDataProvider p : parentProviders) {
                //GWT.log("         Reloading " + p.getParentId() + " " + ProviderStore.forProject(projectId).getCacheSemClass(p.getParentId()).getCanonicLabel());
                p.reloadData(null);
            }
        }
    }

    // Reload from DB the details of the specified Semantic Class id
    public static void reloadSemClass(int projectId, int semClassId) {
        //GWT.log("Reloading (class) " + semClassId);
        SemClassDataProvider parentProvider = ProviderStore.forProject(projectId).getProvider(semClassId);
        if (parentProvider != null) {
            //GWT.log("         reloading " + semClassId + " " + ProviderStore.forProject(projectId).getCacheSemClass(semClassId).getCanonicLabel());
            parentProvider.reloadData(null);
        }
    }
    // =========================================================================
    private final SemClassInfo parentClassInfo;
    private final Integer projectId;
    private final Integer parentId;
    private boolean loaded = false;
    private boolean loading = false;
    private ArrayList<SemClassExtendedInfo> data = new ArrayList<SemClassExtendedInfo>();

    protected SemClassDataProvider(ProvidesKey<SemClassInfo> KEY_PROVIDER, int projectId, SemClassInfo parentClassInfo) {
        super(KEY_PROVIDER);
        this.projectId = projectId;
        this.parentClassInfo = parentClassInfo;
        if (parentClassInfo != null) {
            parentId = parentClassInfo.getId();
        } else {
            parentId = null;
        }
    }

    public Integer getParentId() {
        return parentId;
    }

    public void refreshRowData(int semClassId) {
        //FIXME should refresh only the specified row 
        /*
         ArrayList<SemClassInfo> dataInRange = new ArrayList<SemClassInfo>();
         int rowIndex = 0;
         for (SemClassExtendedInfo info : data) {
         if (semClassId == info.getId()) {
         dataInRange.add(info);
         updateRowData(rowIndex, dataInRange);
         break;
         }
         rowIndex++;
         }
         * 
         */
        ArrayList<SemClassInfo> loadedData = new ArrayList<SemClassInfo>();
        for (Entry<Integer, SemClass> e : ProviderStore.forProject(projectId).getCacheSemClassTreeLevel(semClassId).getHypoGroupsDetails().entrySet()) {
            loadedData.add(new SemClassExtendedInfo(projectId, e.getValue().getId(), parentId));
        }

        updateRowData(0, loadedData);
    }

    void unloadData() {
        loaded = false;
        data.clear();
    }

    void reloadData(final Command executeWhenReloaded) {
        loaded = false;
        loading = true;
        for (SemClassExtendedInfo info : data) {
            ProviderStore.forProject(projectId).unsetParentProvider(info.getId(), SemClassDataProvider.this);
        }

        loadData(new Command() {
            @Override
            public void execute() {
                updateRowCount(data.size(), true);
                ArrayList<SemClassInfo> dataInRange = new ArrayList<SemClassInfo>(data);
                updateRowData(0, dataInRange);
                if (executeWhenReloaded != null) {
                    executeWhenReloaded.execute();
                }
            }
        });
    }

    public static void ensureSemanticClassLoaded(final Integer projectId, Integer semClassId, final Command executeWhenLoaded) {
        if ((ProviderStore.forProject(projectId) == null || ProviderStore.forProject(projectId).getCacheSemClass(semClassId) == null) && termInjector.getTermDataProvider().getRequestManager().isSignedIn()) {
            termInjector.getTermDataProvider().getSemanticClass(projectId, semClassId, new AsyncResponseHandler<VersionedSemClassTreeLevelImpl>() {
                @Override
                public void onSuccess(VersionedSemClassTreeLevelImpl result) {
                    ProviderStore.forProject(projectId).cacheSemClassTreeLevel(result);
                    if (executeWhenLoaded != null) {
                        executeWhenLoaded.execute();
                    }
                }
            });
        }
    }

    private void loadData(final Command executeWhenLoaded) {
        unloadData();
        loading = true;

        termInjector.getTermDataProvider().getSemanticClass(projectId, parentId, new AsyncResponseHandler<VersionedSemClassTreeLevelImpl>() {
            @Override
            public void onSuccess(VersionedSemClassTreeLevelImpl result) {
                ProviderStore.forProject(projectId).cacheSemClassTreeLevel(result);
                data.clear();
                ArrayList<SemClassExtendedInfo> loadedData = new ArrayList<SemClassExtendedInfo>();
                for (Entry<Integer, SemClass> e : result.getHypoGroupsDetails().entrySet()) {
                    loadedData.add(new SemClassExtendedInfo(projectId, e.getValue().getId(), parentId));
                    ProviderStore.forProject(projectId).setParentProvider(e.getValue().getId(), SemClassDataProvider.this);
                }
                data.addAll(loadedData);
                if (executeWhenLoaded != null) {
                    executeWhenLoaded.execute();
                }
                loading = false;
                loaded = true;
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                loading = false;
            }
        });
    }

    @Override
    protected void onRangeChanged(final HasData<SemClassInfo> display) {

        if (loading) {
            GWT.log("please wait while loading....");
            return;
        }

        final Range range = display.getVisibleRange();

        //compute data in range
        final int start = range.getStart();
        int end = start + range.getLength();
        //FixME use range

        if (parentId == null) {
            //create first node for the root of the terminology
            ArrayList<SemClassExtendedInfo> dataInRange = new ArrayList<SemClassExtendedInfo>();
            dataInRange.add(new SemClassExtendedInfo(projectId, SemClass.ROOT_ID, parentId));
            display.setRowData(0, dataInRange);

        } else {

            //Asynch retreival of the hyponyms affected to 
            if (parentId != 0 && !parentClassInfo.hasChild()) {
                Scheduler.get().scheduleFinally(new Command() {
                    @Override
                    public void execute() {
                        display.setRowData(start, new ArrayList<SemClassExtendedInfo>());
                    }
                });
            } else {
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
        }
    }
}
