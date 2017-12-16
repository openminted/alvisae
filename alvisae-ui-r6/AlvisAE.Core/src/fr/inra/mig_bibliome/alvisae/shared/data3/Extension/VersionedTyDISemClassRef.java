/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Extension;

/**
 *
 * @author fpapazian
 */
public interface VersionedTyDISemClassRef extends TyDISemClassRef {

    public int getTermStructVersion();

    public boolean isTermStructVersionObsolete();
    
}