/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

/**
 *
 * @author fpapazian
 */
public enum AnnotationObsolescenceStatus {

    UPTODATE(0, "UPTODATE"), //up-to-date Annotation (i.e. Annotation belongs to head AnnotationSet)
    OUTDATED(1, "OUTDATED"), //Annotation belongs to invalidated (i.e. non-Head) AnnotationSet
    OUTDATEDREF(2, "OUTDATEDREF"), //one of the components or arguments of the Annotation is not up-to-date
    OUTDATEDBACKREF(3, "OUTDATEDBACKREF"), //one of the Source Annotations of the Annotation is not up-to-date
    ;

    public static AnnotationObsolescenceStatus withId(int id) {
        for (AnnotationObsolescenceStatus enumVal : AnnotationObsolescenceStatus.values()) {
            if (enumVal.toInt() == id) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with id=" + id);
    }

    public static AnnotationObsolescenceStatus withName(String name) {
        for (AnnotationObsolescenceStatus enumVal : AnnotationObsolescenceStatus.values()) {
            if (enumVal.toString().equals(name.trim())) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with name=" + name);
    }
    private final int value;
    private final String name;

    private AnnotationObsolescenceStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int toInt() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
