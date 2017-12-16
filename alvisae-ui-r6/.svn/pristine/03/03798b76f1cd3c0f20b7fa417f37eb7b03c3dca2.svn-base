/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ProvidesKey;
import fr.inra.mig_bibliome.alvisae.client.StaneCoreResources;
import fr.inra.mig_bibliome.alvisae.client.StructTermResources;

/**
 * @author fpapazian
 */
public class SmallDialogs {

    public static enum Type {

        None, Info, Warning, Error
    };

    public static enum AllowedAction {

        None, Ok, Cancel, YesCancel
    };

    public static enum Action {

        Apply, Cancel
    };

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    public static class SmallDialogInfo implements Comparable<SmallDialogInfo> {

        public static final ProvidesKey<SmallDialogInfo> KEY_PROVIDER = new ProvidesKey<SmallDialogInfo>() {

            @Override
            public Object getKey(SmallDialogInfo item) {
                return item == null ? null : item.getId();
            }
        };
        private final String id;
        private final Type type;
        private final SafeHtml message;
        private final AllowedAction possibleAction;
        private final Command applyActionCommand;
        private Action actionToBePerformed = null;

        public SmallDialogInfo(Type type, SafeHtml message, AllowedAction possibleAction, Command applyActionCommand) {
            id = HTMLPanel.createUniqueId();
            this.type = type;
            this.possibleAction = possibleAction;
            this.message = message;
            this.applyActionCommand = applyActionCommand;
        }
        
        public SmallDialogInfo(Type type, SafeHtml message, AllowedAction possibleAction) {
            this(type, message, possibleAction, null);
        }

        public String getId() {
            return id;
        }

        public SafeHtml getMessage() {
            return message;
        }

        public AllowedAction getPossibleAction() {
            return possibleAction;
        }

        public Type getType() {
            return type;
        }

        public void setActionToBePerformed(Action actionToBePerformed) {
            this.actionToBePerformed = actionToBePerformed;
        }

        public Action getActionToBePerformed() {
            return actionToBePerformed;
        }

        public Command getApplyActionCommand() {
            return applyActionCommand;
        }
        
        @Override
        public final int compareTo(SmallDialogInfo o) {
            return this.getId().compareTo(o.getId());
        }

    }
    private static final String DIALOGAPPLY_CLASSNAME = "aae_applyBtn";
    private static final String DIALOGCANCEL_CLASSNAME = "aae_cancelBtn";
    private static final SafeHtml infoIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneCoreResources.INSTANCE.InfoMessageIcon()).getHTML());
    private static final SafeHtml warningIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneCoreResources.INSTANCE.WarnMessageIcon()).getHTML());
    private static final SafeHtml errorIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StaneCoreResources.INSTANCE.ErrorMessageIcon()).getHTML());
    private static final SafeHtml Empty = new SafeHtml() {

        @Override
        public String asString() {
            return "";
        }
    };
    private static final SafeHtml applyIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.ApplyIcon()).getHTML());
    private static final SafeHtml cancelIcon = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StructTermResources.INSTANCE.CancelIcon()).getHTML());

    static interface SmallDialogsTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("&nbsp;<span style='height: 16px; width: 16px; background-color: white;'>{0}</span>&nbsp;")
        public SafeHtml IconHolder(SafeHtml icon);

        @SafeHtmlTemplates.Template("<div class='{0}' style='height: 12px; width: 11px; border: lightsteelblue solid 1px; background-color: white;'>{1}</div>")
        public SafeHtml ButtonHolder(String className, SafeHtml icon);

        @SafeHtmlTemplates.Template("<table cellspacing='1' cellpadding='1' style='overflow-x: hidden; height: 100%; width: 100%; border: silver solid 1px; '><tbody><tr>"
        + "<td align='center' style='vertical-align: middle; font-size: smaller;'>{0}{1}</td></tr></tbody></table>")
        public SafeHtml NoButtonMessage(SafeHtml typeIcon, SafeHtml message);

        @SafeHtmlTemplates.Template("<table cellspacing='1' cellpadding='1' style='overflow-x: hidden; height: 100%; width: 100%; border: silver solid 1px; '><tbody><tr>"
        + "<td align='left' style='vertical-align: middle; font-size: smaller;'>{0}{1}</td>"
        + "<td align='center' style='vertical-align: bottom; width: 12px;'>{2}</td></tr>"
        + "</tbody></table>")
        public SafeHtml OneButtonMessage(SafeHtml typeIcon, SafeHtml message, SafeHtml bottomButton);

        @SafeHtmlTemplates.Template("<table cellspacing='1' cellpadding='1' style='overflow-x: hidden; height: 100%; width: 100%; border: silver solid 1px; '><tbody><tr>"
        + "<td align='left' rowspan='2' style='vertical-align: middle; font-size: smaller;'>{0}{1}</td>"
        + "<td align='center' style='vertical-align: top; width: 12px;'>{2}</td></tr>"
        + "<tr><td align='center' style='vertical-align: bottom; width: 12px;'>{3}</td></tr>"
        + "</tbody></table>")
        public SafeHtml TwoButtonsMessage(SafeHtml typeIcon, SafeHtml message, SafeHtml topButton, SafeHtml bottomButton);
    }
    static final SmallDialogsTemplates TEMPLATES = GWT.create(SmallDialogsTemplates.class);

    public static class SmallDialogCell extends AbstractCell<SmallDialogInfo> {

        private static SafeHtml InfoIcon = TEMPLATES.IconHolder(infoIcon);
        private static SafeHtml WarningIcon = TEMPLATES.IconHolder(warningIcon);
        private static SafeHtml ErrorIcon = TEMPLATES.IconHolder(errorIcon);
        private static SafeHtml ApplyButton = TEMPLATES.ButtonHolder(DIALOGAPPLY_CLASSNAME, applyIcon);
        private static SafeHtml CancelButton = TEMPLATES.ButtonHolder(DIALOGCANCEL_CLASSNAME, cancelIcon);

        public SmallDialogCell() {
            super("click");
        }

        @Override
        public void render(Context context, SmallDialogInfo value, SafeHtmlBuilder sb) {
            if (value != null) {
                SafeHtml typeIcon;
                switch (value.getType()) {
                    case Info:
                        typeIcon = InfoIcon;
                        break;
                    case Warning:
                        typeIcon = WarningIcon;
                        break;
                    case Error:
                        typeIcon = ErrorIcon;
                        break;
                    default:
                        typeIcon = Empty;
                }

                switch (value.getPossibleAction()) {
                    case None:
                        sb.append(TEMPLATES.NoButtonMessage(typeIcon, value.getMessage()));
                        break;
                    case Cancel:
                        sb.append(TEMPLATES.OneButtonMessage(typeIcon, value.getMessage(), CancelButton));
                        break;
                    case YesCancel:
                        sb.append(TEMPLATES.TwoButtonsMessage(typeIcon, value.getMessage(), CancelButton, ApplyButton));
                        break;
                    case Ok:
                        sb.append(TEMPLATES.OneButtonMessage(typeIcon, value.getMessage(), ApplyButton));
                        break;
                }
            }
        }

        @Override
        public void onBrowserEvent(Context context, Element parent, SmallDialogInfo value, NativeEvent event, ValueUpdater<SmallDialogInfo> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            // Handle the click event.
            if ("click".equals(event.getType())) {
                // check if click occured in the button image
                EventTarget eventTarget = event.getEventTarget();
                Element targetElement = Element.as(eventTarget);
                if ("IMG".equals(targetElement.getTagName())) {
                    Element parentNde = targetElement.getParentElement();
                    if ("DIV".equals(parentNde.getTagName())) {
                        Action action = null;
                        if (DIALOGAPPLY_CLASSNAME.equals(parentNde.getClassName())) {
                            action = Action.Apply;
                        } else if (DIALOGCANCEL_CLASSNAME.equals(parentNde.getClassName())) {
                            action = Action.Cancel;
                        }

                        if (action != null) {
                            value.setActionToBePerformed(action);
                            if (valueUpdater != null) {
                                valueUpdater.update(value);
                            }
                        }
                    }
                }
            }
        }
    }
}
