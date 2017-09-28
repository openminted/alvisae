/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.server.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.validation.FaultListener.FaultMessages;
import java.text.MessageFormat;

/**
 *
 * @author fpapazian
 */
public class ServerFaultMessages implements FaultMessages<String> {

    //
    private static final MessageFormat InvalidArgumentTypeMsg = new MessageFormat("Invalid argument type \"{0}\" for role \"{2}\" in annotation #{1} ");

    @Override
    public String invalidArgumentType(String annotationType, String id, String role) {
        Object[] args = {annotationType, id, role};
        return InvalidArgumentTypeMsg.format(args);
    }
    //
    private static final MessageFormat InvalidComponentTypeMsg = new MessageFormat("Invalid component type \"{0}\" of Annotation #{1}");

    @Override
    public String invalidComponentType(String annotationType, String id) {
        Object[] args = {annotationType, id};
        return InvalidComponentTypeMsg.format(args);
    }
    //
    private static final MessageFormat InvalidFragmentBoundariesMsg = new MessageFormat("Invalid fragment boundaries [{0}..{1}] of Annotation #{2} of type \"{3}\"");

    @Override
    public String invalidFragmentBoundaries(int start, int end, String id, String annotationType) {
        Object[] args = {start, end, id, annotationType};
        return InvalidFragmentBoundariesMsg.format(args);
    }
    //
    private static final MessageFormat InvalidNumberOfComponentsMsg = new MessageFormat("Invalid number of components ({0}) for Annotation {1} (expect [{2}..{3}])");

    @Override
    public String invalidNumberOfComponents(int size, String id, int minComponents, int maxComponents) {
        Object[] args = {size, id, minComponents, maxComponents};
        return InvalidNumberOfComponentsMsg.format(args);
    }
    //
    private static final MessageFormat InvalidNumberOfFragmentsMsg = new MessageFormat("Invalid number of fragments ({0}) for Annotation {1} (expect [{2}..{3}])");

    @Override
    public String invalidNumberOfFragments(int size, String id, int minFragments, int maxFragments) {
        Object[] args = {size, id, minFragments, maxFragments};
        return InvalidNumberOfFragmentsMsg.format(args);
    }
    //
    private static final MessageFormat InvalidNumberOfPropertyValuesMsg = new MessageFormat("Invalid number of values for property \"{0}\" of Annotation {1} (expect [{2}..{3}])");

    @Override
    public String invalidNumberOfPropertyValues(String key, String id, int minValues, int maxValues) {
        Object[] args = {key, id, minValues, maxValues};
        return InvalidNumberOfPropertyValuesMsg.format(args);
    }
    //
    private static final MessageFormat InvalidPropertyValueMsg = new MessageFormat("Invalid value \"{0}\" for property \"{1}\" of Annotation #{2}");

    @Override
    public String invalidPropertyValue(String value, String key, String id) {
        Object[] args = {value, key, id};
        return InvalidPropertyValueMsg.format(args);
    }
    //
    private static final MessageFormat MissingArgumentMsg = new MessageFormat("Missing argument \"{0}\" for Annotation #{1}");

    @Override
    public String missingArgument(String role, String id) {
        Object[] args = {role, id};
        return MissingArgumentMsg.format(args);
    }
    //
    private static final MessageFormat MissingMandatoryPropertyMsg = new MessageFormat("Missing mandatory property \"{0}\" for Annotation #{1}");

    @Override
    public String missingMandatoryProperty(String key, String id) {
        Object[] args = {key, id};
        return MissingMandatoryPropertyMsg.format(args);
    }
    //
    private static final MessageFormat UnsupportedAnnotationTypeMsg = new MessageFormat("Unsupported annotation type \"{0}\" for Annotation #{1} of kind \"{2}\"");

    @Override
    public String unsupportedAnnotationType(String annotationType, String id, String kind) {
        Object[] args = {annotationType, id, kind};
        return UnsupportedAnnotationTypeMsg.format(args);
    }
    //
    private static final MessageFormat UnsupportedPropertyKeyMsg = new MessageFormat("Unsupported property key \"{0}\" for Annotation #{1}");

    @Override
    public String unsupportedPropertyKey(String key, String id) {
        Object[] args = {key, id};
        return UnsupportedPropertyKeyMsg.format(args);
    }
    //
    private static final MessageFormat UnsupportedRoleMsg = new MessageFormat("Unsupported role \"{0}\" for Annotation #{1}");

    @Override
    public String unsupportedRole(String role, String id) {
        Object[] args = {role, id};
        return UnsupportedRoleMsg.format(args);
    }
    //
    private static final MessageFormat WrongAnnotationKindMsg = new MessageFormat("Wrong annotation kind \"{0}\" for Annotation #{1}");

    @Override
    public String wrongAnnotationKind(String kind, String id) {
        Object[] args = {kind, id};
        return WrongAnnotationKindMsg.format(args);
    }
    //
    private static final MessageFormat ConflictingTextBindingMsg = new MessageFormat("conflicting Text binding with Annotation #{1} of type \"{0}\"");

    @Override
    public String conflictingTextBinding(String type, String id) {
        Object[] args = {type, id};
        return ConflictingTextBindingMsg.format(args);
    }
    //
}
