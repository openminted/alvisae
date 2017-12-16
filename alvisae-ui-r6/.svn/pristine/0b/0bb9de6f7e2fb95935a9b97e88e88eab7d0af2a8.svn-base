/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import fr.inra.mig_bibliome.alvisae.client.data3.Extension.SemClassChangeType;

/**
 * @author fpapazian
 */
public interface SemClassChange {

    /**
     *
     * @return the Semantic class that has changed
     */
    public SemClassBasic getSemClass();

    /**
     *
     * @return the type of this change
     */
    public SemClassChangeType getChangeType();

    /**
     *
     * @return the first other Semantic class involved in the change, if any,
     * otherwise NULL
     */
    public SemClassBasic getOtherSemClass1();
    
        /**
     *
     * @return the second other Semantic class involved in the change, if any,
     * otherwise NULL
     */
    public SemClassBasic getOtherSemClass2();

    /**
     *
     * @return the linked term involved in the change, if any, otherwise NULL
     */
    public TermLinked getLinkedTerm();

}
