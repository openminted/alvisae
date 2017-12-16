/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDITermRef;

/**
 *
 * @author fpapazian
 */
public class PropType_TyDITermRefImpl extends PropertyTypeImpl implements PropType_TyDITermRef {

    protected PropType_TyDITermRefImpl() {
    }

    @Override
    public final native String getTermRefBaseURL() /*-{ return this.TyDIRefBaseURL; }-*/;
}
