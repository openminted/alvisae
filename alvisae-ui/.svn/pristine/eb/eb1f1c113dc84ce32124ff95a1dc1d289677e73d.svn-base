/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;

import fr.inra.mig_bibliome.alvisae.client.SemClass.StructTermUi.SemClassTreeDnDManager;
import fr.inra.mig_bibliome.alvisae.client.StructTermResources;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClass;
import fr.inra.mig_bibliome.alvisae.shared.data3.TermGroupLinkCategory;
import fr.inra.mig_bibliome.alvisae.shared.data3.TermTermLinkCategory;
import gwtquery.plugins.droppable.client.gwt.DragAndDropNodeInfo;

/**
 *
 * @author fpapazian
 */
public class CellFactory {

    //public static final String SEMCLASSID_ATTRNAME = "aae_classId";
    public static final String SEMCLASSMARKER_CLASSNAME = "aae_semClassMkr";
    public static final String SEMCLASSDRAGHND_CLASSNAME = "aae_semClassDrgHnd";
    public static final String SEMCLASSMULTIHYPER_CLASSNAME = "aae_semClassMultiHyper";
    private static final SemClassNodeCell semClassNodeCell = new SemClassNodeCell();
    private static final AbstractTermCell<? extends TermInfo> memberNodeCell = new TermMemberCell();
    private static final AbstractTermCell<? extends TermInfo> linkedNodeCell = new TermLinkedCell();

    public static SemClassNodeCell getSemClassNodeCell() {
        return semClassNodeCell;
    }

    public static AbstractTermCell<? extends TermInfo> getMemberNodeCell() {
        return memberNodeCell;
    }

