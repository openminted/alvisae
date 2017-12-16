/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.view.client.ProvidesKey;
import fr.inra.mig_bibliome.alvisae.shared.data3.DocumentNAssigned;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class DocumentNAssignedImpl extends DocumentInfoImpl implements DocumentNAssigned {

    public static final ProvidesKey<DocumentNAssignedImpl> KEY_PROVIDER = new ProvidesKey<DocumentNAssignedImpl>() {

        @Override
        public Object getKey(DocumentNAssignedImpl item) {
            return item == null ? null : item.getId();
        }
    };
    
    protected DocumentNAssignedImpl() {
    }

    public final native JsArrayInteger _getAnnotatorIds()  /*-{ return this.user_ids; }-*/;

    @Override
    public final Set<Integer> getAnnotatorIds() {
        HashSet<Integer> result = new HashSet<Integer>();
        JsArrayInteger ids = _getAnnotatorIds();
        for (int row = 0; row < ids.length(); row++) {
            result.add(ids.get(row));
        }
        return result;
    }
}
