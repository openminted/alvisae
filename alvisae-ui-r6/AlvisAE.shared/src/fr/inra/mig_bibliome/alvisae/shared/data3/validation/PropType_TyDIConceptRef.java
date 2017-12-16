/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

/**
 * Type of Annotation property that contains a reference to a Concept (TyDI's
 * SemanticClass)
 *
 * @author fpapazian
 */
public interface PropType_TyDIConceptRef extends PropTypeType {

    public final static String NAME = "TyDI_conceptRef";

    /**
     *
     * @return the base URL used as TyDI external reference
     */
    String getSemClassRefBaseURL();
}
