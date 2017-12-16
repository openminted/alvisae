/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

/**
 * Implemented by UndoableEdit that may be prevented to execute (but could sometimes be forced to be executed)
 * @author fpapazian
 */
public interface PreventableEdit {

    /**
     *
     * @return true is there is a cause preventing this Edit to be performed
     */
    public boolean isPrevented();

    /**
     *
     * @return a message to the user explaining the cause preventing the execution of this Edit,
     *<br/>null if there is no preventing cause
     */
    public String getPreventingCause();

    /**
     * 
     * @return true if this Edit could be forced to execute despite the possible preventing cause
     */
    public boolean isForcible();

    /**
     * Create an new Edit which execution may allow the execution of the Edit, by performing any other Edit necessary to remove the preventing cause
     * @throws UnsupportedOperationException if it is not possible to create a compound Edit that can allow the executing of the edit
     */
    public AnnotationCompoundEdit getForcingEdit();
}
