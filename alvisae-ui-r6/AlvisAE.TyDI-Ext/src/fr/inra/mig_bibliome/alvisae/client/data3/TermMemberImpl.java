/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JsArray;
import fr.inra.mig_bibliome.alvisae.shared.data3.TermMember;

/**
 *
 * @author fpapazian
 */
public class TermMemberImpl extends TermBasicImpl implements TermMember {

    protected TermMemberImpl() {
    }

    @Override
    public final native int getMemberType() /*-{ return this.memberType; }-*/;

    @Override
    public final native JsArray<TermLinkedImpl> getLinkedTerms() /*-{ return this.linkedTerms; }-*/;
}
