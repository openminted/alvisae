/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.dom.client.Node;

/**
 *
 * @author fpapazian
 */
public interface AnnotationMarker {

    public String getContainerId();

    public void setContainerId(String containerId);

    public Node getNode();

    public void setNode(Node node);

    public int getStartCharacterOffset();

    public void setStartCharacterOffset(int startCharacterOffset);

    public int getEndCharacterOffset();

    public void setEndCharacterOffset(int endCharacterOffset);

    public String getText();

    public void setText(String text);

    public String getNodeId();
}
