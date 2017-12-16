/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

/**
 * Type of Annotation property that contains a reference to a Term of TyDI, and
 * allow the corresponding Annotation to be used to create new Class or new Term
 * via TyDI extension
 *
 * @author fpapazian
 */
public interface PropType_TyDITermRef extends PropTypeType {

    public final static String NAME = "TyDI_termRef";

    /**
     *
     * @return the base URL used as TyDI external reference
     */
    String getTermRefBaseURL();
}
