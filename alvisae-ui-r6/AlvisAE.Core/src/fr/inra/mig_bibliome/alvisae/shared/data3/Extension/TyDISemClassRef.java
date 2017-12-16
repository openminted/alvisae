/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Extension;

/**
 *
 * @author fpapazian
 */
public interface TyDISemClassRef extends TyDIResourceRef {

    public Integer getTyDISemanticClassId();

    public Integer getTyDICanonicTermId();
    
    public String getCanonicLabel();
    
    public boolean sameShortSemClassRef(String otherSemClassRef);
    
}
