
/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.VersionedBranches;

/**
 *
 * @author fpapazian
 */
public class VersionedBranchesImpl extends BranchesImpl implements VersionedBranches  {
    

    protected VersionedBranchesImpl() {
    }

    @Override
    public final native int getTermStructVersion() /*-{ return this.termStructVersion; }-*/;
    

    @Override
    public final native boolean isTermStructVersionObsolete() /*-{ return this.versionObsolete; }-*/;
}
