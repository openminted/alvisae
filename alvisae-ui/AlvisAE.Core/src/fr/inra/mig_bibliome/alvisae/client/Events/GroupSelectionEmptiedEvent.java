/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events;

import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;

/**
 *
 * @author fpapazian
 */
public class GroupSelectionEmptiedEvent extends GroupSelectionChangedEvent implements GenericAnnotationSelectionEmptiedEvent {

    public GroupSelectionEmptiedEvent(AnnotatedTextHandler annotatedDoc) {
        super(annotatedDoc);
    }
}
