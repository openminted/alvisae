/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDIConceptRef;

/**
 *
 * @author fpapazian
 */
public class PropType_TyDIConceptRefImpl extends PropertyTypeImpl implements PropType_TyDIConceptRef {

    protected PropType_TyDIConceptRefImpl() {
    }

    @Override
    public final native String getSemClassRefBaseURL() /*-{ return this.TyDIRefBaseURL; }-*/;

}
