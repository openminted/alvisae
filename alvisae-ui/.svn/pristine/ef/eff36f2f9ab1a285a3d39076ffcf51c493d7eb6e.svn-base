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
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import java.util.HashMap;

/**
 * An event occurring after any change of the "focused" Annotation (within the working Document).
 * @author fpapazian
 */
public class AnnotationFocusChangedEvent extends GwtEvent<AnnotationFocusChangedEventHandler> {

    public static final Type<AnnotationFocusChangedEventHandler> TYPE = new Type<AnnotationFocusChangedEventHandler>();
    private static HashMap<AnnotationFocusChangedEventHandler, HandlerRegistration> handlers = new HashMap<AnnotationFocusChangedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, AnnotationFocusChangedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(AnnotationFocusChangedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(AnnotationFocusChangedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }

    private final AnnotatedTextHandler document;
    private final Annotation annotation;

    public AnnotationFocusChangedEvent(AnnotatedTextHandler document, Annotation annotation) {
        this.document = document;
        this.annotation = annotation;
    }


    @Override
    public Type<AnnotationFocusChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnnotationFocusChangedEventHandler handler) {
        handler.onAnnotationFocusChanged(this);
    }

    public AnnotatedTextHandler getAnnotatedTextHandler() {
        return document;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

}
