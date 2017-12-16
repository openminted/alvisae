/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Task;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.http.client.Response;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.*;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Annotation.CombinedAnnotationCell;
import fr.inra.mig_bibliome.alvisae.client.Annotation.Grid2;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicTaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocOffsetTaskParams;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Conso.AnnotationSetConsoPlace;
import fr.inra.mig_bibliome.alvisae.client.Document.DocEditingPlace;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.client.StaneCoreResources;
import fr.inra.mig_bibliome.alvisae.client.StaneResources;
import fr.inra.mig_bibliome.alvisae.client.Start.MainToolBar;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.DetailedAsyncResponseHandler;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.NetworkActivityDisplayer;
import fr.inra.mig_bibliome.alvisae.client.data3.*;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskStatus;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import java.util.*;

/**
 * View used to display the campaigns and the related tasks, and choose the task
 * to perform
 *
 * @author fpapazian
 */
public class TaskInstancesList extends Composite implements TaskSelectingView {

    interface TaskInstancesListUiBinder extends UiBinder<DockLayoutPanel, TaskInstancesList> {
    }
    private static TaskInstancesListUiBinder uiBinder = GWT.create(TaskInstancesListUiBinder.class);
    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);

    static class CampaignTaskDefs extends HashMap<Integer, TaskDefinitionImpl> {
    }

    static class CampaignDocs extends HashMap<Integer, DocumentInfoImpl> {
    }

    static {
        StaneResources.INSTANCE.style().ensureInjected();
    }

    interface Styles extends CssResource {

        String SelectedRow();
    }
    //
    @UiField
    Panel toolBarHolder;
    @UiField
    Grid2 campaignGrid;
    @UiField
    Label workflowLabel;
    @UiField(provided = true)
    DataGrid<TaskDefinitionImpl> workflowGrid;
    @UiField
    Label taskDocLabel;
    @UiField
    PushButton nextTaskInstButton;
    @UiField
    PushButton lastTaskInstButton;
    @UiField(provided = true)
    ScrollExposedDataGrid<TaskInstanceImpl> taskInstancesGrid;
    @UiField
    PushButton refreshButton;
    @UiField
    NetworkActivityDisplayer networkActivityDisplayer;
    //
    @UiField
    Styles style;
    //
    private Presenter presenter;
    private CampaignListImpl campaignList;
    private TaskInstanceListImpl docList;
    private HashMap<Integer, CampaignTaskDefs> taskDefsByCampaign = new HashMap<Integer, CampaignTaskDefs>();
    private HashMap<Integer, CampaignDocs> docsByCampaign = new HashMap<Integer, CampaignDocs>();
    private HashMap<Integer, AnnotationSchemaDefinition> schemaByCampaign = new HashMap<Integer, AnnotationSchemaDefinition>();
    private Integer selectedCampaignId = null;
    private Integer selectedDocId = null;
    private Integer selectedUserId = null;
    private Integer selectedTaskId = null;
    private SingleSelectionClickHandler selectionHndlr = null;
    private final ListDataProvider<TaskDefinitionImpl> taskDefinitionsProvider = new ListDataProvider<TaskDefinitionImpl>(TaskDefinitionImpl.KEY_PROVIDER);
    private SingleSelectionModel<TaskInstanceImpl> selectionModel;
    private ArrayList<TaskInstanceImpl> instances = new ArrayList<TaskInstanceImpl>();
    private AsyncDataProvider<TaskInstanceImpl> taskInstancesAsyncProvider;
    private ListHandler<TaskInstanceImpl> sortHandler;
    //used to distinghish 'sorting' from 'reload from server' event for taskInstancesGrid
    private boolean reloadRequired = false;
    private AsyncHandler asynchSortHandler;
    private Column<TaskInstanceImpl, TaskInstanceImpl> statusCol;
    //Number of Task Instance displayed per page
    private int taskInstFirstPageSize = 50;    
    private int taskInstOtherPageSize = 150;
    private int lastScrollPos = 0;

    static class SingleSelectionClickHandler implements ClickHandler {

        public static interface RowSelectedHandler {

            public void rowSelected(int row);

            public void rowUnSelected(int row);
        }
        private final Grid2 grid;
        private final String styleName;
        private final SingleSelectionClickHandler.RowSelectedHandler rowSelectedHandler;

        public SingleSelectionClickHandler(Grid2 grid, String styleName, SingleSelectionClickHandler.RowSelectedHandler rowSelectedHandler) {
            this.grid = grid;
            this.styleName = styleName;
            this.rowSelectedHandler = rowSelectedHandler;
        }
        private Integer lastSelected = null;

        @Override
        public void onClick(ClickEvent event) {
            Integer row = grid.getRowForEvent(event.getNativeEvent());
            setSelected(row);
        }

        void reset() {
            lastSelected = null;
        }

        private void setSelected(Integer row) {
            if (row != null) {
                if (row < grid.getRowCount()) {
                    if (row != lastSelected) {
                        if (lastSelected != null) {
                            grid.getRowFormatter().removeStyleName(lastSelected, styleName);
                        }
                        grid.getRowFormatter().addStyleName(row, styleName);
                        lastSelected = row;
                        if (rowSelectedHandler != null) {
                            rowSelectedHandler.rowSelected(row);
                        }
                    }
                }
            }
        }

        public Integer getSelectedRow() {
            return lastSelected;
        }
    }

    private void refreshCampaignList() {
        refreshCampaignList(null);
    }

    private void refreshCampaignList(final Command executedOnSuccess) {
        nextTaskInstButton.setEnabled(false);
        lastTaskInstButton.setEnabled(false);

        taskDefsByCampaign.clear();
        schemaByCampaign.clear();
        docsByCampaign.clear();
        instances.clear();
        taskInstancesGrid.setVisibleRange(0, 0);

        campaignGrid.resize(0, 2);

        selectedDocId = null;
        selectedTaskId = null;

        selectedUserId = injector.getCoreDataProvider().getRequestManager().getCurrentUserId();

        injector.getCoreDataProvider().getUserCampaignList(selectedUserId, new AsyncCallback<CampaignListImpl>() {
            @Override
            public void onFailure(Throwable caught) {
                campaignGrid.resize(0, 1);
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(CampaignListImpl result) {
                campaignList = result;
                campaignGrid.resize(campaignList.length(), 3);
                for (int row = 0; row < campaignList.length(); row++) {
                    campaignGrid.setText(row, 0, String.valueOf(campaignList.get(row).getId()));
                    campaignGrid.setText(row, 1, campaignList.get(row).getDisplayName());
                    final String guidelines = campaignList.get(row).getGuidelinesUrl();
                    if (guidelines != null) {
                        PushButton hpl = new PushButton(new Image(StaneCoreResources.INSTANCE.GuidelinesIcon()), new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                Window.open(guidelines, "_blank", "");
                            }
                        });
                        campaignGrid.setWidget(row, 2, hpl);
                    }
                }
                if (executedOnSuccess != null) {
                    Scheduler.get().scheduleDeferred(executedOnSuccess);
                }
            }
        });

    }

    private void refreshTaskInstanceList() {
        //
        taskDefinitionsProvider.getList().clear();
        workflowGrid.setVisibleRange(0, taskDefinitionsProvider.getList().size());

        final Integer campaignId = getSelectedCampaignIn();
        if (campaignId != null) {
            if (taskDefsByCampaign.get(campaignId) == null) {

                taskDefinitionsProvider.getList().clear();

                injector.getCoreDataProvider().getWorkflow(campaignId, new AsyncCallback<CDXWS_Workflow_ResponseImpl>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(CDXWS_Workflow_ResponseImpl result) {
                        taskDefinitionsProvider.getList().clear();

                        schemaByCampaign.put(campaignId, result.getAnnotationSchema());
                        TaskDefinitionListImpl defs = result.getTaskDefinitions();
                        CampaignTaskDefs campaignTaskDefs = new CampaignTaskDefs();
                        for (int row = 0; row < defs.length(); row++) {
                            TaskDefinitionImpl td = defs.get(row);
                            campaignTaskDefs.put(td.getId(), td);
                            taskDefinitionsProvider.getList().add(td);
                        }
                        workflowGrid.setVisibleRange(0, taskDefinitionsProvider.getList().size());
                        taskDefsByCampaign.put(campaignId, campaignTaskDefs);
                    }
                });
            } else {
                taskDefinitionsProvider.getList().clear();
                for (TaskDefinitionImpl td : taskDefsByCampaign.get(campaignId).values()) {
                    taskDefinitionsProvider.getList().add(td);
                }
                workflowGrid.setVisibleRange(0, taskDefinitionsProvider.getList().size());
            }

            reloadRequired = true;
            taskInstancesGrid.setVisibleRangeAndClearData(taskInstancesGrid.getVisibleRange(), true);
        }

    }

    public TaskInstancesList() {
        workflowGrid = new DataGrid<TaskDefinitionImpl>();
        taskInstancesGrid = new ScrollExposedDataGrid<TaskInstanceImpl>();
        initWidget(uiBinder.createAndBindUi(this));
        taskInstancesGrid.setPageSize(Integer.MAX_VALUE);
        workflowGrid.setPageSize(Integer.MAX_VALUE);

        toolBarHolder.add(new MainToolBar("help/AaeUserGuide.html#Task-and-Workflow"));

        networkActivityDisplayer.setRequestManager(injector.getCoreDataProvider().getRequestManager());

        nextTaskInstButton.setEnabled(false);
        lastTaskInstButton.setEnabled(false);
        //infinite scrolling : new page displayed when scrolling down
        taskInstancesGrid.getScrollPanel().addScrollHandler(new ScrollHandler() {
            @Override
            public void onScroll(ScrollEvent event) {

                int oldScrollPos = lastScrollPos;
                lastScrollPos = taskInstancesGrid.getScrollPanel().getVerticalScrollPosition();

                // If scrolling up, ignore the event.
                if (oldScrollPos >= lastScrollPos) {
                    return;
                }

                //Height of grid contents (including outside the viewable area) - height of the scroll panel
                int maxScrollTop = taskInstancesGrid.getScrollPanel().getWidget().getOffsetHeight()
                        - taskInstancesGrid.getScrollPanel().getOffsetHeight();

                if (lastScrollPos >= maxScrollTop) {
                    displayMoreInstances(taskInstOtherPageSize);
                }
            }
        });

        initInstanceTable();
        initWorkflowTable();

        refreshCampaignList();

        selectionHndlr = new SingleSelectionClickHandler(campaignGrid, style.SelectedRow(), new SingleSelectionClickHandler.RowSelectedHandler() {
            @Override
            public void rowSelected(int row) {
                selectedCampaignId = campaignList.get(row).getId();
                String label = "\"" + campaignList.get(row).getDisplayName() + "\"";
                taskDocLabel.setText(label);
                workflowLabel.setText(label);
                refreshTaskInstanceList();
            }

            @Override
            public void rowUnSelected(int row) {
                selectedCampaignId = null;
                selectedDocId = null;
                selectedTaskId = null;
                taskDocLabel.setText("");
                workflowLabel.setText("");

                refreshTaskInstanceList();
            }
        });
        campaignGrid.addClickHandler(selectionHndlr);


    }

    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    static class TaskNameCell extends AbstractCell<TaskDefinitionImpl> {

        public static interface TaskNameCellTemplates extends SafeHtmlTemplates {

            @SafeHtmlTemplates.Template("margin-left:{0}EM;")
            public SafeHtml leveledMargin(int level);

            @SafeHtmlTemplates.Template("<span style='{0}'>{1}</span>")
            public SafeHtml leveledName(SafeStyles level, String taskName);
        }
        static final TaskNameCellTemplates TEMPLATES = GWT.create(TaskNameCellTemplates.class);

        static class LeveledMargin implements SafeStyles {

            private final int level;

            public LeveledMargin(int level) {
                this.level = level;
            }

            @Override
            public String asString() {
                return TEMPLATES.leveledMargin(level).asString();
            }
        }

        @Override
        public void render(Cell.Context context, TaskDefinitionImpl taskDef, SafeHtmlBuilder sb) {
            sb.append(TEMPLATES.leveledName(new LeveledMargin(taskDef.getPrecedencelevel()), taskDef.getName()));
        }
    }

    static class TaskAnnotFeatureCell extends AbstractCell<TaskDefinitionImpl> {

        public static interface TaskAnnotFeatureCellTemplates extends SafeHtmlTemplates {

            @SafeHtmlTemplates.Template("<table height='100%' width='100%'><tr><td width='50%'>{0}</td><td width='50%'>{1}</td></tr></table>")
            public SafeHtml containerDiv(SafeHtml leftcontent, SafeHtml rightcontent);

            @SafeHtmlTemplates.Template("<div style='background:#E5E5E5;' height='100%' width='100%'>&nbsp;</div>")
            public SafeHtml emptyDiv();

            @SafeHtmlTemplates.Template("<div title='{1}' height='100%' width='100%'>{0}</div>")
            public SafeHtml iconDiv(SafeHtml icon, String title);
        }
        static final TaskAnnotFeatureCellTemplates TEMPLATES = GWT.create(TaskAnnotFeatureCellTemplates.class);
        static SafeHtml canEditAnnotIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.CanEditAnnotationIcon()).getHTML());
        static SafeHtml canRefAnnotIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.CanReferenceAnnotationIcon()).getHTML());
