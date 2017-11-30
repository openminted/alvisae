/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Campaign;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.view.client.*;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicUserCampaignDocParams;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.NetworkActivityDisplayer;
import fr.inra.mig_bibliome.alvisae.client.data3.*;
import java.util.*;

/**
 * View used to display the campaigns and the related tasks, and choose the task
 * to perform
 *
 * @author fpapazian
 */
public class CampaignAssignment extends Composite implements CampaignAssignmentView {

    interface CampaignAssignmentUiBinder extends UiBinder<DockLayoutPanel, CampaignAssignment> {
    }
    private static CampaignAssignmentUiBinder uiBinder = GWT.create(CampaignAssignmentUiBinder.class);
    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);

    static interface Styles extends CssResource {
    }

    static class CampaignDocs extends HashMap<Integer, DocumentInfoImpl> {
    }

    public interface UserColTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span title='{1}'>{0}</span>")
        public SafeHtml spanWithTitle(String text, String title);
    }
    private static final UserColTemplates TEMPLATES = GWT.create(UserColTemplates.class);
    //
    @UiField(provided = true)
    DataGrid<CampaignImpl> campaignUserGrid;
    @UiField(provided = true)
    DataGrid<DocumentNAssignedImpl> docUserGrid;
    @UiField
    NetworkActivityDisplayer networkActivityDisplayer;
    //
    private Presenter presenter;
    private final LinkedHashMap<Integer, CampaignImpl> campaignById = new LinkedHashMap<Integer, CampaignImpl>();
    private final LinkedHashMap<Integer, ExtendedUserInfoImpl> userById = new LinkedHashMap<Integer, ExtendedUserInfoImpl>();
    private final HashMap<Integer, Set<Integer>> annotatorsByCampaignId = new HashMap<Integer, Set<Integer>>();
    private final HashMap<Integer, Set<Integer>> annotatorsByDocumentId = new HashMap<Integer, Set<Integer>>();
    private HashMap<Integer, CampaignDocs> docsByCampaign = new HashMap<Integer, CampaignDocs>();
    private Integer selectedCampaignId = null;
    private Integer selectedDocId = null;
    private Integer selectedUserId = null;
    private final ListDataProvider<CampaignImpl> campaignProvider = new ListDataProvider<CampaignImpl>(CampaignImpl.KEY_PROVIDER);
    private SingleSelectionModel<CampaignImpl> campaignSelectionModel;
    private final ListDataProvider<DocumentNAssignedImpl> documentProvider = new ListDataProvider<DocumentNAssignedImpl>(DocumentNAssignedImpl.KEY_PROVIDER);
    private MultiSelectionModel<DocumentNAssignedImpl> docSelectionModel;
    private final ListHandler<DocumentNAssignedImpl> sortHandler;

    public CampaignAssignment() {
        campaignUserGrid = new DataGrid<CampaignImpl>();
        docUserGrid = new DataGrid<DocumentNAssignedImpl>();
        initWidget(uiBinder.createAndBindUi(this));
        networkActivityDisplayer.setRequestManager(injector.getCoreDataProvider().getRequestManager());

        campaignProvider.addDataDisplay(campaignUserGrid);
        campaignSelectionModel = new SingleSelectionModel<CampaignImpl>(CampaignImpl.KEY_PROVIDER);
        DefaultSelectionEventManager<CampaignImpl> campaignSelectionHandler = DefaultSelectionEventManager.<CampaignImpl>createDefaultManager();
        campaignUserGrid.setSelectionModel(campaignSelectionModel, campaignSelectionHandler);

        campaignSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                selectedCampaignId = campaignSelectionModel.getSelectedObject() != null ? campaignSelectionModel.getSelectedObject().getId() : null;
                refreshDocList();
            }
        });

        documentProvider.addDataDisplay(docUserGrid);
        docSelectionModel = new MultiSelectionModel<DocumentNAssignedImpl>(DocumentNAssignedImpl.KEY_PROVIDER);
        DefaultSelectionEventManager<DocumentNAssignedImpl> docSelectionHandler = DefaultSelectionEventManager.<DocumentNAssignedImpl>createDefaultManager();
        docUserGrid.setSelectionModel(docSelectionModel, docSelectionHandler);

        reinitUserCampaignTable();

        sortHandler = new ListHandler<DocumentNAssignedImpl>(documentProvider.getList());
        docUserGrid.addColumnSortHandler(sortHandler);

    }

    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    private void reinitUserCampaignTable() {
        campaignById.clear();
        userById.clear();

        docsByCampaign.clear();
        selectedDocId = null;

        selectedUserId = injector.getCoreDataProvider().getRequestManager().getCurrentUserId();


        campaignProvider.getList().clear();
        int i = campaignUserGrid.getColumnCount();
        while (--i >= 0) {
            campaignUserGrid.clearColumnWidth(campaignUserGrid.getColumn(i));
            campaignUserGrid.removeColumn(i);
        }
        campaignUserGrid.setVisibleRange(0, campaignProvider.getList().size());

        injector.getCoreDataProvider().getCampaignList(new AsyncCallback<CampaignListImpl>() {

            @Override
            public void onFailure(Throwable caught) {
                //FIXME
            }

            @Override
            public void onSuccess(CampaignListImpl result) {
                for (int row = 0; row < result.length(); row++) {
                    CampaignImpl c = result.get(row);
                    campaignById.put(c.getId(), c);
                    campaignProvider.getList().add(c);
                }


                injector.getCoreDataProvider().getUserList(new AsyncCallback<ExtendedUserInfoListImpl>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        //FIXME
                    }

                    @Override
                    public void onSuccess(ExtendedUserInfoListImpl result) {

                        for (int row = 0; row < result.length(); row++) {
                            ExtendedUserInfoImpl u = result.get(row);
                            userById.put(u.getId(), u);
                        }

                        //----
                        Column<CampaignImpl, String> idColumn =
                                new Column<CampaignImpl, String>(new TextCell()) {

                                    @Override
                                    public String getValue(CampaignImpl object) {
                                        return String.valueOf(object.getId());
                                    }
                                };
                        campaignUserGrid.addColumn(idColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("#")));
                        campaignUserGrid.setColumnWidth(idColumn, 3, Unit.EM);


                        Column<CampaignImpl, String> nameColumn =
                                new Column<CampaignImpl, String>(new TextCell()) {

                                    @Override
                                    public String getValue(CampaignImpl object) {
                                        return object.getDisplayName();
                                    }
                                };
                        Header<String> nameFooter = new Header<String>(new TextCell()) {

                            @Override
                            public String getValue() {
                                List<CampaignImpl> items = campaignUserGrid.getVisibleItems();
                                if (items.isEmpty()) {
                                    return "";
                                } else {
                                    return "" + items.size();
                                }
                            }
                        };
                        campaignUserGrid.addColumn(nameColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Campaign")), nameFooter);
                        campaignUserGrid.setColumnWidth(idColumn, 11, Unit.EM);


                        for (ExtendedUserInfoImpl u : userById.values()) {
                            final int user_id = u.getId();

                            Column<CampaignImpl, Boolean> userCol = new Column<CampaignImpl, Boolean>(new CheckboxCell(true, false)) {

                                @Override
                                public Boolean getValue(CampaignImpl object) {
                                    Set<Integer> annotators = annotatorsByCampaignId.get(object.getId());
                                    return annotators != null ? annotators.contains(user_id) : false;
                                }
                            };

                            FieldUpdater<CampaignImpl, Boolean> assignColUpdater = new FieldUpdater<CampaignImpl, Boolean>() {

                                @Override
                                public void update(int index, CampaignImpl object, Boolean value) {
                                    int campaignId = object.getId();
                                    HashSet<Integer> toAdd = new HashSet<Integer>();
                                    HashSet<Integer> toDel = new HashSet<Integer>();

                                    if (value) {
                                        annotatorsByCampaignId.get(campaignId).add(user_id);
                                        toAdd.add(user_id);
                                    } else {
                                        annotatorsByCampaignId.get(campaignId).remove(user_id);
                                        toDel.add(user_id);
                                    }
                                    injector.getCoreDataProvider().updateCampaignAnnotators(campaignId, toAdd, toDel, new AsyncCallback<CampaignAnnotatorsListImpl>() {

                                        @Override
                                        public void onFailure(Throwable caught) {
                                            Window.alert("Error while updating assignment of user to campaign.");
                                            throw new UnsupportedOperationException("Error while updating assignment of user to campaign.");
                                        }

                                        @Override
                                        public void onSuccess(CampaignAnnotatorsListImpl result) {
                                            //
                                            campaignUserGrid.redrawFooters();
                                            refreshDocList();
                                        }
                                    });

                                }
                            };
                            userCol.setFieldUpdater(assignColUpdater);


                            Header<String> userFooter = new Header<String>(new TextCell()) {

                                @Override
                                public String getValue() {
                                    int count = 0;
                                    for (Set<Integer> s : annotatorsByCampaignId.values()) {
                                        if (s.contains(user_id)) {
                                            count++;
                                        }
                                    }
                                    return "" + count;
                                }
                            };
                            String head = u.getDisplayName();
                            if (head.length() > 10) {
                                head = head.substring(0, 9) + "...";
                            }
                            String title = u.getDisplayName();
                            campaignUserGrid.addColumn(userCol, new SafeHtmlHeader(TEMPLATES.spanWithTitle(head, title)), userFooter);

                            campaignUserGrid.setColumnWidth(userCol, 7, Unit.EM);
                        }
                        //----
                        //----

                        refreshCampaignList();



                    }
                });

            }
        });
    }

    private void refreshCampaignList() {

        injector.getCoreDataProvider().getCampaignAnnotators(new AsyncCallback<CampaignAnnotatorsListImpl>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error while retrieving Campaign Annotators");
                throw new UnsupportedOperationException("Error while retrieving Campaign Annotators");
            }

            @Override
            public void onSuccess(CampaignAnnotatorsListImpl result) {
                annotatorsByCampaignId.clear();
                for (int row = 0; row < result.length(); row++) {
                    CampaignAnnotatorsImpl ca = result.get(row);
                    annotatorsByCampaignId.put(ca.getCampaignId(), ca.getAnnotatorIds());
                }
                campaignUserGrid.setVisibleRange(0, campaignProvider.getList().size());
            }
        });

    }

    private void refreshDocList() {

        //
        int i = docUserGrid.getColumnCount();
        while (--i >= 0) {
            docUserGrid.clearColumnWidth(docUserGrid.getColumn(i));
            docUserGrid.removeColumn(i);
        }

        documentProvider.getList().clear();
        docUserGrid.setVisibleRange(0, documentProvider.getList().size());

        if (selectedCampaignId != null) {
            //
            Column<DocumentNAssignedImpl, String> idColumn =
                    new Column<DocumentNAssignedImpl, String>(new TextCell()) {

                        @Override
                        public String getValue(DocumentNAssignedImpl object) {
                            return String.valueOf(object.getId());
                        }
                    };
            docUserGrid.addColumn(idColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("#")));
            sortHandler.setComparator(idColumn, new Comparator<DocumentNAssignedImpl>() {

                @Override
                public int compare(DocumentNAssignedImpl o1, DocumentNAssignedImpl o2) {
                    return new Integer(o1.getId()).compareTo(o2.getId());
                }
            });
            idColumn.setSortable(true);
            docUserGrid.setColumnWidth(idColumn, 3, Unit.EM);


            Column<DocumentNAssignedImpl, String> documentColumn =
                    new Column<DocumentNAssignedImpl, String>(new TextCell()) {

                        @Override
                        public String getValue(DocumentNAssignedImpl object) {
                            return object.getDescription();
                        }
                    };

            Header<String> nameFooter = new Header<String>(new TextCell()) {

                @Override
                public String getValue() {
                    List<DocumentNAssignedImpl> items = docUserGrid.getVisibleItems();
                    if (items.isEmpty()) {
                        return "";
                    } else {
                        return "" + items.size();
                    }
                }
            };

            docUserGrid.addColumn(documentColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Document")), nameFooter);
            sortHandler.setComparator(documentColumn, new Comparator<DocumentNAssignedImpl>() {

                @Override
                public int compare(DocumentNAssignedImpl o1, DocumentNAssignedImpl o2) {
                    return o1.getDescription().compareTo(o2.getDescription());
                }
            });
            documentColumn.setSortable(true);
            docUserGrid.setColumnWidth(documentColumn, 11, Unit.EM);


            Column<DocumentNAssignedImpl, String> coverageColumn =
                    new Column<DocumentNAssignedImpl, String>(new TextCell()) {

                        @Override
                        public String getValue(DocumentNAssignedImpl object) {
                            int coverage = annotatorsByDocumentId.get(object.getId()).size();
                            if (coverage == 1) {
                                return "";
                            } else if (coverage > 1) {
                                return "" + coverage;
                            } else {
                                return "!";
                            }
                        }
                    };
            docUserGrid.addColumn(coverageColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Coverage")));
            coverageColumn.setSortable(true);
            sortHandler.setComparator(coverageColumn, new Comparator<DocumentNAssignedImpl>() {

                @Override
                public int compare(DocumentNAssignedImpl o1, DocumentNAssignedImpl o2) {
                    Integer c1 = annotatorsByDocumentId.get(o1.getId()).size();
                    Integer c2 = annotatorsByDocumentId.get(o2.getId()).size();
                    return c1.compareTo(c2);
                }
            });
            docUserGrid.setColumnWidth(coverageColumn, 3, Unit.EM);


            if (annotatorsByCampaignId.get(selectedCampaignId) != null) {
                for (final Integer userId : annotatorsByCampaignId.get(selectedCampaignId)) {

                    ExtendedUserInfoImpl u = userById.get(userId);

                    Column<DocumentNAssignedImpl, Boolean> userCol = new Column<DocumentNAssignedImpl, Boolean>(new CheckboxCell(true, false)) {

                        @Override
                        public Boolean getValue(DocumentNAssignedImpl object) {
                            return annotatorsByDocumentId.get(object.getId()).contains(userId);
                        }
                    };

                    FieldUpdater<DocumentNAssignedImpl, Boolean> assignColUpdater = new FieldUpdater<DocumentNAssignedImpl, Boolean>() {

                        @Override
                        public void update(int index, DocumentNAssignedImpl object, Boolean value) {
                            HashSet<Integer> toAdd = new HashSet<Integer>();
                            HashSet<Integer> toDel = new HashSet<Integer>();

                            for (DocumentNAssignedImpl selectedDoc : docSelectionModel.getSelectedSet()) {
                                int docId = selectedDoc.getId();
                                if (value) {
                                    annotatorsByDocumentId.get(docId).add(userId);
                                    toAdd.add(docId);
                                } else {
                                    annotatorsByDocumentId.get(docId).remove(userId);
                                    toDel.add(docId);
                                }
                            }
                            injector.getCoreDataProvider().updateCampaignUserDocAssignment(userId, selectedCampaignId, toAdd, toDel, new AsyncCallback<DocumentInfoListImpl>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    Window.alert("Error while updating assignment of document to user.");
                                    throw new UnsupportedOperationException("Error while updating assignment of document to user.");
                                }

                                @Override
                                public void onSuccess(DocumentInfoListImpl result) {
                                    docUserGrid.redraw();
                                }
                            });

                        }
                    };
                    userCol.setFieldUpdater(assignColUpdater);

                    Header<String> userFooter = new Header<String>(new TextCell()) {

                        @Override
                        public String getValue() {
                            int count = 0;
                            for (Set<Integer> s : annotatorsByDocumentId.values()) {
                                if (s.contains(userId)) {
                                    count++;
                                }
                            }
                            return "" + count;
                        }
                    };
                    String head = u.getDisplayName();
                    if (head.length() > 10) {
                        head = head.substring(0, 9) + "...";
                    }
                    String title = u.getDisplayName();
                    docUserGrid.addColumn(userCol, new SafeHtmlHeader(TEMPLATES.spanWithTitle(head, title)), userFooter);

                    sortHandler.setComparator(userCol, new Comparator<DocumentNAssignedImpl>() {

                        @Override
                        public int compare(DocumentNAssignedImpl o1, DocumentNAssignedImpl o2) {
                            Boolean c1 = annotatorsByDocumentId.get(o1.getId()).contains(userId);
                            Boolean c2 = annotatorsByDocumentId.get(o2.getId()).contains(userId);
                            return c1.compareTo(c2);
                        }
                    });
                    userCol.setSortable(true);

                    docUserGrid.setColumnWidth(userCol, 7, Unit.EM);
                }
            }

            injector.getCoreDataProvider().getCampaignUserDocAssignment(selectedCampaignId, new AsyncCallback<DocumentNAssignedListImpl>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Error while retrieving documents");
                    throw new UnsupportedOperationException("Error while retrieving documents");
                }

                @Override
                public void onSuccess(DocumentNAssignedListImpl result) {
                    documentProvider.getList().clear();
                    annotatorsByDocumentId.clear();
                    for (int row = 0; row < result.length(); row++) {
                        DocumentNAssignedImpl d = result.get(row);
                        annotatorsByDocumentId.put(d.getId(), d.getAnnotatorIds());
                        documentProvider.getList().add(d);
                    }

                    docUserGrid.setVisibleRange(0, documentProvider.getList().size());

                }
            });
        }
    }

    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setParams(BasicUserCampaignDocParams params) {
        if (selectedCampaignId != null) {
            selectedCampaignId = params.getCampaignId();
        }
    }

    @Override
    public BasicUserCampaignDocParams getParams() {
        if (selectedCampaignId != null) {
            return new BasicUserCampaignDocParams(selectedUserId, selectedCampaignId, selectedDocId);
        } else {
            return null;
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        docsByCampaign.clear();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        docsByCampaign.clear();
    }
}
