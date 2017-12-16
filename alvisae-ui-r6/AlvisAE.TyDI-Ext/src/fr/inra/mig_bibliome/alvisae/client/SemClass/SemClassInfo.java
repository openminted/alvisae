/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.view.client.ProvidesKey;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClass;

/**
 *
 * @author fpapazian
 */
public class SemClassInfo implements Comparable<SemClassInfo> {

    public static final ProvidesKey<SemClassInfo> KEY_PROVIDER = new ProvidesKey<SemClassInfo>() {

        @Override
        public Object getKey(SemClassInfo item) {
            return item == null ? null : item.getId();
        }
    };
    private final int classId;
    private final int projectId;

    public SemClassInfo(int projectId, int classId) {
        this.projectId = projectId;
        this.classId = classId;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getId() {
        return classId;
    }

    public SemClass getFromCache() {
        SemClass semClass = ProviderStore.forProject(projectId).getCacheSemClass(getId());
        return semClass;
    }
    
    public String getCanonicLabel() {
        return getFromCache().getCanonicLabel();
    }

    public boolean isRooted() {
        return getFromCache().isRooted();
    }

    public boolean isRoot() {
        return getId() == SemClass.ROOT_ID;
    }

    public boolean hasChild() {
        return getFromCache()!=null ? getFromCache().getHypoGroupIds().length() > 0 : false;
    }

    public boolean hasSeveralHyperonym() {
        return getFromCache().getHyperGroupIds().length() > 1;
    }

    @Override
    public final int compareTo(SemClassInfo o) {
        return new Integer(this.getId()).compareTo(o.getId());
    }

}
