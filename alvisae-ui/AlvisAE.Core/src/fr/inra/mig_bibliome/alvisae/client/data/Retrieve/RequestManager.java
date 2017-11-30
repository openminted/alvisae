/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data.Retrieve;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.data.AuthenticationException;
import fr.inra.mig_bibliome.alvisae.client.data.AuthenticationInfoImpl;
import fr.inra.mig_bibliome.alvisae.client.data.GenericRequestCallback;
import fr.inra.mig_bibliome.alvisae.client.data3.Basic_UserMe_Response;
import fr.inra.mig_bibliome.alvisae.client.data3.CDXWS_UserMe_Response;
import fr.inra.mig_bibliome.alvisae.client.data3.ExtendedUserInfoImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.UserAuthorizationsImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.AuthenticationInfo;

public class RequestManager extends AbstractRequestManager {

    public static final int LOCAL_USERID = 0;
    //
    //--------------------------------------------------------------------------
    private boolean autosigned = false;
    private final String authenticationUrl;

    public RequestManager(EventBus eventBus, String authenticationUrl) {
        super(eventBus);
        this.authenticationUrl = authenticationUrl;
    }

    //FIXME REMOVE for production version
    @Deprecated
    public void setAutoSignedIn() {
        GWT.log("Autologin is for DEV mode only");
        autosigned = true;
        currentAuthenticationInfo = AuthenticationInfoImpl.getForUserPassord("foo", "foo");
        currentAuthenticationInfo.setId(LOCAL_USERID);
    }

    @Deprecated
    public boolean isAutoSignedIn() {
        return autosigned;
    }

    // =========================================================================
    @Override
    public void signIn(String userName, String password, final AsyncCallback<AuthenticationInfo> resultCallback) {
        AuthenticationInfoImpl authInfo = AuthenticationInfoImpl.getForUserPassord(userName, password);
        signIn(authInfo, resultCallback);
    }

    @Override
    public boolean reSignIn(final AsyncCallback<AuthenticationInfo> resultCallback) {
        AuthenticationInfoImpl authInfo = getApplicationOptions().getAuthenticationInfo();
        if (authInfo != null) {
            signIn(authInfo, resultCallback);
            return true;
        } else {
            return false;
        }
    }

    public AuthenticationInfoImpl changeAuthenticationInfo(CDXWS_UserMe_Response authenticationResponse, String newPassword) {
        AuthenticationInfoImpl authInfo = AuthenticationInfoImpl.getForUserPassord(authenticationResponse.getLoginName(), newPassword);
        authInfo.setId(authenticationResponse.getUserId());
        ExtendedUserInfoImpl extUserInfo = (ExtendedUserInfoImpl) (Basic_UserMe_Response) authenticationResponse;
        if (extUserInfo.isExtendedUserInfo()) {
            authInfo.setAdmin(extUserInfo.isAdmin());
        }
        currentAuthenticationInfo = authInfo;
        return authInfo;
    }

    public void resetAuthenticationInfos(String name, boolean isAdmin) {
        //changing name just for dislpay, but since the name+password are joined before being encoded, it is mandatory to singin again
        currentAuthenticationInfo.setName(name);
        currentAuthenticationInfo.setAdmin(isAdmin);
    }

    public void resetAuthenticationInfoAuths(UserAuthorizationsImpl auths) {
        currentAuthenticationInfo.setAuthorizations(auths);
    }

    private void signIn(final AuthenticationInfoImpl authInfo, final AsyncCallback<AuthenticationInfo> resultCallback) {

        currentAuthenticationInfo = null;

        String methodUrl = getServerBaseUrl() + authenticationUrl;

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, methodUrl);

        builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
        builder.setHeader("Accept", "application/json");

        builder.setHeader("Authorization", "Basic " + authInfo.getBase64EncodedBasicAuthToken());

        submit(builder, new RequestCallback() {

            @Override
            public void onError(Request request, Throwable exception) {
                if (resultCallback != null) {
                    resultCallback.onFailure(new AuthenticationException("Unable to authenticate with the specified credential (1)\n" + exception.getMessage()));
                }
            }

            @Override
            public void onResponseReceived(Request request, Response response) {
                switch (response.getStatusCode()) {
                    case Response.SC_OK:
                        currentAuthenticationInfo = processSigninResponse(authInfo, response.getText());

                        GWT.log("User logged :" + authInfo.getId() + " " + authInfo.getName());
                        getApplicationOptions().setSuccessfullSignIn(authInfo);
                        eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.SignedIn, RequestManager.this));
                        if (resultCallback != null) {
                            resultCallback.onSuccess(currentAuthenticationInfo);
                        }

                        break;
                    case Response.SC_UNAUTHORIZED:
                        if (resultCallback != null) {
                            getApplicationOptions().setUnSuccessfullSignIn();
                            resultCallback.onFailure(new AuthenticationException("Unable to authenticate with the specified credential (2):\n" + response.getStatusCode() + "\n" + response.getText()));

                        }
                        break;
                    case Response.SC_BAD_GATEWAY:
                        if (resultCallback != null) {
                            resultCallback.onFailure(new AuthenticationException("Unable to contact the server (1):\n" + response.getStatusCode()));
                        }
                        break;
                    case 0:
                        //Cross domain request are "preflighted" by an OPTION request (to determine whether the actual request is safe to send);

                        // with FireFox (3.6.16), if the server response to OPTION request is 502 (not 200?), then this Callback will receive a statusCode equals to 0
                        resultCallback.onFailure(new AuthenticationException("Unable to contact the server (2):\n"));
                        break;
                    default:
                        if (resultCallback != null) {
                            getApplicationOptions().setUnSuccessfullSignIn();
                            resultCallback.onFailure(new AuthenticationException("Unable to authenticate with the specified credential (3):\n" + response.getStatusCode() + "\n" + response.getText()));
                        }
                        break;
                }
            }
        });
    }

    protected AuthenticationInfoImpl processSigninResponse(AuthenticationInfoImpl authInfo, String responseText) {
        Basic_UserMe_Response parsedResponse = Basic_UserMe_Response.createFromJSON(responseText);
        authInfo.setId(parsedResponse.getUserId());
        //
        ExtendedUserInfoImpl extUserInfo = (ExtendedUserInfoImpl) parsedResponse;
        if (extUserInfo.isExtendedUserInfo()) {
            authInfo.setAdmin(extUserInfo.isAdmin());
            authInfo.setAuthorizations(extUserInfo.getAuthorizations());
        }
        return authInfo;
    }

    @Override
    public void signOut(final AsyncCallback<Void> resultCallback) {
        currentAuthenticationInfo = null;
    }

    @Override
    public boolean canPerformReSignIn() {
        return getApplicationOptions().hasSavedCredential();
    }

    /**
     * Add a Milestone request - this request will not be sent, but its callback
     * will be executed once all previously submitted request are replied
     */
    public void addMilestone(final Command executeWhenReached) {
        genericCall(AbstractRequestManager.NOOPREQUEST_URL, RequestBuilder.GET, null, null, new GenericRequestCallback<JavaScriptObject>(eventBus,
                new AsyncResponseHandler<JavaScriptObject>() {

                    @Override
                    public void onSuccess(JavaScriptObject result) {
                        if (executeWhenReached != null) {
                            executeWhenReached.execute();
                        }
                    }
                }, this) {

            @Override
            public JavaScriptObject decode(String responseText) {
                return null;
            }
        });

    }
}
