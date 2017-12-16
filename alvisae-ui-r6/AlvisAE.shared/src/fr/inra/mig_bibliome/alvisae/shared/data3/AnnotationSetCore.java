/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

/**
 *
 * @author fpapazian
 */
public interface AnnotationSetCore {

    int getId();

    int getTaskId();
    
    AnnotationSetType getType();

    String getCreationDate();

    String getPublicationDate();

    String getDescription();

    int getRevision();

    boolean isHead();

    int getOwner();
    
    boolean isInvalidated();
}
