/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data.Retrieve;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author fpapazian
 */
public abstract class AsyncResponseHandler<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        //throw new RuntimeException(caught.getMessage());
        GWT.log("AsyncResponseHandler.onFailure" + caught.getMessage());
    }

    @Override
    public abstract void onSuccess(T result);
}
