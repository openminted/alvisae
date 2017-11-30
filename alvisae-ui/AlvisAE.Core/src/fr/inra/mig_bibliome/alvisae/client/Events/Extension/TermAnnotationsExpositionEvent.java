/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events.Extension;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.ResourceLocator;
import java.util.HashMap;

/**
 * Event fired when a document (possibly) containing some terminology resource is displayed
 * @author fpapazian
 */
public class TermAnnotationsExpositionEvent extends GwtEvent<TermAnnotationsExpositionEventHandler> {

    public static enum ChangeType {

        Available,
        Unavailable,;
    }
    public static Type<TermAnnotationsExpositionEventHandler> TYPE = new Type<TermAnnotationsExpositionEventHandler>();
    private static HashMap<TermAnnotationsExpositionEventHandler, HandlerRegistration> handlers = new HashMap<TermAnnotationsExpositionEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, TermAnnotationsExpositionEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(TermAnnotationsExpositionEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(TermAnnotationsExpositionEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }
    private final ChangeType change;
    private final ResourceLocator locator;

    public TermAnnotationsExpositionEvent(ChangeType change, ResourceLocator locator) {
        this.change = change;
        this.locator = locator;
    }

    @Override
    public Type<TermAnnotationsExpositionEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TermAnnotationsExpositionEventHandler handler) {
        handler.onTermAnnotationsExpositionChanged(this);
    }

    public ChangeType getChangeType() {
        return change;
    }

    public ResourceLocator getLocator() {
        return locator;
    }
    
}
