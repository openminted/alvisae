/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;

/**
 *
 * @author fpapazian
 */
public class SetSafeAnnotationReference implements AnnotationReference {

    private final String annotationId;
    private final Integer annotationSetId;

    public SetSafeAnnotationReference(String annotationId, Integer annotationSetId) {
        if (annotationId == null) {
            throw new IllegalArgumentException("AnnotationId MUST NOT be null!");
        }
        this.annotationId = annotationId;
        this.annotationSetId = annotationSetId;
    }

    public SetSafeAnnotationReference(String annotationId) {
        this(annotationId, null);
    }

    public SetSafeAnnotationReference(AnnotationReference aRef) {
        this(aRef.getAnnotationId(), aRef.getAnnotationSetId());
    }

    public SetSafeAnnotationReference(AnnotationReference aRef, Integer editableAnnotationSetId) {
        this(aRef.getAnnotationId(), aRef.getAnnotationSetId() != null ? aRef.getAnnotationSetId() : editableAnnotationSetId);
    }

    @Override
    public String getAnnotationId() {
        return annotationId;
    }

    @Override
    public Integer getAnnotationSetId() {
        return annotationSetId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.annotationId.hashCode();
        hash = 11 * hash + (this.annotationSetId != null ? this.annotationSetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AnnotationReference)) {
            return false;
        }
        final AnnotationReference other = (AnnotationReference) obj;
        if (!getAnnotationId().equals(other.getAnnotationId())) {
            return false;
        }
        if (getAnnotationSetId() != other.getAnnotationSetId() && (getAnnotationSetId() == null || !getAnnotationSetId().equals(other.getAnnotationSetId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SetSafeAnnotationReference{" + annotationSetId + ": " + annotationId + '}';
    }
}
