/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.List;

/**
 * @author fpapazian
 */
public interface StructTermChanges {

    public int getFromVersion();

    public int getToVersion();
    
    public List<SemClassChange> getChanges();
}
