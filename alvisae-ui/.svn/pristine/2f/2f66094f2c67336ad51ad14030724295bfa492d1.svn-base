/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

public enum TaskVisibility {

    Restricted(0, "restricted"),
    Protected(1, "protected"),
    Public(2, "public"),
    ;

    public static TaskVisibility withId(int id) {
        for (TaskVisibility enumVal : TaskVisibility.values()) {
            if (enumVal.toInt() == id) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with id=" + id);
    }
    
    public static TaskVisibility withName(String name) {
        for (TaskVisibility enumVal : TaskVisibility.values()) {
            if (enumVal.toString().equals(name.trim())) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with name=" + name);
    }
    
    
    private final int value;
    private final String name;

    private TaskVisibility(int value, String name) {
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
