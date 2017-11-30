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
import java.util.HashMap;

/**
 * An event indicating that some information should be delivered to the User.
 * @author fpapazian
 */
public class InformationReleasedEvent extends GwtEvent<InformationReleasedEventHandler> {

    public static Type<InformationReleasedEventHandler> TYPE = new Type<InformationReleasedEventHandler>();
    private static HashMap<InformationReleasedEventHandler, HandlerRegistration> handlers = new HashMap<InformationReleasedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, InformationReleasedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(InformationReleasedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(InformationReleasedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }
    
    private final String htmlmessage;

    public InformationReleasedEvent(String htmlmessage) {
        this.htmlmessage = htmlmessage;
    }

    @Override
    public Type<InformationReleasedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(InformationReleasedEventHandler handler) {
        handler.onInformationReleased(this);
    }

    /**
     *
     * @return an HTML fragment containing the text of the message
     */
    public String getMessage() {
        return htmlmessage;
    }
}
