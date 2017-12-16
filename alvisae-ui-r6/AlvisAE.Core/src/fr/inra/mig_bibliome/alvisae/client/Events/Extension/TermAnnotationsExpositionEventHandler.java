/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events.Extension;

import com.google.gwt.event.shared.EventHandler;
/**
 *
 * @author fpapazian
 */
public interface TermAnnotationsExpositionEventHandler extends EventHandler {

    void onTermAnnotationsExpositionChanged(TermAnnotationsExpositionEvent event);

}
