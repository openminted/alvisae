/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import com.google.gwt.i18n.client.Messages;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicFaultListener;

/**
 *
 * @author fpapazian
 */
public interface ClientFaultMessages extends Messages, BasicFaultListener.FaultMessages<String> {

    @Override
    @Messages.DefaultMessage("Invalid argument type \"{0}\" for role \"{2}\" in Annotation #{1} ")
    public String invalidArgumentType(String annotationType, String id, String role);

    @Override
    @Messages.DefaultMessage("Invalid component type \"{0}\" of Annotation #{1}")
    public String invalidComponentType(String annotationType, String id);

    @Override
    @Messages.DefaultMessage("Invalid fragment boundaries [{0}..{1}] of Annotation #{2} of type \"{3}\"")
    public String invalidFragmentBoundaries(int start, int end, String id, String annotationType);

    @Override
    @Messages.DefaultMessage("Invalid number of components ({0}) for Annotation {1} (expect [{2}..{3}])")
    public String invalidNumberOfComponents(int size, String id, int minComponents, int maxComponents);

    @Override
    @Messages.DefaultMessage("Invalid number of fragments ({0}) for Annotation {1} (expect [{2}..{3}])")
    public String invalidNumberOfFragments(int size, String id, int minFragments, int maxFragments);

    @Override
    @Messages.DefaultMessage("Invalid number of values for property \"{0}\" of Annotation {1} (expect [{2}..{3}])")
    public String invalidNumberOfPropertyValues(String key, String id, int minValues, int maxValues);

    @Override
    @Messages.DefaultMessage("Invalid value \"{0}\" for property \"{1}\" of Annotation #{2}")
    public String invalidPropertyValue(String value, String key, String id);

    @Override
    @Messages.DefaultMessage("Missing argument \"{0}\" for Annotation #{1}")
    public String missingArgument(String role, String id);

    @Override
    @Messages.DefaultMessage("Missing mandatory property \"{0}\" for Annotation #{1}")
    public String missingMandatoryProperty(String key, String id);

    @Override
    @Messages.DefaultMessage("Unsupported annotation type \"{0}\" for Annotation #{1} of kind \"{2}\"")
    public String unsupportedAnnotationType(String annotationType, String id, String kind);

    @Override
    @Messages.DefaultMessage("Unsupported property key \"{0}\" for Annotation #{1}")
    public String unsupportedPropertyKey(String key, String id);

    @Override
    @Messages.DefaultMessage("Unsupported role \"{0}\" for Annotation #{1}")
    public String unsupportedRole(String role, String id);

    @Override
    @Messages.DefaultMessage("Wrong annotation kind \"{0}\" for Annotation #{1}")
    public String wrongAnnotationKind(String kind, String id);

    @Override
    @Messages.DefaultMessage("conflicting Text binding with Annotation #{1} of type \"{0}\"")
    public String conflictingTextBinding(String type, String id);
}
