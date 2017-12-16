/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events;

import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationEdit;

/**
 * An event indicating that an Edit operation has been Undone (vs Done)  .
 * @author fpapazian
 */
public class EditUndoneEvent extends EditHappenedEvent {
    public EditUndoneEvent(AnnotationEdit annotationEdit) {
        super(annotationEdit);
    }    
    
}
