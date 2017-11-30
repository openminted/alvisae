/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/*
 * The actual definition of an Annotation Schema
 */
public interface AnnotationSchemaDefinition {

    /**
     *
     * @return the names of all the Annotation types defined in the AnnotationSchema
     */
    public Collection<String> getAnnotationTypes();

    /**
     *
     * @param kind
     * @return the names of all the types corresponding to the specified AnnotationKind
     */
    public List<String> getAnnotationTypes(AnnotationKind kind);

    /**
     *
     * @param type
     * @return the AnnotationTypeDefinition corresponding to the specified type name
     */
    public AnnotationTypeDefinition getAnnotationTypeDefinition(String type);

    /**
     *
     * @param kind
     * @return the AnnotationTypeDefinition of all the types corresponding to the specified AnnotationKind
     */
    public List<AnnotationTypeDefinition> getAnnotationTypeDefinition(AnnotationKind kind);

    static class TypeUrlEntry {

        private final String typeName;
        private final String url;

        public TypeUrlEntry(String typeName, String url) {
            this.typeName = typeName;
            this.url = url;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getUrl() {
            return url;
        }
    }

    /**
     * 
     * @return a Map of all annotationTypes (keys) that allow to reference external TyDI resource such as Term or Semantic classes (in the inner map: k->property name, V-> base url) )
     */
    public Map<String, Map<String, TypeUrlEntry>> getTyDIResourceReferencingTypes();
}
