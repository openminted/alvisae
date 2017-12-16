/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import fr.inra.mig_bibliome.alvisae.client.data3.SemClassImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClass;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClassNTerms;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClassTreeLevel;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class serves as a global repository of instances of Semantic Classes
 * DataProviders, hence allowing dynamic update of the tree after modifications.
 */
/**
 *
 * @author fpapazian
 */
public class ProviderStore {

    /**
     * Part of the repository specific to a terminology project
     */
    public static class ByProject {

        private final int projectId;
        private final HashMap<Integer, HashSet<SemClassDataProvider>> parentProvidersBySemClass = new HashMap<Integer, HashSet<SemClassDataProvider>>();
        private final HashMap<Integer, SemClassDataProvider> providerBySemClass = new HashMap<Integer, SemClassDataProvider>();
        private final HashMap<Integer, ClassDetailMembersDataProvider> detailsProviderBySemClass = new HashMap<Integer, ClassDetailMembersDataProvider>();
        private int marked = -1;
        private final SemClass root = SemClassImpl.createRoot();
        private final HashMap<Integer, SemClass> semClasses = new HashMap<Integer, SemClass>();
        private final HashMap<Integer, SemClassNTerms> semClassNTerms = new HashMap<Integer, SemClassNTerms>();
        private final HashMap<Integer, SemClassTreeLevel> treeLevel = new HashMap<Integer, SemClassTreeLevel>();

        public ByProject(int projectId) {
            this.projectId = projectId;
        }

        public boolean isMarked(int semmClassId) {
            return marked == semmClassId;
        }

        public void toggleMarked(int semmClassId) {
            if (marked == semmClassId) {
                marked = -1;
            } else {
                SemClassDataProvider.refreshSemClassNodes(projectId, marked);
                marked = semmClassId;
            }
            SemClassDataProvider.refreshSemClassNodes(projectId, semmClassId);
        }

        /**
         * Retrieve the unique DataProvider that can generate the children
         * NodeInfo of the specified Semantic Class Id
         */
        public SemClassDataProvider getProvider(int semmClassId) {
            return providerBySemClass.get(semmClassId);
        }

        /**
         * Retrieve the list of all DataProviders that generated NodeInfos
         * corresponding to the specified Semantic Class Id
         */
        public HashSet<SemClassDataProvider> getParentProviders(int semmClassId) {
            return parentProvidersBySemClass.get(semmClassId);
        }

        /*
         *
         */
        public ClassDetailMembersDataProvider getDetailsProvider(int semmClassId) {
            return detailsProviderBySemClass.get(semmClassId);
        }

        /**
         * Remember that the specified DataProvider has generated a NodeInfo
         * corresponding to the specified Semantic Class Id
         */
        public void setParentProvider(int semmClassId, SemClassDataProvider provider) {
            HashSet<SemClassDataProvider> parentProviders = parentProvidersBySemClass.get(semmClassId);
            if (parentProviders == null) {
                parentProviders = new HashSet<SemClassDataProvider>();
                parentProvidersBySemClass.put(semmClassId, parentProviders);
            }
            parentProviders.add(provider);
        }

        /**
         * Forget that the specified DataProvider has generated a NodeInfo
         * corresponding to the specified Semantic Class Id
         */
        public void unsetParentProvider(int semmClassId, SemClassDataProvider provider) {
            HashSet<SemClassDataProvider> parentProviders = parentProvidersBySemClass.get(semmClassId);
            if (parentProviders != null) {
                parentProviders.remove(provider);
            }
        }

        public void clear() {
            for (HashSet<SemClassDataProvider> value : parentProvidersBySemClass.values()) {
                value.clear();
            }
            parentProvidersBySemClass.clear();
            providerBySemClass.clear();
            detailsProviderBySemClass.clear();
            treeLevel.clear();
        }

        private void disposeOutdatedTreeLevel(int semClassId, int semClassVersion) {
            SemClassTreeLevel level = treeLevel.get(semClassId);
            if (level != null) {
                if (level.getVersion() < semClassVersion) {
                    treeLevel.remove(semClassId);
                }
            }
        }

        public void cacheSemClass(SemClass semClass) {
            if (semClass != null) {
                int semClassId = semClass.getId();
                disposeOutdatedTreeLevel(semClassId, semClass.getVersion());
                //GWT.log("Caching class:" + semClass.getId() + "@" + semClass.getVersion() + " " + semClass.getCanonicLabel());
                SemClass previous = semClasses.put(semClassId, semClass);
            }
        }

