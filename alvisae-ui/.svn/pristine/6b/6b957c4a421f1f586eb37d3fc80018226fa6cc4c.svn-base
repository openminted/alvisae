/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.*;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Document.Blinker;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextProcessor;
import fr.inra.mig_bibliome.alvisae.client.data3.validation.ClientFaultMessages;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicAnnotationSchemaValidator;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicFaultListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * base class of the dialog box used to create/edit Relation and Group
 *
 * @author fpapazian
 */
public abstract class GenericEditDialog extends PopupPanel {

    protected static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    protected static final ClientFaultMessages faultMessages = GWT.create(ClientFaultMessages.class);

    interface Binder extends UiBinder<Widget, GenericEditDialog> {
    }
    private static final Binder binder = GWT.create(Binder.class);
    private static BasicAnnotationSchemaValidator validator = new BasicAnnotationSchemaValidator();

    interface Styles extends CssResource {

        String Width100pct();

        String Hidden();

        String PopupMaxWidth();
    }
    private boolean applied;
    private Command applyCommand = null;
    private final List<AnnotationTypeDefinition> annTypeDefs;
    private final List<AnnotationTypeDefinition> editableAnnTypeDefs = new ArrayList<AnnotationTypeDefinition>();
    private List<String> annotationIds;
    private final AnnotatedTextHandler annotatedText;
    private Annotation mainAnnotation = null;
    private final Blinker warnBlinker;
    private final Blinker errorBlinker;
    @UiField
    Label title;
    @UiField
    TableSectionElement idPanel;
    @UiField
    HTML annotationId;
    @UiField
    HTML annotationType;
    @UiField
    HTML annotationDetail;
    @UiField
    ListBox annotationTypes;
    @UiField
    Grid argumentsGrid;
    @UiField
    PushButton cancelModifBtn;
    @UiField
    PushButton applyModifBtn;
    @UiField
    Image warnImage;
    @UiField
    Image errorImage;
    @UiField
    Styles style;

    public GenericEditDialog(AnnotatedTextHandler annotatedText, AnnotationKind kind) {
        super(false, true);
        setWidget(binder.createAndBindUi(this));
        this.addStyleName(style.PopupMaxWidth());
        this.annotatedText = annotatedText;

        //if the edition is associated to a TaskDefinition, then the available Annotation types must be limited to the one of the Task
        TaskDefinition taskDef = annotatedText.getAnnotatedText().getEditedTask();
        Set<String> editedTypes = null;
        if (taskDef != null) {
            editedTypes = new HashSet<String>(taskDef.getEditedAnnotationTypes());
        }

        int itemIndex = 0;
        annTypeDefs = annotatedText.getAnnotatedText().getAnnotationSchema().getAnnotationTypeDefinition(kind);
        for (AnnotationTypeDefinition type : annTypeDefs) {
            if ((editedTypes == null) || editedTypes.contains(type.getType())) {
                editableAnnTypeDefs.add(type);
                annotationTypes.addItem(type.getType());
                String optionStyle = "background-color:" + type.getColor() + ";";
                annotationTypes.getElement().getElementsByTagName("option").getItem(itemIndex).setAttribute("style", optionStyle);
                itemIndex++;
            }
        }
        setAnimationEnabled(true);
        setGlassEnabled(true);

        warnBlinker = new Blinker(warnImage);
        errorBlinker = new Blinker(errorImage);
    }

    public void setText(String title) {
        this.title.setText(title);
    }

    public void setApplyCommand(Command applyCommand) {
        this.applyCommand = applyCommand;
    }

    public void setCreating(List<String> annotationIds) {
        this.annotationIds = annotationIds;
        this.mainAnnotation = null;
        idPanel.addClassName(style.Hidden());
        prepareArgumentGrid(annotationIds);
        applyModifBtn.setFocus(true);
    }

