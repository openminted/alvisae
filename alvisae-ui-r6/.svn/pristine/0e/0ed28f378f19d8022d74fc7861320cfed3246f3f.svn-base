/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.inject.Provides;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.RequestManager;
import fr.inra.mig_bibliome.alvisae.client.data.TermDataProvider;

/**
 * Binding Gin module - provides implementations for several global interface instances
 * @author fpapazian
 */
public class StaneClientExtGinModule extends StaneClientBaseGinModule {

    private static TermDataProvider termDataProvider = null;

    @Override
    protected void configure() {
    }

    @Provides
    public TermDataProvider getTermDataProvider() {
        if (termDataProvider == null) {
            termDataProvider = new TermDataProvider(getMainEventBus());
        }
        return termDataProvider;
    }

}