    public static AbstractTermCell<? extends TermInfo> getLinkedNodeCell() {
        return linkedNodeCell;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static class SemClassNodeInfo extends DefaultNodeInfo<SemClassInfo> {

        public SemClassNodeInfo(AbstractDataProvider<SemClassInfo> nodeDataProvider, SemClassNodeCell nodeCell, AbstractSelectionModel<SemClassInfo> selectionModel, ValueUpdater<SemClassInfo> valueUpdater) {
            super(nodeDataProvider, nodeCell, selectionModel, valueUpdater);
        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static class SemClassExtendedNodeInfo extends DragAndDropNodeInfo<SemClassInfo> {

        public SemClassExtendedNodeInfo(AbstractDataProvider<SemClassInfo> nodeDataProvider, SemClassExtendedNodeCell nodeCell, AbstractSelectionModel<SemClassInfo> selectionModel, ValueUpdater<SemClassInfo> valueUpdater) {
            super(nodeDataProvider, nodeCell, selectionModel, valueUpdater);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static class TermNodeInfo extends DefaultNodeInfo<TermInfo> {

        public TermNodeInfo(AbstractDataProvider<TermInfo> nodeDataProvider, AbstractTermCell<TermInfo> nodeCell, AbstractSelectionModel<TermInfo> selectionModel, ValueUpdater<TermInfo> valueUpdater) {
            super(nodeDataProvider, nodeCell, selectionModel, valueUpdater);
        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private static final SafeHtml dragIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.SemClassDragHandleIcon()).getHTML());
    private static final SafeHtml altDragIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.SemClassAltDragHandleIcon()).getHTML());
    private static final SafeHtml RootIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.RootSemClassNodeIcon()).getHTML());
    private static final SafeHtml RootedNodeIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.RootedNodeIcon()).getHTML());
    private static final SafeHtml RootedLeafNodeIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.RootedLeafNodeIcon()).getHTML());
    private static final SafeHtml HypoNHyperNodeIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.HypoNHyperNodeIcon()).getHTML());
    private static final SafeHtml HypoNodeIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.HypoNodeIcon()).getHTML());
    private static final SafeHtml Empty = new SafeHtml() {

        @Override
        public String asString() {
            return "";
        }
    };

    static interface SemClassNodeTemplates extends SafeHtmlTemplates {

        @Template("<span title='{1} {0}'>{2}&nbsp;&nbsp;{0}</span>")
        public SafeHtml regularNode(String label, int classId, SafeHtml icon);

        @Template("<span title='{1} {0}'>{3}&nbsp;&nbsp;<b>{0}</b>&nbsp;({2})</span>")
        public SafeHtml multiHyperNode(String label, int classId, int nbHyper, SafeHtml icon);

        //@Template("<span id='{1}' title='{2} {0}'><span class='" + SEMCLASSDRAGHND_CLASSNAME + "'>{3}</span>&nbsp;&nbsp;<span class='" + SEMCLASSMARKER_CLASSNAME + "'>{4}</span>&nbsp; {0}</span>")
        //public SafeHtml regularExtendedNode(String label, String extendedSemClassId, int classId, SafeHtml icon, SafeHtml dragIcon);
        @Template("<span id='{1}' title='{2} {0}'><span class='" + SEMCLASSDRAGHND_CLASSNAME + "'>{3}</span>&nbsp;&nbsp;{0}</span>")
        public SafeHtml regularExtendedNode(String label, String extendedSemClassId, int classId, SafeHtml icon);

        //@Template("<span id='{1}' title='{2} {0}'><span class='" + SEMCLASSDRAGHND_CLASSNAME + "'>{5}</span>&nbsp;&nbsp;<span class='" + SEMCLASSMARKER_CLASSNAME + "'>{6}</span>&nbsp;<b>{0}</b>&nbsp;<span class='" + SEMCLASSMULTIHYPER_CLASSNAME + " {4}'>({3})</span></span>")
        //public SafeHtml multiHyperExtendedNode(String label, String extendedSemClassId, int classId, int nbHyper, String flatButtonClassName, SafeHtml icon, SafeHtml dragIcon);
        @Template("<span id='{1}' title='{2} {0}'><span class='" + SEMCLASSDRAGHND_CLASSNAME + "'>{5}</span>&nbsp;&nbsp;<b>{0}</b>&nbsp;<span class='" + SEMCLASSMULTIHYPER_CLASSNAME + " {4}'>({3})</span></span>")
        public SafeHtml multiHyperExtendedNode(String label, String extendedSemClassId, int classId, int nbHyper, String flatButtonClassName, SafeHtml icon);

        @Template("<div id='semClassDragHelper' class='{0}'></div>")
        public SafeHtml outerHelper(String cssClassName);
    }
    static final SemClassNodeTemplates TEMPLATES = GWT.create(SemClassNodeTemplates.class);

    private static SafeHtml getSemClassNodeIcon(SemClassInfo value) {
        SafeHtml image = null;
        if (value != null) {
            if (value.isRoot()) {
                image = RootIcon;
            } else if (value.isRooted()) {
                if (value.hasChild()) {
                    image = RootedNodeIcon;
                } else {
                    image = RootedLeafNodeIcon;
                }
            } else {
                if (value.hasChild()) {
                    image = HypoNHyperNodeIcon;
                } else {
                    image = HypoNodeIcon;
                }
            }
        }
        return image;
    }

    public static class SemClassNodeCell extends AbstractCell<SemClassInfo> {

        @Override
        public void render(Context context, SemClassInfo value, SafeHtmlBuilder sb) {
            SafeHtml image = null;
            if (value != null) {
                image = getSemClassNodeIcon(value);

                if (value.hasSeveralHyperonym()) {
                    sb.append(TEMPLATES.multiHyperNode(value.getCanonicLabel(), value.getId(), value.getFromCache().getHyperGroupIds().length(), image));
                } else {
                    sb.append(TEMPLATES.regularNode(value.getCanonicLabel(), value.getId(), image));
                }
            }
        }
    }

    public static class SemClassExtendedNodeCell extends AbstractCell<SemClassInfo> {

        private PopupPanel popupPanel = new PopupPanel(true);
        private MenuBar menu = new MenuBar(true);
        private MenuBar others = new MenuBar(true);

        {
            popupPanel.add(menu);
            menu.setVisible(true);
            menu.setAutoOpen(true);
            //FIXME NotI18N
            menu.addItem(new MenuItem("others Hyperonyms", others));
        }
        private final SemClassTreeDnDManager treeEventCallback;

        public SemClassExtendedNodeCell(SemClassTreeDnDManager treeEventCallback) {
            super("click", "keydown");
            this.treeEventCallback = treeEventCallback;
        }

        @Override
        public void render(Context context, SemClassInfo value, SafeHtmlBuilder sb) {
            SafeHtml image = null;
            if (value != null) {
                image = getSemClassNodeIcon(value);

                String label;
                SafeHtml dragImage;
                if (value.isRoot()) {
                    label = "[ " + treeEventCallback.getProjectName() + " ]";
                    dragImage = Empty;
                } else {
                    label = value.getCanonicLabel();
                    if (ProviderStore.forProject(value.getProjectId()).isMarked(value.getId())) {
                        dragImage = altDragIcon;
                    } else {
                        dragImage = dragIcon;
                    }
                }
                //
                String extendedSemClassId = ((SemClassExtendedInfo) value).getExtendedSemClassId();
                if (value.hasSeveralHyperonym()) {
                    //sb.append(TEMPLATES.multiHyperExtendedNode(label, extendedSemClassId, value.getId(), value.getFromCache().getHyperGroupIds().length(), StructTermResources.INSTANCE.css().FlatButton(), image, dragImage));
                    sb.append(TEMPLATES.multiHyperExtendedNode(label, extendedSemClassId, value.getId(), value.getFromCache().getHyperGroupIds().length(), StructTermResources.INSTANCE.css().FlatButton(), image));
                } else {
                    //sb.append(TEMPLATES.regularExtendedNode(label, extendedSemClassId, value.getId(), image, dragImage));
                    sb.append(TEMPLATES.regularExtendedNode(label, extendedSemClassId, value.getId(), image));
                }
            }
        }

        @Override
        public void onBrowserEvent(Context context, Element parent, final SemClassInfo value, NativeEvent event, ValueUpdater<SemClassInfo> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);

            // Handle the click event.
            if ("click".equals(event.getType())) {
                if (value instanceof SemClassExtendedInfo) {
                    // check if click occured in the marker img
                    EventTarget eventTarget = event.getEventTarget();
                    Element targetElement = Element.as(eventTarget);
                    if ("IMG".equals(targetElement.getTagName())) {
                        Element parentNde = targetElement.getParentElement();
                        //click on the Semantic Class marker image
                        if ("SPAN".equals(parentNde.getTagName()) && SEMCLASSMARKER_CLASSNAME.equals(parentNde.getClassName())) {

                            //
                            SemClassExtendedInfo extValue = (SemClassExtendedInfo) value;
                            if (valueUpdater != null) {
                                valueUpdater.update(extValue);
                            }
                        }
                    } else //click on the number of hyperonyms
                    if ("SPAN".equals(targetElement.getTagName()) && targetElement.getClassName().contains(SEMCLASSMULTIHYPER_CLASSNAME)) {
                        if (treeEventCallback != null) {

                            SemClassExtendedInfo extValue = (SemClassExtendedInfo) value;
                            SemClass semClass = ProviderStore.forProject(value.getProjectId()).getCacheSemClass(value.getId());

                            others.clearItems();

                            JsArrayInteger hyperIds = semClass.getHyperGroupIds();
                            for (int i = 0; i < hyperIds.length(); i++) {
                                final int hyperId = hyperIds.get(i);
                                SemClass hyperClass = ProviderStore.forProject(value.getProjectId()).getCacheSemClass(hyperId);
                                String label = hyperClass == null ? null : hyperClass.getCanonicLabel();
                                if (label == null) {
                                    label = "「" + String.valueOf(hyperId) + "」";
                                }
                                MenuItem menuItem = new MenuItem(label, new Command() {

                                    @Override
                                    public void execute() {
                                        popupPanel.hide();
                                        treeEventCallback.expandTo(hyperId, value.getId());
                                    }
                                });
                                if (extValue.getParentClassId().intValue() == hyperId) {
                                    menuItem.setEnabled(false);
                                }
                                others.addItem(menuItem);
                            }
                            others.setVisible(true);
                            popupPanel.setPopupPosition(event.getClientX(), event.getClientY());
                            popupPanel.show();
                        }
                    }
                }
            }

        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static abstract class AbstractTermCell<T> extends AbstractCell<T> {

        static interface TermNodeTemplates extends SafeHtmlTemplates {

            @Template("{2}&nbsp;&nbsp;<span title='{1} {0}'>{0}</span>")
            public SafeHtml regularNode(String label, int classId, SafeHtml safeHtml);
        }
        protected static final TermNodeTemplates TEMPLATES = GWT.create(TermNodeTemplates.class);
    }

    public static class TermMemberCell extends AbstractTermCell<TermMemberInfo> {

        private static final SafeHtml CanonicTermIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.LinkCanonicIcon()).getHTML());
        private static final SafeHtml SynonymTermIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.LinkSynonymIcon()).getHTML());
        private static final SafeHtml QuasiSynTermIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.LinkQuasiSynIcon()).getHTML());

        @Override
        public void render(Context context, TermMemberInfo value, SafeHtmlBuilder sb) {

            SafeHtml image = null;
            if (value != null) {
                int memberType = value.getMemberType();
                if (TermGroupLinkCategory.SYNONYM == memberType) {
                    image = SynonymTermIcon;
                } else if (TermGroupLinkCategory.CANONIC == memberType) {
                    image = CanonicTermIcon;
                } else if (TermGroupLinkCategory.QUASISYN == memberType) {
                    image = QuasiSynTermIcon;
                }

                sb.append(TEMPLATES.regularNode(value.getSurfaceForm(), value.getId(), image));
            }
            //
        }
    }
    // =========================================================================

    public static class TermLinkedCell extends AbstractTermCell<TermLinkedInfo> {

        private static final SafeHtml AcronymTermIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.LinkAcronymIcon()).getHTML());
        private static final SafeHtml AcronymInvTermIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.LinkAcronymInvIcon()).getHTML());
        private static final SafeHtml TranslationTermIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.LinkTranslationIcon()).getHTML());
        private static final SafeHtml TypoVarTermIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.LinkTypoVarIcon()).getHTML());
        private static final SafeHtml VariantTermIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.LinkVariantIcon()).getHTML());
        private static final SafeHtml VariantInvTermIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.LinkVariantInvIcon()).getHTML());

        @Override
        public void render(Context context, TermLinkedInfo value, SafeHtmlBuilder sb) {

            SafeHtml image = null;
            if (value != null) {
                int memberType = value.getLinkType();
                boolean isHead = value.isHeadOfLink();
                if (TermTermLinkCategory.ACRONYM == memberType) {
                    if (isHead) {
                        image = AcronymTermIcon;
                    } else {
                        image = AcronymInvTermIcon;
                    }
                } else if (TermTermLinkCategory.TYPOVARIANT == memberType) {
                    image = TypoVarTermIcon;
                } else if (TermTermLinkCategory.VARIANT == memberType) {
                    if (isHead) {
                        image = VariantTermIcon;
                    } else {
                        image = VariantInvTermIcon;
                    }
                } else if (TermTermLinkCategory.TRANSLATION == memberType) {
                    image = TranslationTermIcon;
                }

                sb.append(TEMPLATES.regularNode(value.getSurfaceForm(), value.getId(), image));
            }
        }
    }

    @Deprecated
    public static enum MemberCategory {

        SYNONYM(TermGroupLinkCategory.SYNONYM, StructTermResources.INSTANCE.LinkSynonymIcon()),
        CANONIC(TermGroupLinkCategory.CANONIC, StructTermResources.INSTANCE.LinkCanonicIcon()),
        QUASISYN(TermGroupLinkCategory.QUASISYN, StructTermResources.INSTANCE.LinkQuasiSynIcon());
        private final Integer catId;
        private final ImageResource image;

        private MemberCategory(Integer linkCatId, ImageResource image) {
            this.catId = linkCatId;
            this.image = image;
        }

        public ImageResource getImage() {
            return image;
        }

        public Integer getCatId() {
            return catId;
        }
    }

    @Deprecated
    public static enum LinkCategory {

        TYPOVARIANT(TermTermLinkCategory.TYPOVARIANT, StructTermResources.INSTANCE.LinkTypoVarIcon(), StructTermResources.INSTANCE.LinkTypoVarIcon()),
        ACRONYM(TermTermLinkCategory.ACRONYM, StructTermResources.INSTANCE.LinkAcronymIcon(), StructTermResources.INSTANCE.LinkAcronymInvIcon()),
        VARIANT(TermTermLinkCategory.VARIANT, StructTermResources.INSTANCE.LinkVariantIcon(), StructTermResources.INSTANCE.LinkVariantInvIcon()),
        TRANSLATION(TermTermLinkCategory.TRANSLATION, StructTermResources.INSTANCE.LinkTranslationIcon(), StructTermResources.INSTANCE.LinkTranslationIcon());
        private final Integer linkCatId;
        private final ImageResource directImage;
        private final ImageResource inverseImage;

        private LinkCategory(Integer linkCatId, ImageResource directImage, ImageResource inverseImage) {
            this.linkCatId = linkCatId;

            this.directImage = directImage;
            this.inverseImage = inverseImage;
        }

        public ImageResource getDirectImage() {
            return directImage;
        }

        public ImageResource getInverseImage() {
            return inverseImage;
        }

        public Integer getLinkCatId() {
            return linkCatId;
        }
    }
}
