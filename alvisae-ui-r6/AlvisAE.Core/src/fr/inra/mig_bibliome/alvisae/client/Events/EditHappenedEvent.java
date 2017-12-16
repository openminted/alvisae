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
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationEdit;
import java.util.HashMap;

/**
 * An event indicating that an Edit operation has occurred.
 * @author fpapazian
 */
public abstract class EditHappenedEvent extends GwtEvent<EditHappenedEventHandler> {

    public static Type<EditHappenedEventHandler> TYPE = new Type<EditHappenedEventHandler>();
    private static HashMap<EditHappenedEventHandler, HandlerRegistration> handlers = new HashMap<EditHappenedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, EditHappenedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(EditHappenedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(EditHappenedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }
    private final AnnotationEdit annotationEdit;

    public EditHappenedEvent(AnnotationEdit annotationEdit) {
        this.annotationEdit = annotationEdit;
    }

    @Override
    public Type<EditHappenedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditHappenedEventHandler handler) {
        handler.onEditHappened(this);
    }

    /**
     *
     * @return the actual Edit which triggered this event
     */
    public AnnotationEdit getEdit() {
        return annotationEdit;
    }

}
