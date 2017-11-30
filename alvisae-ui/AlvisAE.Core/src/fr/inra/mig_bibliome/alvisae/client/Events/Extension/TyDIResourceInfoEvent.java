/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2013.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events.Extension;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDIResourceRef;
import java.util.HashMap;

/**
 * Event fired when some resource info are broadcasted to inform other components
 * @author fpapazian
 */
public class TyDIResourceInfoEvent extends GwtEvent<TyDIResourceInfoEventHandler> {
    
    public static GwtEvent.Type<TyDIResourceInfoEventHandler> TYPE = new GwtEvent.Type<TyDIResourceInfoEventHandler>();
 
    private static HashMap<TyDIResourceInfoEventHandler, HandlerRegistration> handlers = new HashMap<TyDIResourceInfoEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, TyDIResourceInfoEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(TyDIResourceInfoEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }    
    
    @Override
    public GwtEvent.Type<TyDIResourceInfoEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TyDIResourceInfoEventHandler handler) {
        handler.onTyDIResourceInfoEvent(this);
    }

    private final TyDIResourceRef resRef;

    public TyDIResourceInfoEvent(TyDIResourceRef resRef) {
        this.resRef = resRef;
    }

    public TyDIResourceRef getTyDIResourceRef() {
        return resRef;
    }
    
}
