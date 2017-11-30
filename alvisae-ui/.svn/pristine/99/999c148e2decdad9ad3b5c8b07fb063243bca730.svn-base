/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class BasicAnnotationSchemaValidator extends AnnotationSchema {

    public static void validateAnnotatedText(AnnotatedText annotatedText, FaultListener faultLstnr) {
        BasicAnnotationSchemaValidator validator = new BasicAnnotationSchemaValidator();
        validator.setAnnotatedText(annotatedText);
        validator.validate(faultLstnr, annotatedText, false);
    }
    
    private AnnotatedText annotatedText;

    public void setAnnotatedText(AnnotatedText annotatedText) {
        this.annotatedText = annotatedText;
    }

    @Override
    protected AnnotationTypeDefinition getAnnotationTypeDefinition(String type) {
        return annotatedText.getAnnotationSchema().getAnnotationTypeDefinition(type);
    }

    @Override
    public boolean validateProperty(FaultListener l, PropertyDefinition propDef, Annotation a, boolean fastFail) {
        return super.validateProperty(l, propDef, a, fastFail);
    }

    @Override
    public boolean validateAnnotation(FaultListener l, AnnotationTypeDefinition typeDef, Annotation a, boolean fastFail) {
        return super.validateAnnotation(l, typeDef, a, fastFail);
    }

    public boolean checkBoundaries(FaultListener l, Annotation annotation, boolean fastFail) {
        boolean result = true;
        if (AnnotationKind.TEXT.equals(annotation.getAnnotationKind())) {
            AnnotationTypeDefinition annTypeDef = getAnnotationTypeDefinition(annotation.getAnnotationType());
            boolean crossingAllowed = annTypeDef.getTextBindingDefinition().isCrossingAllowed();
            //FIXME use actual AnnotationSet, once it is implemented....
            String annotationSet = "";

            List<Fragment> refFragments = annotation.getTextBinding().getSortedFragments();

            for (Annotation otherAnnotation : annotatedText.getAnnotations(AnnotationKind.TEXT)) {
                if (otherAnnotation.getId().equals(annotation.getId())) {
                    continue;
                }

                //FIXME use actual AnnotationSet, once it is implemented....
                String otherAnnotationSet = "";

                //check boundary crossing within the same Annotation Set
                if (!annotationSet.equals(otherAnnotationSet)) {
                    continue;
                }

                AnnotationTypeDefinition otherAnnTypeDef = getAnnotationTypeDefinition(otherAnnotation.getAnnotationType());
                //no check necessary if both Annotation Types allow boundary crossing
                if (otherAnnTypeDef==null || (crossingAllowed && otherAnnTypeDef.getTextBindingDefinition().isCrossingAllowed())) {
                    continue;
                }

                for (Fragment refFrag : refFragments) {
                    for (Fragment otherFrag : otherAnnotation.getTextBinding().getSortedFragments()) {

                        if (((otherFrag.getStart() > refFrag.getStart())
                                && (otherFrag.getStart() < refFrag.getEnd())
                                && (otherFrag.getEnd() > refFrag.getEnd()))
                                || ((otherFrag.getStart() < refFrag.getStart())
                                && (otherFrag.getEnd() > refFrag.getStart())
                                && (otherFrag.getEnd() < refFrag.getEnd()))) {
                            l.conflictingTextBinding(otherAnnTypeDef, otherAnnotation);
                            if (fastFail) {
                                result = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
