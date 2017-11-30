/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import fr.inra.mig_bibliome.alvisae.client.data3.TermLinkedImpl;

/**
 *
 * @author fpapazian
 */
public class TermLinkedInfo extends TermInfo {

    public TermLinkedInfo(TermLinkedImpl classImpl) {
        super(classImpl);
    }

    @Override
    public TermLinkedImpl getTerm() {
        return super.getTerm().cast();
    }

    public int getLinkType() {
        return getTerm().getLinkType();
    }

    public boolean isHeadOfLink() {
        return getTerm().isHeadOfLink();
    }
}
