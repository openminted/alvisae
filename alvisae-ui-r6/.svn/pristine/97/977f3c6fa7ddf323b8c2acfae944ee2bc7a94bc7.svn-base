/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.Extension;

import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDIResourceRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.VersionedTyDISemClassRef;

/**
 *
 * @author fpapazian
 */
public class VersionedTyDISemClassRefImpl extends TyDISemClassRefImpl implements VersionedTyDISemClassRef {
    private final int termStructVersion;
    private final boolean versionObsolete;

    public VersionedTyDISemClassRefImpl(TyDIResourceRef resRef, int semClassId, Integer canonicId, String canonicLabel, int termStructVersion, boolean versionObsolete) {
        super(resRef, semClassId, canonicId);
        this.termStructVersion = termStructVersion;
        this.versionObsolete = versionObsolete;
    }
    
    public VersionedTyDISemClassRefImpl(TyDIResourceRef resRef, int semClassId, Integer canonicId, int termStructVersion, boolean versionObsolete) {
        this(resRef, semClassId, canonicId, null, termStructVersion, versionObsolete);
    }
  
    @Override
    public int getTermStructVersion() {
        return termStructVersion;
    }

    @Override
    public boolean isTermStructVersionObsolete() {
        return versionObsolete;
    }
    
}
