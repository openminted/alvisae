/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import fr.inra.mig_bibliome.alvisae.shared.data3.TermExistsResponse;

/**
 *
 * @author fpapazian
 */
public class TermExistsResponseImpl extends TermBasicImpl implements TermExistsResponse {

    public final static TermExistsResponseImpl createFromJSON(String jsonStr) {
        TermExistsResponseImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    protected TermExistsResponseImpl() {
    }

    @Override
    public final native  int getMessageNumber() /*-{ return this.msgNum; }-*/;    

    @Override
    public final native String getMessageText() /*-{ return this.message; }-*/;    

    @Override
    public final native boolean isClassRepresentative() /*-{ return this.isClassRepresentative; }-*/;    
    
    @Override
    public final native int getRepresentativeOf() /*-{ return this.representativeOf; }-*/;    
            
    @Override
    public final native JsArray<TermMembershipImpl> getTermMemberships() /*-{ return this.inGroups; }-*/;

}
