/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationGroupDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertiesDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.RelationDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.TextBindingDefinition;
import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;

/**
 *
 * @author fpapazian
 */
public class AnnotationTypeDefinitionImpl extends JavaScriptObject implements AnnotationTypeDefinition {

    protected AnnotationTypeDefinitionImpl() {
    }

    @Override
    public final native String getType() /*-{ return this.type; }-*/;

    @Override
    public final AnnotationKind getAnnotationKind() {
        return AnnotationKind.values()[_getKind()];
    }

    private final native int _getKind() /*-{ return this.kind; }-*/;

    @Override
    public final native String getColor() /*-{ return this.color; }-*/;

    @Override
    public final native String getUrl() /*-{ if (this.hasOwnProperty('url')) { return this['url']; } else { return null; } }-*/;
    
    private final native PropertiesDefinition _getPropertiesDefinition() /*-{ return this.propDef; }-*/;

    @Override
    public final PropertiesDefinition getPropertiesDefinition() {
        PropertiesDefinition propsDef = _getPropertiesDefinition();
        if (propsDef==null) {
            PropertiesDefinitionImpl emptyPropsDef = JavaScriptObject.createObject().cast();
            propsDef = emptyPropsDef;
        }
        return propsDef;
    }

    @Override
    public final native TextBindingDefinition getTextBindingDefinition() /*-{ return this.txtBindingDef; }-*/;

    @Override
    public final native AnnotationGroupDefinition getAnnotationGroupDefinition() /*-{ return this.groupDef; }-*/;

    @Override
    public final native RelationDefinition getRelationDefinition() /*-{ return this.relationDef; }-*/;
}
