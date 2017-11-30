/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskPrecedency;

/**
 *
 * @author fpapazian
 */
public class TaskPrecedencyImpl extends JavaScriptObject implements TaskPrecedency {

    protected TaskPrecedencyImpl() {
    }

    @Override
    public final native int getPrecedingTaskId() /*-{ return this.task_id; }-*/;

    @Override
    public final native  boolean isReviewing() /*-{ return this.reviewing; }-*/;

    @Override
    public final native  boolean isSucceeding() /*-{ return this.succeeding; }-*/;

    @Override
    public final native  boolean isTypeReferencing() /*-{ return this.typereferencing; }-*/;
}
