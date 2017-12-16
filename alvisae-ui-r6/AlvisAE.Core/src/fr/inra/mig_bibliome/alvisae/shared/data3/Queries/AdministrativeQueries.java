/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Queries;

import fr.inra.mig_bibliome.alvisae.client.data3.AuthorizationListImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.UserAuthorizationsImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.ExtendedUserInfoImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.ExtendedUserInfoListImpl;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Services related to user authentication
 * @author fpapazian
 */
public interface AdministrativeQueries {

    // [baseurl] :  bibliome.jouy.inra.fr/test/stane/dev/api

    
    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/authorizations
     *
     * @return list of all possible Authorizations in the AlvisAE DB
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getAuthorizationList(AsyncCallback<AuthorizationListImpl> resultCallback);

    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/user
     *
     * @return list all users
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getUserList(AsyncCallback<ExtendedUserInfoListImpl> resultCallback);

    /**
     *
     * <b>Rest Method & URL:</b>
     * GET http://[baseurl]/user/<userId>/authorizations
     *
     * @param userId identifier of the user whose Authorizations will be retrieved
     * @return retrieve user's authorization info for ALL campaigns
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void getUserAuthorizations(int userId, AsyncCallback<UserAuthorizationsImpl> resultCallback);
    
    
    /**
     *
     * <b>Rest Method & URL:</b>
     * PUT http://[baseurl]/user/<userId>/authorizations
     *
     * @param userId identifier of the user whose Authorizations will be retrieved
     * @return retrieve user's authorization info for ALL campaigns
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void setUserAuthorizations(int userId, UserAuthorizationsImpl newAuths, AsyncCallback<UserAuthorizationsImpl> resultCallback);

    
    /**
     * <b>Rest Method & URL:</b>
     * POST http://[baseurl]/user/[userId]
     *
     * @param login value for the login name
     * @param isAdmin value for isAdmin property
     * @param password new value for the password
     *
     * Create a new user
     * 
     * @throws IllegalArgumentException if the specified userId  does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void createUser(String login, boolean isAdmin, String password, AsyncCallback<ExtendedUserInfoImpl> resultCallback);

    /**
     * <b>Rest Method & URL:</b>
     * PUT http://[baseurl]/user/[userId]
     *
     * @param userId Id of an User (might be different from current user)
     * @param login new value for the login name
     * @param isAdmin new value for isAdmin property
     *
     * Update the specified user
     * 
     * @throws IllegalArgumentException if the specified userId  does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void updateUser(int userId, String login, boolean isAdmin, AsyncCallback<ExtendedUserInfoImpl> resultCallback);
    
    /**
     * <b>Rest Method & URL:</b>
     * PUT http://[baseurl]/user/[userId]/chpasswd
     *
     * @param userId Id of an User (might be different from current user)
     * @param newPassword new value for the password
     *
     * Change the password of the specified user
     * 
     * @throws IllegalArgumentException if the specified userId  does not exist (400 Bad Request)
     * @throws SecurityException if the current user is not authenticated (401 Unauthorized) or has no authorization to perform the operation (403 Forbidden)
     */
    void updateUserPassword(int userId, String newPassword, AsyncCallback<JavaScriptObject> resultCallback);
        
}
