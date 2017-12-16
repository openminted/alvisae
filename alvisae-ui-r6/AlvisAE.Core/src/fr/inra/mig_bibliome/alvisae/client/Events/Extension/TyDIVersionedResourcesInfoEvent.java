/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events.Extension;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.CheckedSemClassImpl;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author fpapazian
 */
public class TyDIVersionedResourcesInfoEvent extends GwtEvent<TyDIVersionedResourcesInfoEventHandler> {

    public static GwtEvent.Type<TyDIVersionedResourcesInfoEventHandler> TYPE = new GwtEvent.Type<TyDIVersionedResourcesInfoEventHandler>();
    private static HashMap<TyDIVersionedResourcesInfoEventHandler, HandlerRegistration> handlers = new HashMap<TyDIVersionedResourcesInfoEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, TyDIVersionedResourcesInfoEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(TyDIVersionedResourcesInfoEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    @Override
    public GwtEvent.Type<TyDIVersionedResourcesInfoEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TyDIVersionedResourcesInfoEventHandler handler) {
        handler.onTyDIVersionedResourceInfoEvent(this);
    }
    private final List<CheckedSemClassImpl> resRef;

    public TyDIVersionedResourcesInfoEvent(List<CheckedSemClassImpl> resRef) {
        this.resRef = resRef;
    }

    public  List<CheckedSemClassImpl> getTyDIResourcesRef() {
        return resRef;
    }
}