        public void cacheSemClassTreeLevel(SemClassTreeLevel semClassTreeLevel) {
            int semClassId = semClassTreeLevel.getId();
            //GWT.log("Caching level:" + semClassTreeLevel.getId() + "@" + semClassTreeLevel.getVersion() + " " + semClassTreeLevel.getCanonicLabel());
            SemClassTreeLevel previous = treeLevel.put(semClassId, semClassTreeLevel);
            if (semClasses.containsKey(semClassId)) {
                semClasses.remove(semClassId);
            }
            for (SemClass s : semClassTreeLevel.getHypoGroupsDetails().values()) {
                cacheSemClass(s);
            }
        }

        public SemClass getCacheSemClass(int semClassId) {
            if (semClassId == SemClass.ROOT_ID) {
                return root;
            } else {
                SemClass semClass = treeLevel!=null ? treeLevel.get(semClassId) : null;
                if (semClass == null) {
                    semClass = semClasses!=null ? semClasses.get(semClassId) :null;
                }
                return semClass;
            }
        }

        public void unloadSemClassTreeLevel(int semClassId) {
            treeLevel.remove(semClassId);
            semClasses.remove(semClassId);
            semClassNTerms.remove(semClassId);
        }

        public SemClassTreeLevel getCacheSemClassTreeLevel(int semClassId) {
            SemClass semClass = getCacheSemClass(semClassId);
            if (semClass instanceof SemClassTreeLevel) {
                return (SemClassTreeLevel) semClass;
            } else {
                return null;
            }
        }

        public void cacheSemClassNTerms(SemClassNTerms semClass) {
            int semClassId = semClass.getId();
            int semClassVersion = semClass.getVersion();
            //GWT.log("Caching details:" + semClass.getId() + "@" + semClass.getVersion() + "  " + semClass.getCanonicLabel());
            SemClassNTerms previous = semClassNTerms.put(semClassId, semClass);
        }

        public SemClassNTerms getCacheSemClassNTerms(int semClassId) {
            return semClassNTerms.get(semClassId);
        }
    }
    private final HashMap<Integer, ByProject> providersByProject = new HashMap<Integer, ByProject>();

    private ByProject _forProject(int projectId) {
        return providersByProject.get(projectId);
    }

    private void _clear() {
        for (ByProject value : providersByProject.values()) {
            value.clear();
        }
        providersByProject.clear();
    }

    /**
     * Retrieve, or create if necessary, the DataProvider that can generate the
     * children NodeInfo of the specified Semantic Class.
     */
    private SemClassDataProvider _getOrCreateSemClassProvider(int projectId, SemClassInfo parentClassInfo) {
        ByProject forProject = providersByProject.get(projectId);
        if (forProject == null) {
            forProject = new ByProject(projectId);
            providersByProject.put(projectId, forProject);
        }
        Integer parentSemClassId = parentClassInfo != null ? parentClassInfo.getId() : null;
        SemClassDataProvider forSemclass = forProject.providerBySemClass.get(parentSemClassId);
        if (forSemclass == null) {
            forSemclass = new SemClassDataProvider(SemClassInfo.KEY_PROVIDER, projectId, parentClassInfo);
            forProject.providerBySemClass.put(parentSemClassId, forSemclass);
        }
        return forSemclass;
    }

    private ClassDetailMembersDataProvider _getOrCreateDetailSemClassProvider(int projectId, SemClassInfo semClassInfo) {
        ByProject forProject = providersByProject.get(projectId);
        if (forProject == null) {
            forProject = new ByProject(projectId);
            providersByProject.put(projectId, forProject);
        }
        Integer semClassId = semClassInfo.getId();
        ClassDetailMembersDataProvider forSemclass = forProject.detailsProviderBySemClass.get(semClassId);
        if (forSemclass == null) {
            forSemclass = new ClassDetailMembersDataProvider(TermInfo.KEY_PROVIDER, projectId, semClassInfo);
            forProject.detailsProviderBySemClass.put(semClassId, forSemclass);
        }
        return forSemclass;
    }
    // The global repository of DataProviders
    private static final ProviderStore providerStore = new ProviderStore();

    /**
     * @return the part of the repository specific to the specified terminology
     * project
     */
    public static ByProject forProject(int projectId) {
        return providerStore._forProject(projectId);
    }

    public static SemClassDataProvider getOrCreateSemClassProvider(int projectId, SemClassInfo parentClassInfo) {
        return providerStore._getOrCreateSemClassProvider(projectId, parentClassInfo);
    }

    public static ClassDetailMembersDataProvider getOrCreateSemClassDetailProvider(int projectId, SemClassInfo classInfo) {
        return providerStore._getOrCreateDetailSemClassProvider(projectId, classInfo);
    }

    public static void clear() {
        providerStore._clear();
    }
}
