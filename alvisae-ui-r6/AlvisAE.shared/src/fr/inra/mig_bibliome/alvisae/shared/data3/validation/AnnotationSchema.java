package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSet;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetType;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import fr.inra.mig_bibliome.alvisae.shared.data3.Relation;
import fr.inra.mig_bibliome.alvisae.shared.data3.TextBinding;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Annotation schema.
 * @author rbossy
 *
 */
public abstract class AnnotationSchema {

    /**
     * Return the expected types of components.
     * Return groupDef.getComponentsTypes() if the specified annotation is empty and not homogenous.
     * Otherwise return a singleton containing the type of one of the components.
     * @param groupDef
     * @param components
     * @return
     */
    public static Collection<String> getExpectedTypes(AnnotationGroupDefinition groupDef, Collection<Annotation> components) {
        if (groupDef.isHomogeneous() && !components.isEmpty()) {
            return Collections.singleton(components.iterator().next().getAnnotationType());
        }
        return groupDef.getComponentsTypes();
    }

    private static void lookupBoundaries(Collection<Annotation> refs, Collection<Integer> starts, Collection<Integer> ends) {
        for (Annotation ref : refs) {
            TextBinding textRef = ref.getTextBinding();
            if (textRef == null) {
                continue;
            }
            for (Fragment refFrag : textRef.getFragments()) {
                starts.add(refFrag.getStart());
                ends.add(refFrag.getEnd());
            }
        }
    }

    /**
     * Return the annotation type definition for the specified type.
     * Return null if there is no definition for the specified type.
     * @param type
     * @return
     */
    protected abstract AnnotationTypeDefinition getAnnotationTypeDefinition(String type);

    /**
     * Validates annotations in the specified annotated text.
     * @param l manager to which faults are sent
     * @param text
     * @param fastFail either to stop at the first detected fault
     * @return true iff no fault was detected
     */
    public boolean validate(final FaultListener l, AnnotatedText text, final boolean fastFail) {
        final Boolean[] result = {true};

        text.scanAnnotations(new AnnotatedText.AnnotationProcessor() {

            @Override
            public boolean process(AnnotationSet annotationSet, Annotation a) {
                if (!AnnotationSetType.HtmlAnnotation.equals(annotationSet.getType())) {

                    AnnotationTypeDefinition typeDef = getAnnotationTypeDefinition(a.getAnnotationType());
                    if (typeDef == null) {
                        l.unsupportedAnnotationType(a);
                        if (fastFail) {
                            result[0] = false;
                            return false;
                        } else {
                            return true;
                        }
                    }
                    if (!validateAnnotation(l, typeDef, a, fastFail)) {
                        if (fastFail) {
                            result[0] = false;
                            return false;
                        } else {
                            return true;
                        }
                    }
                    return true;
                }
                return true;
            }
        });
        return result[0];
    }

    protected boolean validateAnnotation(FaultListener l, AnnotationTypeDefinition typeDef, Annotation a, boolean fastFail) {
        boolean result = validateProperties(l, typeDef.getPropertiesDefinition(), a, fastFail);
        if (fastFail && !result) {
            return false;
        }
        if (typeDef.getAnnotationKind() != a.getAnnotationKind()) {
            l.wrongAnnotationKind(typeDef, a);
            return false;
        }
        switch (typeDef.getAnnotationKind()) {
            case TEXT:
                result = validateTextAnnotation(l, typeDef.getTextBindingDefinition(), a, fastFail) && result;
                break;
            case GROUP:
                result = validateGroupAnnotation(l, typeDef.getAnnotationGroupDefinition(), a, fastFail) && result;
                break;
            case RELATION:
                result = validateRelationAnnotation(l, typeDef.getRelationDefinition(), a, fastFail) && result;
                break;
        }
        return result;
    }

    private boolean validateGroupAnnotation(FaultListener l, AnnotationGroupDefinition groupDef, Annotation a, boolean fastFail) {
        boolean result = true;
        Collection<AnnotationReference> componentRefs = a.getAnnotationGroup().getComponentRefs();
        AnnotatedText annotatedText = a.getAnnotatedText();
        Collection<Annotation> components = new ArrayList<Annotation>();
        for (AnnotationReference c : componentRefs) {
            components.add(annotatedText.getAnnotation(c.getAnnotationId()));
        }
        int n = components.size();
        if ((n < groupDef.getMinComponents()) || (n > groupDef.getMaxComponents())) {
            l.invalidNumberOfComponents(groupDef, a);
            if (fastFail) {
                return false;
            }
            result = false;
        }
        Collection<String> allowedTypes = getExpectedTypes(groupDef, components);
        for (Annotation c : components) {
            if (allowedTypes.contains(c.getAnnotationType())) {
                continue;
            }
            l.invalidComponentType(groupDef, a);
            if (fastFail) {
                return false;
            }
            result = false;
        }
        return result;
    }

