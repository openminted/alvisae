/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.UserInfo;

/**
 *
 * @author fpapazian
 */
public class UserInfoImpl extends Basic_UserMe_Response implements UserInfo {

    protected UserInfoImpl() {
    }

    @Override
    public final native int getId() /*-{ return this.id; }-*/;

    @Override
    public final native String getDisplayName() /*-{ return this.login; }-*/;

}
