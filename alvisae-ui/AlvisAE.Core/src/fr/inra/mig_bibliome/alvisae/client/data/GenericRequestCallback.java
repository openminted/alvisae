/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.shared.data3.Queries.AuthenticationQueries;

/**
 *
 * @author fpapazian
 */
public abstract class GenericRequestCallback<T> implements RequestCallback {

    private final AsyncCallback<T> resultCallback;
    private final EventBus eventBus;
    private final AuthenticationQueries requestManager;

    public GenericRequestCallback(EventBus eventBus, AsyncCallback<T> resultCallback, AuthenticationQueries requestManager) {
        this.eventBus = eventBus;
        this.resultCallback = resultCallback;
        this.requestManager = requestManager;
    }

    public abstract T decode(String responseText);

    @Override
    public void onError(Request request, Throwable exception) {
        if (resultCallback != null) {
            resultCallback.onFailure(exception);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onResponseReceived(Request request, Response response) {
        if (response == null && request == null) {
            //Milestone dummy request
            resultCallback.onSuccess(null);
        } else {
            switch (response.getStatusCode()) {
                case Response.SC_OK:

                    try {
                        T result = decode(response.getText());
                        resultCallback.onSuccess(result);
                    } catch (IllegalArgumentException e) {
                        if (resultCallback instanceof DetailedAsyncCallback) {
                            ((DetailedAsyncCallback) resultCallback).onFailure(response);
                        } else {
                            resultCallback.onFailure(new IllegalArgumentException("Unable to process server response :\n" + response.getText() + "\n" + e.getLocalizedMessage()));
                        }
                    }

                    break;
                case Response.SC_UNAUTHORIZED:

                    if (resultCallback != null) {
                        if (resultCallback instanceof DetailedAsyncCallback) {
                            ((DetailedAsyncCallback) resultCallback).onFailure(response);
                        } else {
                            resultCallback.onFailure(new AuthenticationException("UNAUTHORIZED - This operation requires authentication " + response.getStatusCode() + ":\n" + response.getText()));
                        }
                    }
                    break;
                case Response.SC_BAD_REQUEST:
                    if (resultCallback != null) {

                        if (resultCallback instanceof DetailedAsyncCallback) {
                            ((DetailedAsyncCallback) resultCallback).onFailure(response);
                        } else {
                            resultCallback.onFailure(new IllegalArgumentException("BAD_REQUEST " + response.getStatusCode() + ":\n" + response.getText()));
                        }
                    }
                    break;
                default:

                    if (resultCallback != null) {
                        if (resultCallback instanceof DetailedAsyncCallback) {
                            ((DetailedAsyncCallback) resultCallback).onFailure(response);
                        } else {
                            resultCallback.onFailure(new RuntimeException("Unexpected Error " + response.getStatusCode() + ":\n" + response.getText()));
                        }
                    }
                    break;
            }
            if (eventBus != null) {
                eventBus.fireEvent(new ApplicationStatusChangedEvent(ApplicationStatusChangedEvent.ApplicationStatusSwitching.AsynchResponseArrived, requestManager));
            }
        }
    }
}
