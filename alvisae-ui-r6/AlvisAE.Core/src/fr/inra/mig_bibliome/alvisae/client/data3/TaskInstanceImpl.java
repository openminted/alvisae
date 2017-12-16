/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.view.client.ProvidesKey;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskInstance;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskStatus;

/**
 *
 * @author fpapazian
 */
public class TaskInstanceImpl extends JavaScriptObject implements TaskInstance {
    
    public static final ProvidesKey<TaskInstanceImpl> KEY_PROVIDER = new ProvidesKey<TaskInstanceImpl>() {

        @Override
        public Object getKey(TaskInstanceImpl item) {
            return item == null ? null : item.getId() + "-" + item.getDocumentId();
        }
    };  
    
    protected TaskInstanceImpl() {
    }

    @Override
    public final native int getId() /*-{ return this.task_id; }-*/;
    
    @Override
    public final native int getDocumentId() /*-{ return this.doc_id; }-*/;

    @Override
    public final native int getUserId() /*-{ return this.user_id; }-*/;
    
    private final native String _getStatus() /*-{ return this.status; }-*/;
          
    @Override
    public final TaskStatus getStatus() {
        return TaskStatus.withName(_getStatus());
    }
    
    private final native Object _getProperty(String propName) /*-{ if (this.hasOwnProperty(propName)) { return this[propName]; } else { return null; } }-*/;
    
    @Override
    public final Integer getAnnotationSetId() {
        return (Integer) _getProperty("annset_id");
    }

    @Override
    public final String getCreationDate() {
        return (String) _getProperty("created");
    }

    @Override
    public final String getPublicationDate() {
        return (String) _getProperty("published");
    }

    private final native boolean _getBoolPropOrElse(String propName, boolean defaultVal) /*-{ if (this.hasOwnProperty(propName)) { return this[propName]; } else { return defaultVal; } }-*/;

    @Override
    public final Boolean isReadOnly() {
        return _getBoolPropOrElse("readonly", false);
    }
    
    @Override
    public final Boolean isInvalidated() {
        return _getBoolPropOrElse("invalidated", false);
    }
}
