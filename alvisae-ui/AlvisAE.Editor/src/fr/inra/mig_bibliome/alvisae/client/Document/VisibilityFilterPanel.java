/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import fr.inra.mig_bibliome.alvisae.client.SlidingCoverPanel;
import com.google.gwt.dom.client.Element;

/**
 * UI Component used to set up filters to hide/show Annotations on the Associated document
 * @author fpapazian
 */
@Deprecated
public class VisibilityFilterPanel extends SlidingCoverPanel {

    public VisibilityFilterPanel(Element coveredElement, int elementBorderAndMarging) {
        super(coveredElement, elementBorderAndMarging);
    }
}