    public void setEditing(Annotation mainAnnotation) {
        List<String> annIds = new ArrayList<String>();
        for (Annotation a : getAnnotatedTextHandler().getReferenceableAnnotations()) {
            String otherAnnotationId = a.getId();
            //filter out Annotation referencing the main annotation being edited
            if (!getAnnotatedTextHandler().isEqualToOrReferencedBy(mainAnnotation.getId(), otherAnnotationId)) {
                annIds.add(otherAnnotationId);
            }
        }

        //display the type of the edited annotation
        for (int i = 0; i < annotationTypes.getItemCount(); i++) {
            if (mainAnnotation.getAnnotationType().equals(annotationTypes.getItemText(i))) {
                annotationTypes.setSelectedIndex(i);
                break;
            }
        }

        this.annotationIds = annIds;
        this.mainAnnotation = mainAnnotation;
        idPanel.removeClassName(style.Hidden());
        SafeHtmlBuilder sbId = new SafeHtmlBuilder();
        CombinedAnnotationCell.renderKind(mainAnnotation.getAnnotationKind(), sbId);
        sbId.appendHtmlConstant("&nbsp;");
        sbId.appendEscaped(AnnotatedTextProcessor.getBriefId(mainAnnotation.getId()));
        this.annotationId.setHTML(sbId.toSafeHtml());
        SafeHtmlBuilder sbDetail = new SafeHtmlBuilder();
        CombinedAnnotationCell.renderDetail(getAnnotatedTextHandler(), mainAnnotation, sbDetail);
        this.annotationDetail.setHTML(sbDetail.toSafeHtml());
        SafeHtmlBuilder sbKind = new SafeHtmlBuilder();
        AnnotationSchemaCell.renderType(mainAnnotation.getAnnotationType(), sbKind);
        this.annotationType.setHTML(sbKind.toSafeHtml());
        prepareArgumentGrid(annotationIds);
        applyModifBtn.setFocus(true);
    }

    protected void setWarning(String message) {
        if (message != null) {
            warnBlinker.start();
            warnImage.setTitle(message);
        } else {
            warnBlinker.cancel();
            warnImage.setVisible(false);
            warnImage.setTitle("");
        }
    }

    protected void setError(String message) {
        if (message != null) {
            errorBlinker.start();
            errorImage.setTitle(message);
        } else {
            errorBlinker.cancel();
            errorImage.setVisible(false);
            errorImage.setTitle("");
        }
    }

    protected abstract void prepareArgumentGrid(List<String> annotationIds);

    protected abstract void validateAnnotation();

    public AnnotatedTextHandler getAnnotatedTextHandler() {
        return annotatedText;
    }

    public static BasicAnnotationSchemaValidator getAnnotationSchemaValidator() {
        return validator;
    }

    public Annotation getMainAnnotation() {
        return mainAnnotation;
    }

    protected List<AnnotationTypeDefinition> getAllAnnotationTypeDefs() {
        return annTypeDefs;
    }

    protected String getSelectedAnnotationType() {
        return getSelectedAnnotationTypeDef() != null ? getSelectedAnnotationTypeDef().getType() : null;
    }

    protected AnnotationTypeDefinition getSelectedAnnotationTypeDef() {
        int index = annotationTypes.getSelectedIndex();
        if (index == -1) {
            return null;
        } else {
            return editableAnnTypeDefs.get(index);
        }
    }

    protected String getShortDesc(String annotationId) {
        Annotation annotation = getAnnotatedTextHandler().getAnnotation(annotationId);
        StringBuilder result = new StringBuilder();
        result.append("/")
                .append(annotation.getAnnotationType())
                .append("/ ")
                .append(AnnotatedTextProcessor.getBriefId(annotationId))
                .append(" / ");

        switch (annotation.getAnnotationKind()) {
            case TEXT:
                result.append(" ").append(annotation.getAnnotationText(""));
                break;
            case GROUP:
                result.append("GRP");
                break;
            case RELATION:
                result.append("REL");
                break;
        }
        return result.toString();
    }

    private void cancel() {
        applied = false;
        hide();
    }

    private void apply() {
        if (!applied) {
            applied = true;
            if (applyCommand != null) {
                applyCommand.execute();
            }
            hide();
        }
    }

    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent preview) {
        super.onPreviewNativeEvent(preview);
        NativeEvent evt = preview.getNativeEvent();
        if (evt.getType().equals("keydown")) {
            switch (evt.getKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    apply();
                    break;
                case KeyCodes.KEY_ESCAPE:
                    cancel();
                    break;
            }
        }
    }

    @UiHandler("cancelModifBtn")
    void onCancelClicked(ClickEvent event) {
        cancel();
    }

    @UiHandler("applyModifBtn")
    void onApplyClicked(ClickEvent event) {
        //Beware : it is called when pressing Enter under WebKit (?!) (see: https://migale.jouy.inra.fr/redmine/issues/1272 )
        apply();
    }

    @UiHandler("annotationTypes")
    void onAnnotationTypeChanged(ChangeEvent event) {
        prepareArgumentGrid(annotationIds);
    }
}
