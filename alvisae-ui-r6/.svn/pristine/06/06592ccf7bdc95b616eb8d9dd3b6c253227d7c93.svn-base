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
import java.util.ArrayList;

/**
 * An event occurring after any change of the Annotation selection (within the working Document).
 * @author fpapazian
 */
public class TextAnnotationSelectionChangedEvent extends GenericAnnotationSelectionChangedEvent {
    protected TextAnnotationSelectionChangedEvent(AnnotatedTextHandler document) {
        super(document, null);
    }

    public TextAnnotationSelectionChangedEvent(AnnotatedTextHandler document, AnnotationSelections selectedAnnotations) {
        super(document, selectedAnnotations);
    }

    public TextAnnotationSelectionChangedEvent(AnnotatedTextHandler document, Annotation annotation, ArrayList<String> selectedMarks, String mainSelectedMark) {
        super(document, new AnnotationSelections());
        if (annotation != null && selectedMarks != null && !selectedMarks.isEmpty()) {
            getAnnotationSelection().addAnnotationSelection(annotation, selectedMarks);
        }
    }

    public TextAnnotationSelectionChangedEvent(AnnotatedTextHandler document, Annotation annotation) {
        super(document, new AnnotationSelections());
        getAnnotationSelection().addAnnotationSelection(annotation, null);
    }

    /*
    public TextAnnotationSelectionChangedEvent(AnnotatedText document, Annotation annotation) {
        super(document, new AnnotationSelections());
        if (!annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
            throw new IllegalArgumentException("Annotation must be of the TEXT kind");
        }
        getAnnotationSelection().getSelections().add(new Selection.TextAnnotationSelection(annotation));
    }
*/
    public ArrayList<String> getMainSelectedAnnotationMarkers() {
        return getAnnotationSelection() != null ? getAnnotationSelection().getMainSelectedAnnotationMarkers() : null;
    }

    public String getMainSelectedMarker() {
        return getAnnotationSelection() != null ? getAnnotationSelection().getMainSelectedMarker() : null;
    }

}
