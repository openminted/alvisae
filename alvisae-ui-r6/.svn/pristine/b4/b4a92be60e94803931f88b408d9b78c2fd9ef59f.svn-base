/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.Extension;

import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.CheckedSemClass;

/**
 *
 * @author fpapazian
 */
public class CheckedSemClassImpl extends JavaScriptObject implements CheckedSemClass {

    public static final native CheckedSemClassImpl create(int semClassId, int versionNum) /*-{
     a = {};
     a.id=semClassId;
     a.vn=versionNum;
     return a;
     }-*/;

    protected CheckedSemClassImpl() {
    }

    @Override
    public final native int getSemClassId() /*-{ return this.id; }-*/;

    @Override
    public final native int getVersionNum() /*-{ return this.vn; }-*/;
}
