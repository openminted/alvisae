/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events;

import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.DOMRange;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An event occurring whenever the text selection has changed within the working document
 * @author fpapazian
 */
public class RangeSelectionChangedEvent extends GwtEvent<RangeSelectionChangedEventHandler> {

    public static Type<RangeSelectionChangedEventHandler> TYPE = new Type<RangeSelectionChangedEventHandler>();
    private static HashMap<RangeSelectionChangedEventHandler, HandlerRegistration> handlers = new HashMap<RangeSelectionChangedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, RangeSelectionChangedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(RangeSelectionChangedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(RangeSelectionChangedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }
    
    private final AnnotatedTextHandler document;
    private final ArrayList<DOMRange> selectedRanges;

    public RangeSelectionChangedEvent(AnnotatedTextHandler document, ArrayList<DOMRange> selectedRanges) {
        this.document = document;
        this.selectedRanges = selectedRanges;
    }

    @Override
    public Type<RangeSelectionChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RangeSelectionChangedEventHandler handler) {
        handler.onRangeSelectionChanged(this);
    }

    public AnnotatedTextHandler getAnnotatedTextHandler() {
        return document;
    }
    
    public ArrayList<DOMRange> getRangeSelection() {
        return selectedRanges;
    }
}
