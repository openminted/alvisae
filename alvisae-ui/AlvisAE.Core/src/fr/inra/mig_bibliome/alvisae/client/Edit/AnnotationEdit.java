/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDocumentViewMapper;
import fr.inra.mig_bibliome.alvisae.client.Edit.undo.CannotRedoException;
import fr.inra.mig_bibliome.alvisae.client.Edit.undo.CannotUndoException;
import fr.inra.mig_bibliome.alvisae.client.Edit.undo.UndoableEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.EditDoneEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.EditUndoneEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.UndoableEditHappenedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;

/**
 * Base class for any Annotation edit.
 * <p/>
 * an {@link UndoableEditHappenedEvent} will be published when this Edit is initially done.
 * <p/>
 * Then, {@link EditHappenedEvent}s will be triggered each time this Edit is done (@link EditDoneEvent) or undone (@link EditUndoneEvent),
 * @author fpapazian
 */
public abstract class AnnotationEdit implements UndoableEdit {

    //
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    //
    private final AnnotatedTextHandler document;
    private boolean initial = true;
    private boolean died = false;
    private boolean undone = false;

    public AnnotationEdit(AnnotatedTextHandler document) {
        if (document == null) {
            throw new IllegalArgumentException("AnnotatedDocument shall not be null");
        }
        this.document = document;
    }

    public AnnotatedTextHandler getAnnotatedTextHandler() {
        return document;
    }

    protected StaneClientBaseGinInjector getInjector() {
        return injector;
    }

    protected AnnotationDocumentViewMapper getMapper() {
        return AnnotationDocumentViewMapper.getMapper(getAnnotatedTextHandler().getAnnotatedText());
    }

    /**
     *  Performs the cancellation of this Edit
     */
    protected abstract void undoIt();

    @Override
    public void undo() throws CannotUndoException {
        if (canUndo()) {
            undoIt();
            //signal that the Edit happened  as a sequel of an undo command
            injector.getMainEventBus().fireEvent(new EditUndoneEvent(this));
            undone = true;
        }
    }

    @Override
    public boolean canUndo() {
        return !died && !undone;
    }

    /**
     *  Perform this actual Edit
     * @return false if the operation can not be performed; then this Edit will not be added to the Undo Manager
     */
    protected abstract boolean doIt();

    @Override
    public void redo() throws CannotRedoException {
        if (canRedo()) {
            if (doIt()) {
                if (initial) {
                    //signal that a primary Undoable Edit happened so the UndoManager can record it
                    injector.getMainEventBus().fireEvent(new UndoableEditHappenedEvent(this));
                } else {
                    //signal that the Edit happened as a sequel of a redo command
                    injector.getMainEventBus().fireEvent(new EditDoneEvent(this));
                }
            }
            initial = false;
            undone = false;
        }
    }

    @Override
    public boolean canRedo() {
        return !died && (initial || undone);
    }

    @Override
    public void die() {
        died = true;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean isSignificant() {
        return true;
    }
}
