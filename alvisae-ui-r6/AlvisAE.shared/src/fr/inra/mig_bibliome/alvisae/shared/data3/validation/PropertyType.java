/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

public interface PropertyType {

    String getTypeName();

    /**
     * Return either the specified value is valid or not.
     * @param value
     * @return
     */
    boolean accept(String value);

    /**
     * 
     * @return this PropertyType as PropType_ClosedDomain, or null if it is another PropertyType
     */
    PropType_FreeText getAsFreeTextType();
    
    PropType_ClosedDomain getAsClosedDomainType();
    
    PropType_TyDITermRef getAsTyDITermRefType();

    PropType_TyDISemClassRef getAsTyDISemClassRefType();

    PropType_TyDIConceptRef getAsTyDIConceptRefType();
    
    boolean isTyDIResourceRef();
}
