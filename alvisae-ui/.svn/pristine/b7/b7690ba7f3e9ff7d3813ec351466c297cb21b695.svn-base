/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import fr.inra.mig_bibliome.alvisae.client.data3.TermMemberImpl;

/**
 *
 * @author fpapazian
 */
public class TermMemberInfo extends TermInfo {

    public TermMemberInfo(TermMemberImpl classImpl) {
        super(classImpl);
    }

    @Override
    public TermMemberImpl getTerm() {
        return super.getTerm().cast();
    }

    public int getMemberType() {
        return getTerm().getMemberType();
    }
}
