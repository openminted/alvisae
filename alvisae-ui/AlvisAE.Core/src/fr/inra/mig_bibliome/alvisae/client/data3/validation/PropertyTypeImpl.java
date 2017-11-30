/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_ClosedDomain;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_FreeText;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDIConceptRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDISemClassRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDITermRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyType;

/**
 *
 * @author fpapazian
 */
public class PropertyTypeImpl extends JavaScriptObject implements PropertyType {

    protected PropertyTypeImpl() {
    }

    @Override
    public final boolean accept(String value) {
        return true;
    }

    @Override
    public final native String getTypeName() /*-{ return this.valTypeName; }-*/;

    @Override
    public final PropType_ClosedDomain getAsClosedDomainType() {
        PropType_ClosedDomainImpl result = null;
        if (PropType_ClosedDomain.NAME.equals(getTypeName())) {
            result = cast();
        }
        return result;
    }

    @Override
    public final PropType_FreeText getAsFreeTextType() {
        PropType_FreeTextImpl result = null;
        if (PropType_FreeText.NAME.equals(getTypeName())) {
            result = cast();
        }
        return result;
    }

    @Override
    public final PropType_TyDITermRef getAsTyDITermRefType() {
        PropType_TyDITermRefImpl result = null;
        if (PropType_TyDITermRef.NAME.equals(getTypeName())) {
            result = cast();
        }
        return result;
    }

    @Override
    public final PropType_TyDISemClassRef getAsTyDISemClassRefType() {
        PropType_TyDISemClassRefImpl result = null;
        if (PropType_TyDISemClassRef.NAME.equals(getTypeName())) {
            result = cast();
        }
        return result;
    }

    @Override
    public final PropType_TyDIConceptRef getAsTyDIConceptRefType() {
        PropType_TyDIConceptRefImpl result = null;
        if (PropType_TyDIConceptRef.NAME.equals(getTypeName())) {
            result = cast();
        }
        return result;
    }
    
    @Override
    public final boolean isTyDIResourceRef() {
        return (PropType_TyDISemClassRef.NAME.equals(getTypeName())) || (PropType_TyDITermRef.NAME.equals(getTypeName())) || (PropType_TyDIConceptRef.NAME.equals(getTypeName()));
    }
}
