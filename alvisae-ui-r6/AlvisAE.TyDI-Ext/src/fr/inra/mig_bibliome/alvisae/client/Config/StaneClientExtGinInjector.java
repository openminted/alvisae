/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.inject.client.GinModules;
import fr.inra.mig_bibliome.alvisae.client.data.TermDataProvider;

/**
 * Gin Injector used to retrieve "Global" instances
 * @author fpapazian
 */
@GinModules(StaneClientExtGinModule.class)
public interface StaneClientExtGinInjector extends Ginjector {

    public TermDataProvider getTermDataProvider();

}
