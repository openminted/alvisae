/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDIConceptRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDISemClassRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDITermRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyDefinition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fpapazian
 */
public class AnnotationSchemaDefinitionImpl extends JavaScriptObject implements AnnotationSchemaDefinition {

    protected AnnotationSchemaDefinitionImpl() {
    }

    @Override
    public final Collection<String> getAnnotationTypes() {
        return new JSONObject(this).keySet();
    }

    @Override
    public final List<String> getAnnotationTypes(AnnotationKind kind) {
        List<String> types = new ArrayList<String>();
        for (String type : new JSONObject(this).keySet()) {
            AnnotationTypeDefinition typedef = getAnnotationTypeDefinition(type);
            if (typedef.getAnnotationKind().equals(kind)) {
                types.add(type);
            }
        }
        return types;
    }

    @Override
    public final native AnnotationTypeDefinition getAnnotationTypeDefinition(String type) /*-{ return this[type]; }-*/;

    @Override
    public final List<AnnotationTypeDefinition> getAnnotationTypeDefinition(AnnotationKind kind) {
        List<AnnotationTypeDefinition> types = new ArrayList<AnnotationTypeDefinition>();
        for (String type : new JSONObject(this).keySet()) {
            AnnotationTypeDefinition typedef = getAnnotationTypeDefinition(type);
            if (typedef.getAnnotationKind().equals(kind)) {
                types.add(getAnnotationTypeDefinition(type));
            }
        }
        return types;
    }

    @Override
    public final HashMap<String, Map<String, TypeUrlEntry>> getTyDIResourceReferencingTypes() {
        HashMap<String, Map<String, TypeUrlEntry>> tyDIResReferencingTypes = new HashMap<String, Map<String, TypeUrlEntry>>();

        for (String type : new JSONObject(this).keySet()) {
            AnnotationTypeDefinition typedef = getAnnotationTypeDefinition(type);
            //FIXME only TEXT annotation can reference TyDI resource
            if (AnnotationKind.TEXT.equals(typedef.getAnnotationKind())) {
                HashMap<String, TypeUrlEntry> forType = new HashMap<String, TypeUrlEntry>();
                for (PropertyDefinition propTypeDef : typedef.getPropertiesDefinition().getPropertyDefinitions()) {

                    if (propTypeDef != null && propTypeDef.getValuesType() != null) {
                        String propTypeName = propTypeDef.getKey();
                        if (propTypeName != null) {
                            PropType_TyDISemClassRef tyDISemClassRefType = propTypeDef.getValuesType().getAsTyDISemClassRefType();
                            if (tyDISemClassRefType != null) {
                                forType.put(propTypeName, new TypeUrlEntry(PropType_TyDISemClassRef.NAME, tyDISemClassRefType.getSemClassRefBaseURL()));
                            } else {
                                PropType_TyDITermRef tyDITermRefType = propTypeDef.getValuesType().getAsTyDITermRefType();
                                if (tyDITermRefType != null) {
                                    forType.put(propTypeName, new TypeUrlEntry(PropType_TyDITermRef.NAME, tyDITermRefType.getTermRefBaseURL()));
                                } else {
                                    PropType_TyDIConceptRef tyDIConceptRefType = propTypeDef.getValuesType().getAsTyDIConceptRefType();
                                    if (tyDIConceptRefType != null) {
                                        forType.put(propTypeName, new TypeUrlEntry(PropType_TyDIConceptRef.NAME, tyDIConceptRefType.getSemClassRefBaseURL()));
                                    }
                                }
                            }
                        }
                    }
                }
                if (!forType.isEmpty()) {
                    tyDIResReferencingTypes.put(type, forType);
                }
            }
        }
        return tyDIResReferencingTypes;
    }
}
