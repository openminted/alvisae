/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.ExtendedUserInfo;

/**
 *
 * @author fpapazian
 */
public class ExtendedUserInfoImpl extends UserInfoImpl implements ExtendedUserInfo {
   
    protected ExtendedUserInfoImpl() {
    }

    @Override
    public final native boolean isExtendedUserInfo() /*-{ return this.hasOwnProperty('is_admin'); }-*/;    

    @Override
    public final native boolean isAdmin() /*-{ return this.is_admin; }-*/;    

    @Override
    public final native UserAuthorizationsImpl getAuthorizations() /*-{ return this.authorizations; }-*/;

}