    protected boolean validateProperties(FaultListener l, PropertiesDefinition propsDef, Annotation a, boolean fastFail) {
        boolean result = true;
        Properties props = a.getProperties();
        for (String key : props.getKeys()) {
            if (propsDef.isPropertyKeySupported(key)) {
                continue;
            }
            l.unsupportedPropertyKey(propsDef, a, key);
            if (fastFail) {
                return false;
            }
            result = false;
        }
        for (PropertyDefinition propDef : propsDef.getPropertyDefinitions()) {
            if (validateProperty(l, propDef, a, fastFail)) {
                continue;
            }
            if (fastFail) {
                return false;
            }
            result = false;
        }
        return result;
    }

    protected boolean validateProperty(FaultListener l, PropertyDefinition propDef, Annotation a, boolean fastFail) {
        Properties props = a.getProperties();
        String key = propDef.getKey();
        List<String> values = props.getValues(key);
        if (values == null) {
            if (propDef.isMandatory()) {
                l.missingMandatoryProperty(propDef, a, key);
                return false;
            }
            return true;
        }
        boolean result = true;
        int n = values.size();
        if ((n < propDef.getMinValues()) || (n > propDef.getMaxValues())) {
            l.invalidNumberOfPropertyValues(propDef, a, key);
            if (fastFail) {
                return false;
            }
            result = false;
        }
        PropertyType propType = propDef.getValuesType();
        if (propType != null) {
            for (String value : values) {
                if (propType.accept(value)) {
                    continue;
                }
                l.invalidPropertyValue(propDef, a, key, value);
                if (fastFail) {
                    return false;
                }
                result = false;
            }
        }
        return result;
    }

    private boolean validateRelationAnnotation(FaultListener l, RelationDefinition relDef, Annotation a, boolean fastFail) {
        boolean result = true;
        Relation relation = a.getRelation();
        Collection<String> aRoles = relation.getRoles();
        for (String role : relDef.getSupportedRoles()) {
            if (aRoles.contains(role)) {
                continue;
            }
            l.missingArgument(relDef, a, role);
            if (fastFail) {
                return false;
            }
            result = false;
        }
        for (String role : aRoles) {
            Collection<String> argTypes = relDef.getArgumentTypes(role);
            if (argTypes == null || argTypes.isEmpty()) {
                l.unsupportedRole(relDef, a, role);
                if (fastFail) {
                    return false;
                }
                result = false;
            }
            AnnotationReference aRef = relation.getArgumentRef(role);
            Annotation arg = a.getAnnotatedText().getAnnotation(aRef.getAnnotationId());
            if (argTypes == null || argTypes.contains(arg.getAnnotationType())) {
                continue;
            }
            l.invalidArgumentType(relDef, a, role);
            if (fastFail) {
                return false;
            }
            result = false;
        }
        return result;
    }

    private boolean validateTextAnnotation(FaultListener l, TextBindingDefinition textDef, Annotation a, boolean fastFail) {
        boolean result = true;
        Collection<Fragment> fragments = a.getTextBinding().getFragments();
        int n = fragments.size();
        if ((n < textDef.getMinFragments()) || (n > textDef.getMaxFragments())) {
            l.invalidNumberOfFragments(textDef, a);
            if (fastFail) {
                return false;
            }
            result = false;
        }
        String brt = textDef.getBoundariesReferenceType();
        if (brt != null) {
            TreeSet<Integer> starts = new TreeSet<Integer>(); // XXX cache?
            TreeSet<Integer> ends = new TreeSet<Integer>(); // XXX cache?
            lookupBoundaries(a.getAnnotatedText().getAnnotations(brt), starts, ends);
            for (Fragment frag : fragments) {
                if (starts.contains(frag.getStart()) && ends.contains(frag.getEnd())) {
                    continue;
                }
                l.invalidFragmentBoundaries(textDef, a, frag);
                if (fastFail) {
                    return false;
                }
                result = false;
            }
        }
        return result;
    }
}
