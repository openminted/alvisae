/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

public enum AnnotationKind {

    TEXT(0, "TEXT"),
    GROUP(1, "GROUP"),
    RELATION(2, "RELATION"),
    ;

    public static AnnotationKind withId(int id) {
        for (AnnotationKind enumVal : AnnotationKind.values()) {
            if (enumVal.toInt() == id) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with id=" + id);
    }

    public static AnnotationKind withName(String name) {
        for (AnnotationKind enumVal : AnnotationKind.values()) {
            if (enumVal.toString().equals(name.trim())) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with name=" + name);
    }
    
    private final int value;
    private final String name;

    private AnnotationKind(int value, String name) {
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
