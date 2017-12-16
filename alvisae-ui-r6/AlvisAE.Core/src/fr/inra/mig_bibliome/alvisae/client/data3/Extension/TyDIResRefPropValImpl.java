/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.Extension;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.URL;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDIResRefPropVal;

/**
 * Composite property value used as TyDI resource reference. it contains an Url,
 * a version number and label, and can be serialized to/deserialized from an Url
 * with complex multipart fragment
 *
 * @author fpapazian
 */
public class TyDIResRefPropValImpl extends JavaScriptObject implements TyDIResRefPropVal {

    private static final native TyDIResRefPropValImpl _create(String resourceRef, String label, int versionNum)/*-{
     a = {};
     a.r=resourceRef;
     a.l=label;
     a.v=versionNum;
     return a;
     }-*/;

    public static final TyDIResRefPropValImpl create(String resourceRef, String label, int versionNum) {
        if (label.isEmpty()) {
            label = null;
        }
        return _create(resourceRef, label, versionNum).cast();
    }
    private static final String FragSepAndVersPrefix = ResourceLocator.UrlFragmentSeparator + "v=";
    private static final String FragPartSep = "&";
    private static final String FragPartSepAndLabelPrefix = FragPartSep + "l=";

    public final static TyDIResRefPropValImpl createFromUrlWithFragment(String url) {
        try {
            String resourceRef;
            String label = null;
            int version = 0;

            int fragSepIdx = url.indexOf(FragSepAndVersPrefix);
            if (fragSepIdx > -1) {
                resourceRef = url.substring(0, fragSepIdx);
                int fragPartSepIdx = url.indexOf(FragPartSepAndLabelPrefix);
                if (fragPartSepIdx > -1) {
                    try {
                        version = Integer.valueOf(
                                url.substring(fragSepIdx + FragPartSepAndLabelPrefix.length(), fragPartSepIdx));
                    } catch (NumberFormatException ex) {
                    }
                    label = URL.decodePathSegment(url.substring(fragPartSepIdx + FragPartSepAndLabelPrefix.length())).trim();
                } else {
                    try {
                        version = Integer.valueOf(
                                url.substring(fragSepIdx + FragPartSepAndLabelPrefix.length()));
                    } catch (NumberFormatException ex) {
                    }
                }

            } else {
                resourceRef = url;
            }
            return create(resourceRef, label, version);
        } catch (Exception ex) {
            return null;
        }
    }

    protected TyDIResRefPropValImpl() {
    }

    @Override
    public final native String getResourceRef() /*-{ return this.r; }-*/;

    @Override
    public final native String getLabel() /*-{ return this.l; }-*/;

    @Override
    public final native int getVersionNumber() /*-{ return this.v; }-*/;

    @Override
    public final String toUrlWithFragment() {
        return getResourceRef()
                + FragSepAndVersPrefix + getVersionNumber()
                + FragPartSepAndLabelPrefix + URL.encodePathSegment(getLabel());
    }
}
