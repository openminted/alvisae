/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import java.util.HashMap;

/**
 * @author fpapazian
 */
public class ApplicationOptionChangedEvent extends GwtEvent<ApplicationOptionChangedEventHandler> {

    public static Type<ApplicationOptionChangedEventHandler> TYPE = new Type<ApplicationOptionChangedEventHandler>();
    private static HashMap<ApplicationOptionChangedEventHandler, HandlerRegistration> handlers = new HashMap<ApplicationOptionChangedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, ApplicationOptionChangedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(ApplicationOptionChangedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(ApplicationOptionChangedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }
    private final String optionName;
    private final Integer oldValue;
    private final Integer newValue;

    public ApplicationOptionChangedEvent(String optionName, Integer oldValue, Integer newValue) {
        this.optionName = optionName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public Type<ApplicationOptionChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ApplicationOptionChangedEventHandler handler) {
        handler.onApplicationOptionChanged(this);
    }

    public String getOptionName() {
        return optionName;
    }

    public Integer getOldValue() {
        return oldValue;
    }

    public Integer getNewValue() {
        return newValue;
    }
}
