/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import fr.inra.mig_bibliome.alvisae.client.SemClass.CellFactory.AbstractTermCell;
import fr.inra.mig_bibliome.alvisae.client.SemClass.CellFactory.SemClassNodeInfo;
import fr.inra.mig_bibliome.alvisae.client.SemClass.CellFactory.TermNodeInfo;
import java.util.ArrayList;

/**
 *
 * @author fpapazian
 */
public class ClassDetailTreeViewModel implements TreeViewModel {

    private final AbstractSelectionModel<TermInfo> selectionModel;
    private final int projectId;
    private SemClassInfo semClassInfo = null;
    private ClassDetailRootDataProvider rootDataProvider = null;
    private ClassDetailMembersDataProvider rootChildrenDataProvider = null;

    public ClassDetailTreeViewModel(AbstractSelectionModel<TermInfo> selectionModel, int projectId) {
        super();
        this.selectionModel = selectionModel;
        this.projectId = projectId;
    }

    public void setSemanticClass(SemClassInfo info) {
        this.semClassInfo = info;

        if (rootDataProvider != null && info != null && !info.isRoot()) {
            ArrayList<SemClassInfo> dataInRange;
            dataInRange = new ArrayList<SemClassInfo>();
            dataInRange.add(info);
            rootDataProvider.updateRowCount(dataInRange.size(), true);
            rootDataProvider.updateRowData(0, dataInRange);
        }
    }
    
    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        if (value instanceof TermMemberInfo) {
            ClassDetailLinkedDataProvider linkedProvider = new ClassDetailLinkedDataProvider((TermMemberInfo) value);
            return new TermNodeInfo(linkedProvider, (AbstractTermCell<TermInfo>) CellFactory.getLinkedNodeCell(), selectionModel, null);
        } else if (value instanceof SemClassInfo) {
            rootChildrenDataProvider = ProviderStore.getOrCreateSemClassDetailProvider(projectId, semClassInfo);
            return new TermNodeInfo(rootChildrenDataProvider, (AbstractTermCell<TermInfo>) CellFactory.getMemberNodeCell(), selectionModel, null);
        } else if (value instanceof TermLinkedInfo) {
            return null;
        } else {
            rootDataProvider = new ClassDetailRootDataProvider(SemClassInfo.KEY_PROVIDER, semClassInfo);
            return new SemClassNodeInfo(rootDataProvider, CellFactory.getSemClassNodeCell(), null, null);
        }
    }

    @Override
    public boolean isLeaf(Object value) {
        boolean result = true;
        if (value != null) {
            if (value instanceof TermInfo) {
                if (value instanceof TermLinkedInfo) {
                    result = true;
                } else if (value instanceof TermMemberInfo) {
                    TermMemberInfo info = (TermMemberInfo) value;
                    result = info.getTerm().getLinkedTerms().length() == 0;
                }
            } else if (value instanceof SemClassInfo) {
                //the Semantic Class node has always at least one child, the class representative
                result = false;
            }
        } else {
            //the root node is not a leaf!!!
            result = false;
        }
        return result;
    }
}
