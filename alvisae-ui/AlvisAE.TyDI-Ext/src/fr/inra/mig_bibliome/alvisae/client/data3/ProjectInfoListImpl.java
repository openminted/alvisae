/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;

/**
 *
 * @author fpapazian
 */
public class ProjectInfoListImpl extends JsArray<ProjectInfoImpl> {

    public final static ProjectInfoListImpl createFromJSON(String jsonStr) {
        ProjectInfoListImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }

    protected ProjectInfoListImpl() {
    }
}
