/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Queries;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Services related to user authentication
 * @author fpapazian
 */
public interface AuthenticationQueries {

    // [baseurl] :  bibliome.jouy.inra.fr/test/stane/dev/api

    /**
     * 
     * @return id of the authenticated user, null if not authenticated
     */
    public Integer getCurrentUserId();
    
    /**
     * 
     * @return name of the authenticated user, null if not authenticated
     */
    public String getCurrentUserName();
    
    /**
     * 
     * @return true if the authenticated user is application admin, null if not authenticated
     */
    public boolean isCurrentUserAdmin();
    
    /**
     * Perform User authentication on the remote server based on info interactively provided by the user
     * @param userName an name used to uniquely identify the user within this application
     * @param password a passphrase checked by the server to authenticate the user
     * @return AuthenticationInfo that will be used to perform all subsequent User Authenticated queries
     */
    void signIn(String userName, String password, AsyncCallback<AuthenticationInfo> resultCallback);
        
    /**
     * Perform User authentication on the remote server based on saved credentials
     * @param resultCallback
     * @return false, if no credential can be used for signing-in
     */
    boolean reSignIn(final AsyncCallback<AuthenticationInfo> resultCallback);

    /**
     * Perform User sign out
     * @param resultCallback
     */
    void signOut(AsyncCallback<Void> resultCallback);
    
    /**
     * 
     * @return true if sign-in can be performed silently by supplying saved credentials (i.e. without the need of showing sign-in dialog to the user)
     */
    boolean canPerformReSignIn();

    
}
