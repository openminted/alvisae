/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data.Retrieve;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.*;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Config.ApplicationOptions;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.data.AuthenticationInfoImpl;
import fr.inra.mig_bibliome.alvisae.client.data.GenericRequestCallback;
import fr.inra.mig_bibliome.alvisae.client.data3.Extension.ResourceLocator;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.AuthenticationInfo;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.AuthenticationQueries;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author fpapazian
 */
public abstract class AbstractRequestManager implements AuthenticationQueries {

    public static class Entry {

        String name;
        String value;

        public Entry(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    public static String buildQueryString(Entry[] queryEntries) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, n = queryEntries.length; i < n; ++i) {
            Entry queryEntry = queryEntries[i];

            if (i > 0) {
                sb.append("&");
            }

            // encode the characters in the name
            String encodedName = URL.encodeQueryString(queryEntry.getName());
            sb.append(encodedName);

            sb.append("=");

            // encode the characters in the value
            String encodedValue = URL.encodeQueryString(queryEntry.getValue());
            sb.append(encodedValue);
        }

        return sb.toString();
    }

    //--------------------------------------------------------------------------
    private static int MAX_CONCURRENTREQUEST = 2;
    //
    protected static final String NOOPREQUEST_URL = "AlvisAE_NoOpRequest";
    //--------------------------------------------------------------------------
    private String serverBaseUrl = null;
    //
    private Queue<RequestBuilder> pending = new LinkedList<RequestBuilder>();
    private Queue<Request> sent = new LinkedList<Request>();
    private int nbPendingMilestone = 0;
    //
    protected final EventBus eventBus;
    protected AuthenticationInfoImpl currentAuthenticationInfo;
    //
    protected ApplicationOptions applicationOptions = null;

    public AbstractRequestManager(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setServerBaseUrl(String serverBaseUrl) {
        this.serverBaseUrl = ResourceLocator.cleanUrl(serverBaseUrl);
        if (serverBaseUrl!=null) {
            applicationOptions = new ApplicationOptions(serverBaseUrl);
        } else {
            applicationOptions = null;
        }
    }

    public ApplicationOptions getApplicationOptions() {
        return applicationOptions;
    }

    public String getServerBaseUrl() {
        return serverBaseUrl;
    }

    public boolean isSignedIn() {
        return currentAuthenticationInfo != null;
    }

    @Override
    public Integer getCurrentUserId() {
        return currentAuthenticationInfo != null ? currentAuthenticationInfo.getId() : null;
    }

    @Override
    public String getCurrentUserName() {
        return currentAuthenticationInfo != null ? currentAuthenticationInfo.getName() : null;
    }

    @Override
    public boolean isCurrentUserAdmin() {
        return currentAuthenticationInfo != null ? currentAuthenticationInfo.isAdmin() : null;
    }
    
    // =========================================================================
    @Override
    public abstract void signIn(final String userName, String password, final AsyncCallback<AuthenticationInfo> resultCallback);

    @Override
    public void signOut(final AsyncCallback<Void> resultCallback) {
        currentAuthenticationInfo = null;
    }

    /**
     * 
     * @param encodedUrl URL to the REST method to call
     * @param method HTTP method to be used 
     * @param params optional request parameters (POST)
     * @param data optional request data (PUT, POST)
     * @param genericRequestCallback GenericRequestCallback used to process the response
     */
    public void genericCall(String encodedUrl, Method method, Entry[] params, String data, GenericRequestCallback<? extends JavaScriptObject> genericRequestCallback) {
        RequestBuilder builder = new RequestBuilder(method, encodedUrl);

        builder.setHeader("Accept", "application/json");
        //Basic HTTP authentication scheme
        if (currentAuthenticationInfo!=null) {
            builder.setHeader("Authorization", "Basic " + currentAuthenticationInfo.getBase64EncodedBasicAuthToken());
        }
        assert (params == null) || (data == null) : "Params and Data arguments must not be both valued!" ;
        if (params != null) {
            builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
            String paramString = buildQueryString(params);
            builder.setRequestData(paramString);
        } else if (data != null) {
            builder.setRequestData(data);
        }
        submit(builder, genericRequestCallback);
    }

    public void submit(RequestBuilder builder, final RequestCallback clientCallback) {
        //wrap in another RequestCallback to keep track of the activity
        builder.setCallback(new RequestCallback() {

            @Override
            public void onError(Request request, Throwable exception) {
                sent.remove(request);
                if (request != null) {
                    eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, AbstractRequestManager.this));
                    eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.AsynchResponseArrived, AbstractRequestManager.this));
                }
                proceed();
                if (clientCallback != null) {
                    clientCallback.onError(request, exception);
                }
            }

            @Override
            public void onResponseReceived(Request request, Response response) {
                if (request != null) {
                    sent.remove(request);
                    eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Idle, AbstractRequestManager.this));
                    eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.AsynchResponseArrived, AbstractRequestManager.this));
                }
                proceed();
                if (clientCallback != null) {
                    clientCallback.onResponseReceived(request, response);
                }
            }
        });

        pending.add(builder);
        if (NOOPREQUEST_URL.equals(builder.getUrl())) {
            nbPendingMilestone++;
        }
        proceed();
    }

    private void sendNext() {
        RequestBuilder builder = pending.peek();
        if (builder != null) {
            //if next request is a Milestone, wait until last sent request is replied before proceeding
            if (NOOPREQUEST_URL.equals(builder.getUrl())) {
                if (sent.isEmpty()) {
                    builder = pending.poll();
                    nbPendingMilestone--;
                    RequestCallback wrapperCallback = builder.getCallback();
                    if (wrapperCallback != null) {
                        wrapperCallback.onResponseReceived(null, null);
                    }
                }
            } else {
                builder = pending.poll();
                try {
                    Request request = builder.send();
                    sent.add(request);
                    eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.Processing, AbstractRequestManager.this));
                    eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.AsynchRequestSent, AbstractRequestManager.this));
                } catch (RequestException e) {
                    RequestCallback wrapperCallback = builder.getCallback();
                    if (wrapperCallback != null) {
                        wrapperCallback.onError(null, e);
                    }
                }
                //try to send again (after Milestone processing need to send several times until MAX_CONCURRENTREQUEST is reached)
                if (sent.size() <= MAX_CONCURRENTREQUEST) {
                    sendNext();
                }
            }
        }
    }

    private void proceed() {
        if ((!pending.isEmpty()) && (sent.size() <= MAX_CONCURRENTREQUEST)) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                @Override
                public void execute() {
                    sendNext();
                }
            });
        }
    }

    public int getNbUnfinishedRequest() {
        return pending.size() + sent.size() - nbPendingMilestone;
    }
}
