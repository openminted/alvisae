/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events;

import fr.inra.mig_bibliome.alvisae.client.Events.Selection.AnnotationSelections;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;

/**
 * An event occurring when one or several Relation(s) are selected in the UI
 * @author fpapazian
 */
public class RelationSelectionChangedEvent extends GenericAnnotationSelectionChangedEvent {

    protected RelationSelectionChangedEvent(AnnotatedTextHandler document) {
        super(document, null);
    }

    public RelationSelectionChangedEvent(AnnotatedTextHandler document, AnnotationSelections selectedAnnotations) {
        super(document, selectedAnnotations);
    }

    public RelationSelectionChangedEvent(AnnotatedTextHandler document, Annotation annotation) {
        super(document, new AnnotationSelections());
        if (!annotation.getAnnotationKind().equals(AnnotationKind.RELATION)) {
            throw new IllegalArgumentException("Annotation must be of the RELATION kind");
        }
        getAnnotationSelection().getSelections().add(new Selection.RelationAnnotationSelection(annotation));
    }
}
