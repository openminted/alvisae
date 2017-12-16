/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientExtGinInjector;
import fr.inra.mig_bibliome.alvisae.client.StructTermResources;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.SemClassChangeType;
import static fr.inra.mig_bibliome.alvisae.client.data3.Extension.SemClassChangeType.ChangedAncestor;
import fr.inra.mig_bibliome.alvisae.client.data3.StructTermChangesImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClassChange;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class ShowChangesDialogBox {

    private static final StaneClientExtGinInjector termInjector = GWT.create(StaneClientExtGinInjector.class);

    static interface ShowChangesDialogBoxTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<b>{0}</b> change(s) detected between v<b>{1}</b> and v<b>{2}</b> :<br><ul>{3}</ul>")
        public SafeHtml changesSumary(int nbChange, int fromVersion, int toVersion, SafeHtml details);

        @SafeHtmlTemplates.Template("<li>{0}</li> ")
        public SafeHtml item(SafeHtml itemContent);

        @SafeHtmlTemplates.Template("class <span title='{0}'><b>{1}</b> ")
        public SafeHtml semClassChangesHeader(int semClassId, String canonicLabel);

        @SafeHtmlTemplates.Template(" {0} <br>")
        public SafeHtml noOperandExplanation(String message);

        @SafeHtmlTemplates.Template(" {0} <span title='{1}'><b> {2} </b> {3}<br>")
        public SafeHtml oneOperandExplanation(String startMessage, int id, String label, String endMessage);

        @SafeHtmlTemplates.Template(" {0} <span title='{1}'><b> {2} </b> {3} <span title='{4}'><b>{5}</b> {6}<br>")
        public SafeHtml twoOperandsExplanation(String startMessage, int id1, String label1, String middleMessage, int id2, String label2, String endMessage);
    }
    static final ShowChangesDialogBoxTemplates TEMPLATES = GWT.create(ShowChangesDialogBoxTemplates.class);

    private static SafeHtml uterChanges(StructTermChangesImpl result) {
        SafeHtmlBuilder lsb = new SafeHtmlBuilder();

        for (SemClassChange c : result.getChanges()) {
            SafeHtmlBuilder sb = new SafeHtmlBuilder();
            sb.append(TEMPLATES.semClassChangesHeader(c.getSemClass().getId(), c.getSemClass().getCanonicLabel()));
            SemClassChangeType changeType = c.getChangeType();

            boolean direct = false;
            switch (changeType) {
                case AddedSemClass:
                    sb.append(TEMPLATES.noOperandExplanation("has been created"));
                    break;
                case RemovedSemClass:
                    sb.append(TEMPLATES.noOperandExplanation("has been removed"));
                    break;
                case ChangedSemClassCanonic:
                    if (c.getLinkedTerm() != null) {
                        sb.append(TEMPLATES.oneOperandExplanation("has changed of canonical representative (previously ", c.getLinkedTerm().getTermId(), c.getLinkedTerm().getSurfaceForm(), ")"));
                    } else {
                        sb.append(TEMPLATES.noOperandExplanation("has changed of canonical representative (previous not specified)"));
                    }
                    break;
                case AddedTermToSemClass:
                    if (c.getLinkedTerm() != null) {
                        sb.append(TEMPLATES.oneOperandExplanation("was added a new term :", c.getLinkedTerm().getTermId(), c.getLinkedTerm().getSurfaceForm(), ""));
                    } else {
                        sb.append(TEMPLATES.noOperandExplanation("was added a new term (not specified)"));
                    }
                    break;
                case RemovedTermFromSemClass:
                    if (c.getLinkedTerm() != null) {
                        sb.append(TEMPLATES.oneOperandExplanation("was removed of the term :", c.getLinkedTerm().getTermId(), c.getLinkedTerm().getSurfaceForm(), ""));
                    } else {
                        sb.append(TEMPLATES.noOperandExplanation("was removed of a term (not specified)"));
                    }
                    break;
                case ChangedDirectAncestor:
                    direct = true;
                case ChangedAncestor:
                    String msg;
                    if (direct) {
                        msg = "had its direct ancestor changed";
                    } else {
                        msg = "had an ancestor changed";
                    }
                    if (c.getOtherSemClass1() != null || c.getOtherSemClass2() != null) {
                        int fromId;
                        String fromLabel;
                        if (c.getOtherSemClass2() == null) {
                            fromId = 0;
                            fromLabel = "[ Root ]";
                        } else {
                            fromId = c.getOtherSemClass2().getId();
                            fromLabel = c.getOtherSemClass2().getCanonicLabel();
                        }
                        int toId;
                        String toLabel;
                        if (c.getOtherSemClass1() == null) {
                            toId = 0;
                            toLabel = "[ Root ]";
                        } else {
                            toId = c.getOtherSemClass1().getId();
                            toLabel = c.getOtherSemClass1().getCanonicLabel();
                        }
                        sb.append(TEMPLATES.twoOperandsExplanation(msg + " (from", fromId, fromLabel, "to", toId, toLabel, ")"));
                    } else {
                        sb.append(TEMPLATES.noOperandExplanation(msg + " (not specified)"));
                    }
                    break;
            }
            lsb.append(TEMPLATES.item(sb.toSafeHtml()));
        }

        SafeHtmlBuilder gsb = new SafeHtmlBuilder();
        gsb.append(TEMPLATES.changesSumary(result.getChanges().size(), result.getFromVersion(), result.getToVersion(), lsb.toSafeHtml()));
        return gsb.toSafeHtml();
    }

    static void show(Integer projectId, int fromVersion, final SemClassInfo selectedClass) {
        List<Integer> semClassIds = new ArrayList<Integer>();
        if (selectedClass != null) {
            semClassIds.add(selectedClass.getId());
        }

        termInjector.getTermDataProvider().getStructTermChanges(projectId, fromVersion, semClassIds, new AsyncCallback<StructTermChangesImpl>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Could not retrieve changes!");
            }

            @Override
            public void onSuccess(StructTermChangesImpl result) {
                if (result.getChanges().isEmpty()) {
                    Window.alert("No changes to be displayed!");
                } else {

                    final DialogBox dialogBox = createDialogBox(uterChanges(result));
                    if (selectedClass == null) {
                        dialogBox.setText("Global changes from v" + result.getFromVersion() + " to v" + result.getToVersion());
                    } else {
                        dialogBox.setText("Changes for '" + selectedClass.getCanonicLabel() + "'  (v" + result.getFromVersion() + " to v" + result.getToVersion() + ")");
                    }
                    dialogBox.center();
                    dialogBox.show();
                }
            }
        });

    }

    private static DialogBox createDialogBox(SafeHtml content) {

        final DialogBox dialogBox = new DialogBox();

        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setSpacing(4);
        dialogBox.setWidget(dialogContents);

        HTML details = new HTML(content);
        details.addStyleName(StructTermResources.INSTANCE.css().ScrollableContent());
        dialogContents.add(details);
        dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_LEFT);

        Button closeButton = new Button(
                "Close", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });
        dialogContents.add(closeButton);
        dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);

        dialogBox.setGlassEnabled(false);
        dialogBox.setAnimationEnabled(true);
        dialogBox.setModal(false);
        return dialogBox;
    }
}
