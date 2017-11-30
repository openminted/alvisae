/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author fpapazian
 */
public interface TermExistsResponse extends DetailedResponse {

    public int getTermId();

    public String getSurfaceForm();

    public boolean isClassRepresentative();

    public int getRepresentativeOf();

    public JsArray<? extends JavaScriptObject> getTermMemberships();
}
