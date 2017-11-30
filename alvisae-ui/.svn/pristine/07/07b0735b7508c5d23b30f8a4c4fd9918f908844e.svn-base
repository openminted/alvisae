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
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.AuthenticationQueries;
import java.util.HashMap;

/**
 * An event occurring whenever the Application status changes.
 * The status indicates if the application is :
 * Idle, waiting for next user action,
 * Processing some potentially long calculation,
 * Waiting when engaged in a networked dialog,
 * @author fpapazian
 */
public class ApplicationStatusChangedEvent extends GwtEvent<ApplicationStatusChangedEventHandler> {

    public static enum ApplicationStatusSwitching {

        Idle,
        Processing,
        Waiting,
        SignedIn,
        SignedOut,
        AsynchRequestSent,
        AsynchResponseArrived,
        ;
    }
    public static Type<ApplicationStatusChangedEventHandler> TYPE = new Type<ApplicationStatusChangedEventHandler>();
    private static HashMap<ApplicationStatusChangedEventHandler, HandlerRegistration> handlers = new HashMap<ApplicationStatusChangedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, ApplicationStatusChangedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(ApplicationStatusChangedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(ApplicationStatusChangedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }
    private final ApplicationStatusSwitching status;
    private final AuthenticationQueries requestManager;

    public ApplicationStatusChangedEvent(ApplicationStatusSwitching status, AuthenticationQueries requestManager) {
        this.status = status;
        this.requestManager = requestManager;
    }

    @Override
    public Type<ApplicationStatusChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ApplicationStatusChangedEventHandler handler) {
        handler.onApplicationStatusChanged(this);
    }

    public ApplicationStatusSwitching getStatus() {
        return status;
    }

    public AuthenticationQueries getRequestManager() {
        return requestManager;
    }

 }
