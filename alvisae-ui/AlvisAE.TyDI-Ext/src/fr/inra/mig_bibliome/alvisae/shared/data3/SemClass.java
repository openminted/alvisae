/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import com.google.gwt.core.client.JsArrayInteger;

/**
 * @author fpapazian
 */
public interface SemClass extends SemClassBasic {

    /**
     * @return the group Ids of the direct hyperonyms of this Semantic class
     */
    public JsArrayInteger getHyperGroupIds();

    /**
     * @return the group Ids of the direct hyponyms of this Semantic class
     */
    public JsArrayInteger getHypoGroupIds();

    /**
     * @return  true if this Semantic class has no Hyperonyms
     */
    public boolean isRooted();
}
