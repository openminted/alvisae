/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.Extension;

public interface TermAnnotation {

    public final static String DragNDropScope = "TermAnnotationDNDScope";

    /**
     * @return the surface form of the term.
     */
    String getSurfaceForm();

    void setSurfaceForm(String surfaceForm);
    
    /**
     * @return the lemma (or null if there is no lemma for this TermAnnotation)
     */
    String getLemma();

    void setLemma(String lemma);

    /**
     * @return the external id of the corresponding Term from TyDI (or null if
     * this TermAnnotation is not associated to a Term of TyDI) (this Id is
     * typically the URL of the Rest web service that return the term resource
     * itself)
     */
    String getTermExternalId();

    void setTermExternalId(String termExternalId);

    /**
     * @return the external id of the corresponding Semantic Class from TyDI (or
     * null if this TermAnnotation is not associated to a SemanticClass of TyDI)
     * (this Id is typically the URL of the Rest web service that return the
     * Semantic Class resource itself)
     */
    String getSemClassExternalId();

    void setTyDIResourceRef(TyDIResRefPropVal resourceRef);

    /**
     * 
     * @return true if this Annotation can be used to create a new 
     * TyDI Class or Term resource
     */
    boolean isTyDIClassOrTermGenerator();
}
