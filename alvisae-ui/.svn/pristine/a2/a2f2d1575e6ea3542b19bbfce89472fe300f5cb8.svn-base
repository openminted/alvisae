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
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import java.util.HashMap;

/**
 * An event occurring when a Document becomes the current working document in the UI.
 * @author fpapazian
 */
public class WorkingDocumentChangedEvent extends GwtEvent<WorkingDocumentChangedEventHandler> {

    public static enum ChangeType {
	Loaded,
	Unloaded,
	AdditionalAnnotationSetLoaded,
	;
}
    public static final Type<WorkingDocumentChangedEventHandler> TYPE = new Type<WorkingDocumentChangedEventHandler>();
    private static HashMap<WorkingDocumentChangedEventHandler, HandlerRegistration> handlers = new HashMap<WorkingDocumentChangedEventHandler, HandlerRegistration>();

    public static HandlerRegistration register(EventBus eventBus, WorkingDocumentChangedEventHandler handler) {
        HandlerRegistration reg = eventBus.addHandler(TYPE, handler);
        handlers.put(handler, reg);
        return reg;
    }

    public static void unregister(WorkingDocumentChangedEventHandler handler) {
        HandlerRegistration reg = handlers.get(handler);
        if (reg != null) {
            reg.removeHandler();
            handlers.remove(handler);
        }
    }

    public static void unregisterAll(WorkingDocumentChangedEventHandler handler) {
        for (HandlerRegistration reg : handlers.values()) {
            handlers.remove(handler);
        }
        handlers.clear();
    }
    private final AnnotatedTextHandler workingDocument;
    private final DocumentView docView;
    private final ChangeType change;


    public WorkingDocumentChangedEvent(AnnotatedTextHandler workingDocument, DocumentView docView, ChangeType change) {
        this.workingDocument = workingDocument;
        this.docView = docView;
        this.change = change;
    }

    @Override
    public Type<WorkingDocumentChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(WorkingDocumentChangedEventHandler handler) {
        handler.onWorkingDocumentChanged(this);
    }

    /**
     *
     * @return the current working document (null if there is no document open)
     */
    public AnnotatedTextHandler getWorkingDocument() {
        return workingDocument;
    }

    public ChangeType getChangeType() {
        return change;
    }

    /**
     * 
     * @return the DocumentView display the working document
     */
    public DocumentView getDocView() {
        return docView;
    }


}
