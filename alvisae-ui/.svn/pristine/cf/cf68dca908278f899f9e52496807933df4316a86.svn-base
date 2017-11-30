/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author fpapazian
 */
public interface TaskDefinition {

    int getId();

    /**
     * 
     * @return the unique name of this Task within the campaign workflow
     */
    String getName();
    
    int getCardinality();
    
    TaskVisibility getVisibility();

    Set<String> getEditedAnnotationTypes();

    int getPrecedencelevel();

    List<? extends TaskPrecedency> getTaskPrecedencies();
    
    /**
     * 
     * @return the Id of the task reviewed by this Task, or null if no task is reviewed 
     */
    Integer getReviewedTask();
    
    /**
     * 
     * @return set (possibly empty) of id of the tasks that are succeeded by this Task
     */
    Set<Integer> getSucceededTasks();
    
    /**
     * 
     * @return set (possibly empty) of id of the tasks that edit Annotation Types referenced by Annotation types edited in this Task
     */
    Set<Integer> getReferencedAnnotationTypeTasks();
}
