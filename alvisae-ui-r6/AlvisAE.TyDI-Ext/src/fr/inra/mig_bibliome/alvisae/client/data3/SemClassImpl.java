/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JsArrayInteger;
import fr.inra.mig_bibliome.alvisae.shared.data3.SemClass;

/**
 *
 * @author fpapazian
 */
public class SemClassImpl extends SemClassBasicImpl implements SemClass {

    private static final native SemClassImpl _create(int groupId, String canonicLabel) /*-{
    a = {};
    a.groupId=groupId;
    a.canonicId=0;
    a.canonicLabel=canonicLabel;
    a.version=0;
    a.hyperGroupIds=[];
    a.hypoGroupIds=[];
    return a;
    }-*/;

    public final static SemClassImpl createRoot() {
        return _create(ROOT_ID, "");
    }

    protected SemClassImpl() {
    }

    @Override
    public final native JsArrayInteger getHyperGroupIds() /*-{ return this.hyperGroupIds; }-*/;

    @Override
    public final native JsArrayInteger getHypoGroupIds() /*-{ return this.hypoGroupIds; }-*/;

    @Override
    public final boolean isRooted() {
        return getHyperGroupIds().length() == 0 || (getHyperGroupIds().length() == 1 && getHyperGroupIds().get(0) == ROOT_ID);
    }
}
