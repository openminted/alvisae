/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

public enum TaskStatus {

    Pending(0, "pending"),
    ToDo(1, "todo"),
    Upcoming(2, "upcoming"),
    Done(3, "done"),
    Unavailable_Done(4, "na_done"),
    ;
    
    
    public static TaskStatus withId(int id) {
        for (TaskStatus enumVal : TaskStatus.values()) {
            if (enumVal.toInt() == id) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with id=" + id);
    }
    
    public static TaskStatus withName(String name) {
        for (TaskStatus enumVal : TaskStatus.values()) {
            if (enumVal.toString().equals(name.trim())) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("No constant with name=" + name);
    }
    
    private final int value;
    private final String name;

    private TaskStatus(int value, String name) {
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
