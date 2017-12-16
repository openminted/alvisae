/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.Extension;

/**
 *
 * @author fpapazian
 */
public enum SemClassChangeType {

    AddedSemClass(0, "AddedSemClass"),
    RemovedSemClass(1, "RemovedSemClass"),
    ChangedSemClassCanonic(2, "ChangedSemClassCanonic"),
    AddedTermToSemClass(3, "AddedTermToSemClass"),
    RemovedTermFromSemClass(4, "RemovedTermFromSemClass"),
    ChangedDirectAncestor(5, "ChangedDirectAncestor"),
    ChangedAncestor(6, "ChangedAncestor"),
    ;

    public static SemClassChangeType withId(int id) {
        for (SemClassChangeType enumVal : SemClassChangeType.values()) {
            if (enumVal.toInt() == id) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with id=" + id);
    }

    public static SemClassChangeType withName(String name) {
        for (SemClassChangeType enumVal : SemClassChangeType.values()) {
            if (enumVal.toString().equals(name.trim())) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with name=" + name);
    }
    private final int value;
    private final String name;

    private SemClassChangeType(int value, String name) {
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
