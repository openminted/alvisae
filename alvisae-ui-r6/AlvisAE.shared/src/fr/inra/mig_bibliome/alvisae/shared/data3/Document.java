/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.List;

/**
 *
 * @author fpapazian
 */
public interface Document {

    int getId();

    String getContents();

    void setContents(String contents);

    String getDescription();

    int getOwner();

    Properties getProperties();

    List<String> getFragmentsText(List<Fragment> fragments);
}
