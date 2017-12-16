/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class AnnotationSetReplaceUnmatchedSourceEdit extends AnnotationEdit {

    private static MessagesToUserConstants messages = (MessagesToUserConstants) GWT.create(MessagesToUserConstants.class);
    private final AnnotationSet editedAnnotationSet;
    private final List<AnnotationBackReference> newUnmatchedSourceAnnotations;
    private final List<AnnotationBackReference> oldUnmatchedSourceAnnotations;

    public AnnotationSetReplaceUnmatchedSourceEdit(AnnotatedTextHandler document, AnnotationSet editedAnnotationSet, List<AnnotationBackReference> unmatchedSourceAnnotations) {
        super(document);
        this.editedAnnotationSet = editedAnnotationSet;
        assert (editedAnnotationSet == null) : "Edited AnnotationSet must not be null!" ;
        
        this.newUnmatchedSourceAnnotations = unmatchedSourceAnnotations != null ? unmatchedSourceAnnotations : new ArrayList<AnnotationBackReference>();
        this.oldUnmatchedSourceAnnotations = editedAnnotationSet.getUnmatchedSourceAnnotations() != null ? editedAnnotationSet.getUnmatchedSourceAnnotations().getAnnotationBackReferences() : new ArrayList<AnnotationBackReference>();
    }

    @Override
    protected void undoIt() {
        editedAnnotationSet.setUnmatchedSourceAnnotations(oldUnmatchedSourceAnnotations);
    }

    @Override
    protected boolean doIt() {
        editedAnnotationSet.setUnmatchedSourceAnnotations(newUnmatchedSourceAnnotations);
        return true;
    }

    @Override
    public String getPresentationName() {
        return messages.annotationSetReplaceUnmatchedSourcePresentationName(editedAnnotationSet.getDescription());
    }

    @Override
    public String getUndoPresentationName() {
        return messages.undoannotationSetReplaceUnmatchedSourcePresentationName(editedAnnotationSet.getDescription());
    }

    @Override
    public String getRedoPresentationName() {
        return messages.redoannotationSetReplaceUnmatchedSourcePresentationName(editedAnnotationSet.getDescription());
    }
}
