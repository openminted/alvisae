/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDISemClassRef;

/**
 *
 * @author fpapazian
 */
public class PropType_TyDISemClassRefImpl extends PropertyTypeImpl implements PropType_TyDISemClassRef {

    protected PropType_TyDISemClassRefImpl() {
    }

    @Override
    public final native String getSemClassRefBaseURL() /*-{ return this.TyDIRefBaseURL; }-*/;

}
