/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;

/**
 * 
 * @author fpapazian
 */
public class AnnotationReferenceImpl extends JavaScriptObject implements AnnotationReference {

    private static final native AnnotationReferenceImpl create(String annotationId) /*-{
    aRef = {};
    aRef.ann_id=annotationId;
    return aRef;
    }-*/;

    public static final AnnotationReferenceImpl create(String annotationId, Integer annotationSetId) {
        AnnotationReferenceImpl aRef = create(annotationId);
        if (annotationSetId != null) {
            aRef.setAnnotationSetId(annotationSetId);
        }
        return aRef;
    }

    public static final AnnotationReferenceImpl create(AnnotationReference annotationRef) {
        return create(annotationRef.getAnnotationId(), annotationRef.getAnnotationSetId());
    }

    protected AnnotationReferenceImpl() {
    }

    @Override
    public final native String getAnnotationId() /*-{ return this.ann_id; }-*/;

    private final native int _getAnnotationSetId() /*-{ if (this.hasOwnProperty('set_id')) { return this.set_id; } else { return -1; } }-*/;

    @Override
    public final Integer getAnnotationSetId() {
        int annotationSetId = _getAnnotationSetId();
        return annotationSetId != -1 ? annotationSetId : null;

    }

    private final native void setAnnotationSetId(int annotationSetId) /*-{ this.set_id = annotationSetId; }-*/;

    private final static String INVALIDFIELD_PREFIX = "Invalid field ";

    /**
     * Check that the AnnotationReference parsed from a JSON string conforms to the expected
     * structure
     *
     * @throws IllegalArgumentException
     */
    public final void checkStructure() {
        String annId = null;
        try {
            annId = getAnnotationId();
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "AnnotationRefId", ex);
        }
        if (annId == null || annId.trim().isEmpty()) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "-> AnnotationRefId should not be null");
        }
        try {
            Integer annSetId = getAnnotationSetId();
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "AnnotationSetRefId for AnnotationRefId=" + annId, ex);
        }
    }
}
