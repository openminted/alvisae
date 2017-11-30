/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.view.client.ProvidesKey;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskVisibility;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class TaskDefinitionImpl extends JavaScriptObject implements TaskDefinition {

    public static final ProvidesKey<TaskDefinitionImpl> KEY_PROVIDER = new ProvidesKey<TaskDefinitionImpl>() {

        @Override
        public Object getKey(TaskDefinitionImpl item) {
            return item == null ? null : item.getId();
        }
    };    
    protected TaskDefinitionImpl() {
    }
    
    @Override
    public final native int getId() /*-{ return this.id; }-*/;
    
    @Override
    public final native String getName() /*-{ return this.name; }-*/;
    
    @Override
    public final native int getCardinality() /*-{ return this.cardinality; }-*/;

    public final native String _getVisibility() /*-{ return this.visibility; }-*/;
    
    @Override
    public final TaskVisibility getVisibility() {
        return TaskVisibility.withName(_getVisibility());
    }

    private final native String _getEditedAnnotationTypes() /*-{ if (this.hasOwnProperty('edittypes')) { return this.edittypes; } else { return "" } }-*/;

    @Override
    public final Set<String> getEditedAnnotationTypes() {
        HashSet<String> result = new HashSet<String>();
        for (String annotationType : _getEditedAnnotationTypes().split(",")) {
            String annType = annotationType.trim();
            if (!annType.isEmpty()) {
                result.add(annType);
            }
        }
        return result;
    }

    @Override
    public final native int getPrecedencelevel() /*-{ return this.precedencelevel; }-*/;

    private final native JsArray<TaskPrecedencyImpl> _getTaskPrecedencies() /*-{ return this.precedencies; }-*/;
            
    @Override
    public final List<? extends TaskPrecedencyImpl> getTaskPrecedencies() {
        return new JsArrayDecorator<TaskPrecedencyImpl>(_getTaskPrecedencies());
    }
    
    @Override
    public final Integer getReviewedTask() {
        Integer result = null;
        for (TaskPrecedencyImpl dep : getTaskPrecedencies()) {
            if (dep.isReviewing()) {
                result = dep.getPrecedingTaskId();
                break;
            }
        }
        return result;
    }

    @Override
    public final Set<Integer> getSucceededTasks() {
        HashSet<Integer> result = new HashSet<Integer>();
        for (TaskPrecedencyImpl dep : getTaskPrecedencies()) {
            if (dep.isSucceeding()) {
                result.add(dep.getPrecedingTaskId());
            }
        }
        return result;
    }

    @Override
    public final Set<Integer> getReferencedAnnotationTypeTasks() {
        HashSet<Integer> result = new HashSet<Integer>();
        for (TaskPrecedencyImpl dep : getTaskPrecedencies()) {
            if (dep.isTypeReferencing()) {
                result.add(dep.getPrecedingTaskId());
            }
        }
        return result;
    }
    
}
