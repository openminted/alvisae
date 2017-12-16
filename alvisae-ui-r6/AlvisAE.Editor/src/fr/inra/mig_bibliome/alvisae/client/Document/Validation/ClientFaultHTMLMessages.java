/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document.Validation;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicFaultListener;

/**
 *
 * @author fpapazian
 */
public interface ClientFaultHTMLMessages extends Messages, BasicFaultListener.FaultMessages<SafeHtml> {

    public static final String ANNIDREF_CLASSVAL = "aae_refannid";

    @Override
    @Messages.DefaultMessage("Invalid argument type <b>{0}</b> for role <b>{2}</b> in Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span> ")
    public SafeHtml invalidArgumentType(String annotationType, String id, String role);

    @Override
    @Messages.DefaultMessage("Invalid component type <b>{0}</b> of Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span>")
    public SafeHtml invalidComponentType(String annotationType, String id);

    @Override
    @Messages.DefaultMessage("Invalid fragment boundaries [{0}..{1}] of Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{2}</span> of type <b>{3}</b>")
    public SafeHtml invalidFragmentBoundaries(int start, int end, String id, String annotationType);

    @Override
    @Messages.DefaultMessage("Invalid number of components ({0}) for Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span> (expect [{2}..{3}])")
    public SafeHtml invalidNumberOfComponents(int size, String id, int minComponents, int maxComponents);

    @Override
    @Messages.DefaultMessage("Invalid number of fragments ({0}) for Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span> (expect [{2}..{3}])")
    public SafeHtml invalidNumberOfFragments(int size, String id, int minFragments, int maxFragments);

    @Override
    @Messages.DefaultMessage("Invalid number of values for property <b>{0}</b> of Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span> (expect [{2}..{3}])")
    public SafeHtml invalidNumberOfPropertyValues(String key, String id, int minValues, int maxValues);

    @Override
    @Messages.DefaultMessage("Invalid value <b>{0}</b> for property <b>{1}</b> of Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{2}</span>")
    public SafeHtml invalidPropertyValue(String value, String key, String id);

    @Override
    @Messages.DefaultMessage("Missing argument <b>{0}</b> for Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span>")
    public SafeHtml missingArgument(String role, String id);

    @Override
    @Messages.DefaultMessage("Missing mandatory property <b>{0}</b> for Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span>")
    public SafeHtml missingMandatoryProperty(String key, String id);

    @Override
    @Messages.DefaultMessage("Unsupported annotation type <b>{0}</b> for Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span> of kind <b>{2}</b>")
    public SafeHtml unsupportedAnnotationType(String annotationType, String id, String kind);

    @Override
    @Messages.DefaultMessage("Unsupported property key <b>{0}</b> for Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span>")
    public SafeHtml unsupportedPropertyKey(String key, String id);

    @Override
    @Messages.DefaultMessage("Unsupported role <b>{0}</b> for Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span>")
    public SafeHtml unsupportedRole(String role, String id);

    @Override
    @Messages.DefaultMessage("Wrong annotation kind <b>{0}</b> for Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span>")
    public SafeHtml wrongAnnotationKind(String kind, String id);

    @Override
    @Messages.DefaultMessage("conflicting Text binding with Annotation <span style='color:blue;text-decoration:underline;' class='" + ANNIDREF_CLASSVAL + "'>{1}</span> of type <b>{0}</b>")
    public SafeHtml conflictingTextBinding(String type, String id);
}
