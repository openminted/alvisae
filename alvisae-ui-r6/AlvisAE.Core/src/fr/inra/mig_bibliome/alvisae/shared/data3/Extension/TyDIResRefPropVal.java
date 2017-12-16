/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Extension;

/**
 *
 * @author fpapazian
 */
public interface TyDIResRefPropVal {

    public String getResourceRef();

    public String getLabel();

    public int getVersionNumber();
    
    public String toUrlWithFragment();
  
}
