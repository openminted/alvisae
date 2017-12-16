/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JsonUtils;
import fr.inra.mig_bibliome.alvisae.shared.data3.VersionedSemClass;

/**
 *
 * @author fpapazian
 */
public class VersionedSemClassImpl extends SemClassImpl implements VersionedSemClass {

    public final static VersionedSemClassImpl createFromJSON(String jsonStr) {
        VersionedSemClassImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    protected VersionedSemClassImpl() {
    }

    @Override
    public final native int getTermStructVersion() /*-{ return this.termStructVersion; }-*/;
    

    @Override
    public final native boolean isTermStructVersionObsolete() /*-{ return this.versionObsolete; }-*/;
}
