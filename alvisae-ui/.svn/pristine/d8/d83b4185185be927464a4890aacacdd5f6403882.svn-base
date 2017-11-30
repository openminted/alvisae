/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JsArray;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.SourceAnnotations;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class SourceAnnotationsImpl extends JsArray<AnnotationBackReferenceImpl> implements SourceAnnotations {

    public static final SourceAnnotationsImpl create(List<AnnotationBackReference> backRefs) {
        SourceAnnotationsImpl result = JsArray.createArray().cast();
        if (backRefs != null) {
            for (AnnotationBackReference abr : backRefs) {
                result.addBackReference(abr);
            }
        }
        return result;
    }

    protected SourceAnnotationsImpl() {
    }

    @Override
    public final List<AnnotationBackReference> getAnnotationBackReferences() {
        ArrayList<AnnotationBackReference> result = new ArrayList<AnnotationBackReference>();
        for (AnnotationBackReferenceImpl srcAnnRef : new JsArrayDecorator<AnnotationBackReferenceImpl>(this)) {
            result.add(srcAnnRef);
        }
        return result;
    }

    @Override
    public final void addBackReference(AnnotationBackReference annBackRef) {
        push(AnnotationBackReferenceImpl.create(annBackRef));
    }

    @Override
    public final void removeAllBackReferences() {
        setLength(0);
    }
}
