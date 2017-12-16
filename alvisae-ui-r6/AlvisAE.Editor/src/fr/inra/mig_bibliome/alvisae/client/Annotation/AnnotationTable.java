/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.*;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDocumentViewMapper;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView;
import fr.inra.mig_bibliome.alvisae.client.Document.ToolBarExpandCollapseHandler;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationFocusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationSelectionChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationStatusChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TermAnnotationsExpositionEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TermAnnotationsExpositionEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceDirectSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceSelectionChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.GenericAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.GroupSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.RelationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.AnnotationSelections;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.GenericAnnotationSelection;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.GroupAnnotationSelection;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.RelationAnnotationSelection;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.TextAnnotationSelection;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.WorkingDocumentChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.WorkingDocumentChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextProcessor;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSchemaDefHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSetImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.ResourceLocator;
import fr.inra.mig_bibliome.alvisae.client.data3.TextBindingImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetInfo;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TermAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDIResourceRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDISemClassRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import gwtquery.plugins.draggable.client.DraggableOptions;
import gwtquery.plugins.draggable.client.DraggableOptions.CursorAt;
import gwtquery.plugins.draggable.client.DraggableOptions.DragFunction;
import gwtquery.plugins.draggable.client.DraggableOptions.RevertOption;
import gwtquery.plugins.draggable.client.events.DragContext;
import gwtquery.plugins.droppable.client.DroppableOptions;
import gwtquery.plugins.droppable.client.gwt.DragAndDropColumn;
import gwtquery.plugins.droppable.client.gwt.DragAndDropDataGrid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Table displaying the annotations associated to the currently selected
 * document
 *
 * @author fpapazian
 */
public class AnnotationTable extends Composite implements WorkingDocumentChangedEventHandler, EditHappenedEventHandler, AnnotationSelectionChangedEventHandler, TermAnnotationsExpositionEventHandler, TyDIResourceSelectionChangedEventHandler, AnnotationStatusChangedEventHandler {

    interface AnnotationFilter {

        boolean accept(Annotation a);
    }

    interface AnnotationTableUiBinder extends UiBinder<Widget, AnnotationTable> {
    }
    private static AnnotationTableUiBinder uiBinder = GWT.create(AnnotationTableUiBinder.class);
    //
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    //

    static {
        StanEditorResources.INSTANCE.css().ensureInjected();
    }

    //Override some rules of the default table CSS
    interface TableStyle extends DragAndDropDataGrid.Style {
    }

    interface TableResources extends DragAndDropDataGrid.Resources {

        @Source({DragAndDropDataGrid.Style.DEFAULT_CSS, "AnnotationTable.css"})
        @Override
        TableStyle dataGridStyle();
    }
    TableResources resources = GWT.create(TableResources.class);

