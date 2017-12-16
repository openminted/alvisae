/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

public interface Fragment {

    public int getStart();

    public int getEnd();

    public void setStart(int start);

    public void setEnd(int end);
    
    public boolean isSame(Fragment other);
}
