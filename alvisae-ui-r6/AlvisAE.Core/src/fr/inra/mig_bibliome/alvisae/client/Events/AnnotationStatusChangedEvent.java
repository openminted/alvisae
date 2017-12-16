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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An event indicating that some information should be delivered to the User.
 * @author fpapazian
 */
public class AnnotationStatusChangedEvent extends GwtEvent<AnnotationStatusChangedEventHandler> {

    public static Type<AnnotationStatusChangedEventHandler> TYPE = new Type<AnnotationStatusChangedEventHandler>();
    private static HashMap<AnnotationStatusChangedEventHandler, HandlerRegistration> handlers = new HashMap<AnnotationStatusChangedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, AnnotationStatusChangedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(AnnotationStatusChangedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(AnnotationStatusChangedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }
    private final List<String> annotationIds = new ArrayList<String>();
    
    public AnnotationStatusChangedEvent(String annotationId) {
        this.annotationIds.add(annotationId);
    }

    @Override
    public Type<AnnotationStatusChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnnotationStatusChangedEventHandler handler) {
        handler.onAnnotationStatusChanged(this);
    }

    public List<String> getAnnotationIds() {
        return annotationIds;
    }
}
