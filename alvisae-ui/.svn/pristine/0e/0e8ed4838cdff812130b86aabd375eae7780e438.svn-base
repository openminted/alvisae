/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events.Extension;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDIResourceRef;
import java.util.HashMap;

/**
 * Event fired when a terminology resource is selected
 * @author fpapazian
 */
public abstract class TyDIResourceSelectionChangedEvent extends GwtEvent<TyDIResourceSelectionChangedEventHandler> {
    
    public static GwtEvent.Type<TyDIResourceSelectionChangedEventHandler> TYPE = new GwtEvent.Type<TyDIResourceSelectionChangedEventHandler>();
 
    private static HashMap<TyDIResourceSelectionChangedEventHandler, HandlerRegistration> handlers = new HashMap<TyDIResourceSelectionChangedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, TyDIResourceSelectionChangedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(TyDIResourceSelectionChangedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }    
    
    @Override
    public GwtEvent.Type<TyDIResourceSelectionChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TyDIResourceSelectionChangedEventHandler handler) {
        handler.onTyDIResourceSelectionChanged(this);
    }

    private final TyDIResourceRef resRef;

    public TyDIResourceSelectionChangedEvent(TyDIResourceRef resRef) {
        this.resRef = resRef;
    }

    public TyDIResourceRef getTyDIResourceRef() {
        return resRef;
    }
    
}
