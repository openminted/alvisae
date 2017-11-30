/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import fr.inra.mig_bibliome.alvisae.client.data.AuthenticationInfoImpl;

/**
 *
 * @author fpapazian
 */
public class ApplicationOptions {

    private static final String OptionPrefix = "aae.prefs.";
    private static final String OptionLoginSuffix = ".loginName";
    private static final String OptionTokenSuffix = ".token";

    private static String getPrefix() {
        return OptionPrefix;
    }
    
    //some options are specific to each instance of AlvisAE
    private static String getInstanceSpecificPrefix() {
        return OptionPrefix + GWT.getModuleBaseURL();
    }
    
    //
    private final String serverSpecificPrefix;
    
    //login option are specifix to the client instance and to the remote server
    public ApplicationOptions(String serverSpecificPrefix) {
        this.serverSpecificPrefix = serverSpecificPrefix;
    }
    
    private String getInstServSpecificPrefix() {
        return getInstanceSpecificPrefix() + ':' + serverSpecificPrefix;
    }

    public String getLastLoginName() {
        String lastLoginName = null;
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String pref = getInstServSpecificPrefix();
            lastLoginName = store.getItem(pref + OptionLoginSuffix);
        }
        return lastLoginName;
    }

    public void setSuccessfullSignIn(AuthenticationInfoImpl authInfo) {
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String pref = getInstServSpecificPrefix();
            store.setItem(pref + OptionLoginSuffix, authInfo.getName());
            store.setItem(pref + OptionTokenSuffix, authInfo.getBase64EncodedBasicAuthToken());
        }
    }

    public boolean hasSavedCredential() {
        return getAuthenticationInfo() != null;
    }

    public AuthenticationInfoImpl getAuthenticationInfo() {
        AuthenticationInfoImpl result = null;
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String pref = getInstServSpecificPrefix();
            String name = store.getItem(pref + OptionLoginSuffix);
            String token = store.getItem(pref + OptionTokenSuffix);
            result = AuthenticationInfoImpl.getForUserToken(name, token);
        }
        return result;
    }

    public void setUnSuccessfullSignIn() {
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String pref = getInstServSpecificPrefix();
            store.removeItem(pref + OptionTokenSuffix);
        }
    }
}
