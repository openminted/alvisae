package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import java.util.List;

public interface FaultListener {

    /**
     * A relation annotation argument has not the expected type.
     */
    void invalidArgumentType(RelationDefinition relDef, Annotation a, String role);

    /**
     * A group annotation component has not the expected type.
     */
    void invalidComponentType(AnnotationGroupDefinition groupDef, Annotation a);

    /**
     * A text annotation fragment has not the expected boundaries.
     */
    void invalidFragmentBoundaries(TextBindingDefinition textDef, Annotation a, Fragment frag);

    /**
     * A group annotation has too few or too many components.
     */
    void invalidNumberOfComponents(AnnotationGroupDefinition groupDef, Annotation a);

    /**
     * A text annotation has too few or too many fragments.
     */
    void invalidNumberOfFragments(TextBindingDefinition textDef, Annotation a);

    /**
     * A property has too few or too many values.
     */
    void invalidNumberOfPropertyValues(PropertyDefinition propDef, Annotation a, String key);

    /**
     * A property value is not in its defined domain.
     */
    void invalidPropertyValue(PropertyDefinition propDef, Annotation a, String key, String value);

    /**
     * A relation annotation is missing an argument.
     */
    void missingArgument(RelationDefinition relDef, Annotation a, String role);

    /**
     * A mandatory property is not defined.
     */
    void missingMandatoryProperty(PropertyDefinition propDef, Annotation a, String key);

    /**
     * An annotation has an undefined type.
     */
    void unsupportedAnnotationType(Annotation a);

    /**
     * An annotation property has an undefined key.
     */
    void unsupportedPropertyKey(PropertiesDefinition propsDef, Annotation a, String key);

    /**
     * A relation annotation has an undefined role.
     */
    void unsupportedRole(RelationDefinition relDef, Annotation a, String role);

    /**
     * An annotation has an unexpected kind.
     */
    void wrongAnnotationKind(AnnotationTypeDefinition typeDef, Annotation a);

    /**
     * the Text Binding of an Annotation is
     */
    void conflictingTextBinding(AnnotationTypeDefinition typeDef, Annotation a);
}
