/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import fr.inra.mig_bibliome.alvisae.client.Events.Selection.AnnotationSelections;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import java.util.HashMap;

/**
 * An event occurring after any change of the Annotation selection (within the working Document).
 * @author fpapazian
 */
public class GenericAnnotationSelectionChangedEvent extends GwtEvent<AnnotationSelectionChangedEventHandler> {

    public static Type<AnnotationSelectionChangedEventHandler> TYPE = new Type<AnnotationSelectionChangedEventHandler>();
    private static HashMap<AnnotationSelectionChangedEventHandler, HandlerRegistration> handlers = new HashMap<AnnotationSelectionChangedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, AnnotationSelectionChangedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(AnnotationSelectionChangedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(AnnotationSelectionChangedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }

    private final AnnotatedTextHandler document;
    private final AnnotationSelections annotationSelection;

    public GenericAnnotationSelectionChangedEvent(AnnotatedTextHandler document, AnnotationSelections selectedAnnotations) {
        this.document = document;
        this.annotationSelection = new AnnotationSelections(selectedAnnotations);
    }


    @Override
    public Type<AnnotationSelectionChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnnotationSelectionChangedEventHandler handler) {
        handler.onAnnotationSelectionChanged(this);
    }

    public AnnotatedTextHandler getAnnotatedTextHandler() {
        return document;
    }

    public AnnotationSelections getAnnotationSelection() {
        return annotationSelection;
    }

    public Annotation getMainSelectedAnnotation() {
        return getAnnotationSelection() != null ? getAnnotationSelection().getMainSelectedAnnotation() : null;
    }

}
