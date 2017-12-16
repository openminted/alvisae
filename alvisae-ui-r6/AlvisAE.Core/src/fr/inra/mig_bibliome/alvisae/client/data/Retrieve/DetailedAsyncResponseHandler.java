/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data.Retrieve;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import fr.inra.mig_bibliome.alvisae.client.data.DetailedAsyncCallback;

/**
 *
 * @author fpapazian
 */
public abstract class DetailedAsyncResponseHandler<T> implements DetailedAsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        //throw new RuntimeException(caught.getMessage());
        GWT.log("AsyncResponseHandler.onFailure" + caught.getMessage());
    }

    @Override
    public abstract void onFailure(Response response);
    
    @Override
    public abstract void onSuccess(T result);
}
