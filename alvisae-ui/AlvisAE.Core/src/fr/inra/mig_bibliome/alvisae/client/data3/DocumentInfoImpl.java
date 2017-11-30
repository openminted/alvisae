/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.view.client.ProvidesKey;
import fr.inra.mig_bibliome.alvisae.shared.data3.DocumentInfo;

/**
 *
 * @author fpapazian
 */
public class DocumentInfoImpl extends JavaScriptObject implements DocumentInfo {

    public static final ProvidesKey<DocumentInfoImpl> KEY_PROVIDER = new ProvidesKey<DocumentInfoImpl>() {

        @Override
        public Object getKey(DocumentInfoImpl item) {
            return item == null ? null : item.getId();
        }
    };
    
    protected DocumentInfoImpl() {
    }

    @Override
    public final native int getId() /*-{ return this.id; }-*/;

    @Override
    public final native String getDescription() /*-{ return this.description; }-*/;

}
