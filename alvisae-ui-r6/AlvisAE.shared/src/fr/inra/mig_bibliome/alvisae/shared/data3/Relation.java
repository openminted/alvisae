/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.Collection;
import java.util.Map;

public interface Relation {

    public interface Argument {

        String getRole();

        Annotation getArgument();
    }

    /**
     * Return the roles defined in this relation.
     * The returned value is read-only.
     * Will not return null.
     * @return
     */
    Collection<String> getRoles();

    /**
     *
     * @return the mapping role/annotation of this Relation
     */
    Map<String, AnnotationReference> getRolesArguments();

    /**
     * Return the argument's Annotation Reference for the specified role.
     * Return null if there is no argument for the specified role.
     * @param role
	 * @return
     * @throws NullPointerException if role is null
     */
    AnnotationReference getArgumentRef(String role);

    /**
     * Set the argument for the specified role.
     * @param role
     * @param argument
     * @param overwrite either to replace the argument if there is already an argument for the specified role
     * @return true if the argument has been set (false if overwrite is false and there is already an argument for role)
     * @throws NullPointerException if either role or argument is null
     */
    boolean setArgument(String role, AnnotationReference argument, boolean overwrite);

    /**
     * Return all roles of the specified argument.
     * The returned value is read-only.
     * Will not return null.
     * @param argument
     * @return
     * @throws NullPointerException if argument is null
     */
    Collection<String> getRoles(AnnotationReference argument);

    /**
     * Remove the argument for the specified role.
     * @param role
     * @throws NullPointerException if role is null
     */
    void removeArgument(String role);
}
