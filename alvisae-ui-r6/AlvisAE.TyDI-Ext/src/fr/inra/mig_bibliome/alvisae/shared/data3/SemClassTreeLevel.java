/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.Map;

/**
 * @author fpapazian
 */
public interface SemClassTreeLevel extends SemClass {

    /**
     * @return 
     */
    Map<Integer, SemClass> getHypoGroupsDetails();
    
    
    /**
     * 
     * @return the TermId of the term added to this Semantic Class by the corresponding WS call.
     */    
    int getAddedTermId();
}
