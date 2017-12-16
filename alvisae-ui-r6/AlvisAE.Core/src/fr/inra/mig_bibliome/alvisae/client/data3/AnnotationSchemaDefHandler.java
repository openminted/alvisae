/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.client.data3.Extension.ResourceLocator;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition.TypeUrlEntry;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDIConceptRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDISemClassRef;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDITermRef;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class AnnotationSchemaDefHandler {

    public static List<String> getEditableAnnotationTypes(AnnotationSchemaDefinition schema, AnnotationKind kind, TaskDefinition taskDef) {
        Set<String> editedTypes = new LinkedHashSet<String>();
        if (taskDef != null) {
            editedTypes.addAll(taskDef.getEditedAnnotationTypes());
        }
        editedTypes.retainAll(schema.getAnnotationTypes(kind));
        return new ArrayList<String>(editedTypes);
    }
    
    private final AnnotationSchemaDefinition schemaDefinition;
    private Map<String, Map<String, TypeUrlEntry>> tyDIResReferencingTypes = null;

    public AnnotationSchemaDefHandler(AnnotationSchemaDefinition schemaDefinition) {
        this.schemaDefinition = schemaDefinition;
    }

    public List<String> getEditableAnnotationTypes(AnnotationKind kind, TaskDefinition taskDef) {
        return getEditableAnnotationTypes(getSchemaDefinition(), kind, taskDef);
    }    
    
    private void initTyDIResReferencingTypes() {
        tyDIResReferencingTypes = schemaDefinition.getTyDIResourceReferencingTypes();
    }

    public AnnotationSchemaDefinition getSchemaDefinition() {
        return schemaDefinition;
    }

    /**
     *
     * @return true if this Schema allows some Annotation to reference external
     * TyDI resource such as Term or Semantic classes
     */
    public boolean enableTyDIResourceRef() {
        if (tyDIResReferencingTypes == null) {
            initTyDIResReferencingTypes();
        }
        return !tyDIResReferencingTypes.isEmpty();
    }

    /**
     *
     * @return true if the specified annotationType allows to reference external
     * TyDI resource such as Term or Semantic classes
     */
    public boolean isTyDIResReferencingType(String annotationType) {
        if (tyDIResReferencingTypes == null) {
            initTyDIResReferencingTypes();
        }
        return tyDIResReferencingTypes.keySet().contains(annotationType);
    }
    private Map<String, Map<String, List<String>>> TyDIRefPropNamesByAnnotationType = new HashMap<String, Map<String, List<String>>>();

    private List<String> getTyDIRefPropNames(String propTypeName, String annotationType) {
        if (tyDIResReferencingTypes == null) {
            initTyDIResReferencingTypes();
        }
        if (!TyDIRefPropNamesByAnnotationType.containsKey(annotationType)) {
            TyDIRefPropNamesByAnnotationType.put(annotationType, new HashMap<String, List<String>>());
        }
        Map<String, List<String>> propNamesBypropType = TyDIRefPropNamesByAnnotationType.get(annotationType);
        if (!propNamesBypropType.containsKey(propTypeName)) {
            List<String> result = new ArrayList<String>();
            Map<String, TypeUrlEntry> forType = tyDIResReferencingTypes.get(annotationType);
            if (forType != null) {
                for (Entry<String, TypeUrlEntry> e : forType.entrySet()) {
                    if (propTypeName.equals(e.getValue().getTypeName())) {
                        result.add(e.getKey());
                    }
                }
            }
            propNamesBypropType.put(propTypeName, result);
        }
        return TyDIRefPropNamesByAnnotationType.get(annotationType).get(propTypeName);
    }

    //NOTE: an AnnotationType MUST contains ONLY one property of type PropType_TyDITermRef
    public String getTyDITermRefPropName(String annotationType) {
        List<String> propNames = getTyDIRefPropNames(PropType_TyDITermRef.NAME, annotationType);
        return propNames.isEmpty() ? null : propNames.get(0);
    }

    //NOTE: an AnnotationType MUST contains ONLY one property of type PropType_TyDISemClassRef
    public String getTyDISemClassRefPropName(String annotationType) {
        List<String> propNames = getTyDIRefPropNames(PropType_TyDISemClassRef.NAME, annotationType);
        return propNames.isEmpty() ? null : propNames.get(0);
    }

    public List<String> getTyDIConceptRefPropNames(String annotationType) {
        List<String> propNames = getTyDIRefPropNames(PropType_TyDIConceptRef.NAME, annotationType);
        return propNames;
    }

    /**
     *
     * @return the set of the distinct base URL pointing to external TyDI
     * resource
     */
    public HashSet<String> getTyDIResourceBaseURLs() {
        HashSet<String> result = new HashSet<String>();
        for (Map<String, TypeUrlEntry> forType : tyDIResReferencingTypes.values()) {
            for (TypeUrlEntry e : forType.values()) {
                result.add(ResourceLocator.cleanUrl(e.getUrl()));
            }
        }
        return result;
    }
}
