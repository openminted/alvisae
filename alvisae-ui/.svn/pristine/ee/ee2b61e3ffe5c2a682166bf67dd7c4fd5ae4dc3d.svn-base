/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.ConsolidationStatus;

/**
 *
 * @author fpapazian
 */
public class AnnotationBackReferenceImpl extends AnnotationReferenceImpl implements AnnotationBackReference {

    public static final AnnotationBackReferenceImpl create(String annotationId, Integer annotationSetId, ConsolidationStatus status) {
        AnnotationBackReferenceImpl aRef = AnnotationReferenceImpl.create(annotationId, annotationSetId).cast();
        aRef._setConsolidationStatus(status.toInt());
        return aRef;
    }

    public static final AnnotationBackReferenceImpl create(AnnotationReference annotationRef, ConsolidationStatus status) {
        return create(annotationRef.getAnnotationId(), annotationRef.getAnnotationSetId(), status);
    }

    public static final AnnotationBackReferenceImpl create(AnnotationBackReference annBackRef) {
        return create(annBackRef.getAnnotationId(), annBackRef.getAnnotationSetId(), annBackRef.getConsolidationStatus());
    }
    
    protected AnnotationBackReferenceImpl() {
    }

    private final native void _setConsolidationStatus(int consoStatusId) /*-{ this.status = consoStatusId; }-*/;
    
    private final native int _getConsolidationStatus() /*-{ return this.status; }-*/;
    
    @Override
    public final ConsolidationStatus getConsolidationStatus() {
        return ConsolidationStatus.withId(_getConsolidationStatus());
    }
}
