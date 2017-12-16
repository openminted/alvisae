/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3.validation;

import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public interface ConsolidationBlock {

    public int getAdjudicationLevel();

    //a block contains one single kind of Annotation
    public AnnotationKind getAnnotationKind();

    public int getStart();

    public int getEnd();

    public boolean isWithoutConflict();

    //List of Annotation reference indexed by AnnotationSetId 
    public List<List<? extends AnnotationReference>> getMembers();
    
    //List of Annotation reference for the specified AnnotationSetId 
    public List<? extends AnnotationReference> getMembersByAnnotationSet(int annotationSetId);
    
}