//
        private final String columnAnnotationTypeName;
        private final Set<Integer> referencableBy = new HashSet<Integer>();

        public TaskAnnotFeatureCell(String columnAnnotationTypeName, List<TaskDefinitionImpl> taskDefsList, AnnotationSchemaDefinition annotationSchema) {
            this.columnAnnotationTypeName = columnAnnotationTypeName;

            defLoop:
            for (TaskDefinitionImpl taskDef : taskDefsList) {
                for (String editedType : taskDef.getEditedAnnotationTypes()) {
                    AnnotationTypeDefinition annTypeDef = annotationSchema.getAnnotationTypeDefinition(editedType);
                    if (AnnotationKind.GROUP.equals(annTypeDef.getAnnotationKind())) {
                        if (annTypeDef.getAnnotationGroupDefinition().getComponentsTypes().contains(columnAnnotationTypeName)) {
                            referencableBy.add(taskDef.getId());
                            continue defLoop;
                        }
                    } else if (AnnotationKind.RELATION.equals(annTypeDef.getAnnotationKind())) {
                        for (String role : annTypeDef.getRelationDefinition().getSupportedRoles()) {
                            if (annTypeDef.getRelationDefinition().getArgumentTypes(role).contains(columnAnnotationTypeName)) {
                                referencableBy.add(taskDef.getId());
                                continue defLoop;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void render(Context context, TaskDefinitionImpl taskDef, SafeHtmlBuilder sb) {
            boolean canEdit = taskDef.getEditedAnnotationTypes().contains(columnAnnotationTypeName);
            boolean canReference = referencableBy.contains(taskDef.getId());
            SafeHtml editAnnotIcon = canEdit ? TEMPLATES.iconDiv(canEditAnnotIcon, "'" + columnAnnotationTypeName + "' annotations can be edited in task '" + taskDef.getName() + "'") : TEMPLATES.emptyDiv();
            SafeHtml refAnnotIcon = canReference ? TEMPLATES.iconDiv(canRefAnnotIcon, "'" + columnAnnotationTypeName + "' annotations can be referenced in task '" + taskDef.getName() + "'") : TEMPLATES.emptyDiv();

            sb.append(TEMPLATES.containerDiv(editAnnotIcon, refAnnotIcon));
        }
    }

    private void initWorkflowTable() {

        workflowGrid.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);


        Column<TaskDefinitionImpl, TaskDefinitionImpl> nameCol =
                new Column<TaskDefinitionImpl, TaskDefinitionImpl>(new TaskNameCell()) {
            @Override
            public TaskDefinitionImpl getValue(TaskDefinitionImpl taskDef) {
                return taskDef;
            }
        };
        workflowGrid.addColumn(nameCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Task")));


        Column<TaskDefinitionImpl, String> reviewCol =
                new Column<TaskDefinitionImpl, String>(new TextCell()) {
            @Override
            public String getValue(TaskDefinitionImpl taskDef) {
                CampaignTaskDefs campaignTaskDefs = taskDefsByCampaign.get(selectedCampaignId);
                Integer reviewedId = taskDef.getReviewedTask();
                String reviewed = campaignTaskDefs != null && reviewedId != null ? campaignTaskDefs.get(reviewedId).getName() : "";
                return reviewed;
            }
        };
        workflowGrid.addColumn(reviewCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("ReviewOf")));


        Column<TaskDefinitionImpl, String> cardinalityCol =
                new Column<TaskDefinitionImpl, String>(new TextCell()) {
            @Override
            public String getValue(TaskDefinitionImpl taskDef) {
                if (taskDef.getCardinality() == -1) {
                    return "*";
                } else {
                    return String.valueOf(taskDef.getCardinality());
                }
            }
        };
        workflowGrid.addColumn(cardinalityCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Card.")));

        Column<TaskDefinitionImpl, SafeHtml> editedTypeCol =
                new Column<TaskDefinitionImpl, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(TaskDefinitionImpl taskDef) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                AnnotationSchemaDefinition annotationSchema = schemaByCampaign.get(selectedCampaignId);
                for (String typeName : taskDef.getEditedAnnotationTypes()) {
                    SafeHtmlBuilder s = new SafeHtmlBuilder();
                    AnnotationTypeDefinition annTypeDef = annotationSchema.getAnnotationTypeDefinition(typeName);
                    CombinedAnnotationCell.renderKindAsInlinedImg(annTypeDef.getAnnotationKind(), s);
                    s.append(TaskStatusCell.backgroundColorBlock(annTypeDef.getColor(), typeName, typeName));
                    sb.append(TaskStatusCell.TEMPLATES.typeBlock(s.toSafeHtml()));
                    sb.appendHtmlConstant("&nbsp;");
                }
                return sb.toSafeHtml();
            }
        };
        workflowGrid.addColumn(editedTypeCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Editable annotation types")));

        workflowGrid.setColumnWidth(nameCol, 18, Unit.EM);
        workflowGrid.setColumnWidth(reviewCol, 15, Unit.EM);
        workflowGrid.setColumnWidth(cardinalityCol, 4, Unit.EM);

        taskDefinitionsProvider.addDataDisplay(workflowGrid);
    }

    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    static class TaskStatusCell extends AbstractCell<TaskInstanceImpl> {

        public static interface TaskStatusCellTemplates extends SafeHtmlTemplates {

            @SafeHtmlTemplates.Template("background:{0};")
            public SafeHtml backgroungColor(String color);

            @SafeHtmlTemplates.Template("<span height='100%' width='100%' style='{0};' title='{2}'>{1}</span>")
            public SafeHtml coloredStatus2(SafeStyles color, String content, String title);

            @SafeHtmlTemplates.Template("<div height='100%' width='100%'>{0}&nbsp;{1}&nbsp;</div>")
            public SafeHtml graphicStatus(SafeHtml statusIcon, String content);

            @SafeHtmlTemplates.Template("<div height='100%' width='100%' title='{2}'>{0}&nbsp;{1}&nbsp;{3}</div>")
            public SafeHtml invalidatedGraphicStatus(SafeHtml statusIcon, String content, String warningTitle, SafeHtml invalidatedIcon);

            @SafeHtmlTemplates.Template("<div style='float:left; height: 1.7em; margin-right: 4px; margin-bottom: 2px; padding: 3px; border: 1px solid silver;'>{0}</div>")
            public SafeHtml typeBlock(SafeHtml content);

            @SafeHtmlTemplates.Template("<span title='Id:{1}'>{0}</span>")
            public SafeHtml textAndId(String text, int id);
        }
        static final TaskStatusCellTemplates TEMPLATES = GWT.create(TaskStatusCellTemplates.class);
        static SafeHtml invalidatedIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.InvalidatedAnnotationSetIcon()).getHTML());
        static SafeHtml todoIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.TaskToDoIcon()).getHTML());
        static SafeHtml pendingIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.TaskPendingIcon()).getHTML());
        static SafeHtml doneIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.TaskDoneIcon()).getHTML());
        static SafeHtml upcomingIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.TaskUpcomingIcon()).getHTML());
        static SafeHtml naDoneIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneResources.INSTANCE.TaskNADoneIcon()).getHTML());

        public static SafeHtml backgroundColorBlock(String color, String content, String title) {
            return TEMPLATES.coloredStatus2(new ColorStyle(color), content, title);
        }
        private static Map<TaskStatus, SafeHtml> iconByStatus = new EnumMap<TaskStatus, SafeHtml>(TaskStatus.class);

        {
            iconByStatus.put(TaskStatus.Pending, pendingIcon);
            iconByStatus.put(TaskStatus.Done, doneIcon);
            iconByStatus.put(TaskStatus.ToDo, todoIcon);
            iconByStatus.put(TaskStatus.Upcoming, upcomingIcon);
            iconByStatus.put(TaskStatus.Unavailable_Done, naDoneIcon);
        }

        static class ColorStyle implements SafeStyles {

            private final String color;

            public ColorStyle(String color) {
                this.color = color;
            }

            @Override
            public String asString() {
                return TEMPLATES.backgroungColor(color).asString();
            }
        }

        @Override
        public void render(Context context, TaskInstanceImpl taskInst, SafeHtmlBuilder sb) {
            TaskStatus status = taskInst.getStatus();
            if (taskInst.isInvalidated() != null && taskInst.isInvalidated()) {
                sb.append(TEMPLATES.invalidatedGraphicStatus(iconByStatus.get(status), status.toString(), "this task is referencing outdated Annotation Set revision", invalidatedIcon));
            } else {
                sb.append(TEMPLATES.graphicStatus(iconByStatus.get(status), status.toString()));
            }
        }
    }

    private void displayMoreInstances(final int increment) {
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                int prevLastIndex = taskInstancesGrid.getVisibleRange().getLength();
                int lastIndex = Math.min(prevLastIndex + increment, instances.size());
                taskInstancesGrid.setVisibleRange(0, lastIndex);
                boolean hasMore = lastIndex < instances.size();
                if (hasMore) {
                    nextTaskInstButton.setTitle("Retrieve one more page  (" + lastIndex + " > " + Math.min(lastIndex + taskInstOtherPageSize, instances.size()) + ")");
                    lastTaskInstButton.setTitle("Retrieve all  (>> " + instances.size() + ")");
                } else {
                    nextTaskInstButton.setTitle("");
                    lastTaskInstButton.setTitle("");
                }
                nextTaskInstButton.setEnabled(hasMore);
                lastTaskInstButton.setEnabled(hasMore);
            }
        });
    }

    private void initInstanceTable() {
        sortHandler = new ListHandler<TaskInstanceImpl>(instances);
        asynchSortHandler = new AsyncHandler(taskInstancesGrid);
        taskInstancesGrid.setVisibleRange(0, 0);

        taskInstancesAsyncProvider = new AsyncDataProvider<TaskInstanceImpl>() {
            private void refreshTaskList() {
                final Integer campaignId = getSelectedCampaignIn();
                instances.clear();

                taskInstancesGrid.getColumnSortList().clear();
                if (campaignId != null && selectedUserId != null) {
                    Scheduler.get().scheduleDeferred(new Command() {
                        @Override
                        public void execute() {
                            injector.getCoreDataProvider().getTaskList(campaignId, selectedUserId, new AsyncCallback<TaskInstanceListImpl>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    updateRowCount(0, true);
                                    Window.alert("Error while retrieving Task list: " + caught.getMessage());
                                    throw new UnsupportedOperationException("Error while retrieving Task list.", caught);
                                }

                                @Override
                                public void onSuccess(TaskInstanceListImpl result) {
                                    for (int row = 0; row < result.length(); row++) {
                                        TaskInstanceImpl taskInfo = result.get(row);
                                        instances.add(taskInfo);
                                    }
                                    //display only the first page
                                    int nbDisplayedTaskInst = Math.min(taskInstFirstPageSize, instances.size());
                                    taskInstancesAsyncProvider.updateRowCount(nbDisplayedTaskInst, true);
                                    taskInstancesAsyncProvider.updateRowData(0, instances);
                                    displayMoreInstances(nbDisplayedTaskInst);
                                }
                            });
                        }
                    });
                } else {
                    updateRowCount(0, true);
                }
                //by default task are sorted on their status
                taskInstancesGrid.getColumnSortList().push(statusCol);


            }

            @Override
            protected void onRangeChanged(HasData<TaskInstanceImpl> display) {
                final Range range = display.getVisibleRange();

                //Check if this event has been triggered for sorting only
                if (!reloadRequired) {

                    //use ListHandler to perform sorting on the client side
                    final ColumnSortList sortList = taskInstancesGrid.getColumnSortList();

                    ColumnSortEvent.fire(new HasHandlers() {
                        @Override
                        public void fireEvent(GwtEvent<?> event) {
                            sortHandler.onColumnSort((ColumnSortEvent) event);
                        }
                    }, sortList);

                    updateRowCount(instances.size(), true);
                    updateRowData(0, instances);

                } else {

                    final Integer campaignId = getSelectedCampaignIn();
                    if (selectedUserId != null && (docsByCampaign.get(campaignId) == null)) {

                        selectedUserId = injector.getCoreDataProvider().getRequestManager().getCurrentUserId();

                        injector.getCoreDataProvider().getDocumentInfoList(selectedUserId, campaignId, new AsyncCallback<DocumentInfoListImpl>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                updateRowCount(0, true);
                                Window.alert("Error while retrieving document list:" + caught.getMessage());
                                throw new UnsupportedOperationException("Error while retrieving document list:" + caught.getMessage());
                            }

                            @Override
                            public void onSuccess(DocumentInfoListImpl result) {
                                CampaignDocs campaignDocs = new CampaignDocs();

                                for (int row = 0; row < result.length(); row++) {
                                    DocumentInfoImpl docInfo = result.get(row);
                                    campaignDocs.put(docInfo.getId(), docInfo);
                                }
                                docsByCampaign.put(campaignId, campaignDocs);

                                refreshTaskList();
                            }
                        });

                    } else {
                        refreshTaskList();
                    }
                }

                reloadRequired = false;
            }
        };

        //taskInstancesProvider.addDataDisplay(taskInstancesGrid);
        Image loadingIndicator = new Image();
        loadingIndicator.setResource(StaneResources.INSTANCE.ProgressBarImage());

        taskInstancesGrid.setLoadingIndicator(loadingIndicator);
        taskInstancesGrid.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        taskInstancesGrid.addColumnSortHandler(asynchSortHandler);

        selectionModel = new SingleSelectionModel<TaskInstanceImpl>(TaskInstanceImpl.KEY_PROVIDER);
        DefaultSelectionEventManager<TaskInstanceImpl> selectionHandler = DefaultSelectionEventManager.<TaskInstanceImpl>createDefaultManager();
        taskInstancesGrid.setSelectionModel(selectionModel, selectionHandler);

        Column<TaskInstanceImpl, TaskInstanceImpl> documentCol = new Column<TaskInstanceImpl, TaskInstanceImpl>(new AbstractCell<TaskInstanceImpl>() {
            @Override
            public void render(Context context, TaskInstanceImpl taskInst, SafeHtmlBuilder sb) {
                CampaignDocs campaignDocs = docsByCampaign.get(selectedCampaignId);
                sb.append(TaskStatusCell.TEMPLATES.textAndId(campaignDocs.get(taskInst.getDocumentId()).getDescription(), taskInst.getDocumentId()));
            }
        }) {
            @Override
            public TaskInstanceImpl getValue(TaskInstanceImpl taskInst) {
                return taskInst;
            }
        };
        taskInstancesGrid.addColumn(documentCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Document")));
        documentCol.setSortable(true);
        sortHandler.setComparator(documentCol, new Comparator<TaskInstanceImpl>() {
            @Override
            public int compare(TaskInstanceImpl o1, TaskInstanceImpl o2) {
                CampaignDocs campaignDocs = docsByCampaign.get(selectedCampaignId);
                int order = campaignDocs.get(o1.getDocumentId()).getDescription().compareTo(campaignDocs.get(o2.getDocumentId()).getDescription());
                if (order == 0) {
                    order = new Integer(o1.getId()).compareTo(o2.getId());
                }
                return order;
            }
        });

        Column<TaskInstanceImpl, String> nameCol =
                new Column<TaskInstanceImpl, String>(new TextCell()) {
            @Override
            public String getValue(TaskInstanceImpl taskInst) {
                CampaignTaskDefs campaignTaskDefs = taskDefsByCampaign.get(selectedCampaignId);
                return campaignTaskDefs.get(taskInst.getId()).getName();
            }
        };

        taskInstancesGrid.addColumn(nameCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Task")));
        nameCol.setSortable(true);
        sortHandler.setComparator(nameCol, new Comparator<TaskInstanceImpl>() {
            @Override
            public int compare(TaskInstanceImpl o1, TaskInstanceImpl o2) {
                CampaignTaskDefs campaignTaskDefs = taskDefsByCampaign.get(selectedCampaignId);
                return campaignTaskDefs.get(o1.getId()).getName().compareTo(campaignTaskDefs.get(o2.getId()).getName());
            }
        });

        statusCol = new Column<TaskInstanceImpl, TaskInstanceImpl>(new TaskStatusCell()) {
            @Override
            public TaskInstanceImpl getValue(TaskInstanceImpl taskInst) {
                return taskInst;
            }
        };
        taskInstancesGrid.addColumn(statusCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Status")));
        statusCol.setSortable(true);
        sortHandler.setComparator(statusCol, new Comparator<TaskInstanceImpl>() {
            @Override
            public int compare(TaskInstanceImpl o1, TaskInstanceImpl o2) {
                return new Integer(o1.getStatus().toInt()).compareTo(o2.getStatus().toInt());
            }
        });

        Column<TaskInstanceImpl, TaskInstanceImpl> annotateCol = new Column<TaskInstanceImpl, TaskInstanceImpl>(
                new ImageButtonCell(StaneResources.INSTANCE.StartDocAnnotationIcon(), "Annotate this document") {
            @Override
            public boolean displayImageButton(TaskInstanceImpl taskInst) {
                CampaignTaskDefs campaignTaskDefs = taskDefsByCampaign.get(selectedCampaignId);
                boolean isReviewingTask = campaignTaskDefs.get(taskInst.getId()).getReviewedTask() != null;
                return ((!taskInst.isReadOnly())
                        && (!isReviewingTask)
                        && (taskInst.getStatus().equals(TaskStatus.ToDo)
                        || taskInst.getStatus().equals(TaskStatus.Pending)
                        || taskInst.getStatus().equals(TaskStatus.Done)));
            }

            @Override
            public void onClicked(TaskInstanceImpl value) {
                startAnnotate(value.getDocumentId(), value.getId());
            }
        }) {
            @Override
            public TaskInstanceImpl getValue(TaskInstanceImpl taskInst) {
                return taskInst;
            }
        };
        //Note: empty Header is necessary so columns to the right on this one to be sortable... (GWT 2.0.4)
        taskInstancesGrid.addColumn(annotateCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("")));

        Column<TaskInstanceImpl, TaskInstanceImpl> compareASCol = new Column<TaskInstanceImpl, TaskInstanceImpl>(
                new ImageButtonCell(StaneResources.INSTANCE.CompareAnnotationSetsIcon(), "Review preceeding Task") {
            @Override
            public boolean displayImageButton(TaskInstanceImpl taskInst) {
                CampaignTaskDefs campaignTaskDefs = taskDefsByCampaign.get(selectedCampaignId);
                boolean isReviewingTask = campaignTaskDefs.get(taskInst.getId()).getReviewedTask() != null;
                return ((!taskInst.isReadOnly())
                        && (isReviewingTask)
                        && (taskInst.getStatus().equals(TaskStatus.ToDo)
                        || taskInst.getStatus().equals(TaskStatus.Pending)
                        || taskInst.getStatus().equals(TaskStatus.Done)));
            }

            @Override
            public void onClicked(TaskInstanceImpl value) {
                startReview(value.getDocumentId(), value.getId());
            }
        }) {
            @Override
            public TaskInstanceImpl getValue(TaskInstanceImpl taskInst) {
                return taskInst;
            }
        };
        taskInstancesGrid.addColumn(compareASCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("")));

        Column<TaskInstanceImpl, TaskInstanceImpl> deleteCol = new Column<TaskInstanceImpl, TaskInstanceImpl>(
                new ImageButtonCell(StaneResources.INSTANCE.RemoveDocAnnotationIcon(), "Remove all annotations created in this Task") {
            @Override
            public boolean displayImageButton(TaskInstanceImpl taskInst) {
                return (!taskInst.isReadOnly())
                        && taskInst.getStatus().equals(TaskStatus.Pending);
            }

            @Override
            public void onClicked(TaskInstanceImpl value) {
                if (Window.confirm("Do you want to completely remove all annotations you performed on this document?\nNOTE: This action can not be undone")) {
                    removeAnnotationTask(value.getDocumentId(), value.getId());
                }
            }
        }) {
            @Override
            public TaskInstanceImpl getValue(TaskInstanceImpl taskInst) {
                return taskInst;
            }
        };
        taskInstancesGrid.addColumn(deleteCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("")));

        Column<TaskInstanceImpl, TaskInstanceImpl> publishCol = new Column<TaskInstanceImpl, TaskInstanceImpl>(
                new ImageButtonCell(StanEditorResources.INSTANCE.EndDocAnnotationIcon(), "publish the current annotated status of this document") {
            @Override
            public boolean displayImageButton(TaskInstanceImpl taskInst) {
                return (!taskInst.isReadOnly())
                        && taskInst.getStatus().equals(TaskStatus.Pending);
            }

            @Override
            public void onClicked(TaskInstanceImpl value) {
                publishAnnotationTask(value.getDocumentId(), value.getId());
            }
        }) {
            @Override
            public TaskInstanceImpl getValue(TaskInstanceImpl taskInst) {
                return taskInst;
            }
        };
        taskInstancesGrid.addColumn(publishCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("")));


        Column<TaskInstanceImpl, String> publishDateCol =
                new Column<TaskInstanceImpl, String>(new TextCell()) {
            @Override
            public String getValue(TaskInstanceImpl taskInst) {
                if (taskInst.getStatus().equals(TaskStatus.Done)) {
                    return taskInst.getPublicationDate();
                } else {
                    return null;
                }
            }
        };

        taskInstancesGrid.addColumn(publishDateCol, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Publication date")));
        publishDateCol.setSortable(true);

        sortHandler.setComparator(publishDateCol, new Comparator<TaskInstanceImpl>() {
            @Override
            public int compare(TaskInstanceImpl o1, TaskInstanceImpl o2) {
                String d1 = o1.getPublicationDate();
                d1 = d1 == null ? "" : d1;
                String d2 = o2.getPublicationDate();
                d2 = d2 == null ? "" : d2;
                return d1.compareTo(d2);
            }
        });


        //taskInstancesGrid.setWidth("100%");
        taskInstancesGrid.setColumnWidth(statusCol, 11, Unit.EM);
        taskInstancesGrid.setColumnWidth(annotateCol, 46.0, Unit.PX);
        taskInstancesGrid.setColumnWidth(deleteCol, 46.0, Unit.PX);
        taskInstancesGrid.setColumnWidth(compareASCol, 46.0, Unit.PX);
        taskInstancesGrid.setColumnWidth(publishCol, 46.0, Unit.PX);

        taskInstancesAsyncProvider.addDataDisplay(taskInstancesGrid);

    }

    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setParams(BasicUserCampaignDocOffsetTaskParams params) {
        if (selectedCampaignId != null) {
            if (params.getCampaignId() != selectedCampaignId || params.getUserId() != selectedUserId) {
                refreshCampaignList();
            } else {
                refreshTaskInstanceList();
            }
        } else {
            selectedUserId = params.getUserId();
            selectedCampaignId = params.getCampaignId();
            selectedDocId = params.getDocumentId();
            selectedTaskId = params.getTaskId();
            refreshTaskInstanceList();
        }
    }

    @Override
    public BasicUserCampaignDocOffsetTaskParams getParams() {
        if (selectedCampaignId != null) {
            return new BasicUserCampaignDocOffsetTaskParams(selectedUserId, selectedCampaignId, selectedDocId, 0, selectedTaskId);
        } else {
            return null;
        }
    }

    private Integer getSelectedCampaignIn() {
        return selectedCampaignId;
    }

    @UiHandler("refreshButton")
    void handleRefreshClick(ClickEvent e) {
        refreshCampaignList(new Command() {
            @Override
            public void execute() {
                if (selectedCampaignId != null) {
                    Integer campaingIndex = null;
                    for (int row = 0; row < campaignList.length(); row++) {
                        if (campaignList.get(row).getId() == selectedCampaignId) {
                            campaingIndex = row;
                            break;
                        }
                    }
                    selectionHndlr.reset();
                    selectionHndlr.setSelected(campaingIndex);
                }
            }
        });
    }

    @UiHandler("nextTaskInstButton")
    void handleNextTaskInstClick(ClickEvent e) {
        displayMoreInstances(taskInstOtherPageSize);
    }

    @UiHandler("lastTaskInstButton")
    void handleLastTaskInstClick(ClickEvent e) {
        displayMoreInstances(instances.size());
    }

    private void publishAnnotationTask(final int docId, final int taskId) {
        int userId = injector.getCoreDataProvider().getRequestManager().getCurrentUserId();

        injector.getCoreDataProvider().publishAnnotationTask(userId, selectedCampaignId, docId, taskId, new DetailedAsyncResponseHandler<JavaScriptObject>() {
            @Override
            public void onFailure(Response response) {
                int statusCode = response.getStatusCode();
                String responseText = response.getText();
                if (statusCode == 422) {
                    Window.alert("Publication was denied\n" + responseText);
                } else {
                    Window.alert("Error while publishing task :" + responseText);
                }
            }

            @Override
            public void onSuccess(JavaScriptObject result) {
                refreshTaskInstanceList();
            }
        });
    }

    private void removeAnnotationTask(final int docId, final int taskId) {
        int userId = injector.getCoreDataProvider().getRequestManager().getCurrentUserId();

        injector.getCoreDataProvider().removeAnnotationTask(userId, selectedCampaignId, docId, taskId, new AsyncCallback<JavaScriptObject>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error while publishing removing task :" + caught.getMessage());
            }

            @Override
            public void onSuccess(JavaScriptObject result) {
                refreshTaskInstanceList();
            }
        });
    }

    private void startAnnotate(final int docId, final int taskId) {
        injector.getMainEventBus().fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing, null));
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                presenter.goTo(new DocEditingPlace(new BasicUserCampaignDocOffsetTaskParams(injector.getCoreDataProvider().getRequestManager().getCurrentUserId(), selectedCampaignId, docId, 0, taskId)));
                injector.getMainEventBus().fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, null));
            }
        });
    }

    private void startReview(final int docId, final int taskId) {
        injector.getMainEventBus().fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing, null));

        final int currentUserId = injector.getCoreDataProvider().getRequestManager().getCurrentUserId();
        injector.getCoreDataProvider().getReviewableTaskInstances(currentUserId, selectedCampaignId, docId, taskId, new AsyncCallback<TaskInstanceListImpl>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error while retrieving reviewable tasks :" + caught.getMessage());
            }

            @Override
            public void onSuccess(TaskInstanceListImpl result) {

                if (result.length() <= 0) {
                    Window.alert("Not enough reviewable task to start the review");
                } else {
                    int reviewedTaskId = 0;
                    List<Integer> userIds = new ArrayList<Integer>();
                    for (int row = 0; row < result.length(); row++) {
                        TaskInstanceImpl taskInfo = result.get(row);
                        userIds.add(taskInfo.getUserId());
                        reviewedTaskId = taskInfo.getId();
                    }
                    presenter.goTo(new AnnotationSetConsoPlace(new BasicTaskReviewParams(currentUserId, selectedCampaignId, docId, 0, taskId, userIds, reviewedTaskId)));
                    injector.getMainEventBus().fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, null));
                }
            }
        });

    }

    @Override
    protected void onAttach() {
        super.onAttach();
        EventBus eventBus = injector.getMainEventBus();
        taskDefsByCampaign.clear();
        docsByCampaign.clear();

        refreshCampaignList();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        taskDefsByCampaign.clear();
        docsByCampaign.clear();
    }
}
