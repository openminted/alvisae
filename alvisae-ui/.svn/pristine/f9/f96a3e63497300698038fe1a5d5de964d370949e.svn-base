/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import fr.inra.mig_bibliome.alvisae.client.Edit.undo.CannotRedoException;
import fr.inra.mig_bibliome.alvisae.client.Edit.undo.CannotUndoException;
import fr.inra.mig_bibliome.alvisae.client.Edit.undo.UndoableEdit;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * largely inspired by javax.swing.undo.CompoundEdit
 */
/**
 *
 * @author fpapazian
 */
public class AnnotationCompoundEdit implements UndoableEdit {

    /**
     * True if this edit has never received <code>end</code>.
     */
    boolean inProgress;
    /**
     * The collection of <code>UndoableEdit</code>s
     * undone/redone en masse by this <code>CompoundEdit</code>.
     */
    protected ArrayList<UndoableEdit> edits;

    public AnnotationCompoundEdit() {
        inProgress = true;
        edits = new ArrayList<UndoableEdit>();
    }

    /**
     * Sends <code>undo</code> to all contained
     * <code>UndoableEdits</code> in the reverse of
     * the order in which they were added.
     */
    @Override
    public void undo() throws CannotUndoException {
        int i = edits.size();
        while (i-- > 0) {
            UndoableEdit e = (UndoableEdit) edits.get(i);
            e.undo();
        }
    }

    /**
     * Sends <code>redo</code> to all contained
     * <code>UndoableEdit</code>s in the order in
     * which they were added.
     */
    @Override
    public void redo() throws CannotRedoException {
        Iterator<UndoableEdit> cursor = edits.iterator();
        while (cursor.hasNext()) {
            ((UndoableEdit) cursor.next()).redo();
        }
    }

    /**
     * Returns the last <code>UndoableEdit</code> in
     * <code>edits</code>, or <code>null</code>
     * if <code>edits</code> is empty.
     */
    protected UndoableEdit lastEdit() {
        int count = edits.size();
        if (count > 0) {
            return (UndoableEdit) edits.get(count - 1);
        } else {
            return null;
        }
    }

    /**
     * Sends <code>die</code> to each subedit,
     * in the reverse of the order that they were added.
     */
    @Override
    public void die() {
        int size = edits.size();
        for (int i = size - 1; i >= 0; i--) {
            UndoableEdit e = (UndoableEdit) edits.get(i);
// 	    System.out.println("CompoundEdit(" + i + "): Discarding " +
// 			       e.getUndoPresentationName());
            e.die();
        }
    }

    /**
     * If this edit is <code>inProgress</code>,
     * accepts <code>anEdit</code> and returns true.
     *
     * <p>The last edit added to this <code>CompoundEdit</code>
     * is given a chance to <code>addEdit(anEdit)</code>.
     * If it refuses (returns false), <code>anEdit</code> is
     * given a chance to <code>replaceEdit</code> the last edit.
     * If <code>anEdit</code> returns false here,
     * it is added to <code>edits</code>.
     *
     * @param anEdit the edit to be added
     * @return true if the edit is <code>inProgress</code>;
     *	otherwise returns false
     */
    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        if (!inProgress) {
            return false;
        } else {
            UndoableEdit last = lastEdit();

            // If this is the first subedit received, just add it.
            // Otherwise, give the last one a chance to absorb the new
            // one.  If it won't, give the new one a chance to absorb
            // the last one.

            if (last == null) {
                edits.add(anEdit);
            } else if (!last.addEdit(anEdit)) {
                if (anEdit.replaceEdit(last)) {
                    edits.remove(edits.size() - 1);
                }
                edits.add(anEdit);
            }

            return true;
        }
    }

    /**
     * Sets <code>inProgress</code> to false.
     *
     * @see #canUndo
     * @see #canRedo
     */
    public void end() {
        inProgress = false;
    }

    @Override
    public boolean canUndo() {
        boolean canUndo = true;
        if (!isInProgress()) {
            Iterator<UndoableEdit> cursor = edits.iterator();
            while (cursor.hasNext()) {
                if (!cursor.next().canUndo()) {
                    canUndo = false;
                    break;
                }
            }
        } else {
            canUndo = false;
        }
        return canUndo;
    }

    @Override
    public boolean canRedo() {
        boolean canRedo = true;
        if (!isInProgress()) {
            Iterator<UndoableEdit> cursor = edits.iterator();
            while (cursor.hasNext()) {
                if (!cursor.next().canRedo()) {
                    canRedo = false;
                    break;
                }
            }
        } else {
            canRedo = false;
        }
        return canRedo;
    }

    /**
     * Returns true if this edit is in progress--that is, it has not
     * received end. This generally means that edits are still being
     * added to it.
     *
     * @see	#end
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Returns true if any of the <code>UndoableEdit</code>s
     * in <code>edits</code> do.
     * Returns false if they all return false.
     */
    @Override
    public boolean isSignificant() {
        Iterator<UndoableEdit> cursor = edits.iterator();
        while (cursor.hasNext()) {
            if (((UndoableEdit) cursor.next()).isSignificant()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <code>getPresentationName</code> from the
     * last <code>UndoableEdit</code> added to
     * <code>edits</code>. If <code>edits</code> is empty,
     * calls super.
     */
    @Override
    public String getPresentationName() {
        UndoableEdit last = lastEdit();
        if (last != null) {
            return last.getPresentationName();
        } else {
            return "";
        }
    }

    /**
     * Returns <code>getUndoPresentationName</code>
     * from the last <code>UndoableEdit</code>
     * added to <code>edits</code>.
     * If <code>edits</code> is empty, calls super.
     */
    @Override
    public String getUndoPresentationName() {
        UndoableEdit last = lastEdit();
        if (last != null) {
            return last.getUndoPresentationName();
        } else {
            return "";
        }
    }

    /**
     * Returns <code>getRedoPresentationName</code>
     * from the last <code>UndoableEdit</code>
     * added to <code>edits</code>.
     * If <code>edits</code> is empty, calls super.
     */
    @Override
    public String getRedoPresentationName() {
        UndoableEdit last = lastEdit();
        if (last != null) {
            return last.getRedoPresentationName();
        } else {
            return "";
        }
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }
}
