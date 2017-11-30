/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientExtGinInjector;
import fr.inra.mig_bibliome.alvisae.client.SemClass.CellFactory.SemClassExtendedNodeCell;
import fr.inra.mig_bibliome.alvisae.client.SemClass.CellFactory.SemClassExtendedNodeInfo;
import fr.inra.mig_bibliome.alvisae.client.SemClass.StructTermUi.SemClassTreeDnDManager;
import fr.inra.mig_bibliome.alvisae.client.StructTermResources;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TermAnnotation;
import gwtquery.plugins.draggable.client.DraggableOptions;
import gwtquery.plugins.draggable.client.DraggableOptions.CursorAt;
import gwtquery.plugins.draggable.client.DraggableOptions.DragFunction;
import gwtquery.plugins.draggable.client.DraggableOptions.RevertOption;
import gwtquery.plugins.draggable.client.events.DragContext;
import gwtquery.plugins.droppable.client.DroppableOptions;

/**
 *
 * @author fpapazian
 */
public class SemClassTreeViewModel implements TreeViewModel {

    private static final StaneClientExtGinInjector termInjector = GWT.create(StaneClientExtGinInjector.class);

    static {
        StructTermResources.INSTANCE.css().ensureInjected();
    }
    //
    private static final DragFunction initHelperContent = new DragFunction() {

        @Override
        public void f(DragContext context) {
            SemClassExtendedInfo draggedSemClass = context.getDraggableData();
            context.getHelper().setInnerHTML(draggedSemClass.getCanonicLabel());
        }
    };
    //
    private final SingleSelectionModel<SemClassInfo> selectionModel;
    private final int projectId;
    private final String dragContainerId;
    private final SemClassTreeDnDManager treeEventCallback;
    private final SemClassExtendedNodeCell cell;
    private final DraggableOptions stdDragOptions;

    public SemClassTreeViewModel(SingleSelectionModel<SemClassInfo> selectionModel, int projectId, String dragContainerId, SemClassTreeDnDManager treeEventCallback) {
        super();
        this.selectionModel = selectionModel;
        this.projectId = projectId;
        this.dragContainerId = dragContainerId;
        this.treeEventCallback = treeEventCallback;
        cell = new CellFactory.SemClassExtendedNodeCell(treeEventCallback);

        stdDragOptions = initDragOption();
    }

    private DraggableOptions initDragOption() {
        DraggableOptions dragOptions = new DraggableOptions();
        dragOptions.setHelper($(CellFactory.TEMPLATES.outerHelper(StructTermResources.INSTANCE.css().DragHelper()).asString()));

        // the cell being greater than the helper, force the position of the helper on the mouse cursor.
        dragOptions.setCursorAt(new CursorAt(10, -2, null, null));
        dragOptions.setRevert(RevertOption.ON_INVALID_DROP);
        // fill Helper when drag start 
        dragOptions.setOnDragStart(initHelperContent);

        dragOptions.setHandle("." + CellFactory.SEMCLASSDRAGHND_CLASSNAME);
        dragOptions.setAppendTo("body");
        dragOptions.setOpacity((float) 0.8);
        dragOptions.setCursor(Cursor.MOVE);
        dragOptions.setContainment("#" + dragContainerId);
        dragOptions.setScroll(true);
        dragOptions.setScope(TermAnnotation.DragNDropScope);
        return dragOptions;
    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {

        SemClassInfo parentClassInfo = null;
        if ((value != null) && (value instanceof SemClassInfo)) {
            parentClassInfo = (SemClassInfo) value;
        }
        SemClassDataProvider semClassDataProvider = ProviderStore.getOrCreateSemClassProvider(projectId, parentClassInfo);

        //the updater enforce that only one Semantic class is marked at the same time
        ValueUpdater<SemClassInfo> valueUpdater = new ValueUpdater<SemClassInfo>() {

            @Override
            public void update(SemClassInfo value) {
                ProviderStore.forProject(projectId).toggleMarked(value.getId());
            }
        };
        SemClassExtendedNodeInfo ni = new CellFactory.SemClassExtendedNodeInfo(semClassDataProvider, cell, selectionModel, valueUpdater);

        DroppableOptions dropOptions = ni.getDroppableOptions();

        if (parentClassInfo == null) {
            ni.setCellDroppableOnly();

        } else {
            //draggable and droppable
            ni.setCellDragAndDropBehaviour(null);
            ni.setDraggableOptions(stdDragOptions);
        }
        
        dropOptions.setOnDrop(treeEventCallback.getSemClassDropHandler());
        dropOptions.setScope(TermAnnotation.DragNDropScope);
        return ni;
    }

    @Override
    public boolean isLeaf(Object value) {
        if ((value != null) && (value instanceof SemClassInfo)) {
            SemClassInfo info = (SemClassInfo) value;
            return !info.isRoot() && !info.hasChild();
        } else {
            //the root node is not a leaf!!!            
            return false;
        }
    }
}
