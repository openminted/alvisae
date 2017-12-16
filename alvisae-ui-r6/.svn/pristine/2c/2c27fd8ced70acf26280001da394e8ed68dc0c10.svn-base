/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import fr.inra.mig_bibliome.alvisae.client.Annotation.Metadata.AnnotationPropertySheet;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationChangeTypeEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationModificationEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationRelationEditionEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationSelectionChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.GenericAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.TextAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.RelationBase;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class AnnotationDetailsUi extends Composite implements AnnotationSelectionChangedEventHandler, EditHappenedEventHandler {

    interface AnnotationDetailsUiBinder extends UiBinder<Widget, AnnotationDetailsUi> {
    }
    private static AnnotationDetailsUiBinder uiBinder = GWT.create(AnnotationDetailsUiBinder.class);
    //
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    interface Styles extends CssResource {
    }
    //
    @UiField
    AnnotationPropertySheet annotPropSheet;
    private AnnotatedTextHandler registeredAnnotatedText = null;
    private List<AnnotatedTextHandler> sourceAnnotatedTexts = new ArrayList<AnnotatedTextHandler>();
    private AnnotatedTextHandler listenedAnnotatedText = null;
    private CombinedAnnotationCreationHelper helper;
    private Annotation annotation = null;
    private String mainSelectedMark;
    private boolean readonly = false;
    private LinkedList<GenericAnnotationSelectionChangedEvent> pending = new LinkedList<GenericAnnotationSelectionChangedEvent>();

    public AnnotationDetailsUi() {
        initWidget(uiBinder.createAndBindUi(this));

        //if annotation type is changed via PropertySheet, then perform change immediately
        final ValueUpdater<String> annotationTypeChangeHandler = new ValueUpdater<String>() {
            @Override
            public void update(String newAnnotationType) {
                Annotation ann = annotation;
                if (listenedAnnotatedText != null && ann != null && !ann.getAnnotationType().equals(newAnnotationType)) {
                    AnnotationModificationEdit typeChangeEdit;
                    if (ann.getAnnotationKind() == AnnotationKind.RELATION) {
                        List<String> annotationIds = new ArrayList<String>();
                        for (AnnotationReference argRef : ann.getRelation().getRolesArguments().values()) {
                            annotationIds.add(argRef.getAnnotationId());
                        }
                        RelationBase relBase = helper.computeRelevantRelation(newAnnotationType, annotationIds);
                        typeChangeEdit = new AnnotationRelationEditionEdit(listenedAnnotatedText, ann, relBase.getType(), relBase.getArgumentRoleMap());
                    } else {
                        typeChangeEdit = new AnnotationChangeTypeEdit(listenedAnnotatedText, ann, newAnnotationType);
                    }
                    typeChangeEdit.redo();
                }
            }
        };
        annotPropSheet.setAnnotationTypeUpdater(annotationTypeChangeHandler);
    }

    public void clearDisplay() {
        annotPropSheet.display(null, null, false, true, null);
    }
    
    private void displayAnnotationList(final AnnotatedTextHandler annotatedText, final Annotation annotation, String mainSelectedMark) {

        //display only annotation from the registered AnnotatedTextHandler if any
        if (registeredAnnotatedText == null || this.registeredAnnotatedText != annotatedText) {
            return;
        }

        this.listenedAnnotatedText = annotatedText;
        this.annotation = annotation;
        this.mainSelectedMark = mainSelectedMark;
        if (listenedAnnotatedText == null || annotation == null || !annotation.getAnnotationKind().equals(AnnotationKind.RELATION)) {
            this.helper = null;
        } else {
            this.helper = new CombinedAnnotationCreationHelper(listenedAnnotatedText, AnnotationKind.RELATION);
        }

        if (annotatedText != null && annotation != null) {
            annotPropSheet.display(annotatedText, annotation, true, isReadOnly(), sourceAnnotatedTexts);
        } else {
            clearDisplay();
        }
    }

    public void setReadOnly(boolean readonly) {
        this.readonly = readonly;
    }

    public boolean isReadOnly() {
        return readonly;
    }

    public void setRegisteredAnnotatedText(AnnotatedTextHandler annotatedText) {
        this.registeredAnnotatedText = annotatedText;
    }

    public void addSourceAnnotatedText(AnnotatedTextHandler annotatedText) {
        this.sourceAnnotatedTexts.add(annotatedText);
    }
    
    final RepeatingCommand processLastEvent = new Scheduler.RepeatingCommand() {
        @Override
        public boolean execute() {
            GenericAnnotationSelectionChangedEvent processedEvent = pending.poll();
            if (processedEvent != null) {
                pending.clear();
                final String mainMarker;
                if (processedEvent instanceof TextAnnotationSelectionChangedEvent) {
                    mainMarker = ((TextAnnotationSelectionChangedEvent) processedEvent).getMainSelectedMarker();
                } else {
                    mainMarker = null;
                }

                displayAnnotationList(processedEvent.getAnnotatedTextHandler(), processedEvent.getMainSelectedAnnotation(), mainMarker);
            }
            return false;
        }
    };

    @Override
    public void onAnnotationSelectionChanged(GenericAnnotationSelectionChangedEvent event) {
        pending.addFirst(event);
        //actually start processing after a little delay to let other events to gather...
        Scheduler.get().scheduleFixedPeriod(processLastEvent, 150);
    }

    @Override
    public void onEditHappened(EditHappenedEvent event) {
        if (event.getEdit() instanceof AnnotationModificationEdit) {
            AnnotationModificationEdit edit = (AnnotationModificationEdit) event.getEdit();
            final Annotation editedAnnotation = edit.getAnnotation();
            if ((listenedAnnotatedText != null && editedAnnotation.getAnnotatedText().equals(listenedAnnotatedText.getAnnotatedText())) && (annotation != null && editedAnnotation.getId().equals(annotation.getId()))) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        displayAnnotationList(listenedAnnotatedText, editedAnnotation, mainSelectedMark);
                    }
                });
            }
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        TextAnnotationSelectionChangedEvent.register(injector.getMainEventBus(), this);
        EditHappenedEvent.register(injector.getMainEventBus(), this);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        TextAnnotationSelectionChangedEvent.unregister(this);
        EditHappenedEvent.unregister(this);
        listenedAnnotatedText = null;
    }
}
