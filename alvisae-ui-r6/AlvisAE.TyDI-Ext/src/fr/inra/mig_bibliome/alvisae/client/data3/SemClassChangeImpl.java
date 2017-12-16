/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.SemClassChangeType;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClassChange;

/**
 *
 * @author fpapazian
 */
public class SemClassChangeImpl extends JavaScriptObject implements SemClassChange {

    protected SemClassChangeImpl() {
    }

    @Override
    public final native SemClassBasicImpl getSemClass() /*-{ return this.semClass; }-*/;

    private final native int _getChangeType() /*-{ return this.change; }-*/;

    @Override
    public final SemClassChangeType getChangeType() {
        return SemClassChangeType.values()[_getChangeType()];
    }

    @Override
    public final native SemClassBasicImpl getOtherSemClass1() /*-{  if (this.hasOwnProperty('otherSemClass1')) { return this.otherSemClass1; } else { return null; } }-*/;

    @Override
    public final native SemClassBasicImpl getOtherSemClass2() /*-{  if (this.hasOwnProperty('otherSemClass2')) { return this.otherSemClass2; } else { return null; } }-*/;

    @Override
    public final native TermLinkedImpl getLinkedTerm() /*-{  if (this.hasOwnProperty('term')) { return this.term; } else { return null; } }-*/;

}
