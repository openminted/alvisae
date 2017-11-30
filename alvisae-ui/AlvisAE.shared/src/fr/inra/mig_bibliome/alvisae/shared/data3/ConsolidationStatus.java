/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

/**
 *
 * @author fpapazian
 */
public enum ConsolidationStatus {

    POSTPONED(0, "POSTPONED"),
    RESOLVED(1, "RESOLVED"),
    REJECTED(2, "REJECTED"),
    SPLIT(3, "SPLIT"),
    ;

    public static ConsolidationStatus withId(int id) {
        for (ConsolidationStatus enumVal : ConsolidationStatus.values()) {
            if (enumVal.toInt() == id) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with id=" + id);
    }

    public static ConsolidationStatus withName(String name) {
        for (ConsolidationStatus enumVal : ConsolidationStatus.values()) {
            if (enumVal.toString().equals(name.trim())) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with name=" + name);
    }
    private final int value;
    private final String name;

    private ConsolidationStatus(int value, String name) {
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
