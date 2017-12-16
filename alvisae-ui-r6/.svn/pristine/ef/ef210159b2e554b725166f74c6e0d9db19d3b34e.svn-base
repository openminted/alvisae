/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.AlvisaeCore;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationMultipleValuesPropertyEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceBroadcastInfoEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceInfoEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceInfoEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceRefSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.Extension.TyDIResourceRequestInfoEvent;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.client.Widgets.TreeUtils;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.ResourceLocator;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.TyDIResRefPropValImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.TyDISemClassRefImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.TyDITermRefImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDIResourceRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDISemClassRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.VersionedTyDISemClassRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDIConceptRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDISemClassRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDITermRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class PropertiesTree extends CellTree {

    private static final String ADDVALBTN_CLASSVAL = "aae_AddValBtn";
    private static final String DELVALBTN_CLASSVAL = "aae_DelValBtn";
    public static final String CHANGETYPEBTN_CLASSVAL = "aae_ChgTypBtn";
    protected static final SafeHtml AddSmallIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.AddSmallIcon()).getHTML());
    protected static final SafeHtml DelSmallIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.DelSmallIcon()).getHTML());

    //
    static interface PropertiesTreeTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span " + CHANGETYPEBTN_CLASSVAL + "='true'>{0}</span>")
        public SafeHtml spanChangeType(SafeHtml content);

        @SafeHtmlTemplates.Template("<b>{0}</b>")
        public SafeHtml spanKey(String label);

        @SafeHtmlTemplates.Template("<div><b>{0}</b><span " + ADDVALBTN_CLASSVAL + "='true' title='add new value' style='position:absolute;right:1px;top:1px;background-color:white;border:silver solid 1px;'>{1}</span></div>")
        public SafeHtml spanKeyAdd(String label, SafeHtml icon);

        @SafeHtmlTemplates.Template("<div title='{0}'>{0}<span " + DELVALBTN_CLASSVAL + "='true' title='remove value' style='position:absolute;right:3px;top:1px;background-color:white;border:silver solid 1px;'>{1}</span></div>")
        public SafeHtml spanValDel(String value, SafeHtml icon);

        @SafeHtmlTemplates.Template("<span title='remove value' style='position:absolute;right:1px;background-color:white;'>{0}</span>")
        public SafeHtml spanDelButton(SafeHtml icon);

        @SafeHtmlTemplates.Template("<div title='{0}' style='font:smaller;color:grey;'>{1}<span " + DELVALBTN_CLASSVAL + "='true' title='remove value' style='position:absolute;right:3px;top:1px;background-color:white;border:silver solid 1px;'>{2}</span></div>")
        public SafeHtml spanTyDIRefValDel(String value, SafeHtml htmlvalue, SafeHtml icon);

        @SafeHtmlTemplates.Template("<div title='{0}' style='font:smaller;color:grey;'>{1}</div>")
        public SafeHtml spanTyDIRefVal(String value, SafeHtml htmlvalue);
    }
    static final PropertiesTreeTemplates TREETEMPLATES = GWT.create(PropertiesTreeTemplates.class);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static interface PropValueChangeHandler {

        void createValueEntry(PropKeyInfo keyInfo);

        void removeValueEntry(PropValueInfo valInfo);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static interface GenericPropInfo {
    }

    public static interface PropKeyInfo extends GenericPropInfo {

        public String getLabel();

        public List<String> getValues();

        public ListDataProvider<PropValueInfo> getValuesDataProvider();

        public PropValueInfo addValue(String value);

        public void removeValue(PropValueInfo valueInfo);

        public String getValue(int index);

        public boolean isReadOnly();
    }

    public static abstract class AbstractPropKeyInfo implements PropKeyInfo, Comparable<PropKeyInfo> {

        public static final ProvidesKey<PropKeyInfo> KEY_PROVIDER = new ProvidesKey<PropKeyInfo>() {
            @Override
            public Object getKey(PropKeyInfo item) {
                return item == null ? null : item.getLabel();
            }
        };

        private AbstractPropKeyInfo(List<String> values) {
            this.values = new ArrayList<String>();
            if (values != null) {
                this.values.addAll(values);
            }
        }
        private final List<String> values;
        private ListDataProvider<PropValueInfo> valuesDataProvider = null;

        @Override
        public List<String> getValues() {
            return values;
        }

        @Override
        public String getValue(int index) {
            if (index < values.size()) {
                return values.get(index);
            } else {
                return null;
            }
        }

        @Override
        public final int compareTo(PropKeyInfo o) {
            return this.getLabel().compareTo(o.getLabel());
        }

        private PropValueInfo resetValuesDataProvider() {
            PropValueInfo lastValueInfo = null;

            if (valuesDataProvider == null) {
                valuesDataProvider = new ListDataProvider<PropValueInfo>();
            }
            valuesDataProvider.getList().clear();
            for (int i = 0; i < values.size(); i++) {
                lastValueInfo = new PropValueInfo(this, i);
                valuesDataProvider.getList().add(lastValueInfo);
            }

            return lastValueInfo;
        }

        @Override
        public ListDataProvider<PropValueInfo> getValuesDataProvider() {
            if (valuesDataProvider == null) {
                resetValuesDataProvider();
            }
            return valuesDataProvider;
        }

        @Override
        public PropValueInfo addValue(String value) {
            int i = values.size();
            values.add(value);
            //workaround : always replace all values, in order to avoid tree weird behaviour when adding signel value (GWT2.4)
            PropValueInfo lastAddedValueInfo = resetValuesDataProvider();

            getValuesDataProvider().refresh();
            return lastAddedValueInfo;
        }

        @Override
        public void removeValue(PropValueInfo valueInfo) {
            if (getValuesDataProvider().getList().contains(valueInfo)) {
                int index = valueInfo.getIndex();
                values.remove(index);
            }
            resetValuesDataProvider();
            getValuesDataProvider().refresh();
        }
    }

    public static class RealPropKeyInfo extends AbstractPropKeyInfo {

        private final PropertyDefinition propDef;
        private final boolean readOnly;

        public RealPropKeyInfo(PropertyDefinition propDef, List<String> values, boolean readOnly) {
            super(values);
            this.propDef = propDef;
            this.readOnly = readOnly;
        }

        @Override
        public String getLabel() {
            return propDef.getKey();
        }

        public PropertyDefinition getPropDef() {
            return propDef;
        }

        @Override
        public boolean isReadOnly() {
            return readOnly;
        }
    }

    public static class TyDIRefValuesRealPropKeyInfo extends RealPropKeyInfo {

        private final PropertyType valType;

        public TyDIRefValuesRealPropKeyInfo(PropertyDefinition propDef, List<String> values, PropertyType valType, boolean readOnly) {
            super(propDef, values, readOnly);
            this.valType = valType;
        }

        public PropertyType getValType() {
            return valType;
        }
    }

    public static class PropKeyNodeCell extends AbstractCell<PropKeyInfo> {

        private final PropValueChangeHandler propValueChangeHandler;

        public PropKeyNodeCell(PropValueChangeHandler propValueChangeHandler) {
            super("click");
            this.propValueChangeHandler = propValueChangeHandler;
        }

        @Override
        public void render(Cell.Context context, PropKeyInfo value, SafeHtmlBuilder sb) {
            if (value != null) {
                String key = value.getLabel();
                if (value.isReadOnly()) {
                    sb.append(TREETEMPLATES.spanKey(key));
                } else {
                    sb.append(TREETEMPLATES.spanKeyAdd(key, AddSmallIcon));
                }
            }
        }

        @Override
        public void onBrowserEvent(Cell.Context context, Element parent, PropKeyInfo value, NativeEvent event, ValueUpdater<PropKeyInfo> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);

            String eventType = event.getType();
            if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                EventTarget evtTarget = event.getEventTarget();
                Element targetElement = evtTarget.cast();
                if (targetElement.getParentElement().getAttribute(ADDVALBTN_CLASSVAL).equals("true")) {
                    if (propValueChangeHandler != null) {
                        propValueChangeHandler.createValueEntry(value);
                    }
                }
            }
        }
    }

    public static class PropKeyNodeInfo extends TreeViewModel.DefaultNodeInfo<PropKeyInfo> {

        public PropKeyNodeInfo(AbstractDataProvider<PropKeyInfo> nodeDataProvider, PropKeyNodeCell nodeCell, SelectionModel<GenericPropInfo> selectionModel, ValueUpdater<PropKeyInfo> valueUpdater) {
            super(nodeDataProvider, nodeCell, selectionModel, valueUpdater);
        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static class PropValueInfo implements GenericPropInfo, Comparable<PropValueInfo> {

        public static final ProvidesKey<PropValueInfo> KEY_PROVIDER = new ProvidesKey<PropValueInfo>() {
            @Override
            public Object getKey(PropValueInfo item) {
                return item == null ? null : item.getId();
            }
        };
        private final PropKeyInfo keyInfo;
        private final int index;

        public PropValueInfo(PropKeyInfo keyInfo, int index) {
            this.keyInfo = keyInfo;
            this.index = index;
        }

        public String getId() {
            return keyInfo.getLabel() + "~" + index;
        }

        public String getValue() {
            return keyInfo.getValue(index);
        }

        void setValue(String value) {
            keyInfo.getValues().set(index, value);
        }

        public Integer getIndex() {
            return index;
        }

        public PropKeyInfo getKeyInfo() {
            return keyInfo;
        }

        public void remove() {
            getKeyInfo().removeValue(this);
        }

        @Override
        public final int compareTo(PropValueInfo o) {
            return this.getId().compareTo(o.getId());
        }
    }

    public static class PropValueNodeCell extends AbstractCell<PropValueInfo> {

        protected final boolean readonly;
        protected final PropValueChangeHandler propValueChangeHandler;

        public PropValueNodeCell(boolean readonly, PropValueChangeHandler propValueChangeHandler) {
            super("click");
            this.readonly = readonly;
            this.propValueChangeHandler = propValueChangeHandler;
        }

        @Override
        public void render(Cell.Context context, PropValueInfo value, SafeHtmlBuilder sb) {
            if (value != null) {
                if (readonly) {
                    sb.appendEscaped(value.getValue());
                } else {
                    sb.append(TREETEMPLATES.spanValDel(value.getValue(), DelSmallIcon));
                }
            }
        }

        @Override
        public void onBrowserEvent(Cell.Context context, Element parent, PropValueInfo value, NativeEvent event, ValueUpdater<PropValueInfo> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);

            String eventType = event.getType();
            if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                EventTarget evtTarget = event.getEventTarget();
                Element targetElement = evtTarget.cast();
                if (targetElement.getParentElement().getAttribute(DELVALBTN_CLASSVAL).equals("true")) {
                    if (propValueChangeHandler != null) {
                        propValueChangeHandler.removeValueEntry(value);
                    }
                }
            }
        }
    }

    public static class PropValueNodeInfo extends TreeViewModel.DefaultNodeInfo<PropValueInfo> {

        public PropValueNodeInfo(AbstractDataProvider<PropValueInfo> nodeDataProvider, Cell<PropValueInfo> nodeCell, SelectionModel<GenericPropInfo> selectionModel) {
            super(nodeDataProvider, nodeCell, selectionModel, null);
        }
    }

    //
    private static CompositeCell<PropValueInfo> createEditableFreeTextCell(final PropValueChangeHandler propValueChangeHandler, final FieldUpdater<PropValueInfo, String> valueUpdater) {

        CompositeCell<PropValueInfo> editableFreeTextCell;

        List<HasCell<PropValueInfo, ?>> hasCells = new ArrayList<HasCell<PropValueInfo, ?>>();

        hasCells.add(new HasCell<PropValueInfo, String>() {
            private EditTextCell cell = new EditTextCell();

            @Override
            public EditTextCell getCell() {
                return cell;
            }

            @Override
            public FieldUpdater<PropValueInfo, String> getFieldUpdater() {
                return valueUpdater;
            }

            @Override
            public String getValue(PropValueInfo object) {
                return object.getValue();
            }
        });

        hasCells.add(new HasCell<PropValueInfo, PropValueInfo>() {
            private AbstractCell<PropValueInfo> cell = new AbstractCell<PropValueInfo>("click") {
                @Override
                public void render(Cell.Context context, PropValueInfo value, SafeHtmlBuilder sb) {
                    sb.append(TREETEMPLATES.spanDelButton(DelSmallIcon));
                }

                @Override
                public void onBrowserEvent(Cell.Context context, Element parent, PropValueInfo value, NativeEvent event, ValueUpdater<PropValueInfo> valueUpdater) {
                    super.onBrowserEvent(context, parent, value, event, valueUpdater);

                    String eventType = event.getType();
                    if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                        if (propValueChangeHandler != null) {
                            propValueChangeHandler.removeValueEntry(value);
                        }
                    }
                }
            };

            @Override
            public AbstractCell<PropValueInfo> getCell() {
                return cell;
            }

            @Override
            public FieldUpdater<PropValueInfo, PropValueInfo> getFieldUpdater() {
                return null;
            }

            @Override
            public PropValueInfo getValue(PropValueInfo object) {
                return object;
            }
        });
        editableFreeTextCell = new CompositeCell<PropValueInfo>(hasCells) {
        };
        return editableFreeTextCell;
    }

    private static CompositeCell<PropValueInfo> createEditableClosedDomainCell(final PropValueChangeHandler propValueChangeHandler, final FieldUpdater<PropValueInfo, String> valueUpdater, String defaultValue, final List<String> domainValues) {

        CompositeCell<PropValueInfo> editableClosedDomainCell;
        List<HasCell<PropValueInfo, ?>> hasCells = new ArrayList<HasCell<PropValueInfo, ?>>();

        hasCells.add(new HasCell<PropValueInfo, String>() {
            private SelectionCell cell = new SelectionCell(domainValues);

            @Override
            public SelectionCell getCell() {
                return cell;
            }

            @Override
            public FieldUpdater<PropValueInfo, String> getFieldUpdater() {
                return valueUpdater;
            }

            @Override
            public String getValue(PropValueInfo object) {
                return object.getValue();
            }
        });

        hasCells.add(new HasCell<PropValueInfo, PropValueInfo>() {
            private AbstractCell<PropValueInfo> cell = new AbstractCell<PropValueInfo>("click") {
                @Override
                public void render(Cell.Context context, PropValueInfo value, SafeHtmlBuilder sb) {
                    sb.append(TREETEMPLATES.spanDelButton(DelSmallIcon));
                }

                @Override
                public void onBrowserEvent(Cell.Context context, Element parent, PropValueInfo value, NativeEvent event, ValueUpdater<PropValueInfo> valueUpdater) {
                    super.onBrowserEvent(context, parent, value, event, valueUpdater);

                    String eventType = event.getType();
                    if ("click".equals(eventType) && !event.getAltKey() && !event.getCtrlKey() && !event.getMetaKey()) {
                        if (propValueChangeHandler != null) {
                            propValueChangeHandler.removeValueEntry(value);
                        }
                    }
                }
            };

            @Override
            public AbstractCell<PropValueInfo> getCell() {
                return cell;
            }

            @Override
            public FieldUpdater<PropValueInfo, PropValueInfo> getFieldUpdater() {
                return null;
            }

            @Override
            public PropValueInfo getValue(PropValueInfo object) {
                return object;
            }
        });
        editableClosedDomainCell = new CompositeCell<PropValueInfo>(hasCells) {
        };

        return editableClosedDomainCell;

    }

    public static abstract class PropValueFieldValueUpdater implements FieldUpdater<PropValueInfo, String>, ValueUpdater<PropValueInfo> {
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static class PropertiesTreeViewModel implements TreeViewModel, TyDIResourceInfoEventHandler {

        public static TyDIResourceRef getTyDIResourceRefExternalId(PropertyType valType, String resourceExternalId) {
            TyDIResourceRef tyDIResRef = null;
            PropType_TyDISemClassRef semClassRefType = valType.getAsTyDISemClassRefType();
            if (semClassRefType != null) {
                String baseUrl = semClassRefType.getSemClassRefBaseURL();
                if (baseUrl != null && !baseUrl.isEmpty()) {
                    Integer semClassId = TyDISemClassRefImpl.getSemClassIdFromSemClassExternalId(resourceExternalId);
                    if (semClassId != null) {
                        tyDIResRef = new TyDISemClassRefImpl(new ResourceLocator(baseUrl), semClassId, null);
                    }
                }
            } else {
                PropType_TyDIConceptRef conceptRefType = valType.getAsTyDIConceptRefType();
                if (conceptRefType != null) {

                    String baseUrl = conceptRefType.getSemClassRefBaseURL();
                    if (baseUrl != null && !baseUrl.isEmpty()) {
                        Integer semClassId = TyDISemClassRefImpl.getSemClassIdFromSemClassExternalId(resourceExternalId);
                        if (semClassId != null) {
                            tyDIResRef = new TyDISemClassRefImpl(new ResourceLocator(baseUrl), semClassId, null);
                        }
                    }
                } else {
                    PropType_TyDITermRef termRefType = valType.getAsTyDITermRefType();
                    if (termRefType != null) {

                        String baseUrl = termRefType.getTermRefBaseURL();
                        if (baseUrl != null && !baseUrl.isEmpty()) {
                            Integer termId = TyDITermRefImpl.getTermIdFromTermExternalId(resourceExternalId);
                            if (termId != null) {
                                tyDIResRef = new TyDITermRefImpl(new ResourceLocator(baseUrl), termId);
                            }
                        }
                    }
                }
            }
            return tyDIResRef;
        }

        public class PropTyDIRefValueNodeCell extends PropValueNodeCell {

            public PropTyDIRefValueNodeCell(boolean readonly, PropValueChangeHandler propValueChangeHandler) {
                super(readonly, propValueChangeHandler);
            }

            @Override
            public void render(Cell.Context context, PropValueInfo value, SafeHtmlBuilder sb) {
                if (value != null) {
                    String strValue = value.getValue();
                    SafeHtml htmlValue;

                    if (value.getKeyInfo() instanceof TyDIRefValuesRealPropKeyInfo) {
                        SafeHtmlBuilder shb = new SafeHtmlBuilder();
                        PropertyType valType = ((TyDIRefValuesRealPropKeyInfo) value.getKeyInfo()).getValType();

                        String semClassReference = strValue;
                        TyDIResourceRef tyDIResRef = PropertiesTreeViewModel.getTyDIResourceRefExternalId(valType, semClassReference);
                        if (tyDIResRef != null && (tyDIResRef instanceof TyDISemClassRefImpl)) {

                            boolean warningDisplayed = false;
                            if (termStructVersionNum == null) {
                                shb.append(TEMPLATES.someIcon(StanEditorResources.INSTANCE.css().unknownIcon()));
                                warningDisplayed = true;
                            } else if (versionObsolete) {
                                shb.append(TEMPLATES.someIcon(StanEditorResources.INSTANCE.css().warnIcon()));
                                warningDisplayed = true;
                            }

                            //Check if the semantic class reference is an url with a fragment embedding the concept label
                            TyDIResRefPropValImpl resRefPropVal = TyDIResRefPropValImpl.createFromUrlWithFragment(semClassReference);
                            if (resRefPropVal != null && resRefPropVal.getLabel() != null) {
                                if (!warningDisplayed && resRefPropVal.getVersionNumber() != termStructVersionNum) {
                                    shb.append(TEMPLATES.someIcon(StanEditorResources.INSTANCE.css().warnIcon()));
                                    warningDisplayed = true;
                                }
                                shb.appendEscaped(resRefPropVal.getLabel())
                                        .appendEscaped(" [" + resRefPropVal.getVersionNumber() + "]");
                            } else {
                                //semantic class reference is a simple url : label has to be retrieved by calling TyDI web service
                                TyDISemClassRefImpl semClassRef = (TyDISemClassRefImpl) tyDIResRef;
                                if (semClassLabels.containsKey(semClassRef.getTyDISemanticClassId())) {
                                    shb.appendEscaped(semClassLabels.get(semClassRef.getTyDISemanticClassId()));
                                } else {
                                    shb.appendEscaped(semClassReference);
                                }
                            }
                        } else {
                            shb.appendEscaped(semClassReference);
                        }
                        htmlValue = shb.toSafeHtml();

                    } else {
                        htmlValue = SafeHtmlUtils.fromString(strValue);
                    }

                    if (readonly) {
                        sb.append(TREETEMPLATES.spanTyDIRefVal(strValue, htmlValue));
                    } else {
                        sb.append(TREETEMPLATES.spanTyDIRefValDel(strValue, htmlValue, DelSmallIcon));
                    }
                }
            }
        }
        //Cells used to Property values
        private final PropValueNodeCell PropUnRemovableValCell = new PropValueNodeCell(true, null);
        private final PropValueNodeCell PropRemovableValCell;
        private final PropValueNodeCell PropUnRemovableTyDIRefValCell = new PropTyDIRefValueNodeCell(true, null);
        private final PropValueNodeCell PropRemovableTyDIRefValCell;
        //
        private AnnotatedTextHandler annotatedTextHnd;
        private Annotation annotation;
        private boolean nonModifiableAnnotation;
        private final ListDataProvider<PropKeyInfo> propsDataProvider = new ListDataProvider<PropKeyInfo>();
        private final SingleSelectionModel<GenericPropInfo> selectionModel;
        private final PropValueNodeChangedHandler propValueNodeChangedHandler;
        private final PropValueChangeHandler propValueChangeHandler;
        private final PropValueFieldValueUpdater propValUpdater;

        public PropertiesTreeViewModel(PropValueNodeChangedHandler propValueNodeChangedHandler, SingleSelectionModel<GenericPropInfo> selectionModel) {
            this.propValueNodeChangedHandler = propValueNodeChangedHandler;
            this.selectionModel = selectionModel;

            this.propValueChangeHandler = new PropValueChangeHandler() {
                @Override
                public void createValueEntry(PropKeyInfo keyInfo) {
                    if (keyInfo != null && !keyInfo.isReadOnly() && getAnnotation() != null) {
                        PropValueInfo valueInfo = keyInfo.addValue("...");

                        //if this is the first value created need to refresh the parent (Property key node was a leaf before)
                        performEditUpdatePropValue(valueInfo);

                        if (PropertiesTreeViewModel.this.propValueNodeChangedHandler != null) {
                            //inform the widget that a new value has been added
                            PropertiesTreeViewModel.this.propValueNodeChangedHandler.onValueEntryCreated(valueInfo);
                        }
                    }
                }

                @Override
                public void removeValueEntry(PropValueInfo valueInfo) {
                    if (valueInfo != null && !valueInfo.getKeyInfo().isReadOnly() && getAnnotation() != null) {
                        valueInfo.remove();

                        performEditUpdatePropValue(valueInfo);

                        //no actual refresh of the treeView, will be done by processing the onEdit event

                        if (PropertiesTreeViewModel.this.propValueNodeChangedHandler != null) {
                            PropertiesTreeViewModel.this.propValueNodeChangedHandler.onValueEntryRemoved(valueInfo.getKeyInfo());
                        }
                    }
                }
            };

            this.propValUpdater = new PropValueFieldValueUpdater() {
                @Override
                public void update(int index, PropValueInfo valueInfo, String value) {

                    if (!valueInfo.getKeyInfo().isReadOnly() && getAnnotation() != null) {

                        valueInfo.setValue(value);
                        performEditUpdatePropValue(valueInfo);

                    }
                }

                @Override
                public void update(PropValueInfo valueInfo) {
                    if (!valueInfo.getKeyInfo().isReadOnly() && getAnnotation() != null) {
                        performEditUpdatePropValue(valueInfo);
                    }
                }
            };

            PropRemovableValCell = new PropValueNodeCell(false, propValueChangeHandler);
            PropRemovableTyDIRefValCell = new PropTyDIRefValueNodeCell(false, propValueChangeHandler);

        }

        public AnnotatedTextHandler getAnnotatedTextHandler() {
            return annotatedTextHnd;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        private PropKeyInfo getKeyInfo(String keyLabel) {
            for (PropKeyInfo info : propsDataProvider.getList()) {
                if (info.getLabel().equals(keyLabel)) {
                    return info;
                }
            }
            return null;
        }

        private PropValueInfo getValueInfo(String keyLabel, Integer index) {
            PropValueInfo result = null;
            PropKeyInfo keyInfo = getKeyInfo(keyLabel);
            if (keyInfo != null) {
                List<PropValueInfo> valueInfos = keyInfo.getValuesDataProvider().getList();
                if (index < valueInfos.size()) {
                    result = valueInfos.get(index);
                }
            }
            return result;
        }

        public void performEditUpdatePropValue(PropValueInfo valueInfo) {
            if (!valueInfo.getKeyInfo().isReadOnly() && getAnnotation() != null) {
                String key = valueInfo.getKeyInfo().getLabel();
                Annotation ann = getAnnotation();
                List<String> oldValues = new ArrayList<String>();
                if (ann.getProperties() != null && ann.getProperties().getValues(key) != null) {
                    oldValues.addAll(ann.getProperties().getValues(key));
                }
                List<String> newValues = new ArrayList<String>(valueInfo.getKeyInfo().getValues());

                AnnotationMultipleValuesPropertyEdit propedit = new AnnotationMultipleValuesPropertyEdit(annotatedTextHnd, ann, key, oldValues, newValues);
                propedit.redo();
            }
        }

        @Override
        public <T> TreeViewModel.NodeInfo<?> getNodeInfo(T value) {
            if (value == null) {
                //root
                return new PropKeyNodeInfo(propsDataProvider, new PropKeyNodeCell(propValueChangeHandler), selectionModel, null);
            } else if (value instanceof PropKeyInfo) {

                PropKeyInfo keyInfo = (PropKeyInfo) value;
                Cell<PropValueInfo> cell;

                if (keyInfo.isReadOnly()) {
                    PropertyType valType = ((RealPropKeyInfo) keyInfo).getPropDef().getValuesType();
                    if ((valType != null) && (valType.getAsTyDITermRefType() != null || valType.getAsTyDISemClassRefType() != null)) {
                        //Non-editable, Non-removable Termlink value
                        cell = PropUnRemovableTyDIRefValCell;
                    } else {
                        //Non-editable, Non-removable Value
                        cell = PropUnRemovableValCell;
                    }
                } else {
                    if (value instanceof RealPropKeyInfo) {
                        PropertyType valType = ((RealPropKeyInfo) keyInfo).getPropDef().getValuesType();
                        if (valType == null) {
                            //no type => Free text (editable and removable Value)
                            cell = createEditableFreeTextCell(propValueChangeHandler, propValUpdater);
                        } else {
                            if (valType.getAsTyDIConceptRefType() != null) {
                                //Non-editable but removable Value
                                cell = PropRemovableTyDIRefValCell;
                            } else if (valType.getAsClosedDomainType() != null) {
                                //editable via selection on combobox and removable Value
                                cell = createEditableClosedDomainCell(propValueChangeHandler, propValUpdater, valType.getAsClosedDomainType().getDefaultValue(), valType.getAsClosedDomainType().getDomainValues());
                            } else {
                                //editable and removable Value
                                cell = createEditableFreeTextCell(propValueChangeHandler, propValUpdater);
                            }
                        }
                    } else {
                        //Non-editable and non-removable Value
                        cell = new PropValueNodeCell(true, null);
                    }
                }
                return new PropValueNodeInfo(keyInfo.getValuesDataProvider(), cell, selectionModel);
            } else {
                //should not happen
                return null;
            }
        }

        @Override
        public boolean isLeaf(Object value) {
            if (getAnnotation() == null) {
                return false;
            } else if (value == null) {
                //the root node is not a leaf!!!
                return false;
            } else if (value instanceof PropKeyInfo) {
                return ((PropKeyInfo) value).getValues().isEmpty();
            } else {
                return true;
            }
        }

        public void setAnnotation(AnnotatedTextHandler annotatedTextHnd, final Annotation annotation, final boolean readonly, final Scheduler.ScheduledCommand executedAfter) {

            this.annotatedTextHnd = annotatedTextHnd;
            this.annotation = annotation;
            this.nonModifiableAnnotation = readonly;
            propsDataProvider.getList().clear();

            referencedSemClasses.clear();

            if (annotation != null) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        List<PropKeyInfo> keyList = new ArrayList<PropKeyInfo>();

                        AnnotationSchemaDefinition schema = annotation.getAnnotatedText().getAnnotationSchema();
                        AnnotationTypeDefinition annTypeDef = schema.getAnnotationTypeDefinition(annotation.getAnnotationType());
                        Collection<PropertyDefinition> propDefs = null;
                        if (annTypeDef != null) {
                            Properties props = annotation.getProperties();
                            propDefs = annTypeDef.getPropertiesDefinition().getPropertyDefinitions();
                            for (PropertyDefinition propDef : propDefs) {
                                String key = propDef.getKey();
                                PropertyType valType = propDef.getValuesType();

                                //SemClass or Term link can not be removed, just modified by DnD on Termino/Onto
                                boolean nonModifiableKey = readonly || (valType != null && (valType.getAsTyDISemClassRefType() != null || valType.getAsTyDITermRefType() != null));
                                if (valType != null && valType.isTyDIResourceRef()) {
                                    List<String> values = (props != null) ? props.getValues(key) : null;
                                    //property values are external id (=url) that should be transcribed to the corresponding label
                                    keyList.add(new TyDIRefValuesRealPropKeyInfo(propDef, values, valType, nonModifiableKey));
                                    //Request info for values corresponding to referenced to TyDI class/concept
                                    if (values != null) {
                                        for (String val : values) {
                                            TyDIResourceRef tyDIResRef = PropertiesTreeViewModel.getTyDIResourceRefExternalId(valType, val);
                                            if (tyDIResRef != null) {
                                                if (tyDIResRef instanceof TyDISemClassRef) {
                                                    referencedSemClasses.add(((TyDISemClassRef) tyDIResRef).getTyDISemanticClassId());
                                                }
                                                EventBus eventBus = injector.getMainEventBus();
                                                eventBus.fireEvent(new TyDIResourceRequestInfoEvent(tyDIResRef));
                                            }
                                        }
                                    }
                                } else {
                                    keyList.add(new RealPropKeyInfo(propDef, props != null ? props.getValues(key) : null, nonModifiableKey));
                                }
                            }
                        }
                        propsDataProvider.setList(keyList);
                        if (executedAfter != null) {
                            Scheduler.get().scheduleDeferred(executedAfter);
                        }
                    }
                });
            }
        }
        //Keep track of Semantic classes labels of the current Terminology project
        private final Map<Integer, String> semClassLabels = new HashMap<Integer, String>();
        private final Set<Integer> referencedSemClasses = new HashSet<Integer>();
        String instBaseUrl;
        Integer projectId;
        private Integer termStructVersionNum;
        private boolean versionObsolete = false;

        static interface Templates extends SafeHtmlTemplates {

            @SafeHtmlTemplates.Template("<div class='{0}' style='display:inline-block;'></div>")
            public SafeHtml someIcon(String style);
        }
        static final Templates TEMPLATES = GWT.create(Templates.class);

        @Override
        public void onTyDIResourceInfoEvent(TyDIResourceInfoEvent event) {
            if (event instanceof TyDIResourceBroadcastInfoEvent) {
                //collect labels of classes/concepts
                TyDIResourceRef resRef = event.getTyDIResourceRef();
                if (resRef != null) {
                    if (resRef instanceof TyDISemClassRef) {
                        boolean refreshNeeded = false;

                        TyDISemClassRef semClassRef = (TyDISemClassRef) resRef;
                        String currInstBaseUrl = resRef.getTyDIInstanceBaseUrl();
                        Integer currProjectId = resRef.getTyDIProjectId();
                        if (!(currProjectId.equals(projectId)) || !currInstBaseUrl.equals(instBaseUrl)) {
                            termStructVersionNum = null;
                            instBaseUrl = currInstBaseUrl;
                            projectId = currProjectId;
                            refreshNeeded = true;
                        }

                        if (resRef instanceof VersionedTyDISemClassRef) {
                            VersionedTyDISemClassRef versionedTyDIRef = (VersionedTyDISemClassRef) resRef;
                            refreshNeeded = (versionedTyDIRef.isTermStructVersionObsolete() != versionObsolete)
                                    || (termStructVersionNum == null) || (versionedTyDIRef.getTermStructVersion() != termStructVersionNum);
                            termStructVersionNum = versionedTyDIRef.getTermStructVersion();
                            versionObsolete = versionedTyDIRef.isTermStructVersionObsolete();
                        }


                        Integer classId = semClassRef.getTyDISemanticClassId();
                        semClassLabels.put(classId, semClassRef.getCanonicLabel());
                        if (refreshNeeded || referencedSemClasses.contains(classId)) {
                            //force refresh
                            propsDataProvider.refresh();
                        }
                    }
                }
            }
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static interface PropValueNodeChangedHandler {

        void onValueEntryCreated(PropValueInfo valInfo);

        void onValueEntryRemoved(PropKeyInfo keyInfo);
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static interface TreeResources extends CellTree.Resources {

        @ClientBundle.Source("PropertiesTree.css")
        public CellTree.Style cellTreeStyle();
    }
    private static final TreeResources res = GWT.create(TreeResources.class);

    static {
        res.cellTreeStyle().ensureInjected();
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    private static TreeUtils.TreeNodeTraveller nodeTraveller = new TreeUtils.TreeNodeTraveller();
    private final SingleSelectionModel<GenericPropInfo> selectionModel;

    public PropertiesTree(PropertiesTreeViewModel model, SingleSelectionModel<GenericPropInfo> selectionModel) {
        super(model, null, res);

        setAnimationEnabled(false);
        setDefaultNodeSize(500);

        this.selectionModel = selectionModel;
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                GenericPropInfo selected = getSelectionModel().getSelectedObject();
                if (selected != null && selected instanceof PropValueInfo) {
                    PropValueInfo valueInfo = (PropValueInfo) selected;
                    if (valueInfo.getKeyInfo() instanceof RealPropKeyInfo) {
                        RealPropKeyInfo keyInfo = (RealPropKeyInfo) valueInfo.getKeyInfo();
                        PropertyType valType = keyInfo.getPropDef().getValuesType();
                        if (valType != null && valType.isTyDIResourceRef()) {
                            TyDIResourceRef tyDIResRef = PropertiesTreeViewModel.getTyDIResourceRefExternalId(valType, valueInfo.getValue());
                            if (tyDIResRef != null) {
                                EventBus eventBus = injector.getMainEventBus();
                                eventBus.fireEvent(new TyDIResourceRefSelectionChangedEvent(tyDIResRef));
                            }
                        }
                    }
                }
            }
        });

    }

    private void setNodeOpen(PropValueInfo valInfo) {
        ChildNodeRef nodeRef = getNode(valInfo.keyInfo);
        if (nodeRef != null) {
            nodeRef.parentNode.setChildOpen(nodeRef.index, true);
        }
    }

    class ChildNodeRef {

        private final TreeNode parentNode;
        private final int index;

        public ChildNodeRef(TreeNode parentNode, int index) {
            this.parentNode = parentNode;
            this.index = index;
        }
    }

    private ChildNodeRef getNode(final Object value) {

        TreeUtils.TreeNodeTraveller.NodeExpressionWithResult<ChildNodeRef> expression = new TreeUtils.TreeNodeTraveller.NodeExpressionWithResult<ChildNodeRef>() {
            ChildNodeRef r = null;

            @Override
            public boolean evaluateAndContinue(TreeNode parentNode, int childNodeIndex) {
                boolean result;

                Object childvalue = parentNode.getChildValue(childNodeIndex);
                if ((childvalue != null) && (childvalue == value)) {
                    result = false;
                    setResult(new ChildNodeRef(parentNode, childNodeIndex));
                } else {
                    result = true;
                }
                return result;
            }

            @Override
            public void setResult(ChildNodeRef result) {
                r = result;
            }

            @Override
            public ChildNodeRef getResult() {
                return r;
            }
        };

        nodeTraveller.travelAndOpenNodes(getRootTreeNode(), expression);

        return expression.getResult();
    }

    private void TreeNodeExpand(TreeNode node) {
        if (node != null) {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (!node.isChildLeaf(i)) {
                    TreeNodeExpand(node.setChildOpen(i, true));
                }
            }
        }
    }

    public SingleSelectionModel<GenericPropInfo> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public PropertiesTreeViewModel getTreeViewModel() {
        return (PropertiesTreeViewModel) super.getTreeViewModel();
    }

    void openNodeAndSelect(PropValueInfo valInfo) {
        setNodeOpen(valInfo);
        getSelectionModel().setSelected(valInfo, true);
    }

    public ScheduledCommand expandAndSelect(final Object previouslySelected) {
        return new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {

                //expand all
                TreeNodeExpand(getRootTreeNode());

                //Restore previously selected Node
                if (previouslySelected != null) {
                    if (previouslySelected instanceof PropKeyInfo) {
                        PropKeyInfo keyInfo = getTreeViewModel().getKeyInfo(((PropKeyInfo) previouslySelected).getLabel());
                        if (keyInfo != null) {
                            getSelectionModel().setSelected(keyInfo, true);
                        }
                    } else if (previouslySelected instanceof PropValueInfo) {

                        PropValueInfo valueInfo = getTreeViewModel().getValueInfo(((PropValueInfo) previouslySelected).getKeyInfo().getLabel(), ((PropValueInfo) previouslySelected).getIndex());
                        if (valueInfo != null) {
                            getSelectionModel().setSelected(valueInfo, true);
                        }
                    }
                }
            }
        };
    }
}
