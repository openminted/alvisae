/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;

/**
 *
 * @author fpapazian
 */
public class CDXWS_Workflow_ResponseImpl extends JavaScriptObject  {

    /**
     * @throws IllegalArgumentException if invalid or not expected JSON 
     */
    public final static CDXWS_Workflow_ResponseImpl createFromJSON(String jsonStr) {
        CDXWS_Workflow_ResponseImpl result = JsonUtils.safeEval(jsonStr).cast();
        return result;
    }
    
    protected CDXWS_Workflow_ResponseImpl() {
        
    }
    
    public final native TaskDefinitionListImpl getTaskDefinitions() /*-{ return this.taskdefinitions; }-*/;

    public final native AnnotationSchemaDefinition getAnnotationSchema() /*-{ return this.schema; }-*/;
}