    //
    interface Styles extends CssResource {
    }
    @UiField(provided = true)
    DragAndDropDataGrid<Annotation> annotationsGrid;
    @UiField
    LayoutPanel layoutPanel;
    @UiField
    FlowPanel toolBar;
    @UiField
    Panel syncWithConceptPanel;
    @UiField
    CheckBox syncWithConcept;
    @UiField
    Image expandCollapseImg;
    @UiField
    LayoutPanel tablePanel;
    @UiField
    MenuBar filterMenu;
    @UiField
    MenuItem annSetMenuItem;
    //
    @UiField
    Styles style;
//
    private AnnotationIdColumn<Annotation> idColumn;
    private AnnotationSetColumn<Annotation> annSetColumn;
    private AnnotationKindColumn<Annotation> kindColumn;
    private AnnotationTypeColumn<Annotation> typeColumn;
    private TermAnnotationColumn<Annotation> termColumn;
    private CombinedAnnotationColumn<Annotation> detailColumn;
    private AnnotationVeiledColumn<Annotation> veiledColumn;
//
    private AnnotatedTextHandler lastWorkingDocument = null;
    private AnnotatedTextHandler registeredAnnotatedText = null;
    private AnnotationSchemaDefHandler schemaHandler = null;
    //list of the displayed annotations
    private final ArrayList<Annotation> annotations = new ArrayList<Annotation>();
    private final ListDataProvider<Annotation> annotationProvider;
    private final ListHandler<Annotation> sortHandler;
    private final List<AnnotationFilter> annotationFilters = new ArrayList<AnnotationFilter>();
    private final MultiSelectionModel<Annotation> selectionModel;
    //Set of Annotation selected by the user
    private Set<Annotation> currentSelectedSet = new LinkedHashSet<Annotation>();
    private Set<Annotation> prevSelectedSet = new LinkedHashSet<Annotation>();
    private HashMap<Annotation, Integer> indexByAnnotation = new HashMap<Annotation, Integer>();
    private AnnotationSelections selectedTextAnnotations = new AnnotationSelections();
    private AnnotationSelections selectedGroupAnnotations = new AnnotationSelections();
    private AnnotationSelections selectedRelationAnnotations = new AnnotationSelections();
    //Label of AnnotationSets indexed by AnnotationSetId
    private HashMap<Integer, AnnotationSetInfo> annSetInfos = new HashMap<Integer, AnnotationSetInfo>();
    //List of Annotation Set that can be viewed by current user
    private HashSet<Integer> authorizedAnnSetIds = new HashSet<Integer>();
    private final ToolBarExpandCollapseHandler toolBarExpandCollapseHandler;
    //List of Annotation Set that are currently displayed in the Table
    private Set<Integer> selectedAnnSet = new HashSet<Integer>();
    //
    private CombinedAnnotationCell combinedAnnotationCell = new SelectorCombinedAnnotationCellImpl();
    SafeHtml checkedIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.CheckedIcon()).getHTML());
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static final ProvidesKey<Annotation> KEY_PROVIDER = new ProvidesKey<Annotation>() {
        @Override
        public Object getKey(Annotation item) {
            return item == null ? null : item.getId();
        }
    };
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public interface AnnotationCellIdTemplates extends SafeHtmlTemplates {

        @Template("<span class='{0}' title='{1}'>{2}</span>")
        public SafeHtml spanId(String style, String annotationId, String briefId);

        @Template("<span style='color:red;'>???</span>")
        public SafeHtml missingId();
    }
    private static final AnnotationCellIdTemplates TEMPLATES = GWT.create(AnnotationCellIdTemplates.class);

    public static void renderId(AnnotatedTextHandler annotatedText, Annotation annotation, SafeHtmlBuilder sb) {
        if (annotation != null && annotatedText != null) {
            renderId(annotatedText, annotatedText.getAnnotationReference(annotation.getId()), sb);
        }
    }

    public static void renderId(AnnotatedTextHandler annotatedText, AnnotationReference annotationRef, SafeHtmlBuilder sb) {
        if (annotatedText != null) {
            if (annotationRef == null) {
                sb.append(TEMPLATES.missingId());
            } else {
                String annotationId = annotationRef.getAnnotationId();
                AnnotationSetImpl userAnnotationSet = annotatedText.getEditableUsersAnnotationSet();
                Integer userAnnSetId = userAnnotationSet != null ? userAnnotationSet.getId() : null;
                String fullAnnId = (annotationRef.getAnnotationSetId() == null ? userAnnSetId : annotationRef.getAnnotationSetId()) + ":" + annotationId;
                String idClass;
                switch (annotatedText.getAnnotationObsolescenceStatus(annotationRef, userAnnSetId)) {
                    case OUTDATED:
                        idClass = StanEditorResources.INSTANCE.css().cwOutdatedId();
                        break;
                    case OUTDATEDREF:
                    case OUTDATEDBACKREF:
                        idClass = StanEditorResources.INSTANCE.css().cwOutdatedRefId();
                        break;
                    default:
                        idClass = StanEditorResources.INSTANCE.css().cwId();
                }
                sb.append(TEMPLATES.spanId(idClass, fullAnnId, AnnotatedTextProcessor.getBriefId(annotationId)));
            }
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static abstract class AnnotationIdColumn<T> extends Column<T, AnnotationReference> {

        public static class DblClickableIdCell extends AbstractCell<AnnotationReference> {

            private AnnotatedTextHandler annotatedText;

            public DblClickableIdCell() {
                super("dblclick");
            }

            @Override
            public void render(Context context, AnnotationReference annotationRef, SafeHtmlBuilder sb) {
                renderId(getAnnotatedText(), annotationRef, sb);
            }

            public AnnotatedTextHandler getAnnotatedText() {
                return annotatedText;
            }

            public void setDocument(AnnotatedTextHandler annotatedText) {
                this.annotatedText = annotatedText;
            }
        }

        public AnnotationIdColumn() {
            super(new DblClickableIdCell());

        }

        public DblClickableIdCell getIdCell() {
            return (DblClickableIdCell) getCell();
        }
    };

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // this column contains a drag handle when the corresponding annoation can be dragged to a term ressource, and then acquire the corresponding Term and Class identifier
    public static class TermAnnotationCell extends AbstractCell<TermAnnotationBox> {

        public static interface TermAnnotationCellTemplates extends SafeHtmlTemplates {

            @Template("<div id='termAnnotationDragHelper' class='{0}'></div>")
            public SafeHtml outerHelper(String cssClassName);

            @Template("<div class='{0}'/>")
            public SafeHtml divTermIcon(String style);
        }
        public static final TermAnnotationCellTemplates TEMPLATES = GWT.create(TermAnnotationCellTemplates.class);

        @Override
        public void render(Context context, TermAnnotationBox value, SafeHtmlBuilder sb) {
            if (value != null) {
                if (value.isTyDIClassOrTermGenerator()) {
                    if (value.getSemClassExternalId() == null) {
                        sb.append(TEMPLATES.divTermIcon(StanEditorResources.INSTANCE.css().termGenerNotLinked()));
                    } else {
                        sb.append(TEMPLATES.divTermIcon(StanEditorResources.INSTANCE.css().termGenerLinked()));
                    }
                } else {
                    if (value.getSemClassExternalId() == null) {
                        sb.append(TEMPLATES.divTermIcon(StanEditorResources.INSTANCE.css().termLinkerNotLinked()));
                    } else {
                        sb.append(TEMPLATES.divTermIcon(StanEditorResources.INSTANCE.css().termLinkerLinked()));
                    }
                }
            }
        }
    }

    public abstract class TermAnnotationColumn<T> extends DragAndDropColumn<T, TermAnnotationBox> {

        public TermAnnotationColumn() {
            super(new TermAnnotationCell());
        }
    };

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static class SelectorCombinedAnnotationCell extends CombinedAnnotationCell {

        private final static StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
        private final boolean textDetailOnly;

        public SelectorCombinedAnnotationCell(boolean textDetailOnly) {
            super("dblclick");
            this.textDetailOnly = textDetailOnly;
        }

        public boolean isTextDetailOnly() {
            return textDetailOnly;
        }

        @Override
        public void render(Context context, Annotation annotation, SafeHtmlBuilder sb) {
            renderDetail(getDocument(), annotation, isTextDetailOnly(), sb);
        }

        @Override
        public void onBrowserEvent(Context context, Element parent, Annotation value, NativeEvent event, ValueUpdater<Annotation> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            if (getDocument() != null) {
                String eventType = event.getType();
                if ("dblclick".equals(eventType)) {
                    EventTarget evtTarget = event.getEventTarget();
                    Element targetElement = evtTarget.cast();
                    String annotationId = targetElement.getAttribute(CombinedAnnotationCell.REFANNID_ATTRNAME);
                    final GenericAnnotationSelectionChangedEvent selEvent;
                    if (annotationId != null && !annotationId.isEmpty()) {
                        Annotation annotation = getDocument().getAnnotation(annotationId);
                        if (annotation != null) {
                            if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                                AnnotationDocumentViewMapper mapper = AnnotationDocumentViewMapper.getMapper(getDocument().getAnnotatedText());
                                ArrayList<String> markers = mapper.getMarkerIdsFromAnnotationId(annotation.getId());
                                if (markers != null && !markers.isEmpty()) {
                                    selEvent = new TextAnnotationSelectionChangedEvent(getDocument(), annotation, markers, markers.get(0));
                                } else {
                                    selEvent = null;
                                }
                            } else if (annotation.getAnnotationKind().equals(AnnotationKind.GROUP)) {
                                selEvent = new GroupSelectionChangedEvent(getDocument(), annotation);
                            } else if (annotation.getAnnotationKind().equals(AnnotationKind.RELATION)) {
                                selEvent = new RelationSelectionChangedEvent(getDocument(), annotation);
                            } else {
                                selEvent = null;
                            }
                            if (selEvent != null) {
                                EventBus eventBus = injector.getMainEventBus();
                                eventBus.fireEvent(new TextAnnotationSelectionEmptiedEvent(getDocument()));
                                eventBus.fireEvent(new GroupSelectionEmptiedEvent(getDocument()));
                                eventBus.fireEvent(new RelationSelectionEmptiedEvent(getDocument()));
                                eventBus.fireEvent(selEvent);
                            }
                        } else {
                            GWT.log("NOT loaded referenced Annotation!!! id=*" + annotationId);
                        }
                    }
                }
            }
        }
    }

    public class SelectorCombinedAnnotationCellImpl extends SelectorCombinedAnnotationCell {

        public SelectorCombinedAnnotationCellImpl() {
            super(false);
        }

        @Override
        public void render(Context context, Annotation annotation, SafeHtmlBuilder sb) {
            indexByAnnotation.put(annotation, context.getIndex());
            super.render(context, annotation, sb);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public abstract static class CombinedAnnotationColumn<T> extends Column<T, Annotation> {

        public CombinedAnnotationColumn(CombinedAnnotationCell combinedAnnotationCell) {
            super(combinedAnnotationCell);
        }
    };
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static abstract class AnnotationSetColumn<T> extends Column<T, AnnotationSetInfo> {

        public AnnotationSetColumn() {
            super(new AnnotationSetDescriptionCell());
        }
    };

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static class AnnotationKindCell extends CombinedAnnotationCell {

        public AnnotationKindCell() {
            super("click");
        }

        @Override
        public void render(Context context, Annotation annotation, SafeHtmlBuilder sb) {
            if (annotation != null) {
                renderKind(annotation.getAnnotationKind(), sb);
            }
        }
    }

    public static abstract class AnnotationKindColumn<T> extends Column<T, Annotation> {

        public AnnotationKindColumn() {
            super(new AnnotationKindCell());
        }
    };

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static class AnnotationTypeCell extends CombinedAnnotationCell {

        @Override
        public void render(Context context, Annotation annotation, SafeHtmlBuilder sb) {
            if (annotation != null) {
                AnnotationSchemaCell.renderType(annotation.getAnnotationType(), sb);
            }
        }
    }

    public static abstract class AnnotationTypeColumn<T> extends Column<T, Annotation> {

        public AnnotationTypeColumn() {
            super(new AnnotationTypeCell());
        }
    };

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public class AnnotationVeiledCell extends AbstractCell<Annotation> {

        public AnnotationVeiledCell() {
            super("click");
        }

        @Override
        public void render(Context context, Annotation annotation, SafeHtmlBuilder sb) {

            if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT) && lastWorkingDocument.isFormattingAnnotation(annotation.getId())) {
            } else {
                ImageResource image = null;
                AnnotationDocumentViewMapper mapper = AnnotationDocumentViewMapper.getMapper(lastWorkingDocument.getAnnotatedText());

                if (mapper.isVeiled(annotation.getId())) {
                    image = StanEditorResources.INSTANCE.VeiledAnnotationIcon();
                } else {
                    image = StanEditorResources.INSTANCE.UnVeiledAnnotationIcon();
                }
                sb.appendHtmlConstant(AbstractImagePrototype.create(image).getHTML());
            }
        }

        @Override
        public void onBrowserEvent(Context context, Element parent, Annotation annotation, NativeEvent event, ValueUpdater<Annotation> valueUpdater) {
            super.onBrowserEvent(context, parent, annotation, event, valueUpdater);

            String eventType = event.getType();
            if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                AnnotationDocumentViewMapper mapper = AnnotationDocumentViewMapper.getMapper(lastWorkingDocument.getAnnotatedText());

                //Note : currentSelectedSet is updated AFTER this click event
                // (i.e. SelectionChangeEvent happens after click event)
                if (currentSelectedSet.contains(annotation) && currentSelectedSet.size() > 1) {
                    //clicked line was previously selected and there is more than 1 line selected
                    for (Annotation a : currentSelectedSet) {
                        boolean veiledStatus = mapper.toggleVeiledStatus(a.getId());
                    }
                } else {
                    boolean veiledStatus = mapper.toggleVeiledStatus(annotation.getId());
                }
            }
        }
    }

    public abstract class AnnotationVeiledColumn<T> extends Column<T, Annotation> {

        public AnnotationVeiledColumn() {
            super(new AnnotationVeiledCell());
        }
    };
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public AnnotationTable() {
        annotationsGrid = new DragAndDropDataGrid<Annotation>(15, resources);
        initWidget(uiBinder.createAndBindUi(this));

        annSetMenuItem.setHTML(SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.AnnotationSetsIcon()).getHTML()));

        //FIXME compute toolbar top and height at runtime
        toolBarExpandCollapseHandler = new ToolBarExpandCollapseHandler(expandCollapseImg, layoutPanel, toolBar, 0, 25, tablePanel);
        expandCollapseImg.addClickHandler(toolBarExpandCollapseHandler);
        toolBarExpandCollapseHandler.setExpanded(false);

        annotationProvider = new ListDataProvider<Annotation>(annotations, KEY_PROVIDER);

        selectionModel = new MultiSelectionModel<Annotation>(KEY_PROVIDER);
        sortHandler = new ListHandler<Annotation>(annotationProvider.getList());
        initTable();
        syncWithConcept.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                refreshAnnotationListDisplay();
            }
        });
    }

    private void initTable() {

        annotationProvider.addDataDisplay(annotationsGrid);
        DefaultSelectionEventManager<Annotation> selectionHandler = DefaultSelectionEventManager.<Annotation>createDefaultManager();
        annotationsGrid.setSelectionModel(selectionModel, selectionHandler);

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                prevSelectedSet = currentSelectedSet;

                //Note: by default selected items are not sorted (in the order of selection).
                //the purpose of this block is to make sure that "newly selected" item come last in the linked set
                HashSet<Annotation> previouslySelected = new LinkedHashSet<Annotation>(selectionModel.getSelectedSet());
                HashSet<Annotation> newlySelected = new HashSet<Annotation>(previouslySelected);

                previouslySelected.retainAll(prevSelectedSet);
                newlySelected.removeAll(prevSelectedSet);

                currentSelectedSet = new LinkedHashSet<Annotation>(previouslySelected);
                currentSelectedSet.addAll(newlySelected);
                updateSelection();
            }
        });

        annotationsGrid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
        annotationsGrid.addColumnSortHandler(sortHandler);

        //
        idColumn = new AnnotationIdColumn<Annotation>() {
            @Override
            public AnnotationReference getValue(Annotation annotation) {
                return lastWorkingDocument.getAnnotationReference(annotation.getId());
            }

            @Override
            public void onBrowserEvent(Context context, Element elem, Annotation annotation, NativeEvent event) {
                super.onBrowserEvent(context, elem, annotation, event);
                String eventType = event.getType();
                if ("dblclick".equals(eventType)) {
                    injector.getMainEventBus().fireEvent(new AnnotationFocusChangedEvent(lastWorkingDocument, annotation));
                }
            }
        };


        Header<String> idFooter = new Header<String>(new TextCell()) {
            @Override
            public String getValue() {
                List<Annotation> items = annotationsGrid.getVisibleItems();
                if (items.isEmpty()) {
                    return "";
                } else {
                    return "" + items.size();
                }
            }
        };

        annotationsGrid.addColumn(idColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Id")), idFooter);
        idColumn.setSortable(true);
        sortHandler.setComparator(idColumn, new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        //
        annSetColumn = new AnnotationSetColumn<Annotation>() {
            @Override
            public AnnotationSetInfo getValue(Annotation annotation) {
                return annSetInfos.get(lastWorkingDocument.getAnnotationSetId(annotation.getId()));
            }
        };

        annotationsGrid.addColumn(annSetColumn, "Annotation Set");
        annSetColumn.setSortable(true);
        sortHandler.setComparator(annSetColumn, new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                return annSetColumn.getValue(o1).getDescription().compareTo(annSetColumn.getValue(o2).getDescription());
            }
        });

        //
        kindColumn = new AnnotationKindColumn<Annotation>() {
            @Override
            public Annotation getValue(Annotation annotation) {
                return annotation;
            }
        };
        annotationsGrid.addColumn(kindColumn, "Kind");
        kindColumn.setSortable(true);

        sortHandler.setComparator(kindColumn, new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                int order = o1.getAnnotationKind().compareTo(o2.getAnnotationKind());
                if (order == 0 && AnnotationKind.TEXT.equals(o1.getAnnotationKind())) {
                    order = TextBindingImpl.COMPARATOR.compare(o1.getTextBinding(), o2.getTextBinding());
                }
                return order;
            }
        });

        //
        typeColumn = new AnnotationTypeColumn<Annotation>() {
            @Override
            public Annotation getValue(Annotation annotation) {
                return annotation;
            }
        };
        annotationsGrid.addColumn(typeColumn, "Type");
        typeColumn.setSortable(true);
        sortHandler.setComparator(typeColumn, new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                return o1.getAnnotationType().compareTo(o2.getAnnotationType());
            }
        });

        //
        termColumn = new TermAnnotationColumn<Annotation>() {
            @Override
            public TermAnnotationBox getValue(Annotation annotation) {
                return (annotation instanceof TermAnnotationBox) ? (TermAnnotationBox) annotation : null;
            }
        };

        annotationsGrid.addColumn(termColumn, "");
        termColumn.setSortable(true);
        sortHandler.setComparator(termColumn, new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                return isTermAnnotation(o1).compareTo(isTermAnnotation(o2));
            }
        });


        //The column "termColumn" is visible only if current schema support "Term Annotation" (i.e. which contains property to link it to a term or a semantic class)
        //The column "termColumn" contains a draggable icon only in the row where the corresponding annotation is a "Term Annotation"
        {
            //setup drag operation
            DraggableOptions dragOptions = termColumn.getDraggableOptions();

            dragOptions.setHelper($(TermAnnotationCell.TEMPLATES.outerHelper(StanEditorResources.INSTANCE.css().DragHelper()).asString()));

            // the cell being greater than the helper, force the position of the helper on the mouse cursor.
            dragOptions.setCursorAt(new CursorAt(10, 0, null, null));

            dragOptions.setRevert(RevertOption.ON_INVALID_DROP);

            // fill Helper when drag start
            dragOptions.setOnBeforeDragStart(new DragFunction() {
                @Override
                public void f(DragContext context) {
                    context.getDraggableData();
                }
            });

            dragOptions.setOnDragStart(new DragFunction() {
                @Override
                public void f(gwtquery.plugins.draggable.client.events.DragContext context) {

                    if (context.getDraggableData() instanceof TermAnnotation && ((TermAnnotation) context.getDraggableData()).isTyDIClassOrTermGenerator()) {
                        Annotation draggedAnnotation = context.getDraggableData();
                        SafeHtmlBuilder sb = new SafeHtmlBuilder();
                        CombinedAnnotationCell.renderDetail(lastWorkingDocument, draggedAnnotation, true, sb);
                        context.getHelper().setInnerHTML(sb.toSafeHtml().asString());
                    } else {
                        //Cancel Dragging : Should be available in later release of gwtquery-plugins, see: http://code.google.com/p/gwtquery-plugins/issues/detail?id=27
                        //In the meantime, just display a icon showing that this annoation should not be dragged
                        SafeHtml fromSafeConstant = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.ForbiddenDragIcon()).getHTML());
                        context.getHelper().setInnerHTML(fromSafeConstant.asString());
                    }
                }
            });

            dragOptions.setOpacity((float) 0.9);

            dragOptions.setAppendTo("body");
            dragOptions.setCursor(Cursor.MOVE);
            dragOptions.setScope(TermAnnotation.DragNDropScope);

            //setup option operation
            DroppableOptions dropOptions = termColumn.getDroppableOptions();

            termColumn.setCellDraggableOnly();
        }
        //
        detailColumn = new CombinedAnnotationColumn<Annotation>(combinedAnnotationCell) {
            @Override
            public Annotation getValue(Annotation annotation) {
                return annotation;
            }
        };

        annotationsGrid.addColumn(detailColumn, "Details");
        detailColumn.setSortable(true);
        sortHandler.setComparator(detailColumn,
                new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                int result = o1.getAnnotationKind().compareTo(o2.getAnnotationKind());
                if (result == 0 && AnnotationKind.TEXT.equals(o1.getAnnotationKind())) {
                    String t1 = lastWorkingDocument.isFormattingAnnotation(o1.getId()) ? "" : AnnotatedTextProcessor.getAnnotationText(o1);
                    String t2 = lastWorkingDocument.isFormattingAnnotation(o2.getId()) ? "" : AnnotatedTextProcessor.getAnnotationText(o2);
                    result = t1.compareTo(t2);
                }
                return result;
            }
        });

        //
        veiledColumn = new AnnotationVeiledColumn<Annotation>() {
            @Override
            public Annotation getValue(Annotation annotation) {
                return annotation;
            }
        };

        annotationsGrid.addColumn(veiledColumn, "Visible");
        veiledColumn.setSortable(true);
        sortHandler.setComparator(veiledColumn,
                new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                AnnotationDocumentViewMapper mapper = AnnotationDocumentViewMapper.getMapper(lastWorkingDocument.getAnnotatedText());

                Boolean t1 = lastWorkingDocument.isFormattingAnnotation(o1.getId()) ? null : mapper.isVeiled(o1.getId());
                Boolean t2 = lastWorkingDocument.isFormattingAnnotation(o2.getId()) ? null : mapper.isVeiled(o2.getId());

                int result;
                if (t1 == null && t2 == null) {
                    result = 0;
                } else if (t1 == null) {
                    result = -1;
                } else if (t2 == null) {
                    result = +1;
                } else {
                    result = t1.compareTo(t2);
                }
                return result;
            }
        });

        resetColumnSize(false);
    }

    private void resetColumnSize(boolean showTermColumn) {
        annotationsGrid.setWidth("100%");
        annotationsGrid.setColumnWidth(idColumn, 5.0, Unit.PCT);
        annotationsGrid.setColumnWidth(annSetColumn, 13.0, Unit.PCT);
        annotationsGrid.setColumnWidth(kindColumn, 26.0, Unit.PX);
        annotationsGrid.setColumnWidth(typeColumn, 15.0, Unit.PCT);
        annotationsGrid.setColumnWidth(termColumn, showTermColumn ? 26.0 : 0.0, Unit.PX);
        annotationsGrid.setColumnWidth(detailColumn, 64.0, Unit.PCT);
        annotationsGrid.setColumnWidth(veiledColumn, 36.0, Unit.PX);
    }

    private void reinitAnnSetFilter(boolean usersAnnSetOnly) {
        if (usersAnnSetOnly) {
            AnnotationSetImpl usersAnnSet = lastWorkingDocument.getEditableUsersAnnotationSet();
            if (usersAnnSet != null) {
                selectedAnnSet.add(usersAnnSet.getId());
            }
        } else {
            HashSet<Integer> allButFormatting = new HashSet<Integer>(lastWorkingDocument.getLoadedAnnotationSetIds());
            if (lastWorkingDocument.getFormattingAnnotationSet() != null) {
                allButFormatting.remove(lastWorkingDocument.getFormattingAnnotationSet().getId());
            }
            selectedAnnSet.addAll(allButFormatting);
        }
    }

    private void reinitAnnSetFilterMenu() {
        MenuBar annSetMenuBar = new MenuBar(true);

        annSetMenuBar.addItem(SafeHtmlUtils.fromString("Reset filter"), new Command() {
            @Override
            public void execute() {
                reinitAnnSetFilter(false);
                filterAnnotationListAndRefresh(selectedAnnSet);
                reinitAnnSetFilterMenu();
            }
        });

        annSetMenuBar.addSeparator();


        for (Entry<Integer, AnnotationSetInfo> e : annSetInfos.entrySet()) {

            final int annSetId = e.getKey();
            AnnotationSetInfo asInfo = e.getValue();
            Command command;
            SafeHtmlBuilder shbuilder = new SafeHtmlBuilder();

            if (authorizedAnnSetIds.contains(annSetId)) {
                command = new Command() {
                    @Override
                    public void execute() {
                        if (selectedAnnSet.contains(annSetId)) {
                            selectedAnnSet.remove(annSetId);
                        } else {
                            selectedAnnSet.add(annSetId);
                        }
                        filterAnnotationListAndRefresh(selectedAnnSet);
                        reinitAnnSetFilterMenu();
                    }
                };

                if (selectedAnnSet.contains(annSetId)) {
                    shbuilder.append(checkedIcon).appendEscaped("  ");
                }
                AnnotationSetDescriptionCell.render(asInfo, shbuilder);
                shbuilder.appendHtmlConstant("  ").appendEscaped("(").append(asInfo.getNbTextAnnotations()).appendEscaped("/").append(asInfo.getNbGroups()).appendEscaped("/").append(asInfo.getNbRelations()).appendEscaped(")");

            } else {
                command = null;
                AnnotationSetDescriptionCell.render(asInfo, shbuilder);
            }
            MenuItem item = new MenuItem(shbuilder.toSafeHtml(), command);
            item.setEnabled(command != null);
            annSetMenuBar.addItem(item);
        }
        annSetMenuItem.setSubMenu(annSetMenuBar);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private void updateSelection() {


        //TODO should someday simplify the selection scheme and does not distinguish the selection depending on annotation kind

        boolean someTextAnnSelected = !selectedTextAnnotations.isEmpty();
        boolean someGroupSelected = !selectedGroupAnnotations.isEmpty();
        boolean someRelationSelected = !selectedRelationAnnotations.isEmpty();

        selectedTextAnnotations.clear();
        selectedGroupAnnotations.clear();
        selectedRelationAnnotations.clear();

        if (lastWorkingDocument != null) {
            AnnotationDocumentViewMapper mapper = AnnotationDocumentViewMapper.getMapper(lastWorkingDocument.getAnnotatedText());

            GenericAnnotationSelection lastOccuredSelection = null;
            for (Annotation annotation : currentSelectedSet) {
                switch (annotation.getAnnotationKind()) {
                    case TEXT:
                        lastOccuredSelection = new TextAnnotationSelection(annotation, mapper.getMarkerIdsFromAnnotationId(annotation.getId()));
                        selectedTextAnnotations.getSelections().add(lastOccuredSelection);
                        break;
                    case GROUP:
                        lastOccuredSelection = new GroupAnnotationSelection(annotation);
                        selectedGroupAnnotations.getSelections().add(lastOccuredSelection);
                        break;
                    case RELATION:
                        lastOccuredSelection = new RelationAnnotationSelection(annotation);
                        selectedRelationAnnotations.getSelections().add(lastOccuredSelection);
                        break;
                }

            }

            EventBus eventBus = injector.getMainEventBus();
            LinkedList<GenericAnnotationSelectionChangedEvent> events = new LinkedList<GenericAnnotationSelectionChangedEvent>();

            if (selectedTextAnnotations.isEmpty()) {
                if (someTextAnnSelected) {
                    eventBus.fireEvent(new TextAnnotationSelectionEmptiedEvent(lastWorkingDocument));
                }
            } else {
                TextAnnotationSelectionChangedEvent selectionChangedEvent = new TextAnnotationSelectionChangedEvent(lastWorkingDocument, selectedTextAnnotations);
                if (lastOccuredSelection instanceof TextAnnotationSelection) {
                    events.addLast(selectionChangedEvent);
                } else {
                    events.addFirst(selectionChangedEvent);
                }
            }
            if (selectedGroupAnnotations.isEmpty()) {
                if (someGroupSelected) {
                    eventBus.fireEvent(new GroupSelectionEmptiedEvent(lastWorkingDocument));
                }
            } else {
                GroupSelectionChangedEvent selectionChangedEvent = new GroupSelectionChangedEvent(lastWorkingDocument, selectedGroupAnnotations);
                if (lastOccuredSelection instanceof GroupAnnotationSelection) {
                    events.addLast(selectionChangedEvent);
                } else {
                    events.addFirst(selectionChangedEvent);
                }
            }
            if (selectedRelationAnnotations.isEmpty()) {
                if (someRelationSelected) {
                    eventBus.fireEvent(new RelationSelectionEmptiedEvent(lastWorkingDocument));
                }
            } else {
                RelationSelectionChangedEvent selectionChangedEvent = new RelationSelectionChangedEvent(lastWorkingDocument, selectedRelationAnnotations);
                if (lastOccuredSelection instanceof RelationAnnotationSelection) {
                    events.addLast(selectionChangedEvent);
                } else {
                    events.addFirst(selectionChangedEvent);
                }
            }

            //selection events must be fired after clearing selection events
            for (GenericAnnotationSelectionChangedEvent e : events) {
                eventBus.fireEvent(e);
            }
        }

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private void displayAnnotationList(AnnotatedTextHandler workingDocument, DocumentView docView, boolean initialLoad) {
        lastWorkingDocument = workingDocument;
        combinedAnnotationCell.setDocument(lastWorkingDocument);
        idColumn.getIdCell().setDocument(lastWorkingDocument);

        if (workingDocument != null) {
            schemaHandler = new AnnotationSchemaDefHandler(workingDocument.getAnnotatedText().getAnnotationSchema());
        } else {
            schemaHandler = null;
        }
        
        //show term colum if the Annotation schema allows references to TyDI resource
        resetColumnSize(schemaHandler != null && schemaHandler.enableTyDIResourceRef());

        if (lastWorkingDocument == null) {
            annotationsGrid.setRowCount(0);
        } else {
            annSetInfos.clear();
            for (AnnotationSetInfo asi : lastWorkingDocument.getAnnotatedText().getAnnotationSetInfoList()) {
                annSetInfos.put(asi.getId(), asi);
            }
            authorizedAnnSetIds.clear();
            authorizedAnnSetIds.addAll(lastWorkingDocument.getLoadedAnnotationSetIds());
            reinitAnnSetFilter(initialLoad);
            reinitAnnSetFilterMenu();
        }

        refreshAnnotationListDisplay();
    }

    private Boolean isTermAnnotation(Annotation annotation) {
        return (schemaHandler != null && schemaHandler.isTyDIResReferencingType(annotation.getAnnotationType()));
    }

    private void filterAnnotationListAndRefresh(Set<Integer> displayAnnSetIds) {
        annotationProvider.getList().clear();
        Collection<Annotation> annotationSrc;
        if (displayAnnSetIds == null) {
            annotationSrc = lastWorkingDocument.getAnnotations();
        } else {
            annotationSrc = lastWorkingDocument.getAnnotationsForAnnSets(displayAnnSetIds);
        }
        ArrayList<Annotation> annotationList = new ArrayList<Annotation>();
        AnnotationLoop:
        for (Annotation a : annotationSrc) {
            //apply annotation filters
            if (syncWithConcept.getValue()) {
                for (AnnotationFilter f : annotationFilters) {
                    if (!f.accept(a)) {
                        continue AnnotationLoop;
                    }
                }
            }

            if (isTermAnnotation(a)) {
                String tyDITermRefPropName = schemaHandler.getTyDITermRefPropName(a.getAnnotationType());
                String tyDIClassRefPropName = schemaHandler.getTyDISemClassRefPropName(a.getAnnotationType());
                List<String> tyDIConceptRefPropNames = schemaHandler.getTyDIConceptRefPropNames(a.getAnnotationType());
                annotationList.add(new TermAnnotationBox(lastWorkingDocument, a, tyDITermRefPropName, tyDIClassRefPropName, tyDIConceptRefPropNames));
            } else {
                annotationList.add(a);
            }
        }
        annotationProvider.getList().addAll(annotationList);
        annotationsGrid.setVisibleRange(0, annotationProvider.getList().size());
        // #2450 force sorting if the table was previously sorted
        ColumnSortList sortList = annotationsGrid.getColumnSortList();
        if (sortList != null && sortList.size() > 0) {
            ColumnSortEvent.fire(annotationsGrid, sortList);
        }
        //annotationProvider.refresh();
    }

    private void refreshAnnotationListDisplay() {
        if (lastWorkingDocument != null) {
            //display only annotation from the registered AnnotatedTextHandler if any
            if (registeredAnnotatedText == null || this.registeredAnnotatedText == lastWorkingDocument) {

                annotationProvider.getList().clear();
                indexByAnnotation.clear();

                filterAnnotationListAndRefresh(selectedAnnSet);
            }
        }
    }

    public void setRegisteredAnnotatedText(AnnotatedTextHandler annotatedText) {
        this.registeredAnnotatedText = annotatedText;
        displayAnnotationList(annotatedText, null, true);
    }

    @Override
    public void onWorkingDocumentChanged(WorkingDocumentChangedEvent event) {
        if (registeredAnnotatedText == null || this.registeredAnnotatedText == event.getWorkingDocument()) {
            displayAnnotationList(event.getWorkingDocument(), event.getDocView(), !WorkingDocumentChangedEvent.ChangeType.AdditionalAnnotationSetLoaded.equals(event.getChangeType()));
        }
    }

    @Override
    public void onAnnotationSelectionChanged(GenericAnnotationSelectionChangedEvent event) {

        if (lastWorkingDocument != null && lastWorkingDocument.equals(event.getAnnotatedTextHandler())) {

            if (event instanceof TextAnnotationSelectionChangedEvent) {
                if (event instanceof TextAnnotationSelectionEmptiedEvent) {
                    selectedTextAnnotations.clear();
                } else {
                    selectedTextAnnotations.replaceSelection(event.getAnnotationSelection());
                }
            } else if (event instanceof GroupSelectionChangedEvent) {
                if (event instanceof GroupSelectionEmptiedEvent) {
                    selectedGroupAnnotations.clear();
                } else {
                    selectedGroupAnnotations.replaceSelection(event.getAnnotationSelection());
                }
            } else if (event instanceof RelationSelectionChangedEvent) {
                if (event instanceof RelationSelectionEmptiedEvent) {
                    selectedRelationAnnotations.clear();
                } else {
                    selectedRelationAnnotations.replaceSelection(event.getAnnotationSelection());
                }
            }


            HashSet<String> selectedIds = new HashSet<String>();
            HashSet<String> mainSelectedId = new HashSet<String>();
            if (!selectedTextAnnotations.isEmpty()) {
                selectedIds.addAll(selectedTextAnnotations.getSeletedAnnotationIds());
                mainSelectedId.add(selectedTextAnnotations.getMainSelectedAnnotation().getId());
            }
            if (!selectedGroupAnnotations.isEmpty()) {
                selectedIds.addAll(selectedGroupAnnotations.getSeletedAnnotationIds());
                mainSelectedId.add(selectedGroupAnnotations.getMainSelectedAnnotation().getId());
            }
            if (!selectedRelationAnnotations.isEmpty()) {
                selectedIds.addAll(selectedRelationAnnotations.getSeletedAnnotationIds());
                mainSelectedId.add(selectedRelationAnnotations.getMainSelectedAnnotation().getId());
            }


            for (Annotation annotation : annotations) {
                String annotationId = annotation.getId();

                if (selectionModel.isSelected(annotation)) {
                    if (!selectedIds.contains(annotationId)) {
                        selectionModel.setSelected(annotation, false);
                    }
                } else {
                    if (selectedIds.contains(annotationId)) {
                        selectionModel.setSelected(annotation, true);

                        //bring into view the row corresponding to the main annotation
                        if (mainSelectedId.contains(annotationId)) {
                            //
                            try {
                                Integer index = indexByAnnotation.get(annotation);
                                if (index != null) {
                                    annotationsGrid.getRowElement(index).scrollIntoView();
                                } else {
                                    //Case of a new line appended to the table when a annotation is just created
                                    if (annotations.contains(annotation)) {
                                        index = annotationsGrid.getRowCount() - 1;
                                        annotationsGrid.getRowElement(index).scrollIntoView();
                                    }

                                }
                            } catch (IndexOutOfBoundsException e) {
                                // scrolling not possible because the row is outside the the visible page
                                //FIXME : force to populate the table with the desired row...
                            }
                        }

                    }
                }

            }
        }
    }

    @Override
    public void onEditHappened(EditHappenedEvent event) {
        if (event.getEdit() instanceof AnnotationEdit) {
            if (((AnnotationEdit) event.getEdit()).getAnnotatedTextHandler().getAnnotatedText().equals(lastWorkingDocument.getAnnotatedText())) {
                refreshAnnotationListDisplay();
            }
        }
    }

    @Override
    public void onTermAnnotationsExpositionChanged(TermAnnotationsExpositionEvent event) {
        if (TermAnnotationsExpositionEvent.ChangeType.Available.equals(event.getChangeType())) {
            syncWithConcept.setEnabled(true);
        } else {
            annotationFilters.clear();
            syncWithConcept.setEnabled(false);
        }
    }

    @Override
    public void onTyDIResourceSelectionChanged(TyDIResourceSelectionChangedEvent event) {

        //if user has selected a class/concept in TyDI extension
        if (event instanceof TyDIResourceDirectSelectionChangedEvent) {

            TyDIResourceRef resRef = event.getTyDIResourceRef();
            annotationFilters.clear();

            if (resRef != null) {
                if (resRef instanceof TyDISemClassRef) {
                    final TyDISemClassRef selectedSemClassRef = (TyDISemClassRef) resRef;

                    SafeHtmlBuilder sb = new SafeHtmlBuilder();
                    if (selectedSemClassRef.getCanonicLabel() != null) {
                        sb.appendHtmlConstant("<i>")
                                .appendEscaped(selectedSemClassRef.getCanonicLabel())
                                .appendHtmlConstant("</i>");

                    } else {
                        sb.appendHtmlConstant("<i>").appendEscaped("[")
                                .appendEscaped(selectedSemClassRef.getTyDISemanticClassId().toString())
                                .appendEscaped("]").appendHtmlConstant("</i>");
                    }
                    syncWithConcept.setHTML(sb.toSafeHtml());

                    annotationFilters.add(new AnnotationFilter() {
                        @Override
                        public boolean accept(Annotation annotation) {
                            if (annotation != null && isTermAnnotation(annotation)) {
                                Properties props = annotation.getProperties();
                                if (props != null) {
                                    List<String> propNames = new ArrayList<String>();
                                    String tyDIClassRefPropName = schemaHandler.getTyDISemClassRefPropName(annotation.getAnnotationType());
                                    if (tyDIClassRefPropName != null) {
                                        propNames.add(tyDIClassRefPropName);
                                    }
                                    propNames.addAll(schemaHandler.getTyDIConceptRefPropNames(annotation.getAnnotationType()));
                                    for (String propName : propNames) {
                                        List<String> values = props.getValues(propName);
                                        if (values != null && !values.isEmpty()) {
                                            for (String scRef : values) {
                                                if (scRef != null) {
                                                    if (selectedSemClassRef.sameShortSemClassRef(scRef)) {
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                return false;
                            } else {
                                return false;
                            }
                        }
                    });
                    if (syncWithConcept.getValue()) {
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {
                                refreshAnnotationListDisplay();
                            }
                        });
                    }

                };
            } else {
            }
        }
    }

    @Override
    public void onAnnotationStatusChanged(AnnotationStatusChangedEvent event) {
        if (lastWorkingDocument != null) {
            for (String annId : event.getAnnotationIds()) {
                AnnotationDocumentViewMapper mapper = AnnotationDocumentViewMapper.getMapper(lastWorkingDocument.getAnnotatedText());
                Annotation annotation = mapper.getAnnotation(annId);
                if (annotation != null) {
                    Integer index = indexByAnnotation.get(annotation);
                    if (index != null) {
                        annotationsGrid.redrawRow(index);
                    }
                }
            }
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        EventBus eventBus = injector.getMainEventBus();
        WorkingDocumentChangedEvent.register(eventBus, this);
        EditHappenedEvent.register(eventBus, this);
        TextAnnotationSelectionChangedEvent.register(eventBus, this);
        TermAnnotationsExpositionEvent.register(eventBus, this);
        TyDIResourceSelectionChangedEvent.register(eventBus, this);
        AnnotationStatusChangedEvent.register(eventBus, this);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        WorkingDocumentChangedEvent.unregister(this);
        EditHappenedEvent.unregister(this);
        TextAnnotationSelectionChangedEvent.unregister(this);
        TermAnnotationsExpositionEvent.unregister(this);
        TyDIResourceSelectionChangedEvent.unregister(this);
        AnnotationStatusChangedEvent.unregister(this);
    }
}
