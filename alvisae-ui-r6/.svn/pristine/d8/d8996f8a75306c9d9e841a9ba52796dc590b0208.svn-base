/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;


import java.util.List;

public interface RelationDefinition {
	/**
	 * Return the ordered list of roles supported for annotations of this type.
	 * Will not return null.
	 * Returned value is read-only.
	 * @return
	 */
	List<String> getSupportedRoles();
	
	/**
	 * Return the expected types of arguments of annotations of this type with the specified role.
	 * Return null if the specified role is not supported.
	 * Returned value is read-only.
	 * @param role
	 * @return
	 */
	List<String> getArgumentTypes(String role);
}
