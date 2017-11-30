/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events;

import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;

/**
 * An event occurring when a widget wants to expand to the maximum possible space in the screen or to return to it previous size.
 * @author fpapazian
 */
public class MaximizingWidgetEvent extends GwtEvent<MaximizingWidgetEventHandler> {

    public static Type<MaximizingWidgetEventHandler> TYPE = new Type<MaximizingWidgetEventHandler>();
    private static HashMap<MaximizingWidgetEventHandler, HandlerRegistration> handlers = new HashMap<MaximizingWidgetEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, MaximizingWidgetEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(MaximizingWidgetEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }
    
    //
    private final Widget widget;
    private final boolean maximizing;

    public MaximizingWidgetEvent(Widget widget, boolean maximizing) {
        this.widget = widget;
        this.maximizing = maximizing;
    }

    @Override
    public Type<MaximizingWidgetEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MaximizingWidgetEventHandler handler) {
        handler.onMaximizingWidget(this);
    }

    public Widget getWidget() {
        return widget;
    }

    public boolean isMaximizing() {
        return maximizing;
    }
    
    
}
