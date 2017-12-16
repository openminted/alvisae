/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;


import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fpapazian
 */
public class BasicFaultListener<T> implements FaultListener {

    private final FaultMessages<T> faultMessages;
    private List<T> msgs = new ArrayList<T>();
    private Map<Integer, Annotation> conflictingAnnotation = new HashMap<Integer, Annotation>();

    public BasicFaultListener(FaultMessages<T> faultMessages) {
        this.faultMessages = faultMessages;
    }
    
    public void reset() {
        msgs.clear();
        conflictingAnnotation.clear();
    }

    /**
     * 
     * @return Messages produced during validation
     */
    public List<T> getMessages() {
        return msgs;
    }

    public T getLastMessage() {
        return !msgs.isEmpty() ? msgs.get(msgs.size() - 1) : null;
    }

    public Annotation getConflictingAnnotation(int index) {
        return conflictingAnnotation.get(index);
    }

    @Override
    public void invalidArgumentType(RelationDefinition relDef, Annotation a, String role) {
        AnnotatedText annotatedText = a.getAnnotatedText();
        Annotation annotation = annotatedText.getAnnotation(a.getRelation().getArgumentRef(role).getAnnotationId());
        msgs.add(faultMessages.invalidArgumentType(annotation.getAnnotationType(), a.getId(), role));
    }

    @Override
    public void invalidComponentType(AnnotationGroupDefinition groupDef, Annotation a) {
        msgs.add(faultMessages.invalidComponentType(a.getAnnotationType(), a.getId()));
    }

    @Override
    public void invalidFragmentBoundaries(TextBindingDefinition textDef, Annotation a, Fragment frag) {
        msgs.add(faultMessages.invalidFragmentBoundaries(frag.getStart(), frag.getEnd(), a.getId(), a.getAnnotationType()));
    }

    @Override
    public void invalidNumberOfComponents(AnnotationGroupDefinition groupDef, Annotation a) {
        msgs.add(faultMessages.invalidNumberOfComponents(a.getAnnotationGroup().getComponentRefs().size(), a.getId(), groupDef.getMinComponents(), groupDef.getMaxComponents()));
    }

    @Override
    public void invalidNumberOfFragments(TextBindingDefinition textDef, Annotation a) {
        msgs.add(faultMessages.invalidNumberOfFragments(a.getTextBinding().getFragments().size(), a.getId(), textDef.getMinFragments(), textDef.getMaxFragments()));
    }

    @Override
    public void invalidNumberOfPropertyValues(PropertyDefinition propDef, Annotation a, String key) {
        msgs.add(faultMessages.invalidNumberOfPropertyValues(key, a.getId(), propDef.getMinValues(), propDef.getMaxValues()));
    }

    @Override
    public void invalidPropertyValue(PropertyDefinition propDef, Annotation a, String key, String value) {
        msgs.add(faultMessages.invalidPropertyValue(value, key, a.getId()));
    }

    @Override
    public void missingArgument(RelationDefinition relDef, Annotation a, String role) {
        msgs.add(faultMessages.missingArgument(role, a.getId()));
    }

    @Override
    public void missingMandatoryProperty(PropertyDefinition propDef, Annotation a, String key) {
        msgs.add(faultMessages.missingMandatoryProperty(key, a.getId()));
    }

    @Override
    public void unsupportedAnnotationType(Annotation a) {
        msgs.add(faultMessages.unsupportedAnnotationType(a.getAnnotationType(), a.getId(), a.getAnnotationKind().toString()));
    }

    @Override
    public void unsupportedPropertyKey(PropertiesDefinition propsDef, Annotation a, String key) {
        //msgs.add(faultMessages.unsupportedPropertyKey(key, a.getId()));
    }

    @Override
    public void unsupportedRole(RelationDefinition relDef, Annotation a, String role) {
        msgs.add(faultMessages.unsupportedRole(role, a.getId()));
    }

    @Override
    public void wrongAnnotationKind(AnnotationTypeDefinition typeDef, Annotation a) {
        msgs.add(faultMessages.wrongAnnotationKind(a.getAnnotationKind().toString(), a.getId()));
    }

    @Override
    public void conflictingTextBinding(AnnotationTypeDefinition typeDef, Annotation a) {
        conflictingAnnotation.put(msgs.size(), a);
        msgs.add(faultMessages.conflictingTextBinding(a.getAnnotationType(), a.getId()));
    }
}
