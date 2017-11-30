/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

public enum AnnotationSetType {

    HtmlAnnotation(2, "HtmlAnnotation"),
    UserAnnotation(1, "UserAnnotation"),
    AlvisNLPAnnotation(4, "AlvisNLP"),
    ThirdPartyAnnotation(3, "ThirdParty"),
    ;
    
    public static AnnotationSetType withId(int id) {
        for (AnnotationSetType enumVal : AnnotationSetType.values()) {
            if (enumVal.toInt() == id) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with id=" + id);
    }
    
    public static AnnotationSetType withName(String name) {
        for (AnnotationSetType enumVal : AnnotationSetType.values()) {
            if (enumVal.toString().equals(name.trim())) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with name=" + name);
    }
    
    private final int value;
    private final String name;

    private AnnotationSetType(int value, String name) {
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
