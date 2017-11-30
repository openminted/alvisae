/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data;

import fr.inra.mig_bibliome.alvisae.client.data3.UserAuthorizationsImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.AuthenticationInfo;

/**
 *
 * @author fpapazian
 */
public class AuthenticationInfoImpl implements AuthenticationInfo {

    public static native String base64Encode(String input) /*-{ return $wnd.Base64.encode(input);  }-*/;

    public static AuthenticationInfoImpl getForUserPassord(String name, String password) {
        return getForUserToken(name, base64Encode(name + ":" + password));
    }

    public static AuthenticationInfoImpl getForUserToken(String name, String token) {
        if (name != null && !name.isEmpty() && token != null && !token.isEmpty()) {
            return new AuthenticationInfoImpl(name, token);
        } else {
            return null;
        }
    }
    private Integer id = null;
    private String name;
    private final String authenticationToken;
    private Boolean isAdmin = false;
    private UserAuthorizationsImpl auths=null;

    private AuthenticationInfoImpl(String name, String authenticationToken) {
        this.name = name;
        this.authenticationToken = authenticationToken;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
        
    @Override
    public String getBase64EncodedBasicAuthToken() {
        return authenticationToken;
    }

    @Override
    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public UserAuthorizationsImpl getAuthorizations() {
        return auths;
    }

    public void setAuthorizations(UserAuthorizationsImpl auths) {
        this.auths = UserAuthorizationsImpl.createFromJSON(auths.getJSON());
    }
}
