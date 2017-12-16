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
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import java.util.HashMap;
import java.util.List;

/**
 * An event occurring whenever the text selection has changed within the working document
 * @author fpapazian
 */
public class TargetSelectionChangedEvent extends GwtEvent<TargetSelectionChangedEventHandler> {

    public static final Type<TargetSelectionChangedEventHandler> TYPE = new Type<TargetSelectionChangedEventHandler>();
    private static HashMap<TargetSelectionChangedEventHandler, HandlerRegistration> handlers = new HashMap<TargetSelectionChangedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, TargetSelectionChangedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(TargetSelectionChangedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(TargetSelectionChangedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }
    
    private final List<Fragment> selectedTargets;

    public TargetSelectionChangedEvent(List<Fragment> selectedTargets) {
        this.selectedTargets = selectedTargets;
    }

    @Override
    public Type<TargetSelectionChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TargetSelectionChangedEventHandler handler) {
        handler.onTargetSelectionChanged(this);
    }

    public List<Fragment> getTargetSelection() {
        return selectedTargets;
    }
}
