/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

/**
 * @author fpapazian
 */
public interface TaskPrecedency {

    /**
     *  
     * @return the id of the task preceding (in the workflow) the Task owning this TaskPrecedency
     */
    int getPrecedingTaskId();

    /**
     * 
     * @return true if this TaskPrecedency type is : reviewing(i.e. the preceding Task is reviewed by the current Task)
     */
    boolean isReviewing();
    
    /**
     * 
     * @return true if this TaskPrecedency type is : succeeding (i.e. the preceding Task is succeeded by the current Task)
     */
    boolean isSucceeding();

        /**
     * 
     * @return true if this TaskPrecedency type is : annotation type referencing (i.e.  the preceding Task edits Annotation types referenced by Annotation types edited in the current Task)
     */
    boolean isTypeReferencing();

}
