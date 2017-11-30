/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import com.google.gwt.core.client.JsArrayString;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_ClosedDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class PropType_ClosedDomainImpl extends PropertyTypeImpl implements PropType_ClosedDomain {

    protected PropType_ClosedDomainImpl() {
    }

    private final native JsArrayString _getDomainValues() /*-{ return this.domain; }-*/;

    @Override
    public final List<String> getDomainValues() {
        List<String> result = null;
        JsArrayString array = _getDomainValues();
        if (array != null) {
            result = new ArrayList<String>();
            for (int i = 0, n = array.length(); i < n; ++i) {
                //@SuppressWarnings("unchecked")
                String value = array.get(i);
                result.add(value);
            }
            result = Collections.unmodifiableList(result);
        }
        return result;
    }

    @Override
    public final native String getDefaultValue() /*-{ return this.defaultVal; }-*/;

}
