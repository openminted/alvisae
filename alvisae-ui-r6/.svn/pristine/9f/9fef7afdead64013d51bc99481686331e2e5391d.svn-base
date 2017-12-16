/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provides;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import fr.inra.mig_bibliome.alvisae.client.data.CoreDataProvider;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.RequestManager;

/**
 * Binding Gin module - provides implementations for several global interface instances
 * @author fpapazian
 */
public class StaneClientBaseGinModule extends AbstractGinModule {

    private static EventBus eventBus = null;
    private static PlaceController placeController = null;
    private static CoreDataProvider dataProvider = null;
    private static RequestManager requestManager = null;

    @Override
    protected void configure() {
    }

    @Provides
    public EventBus getMainEventBus() {
        if (eventBus == null) {
            eventBus = new SimpleEventBus();
        }
        return eventBus;
    }

    @Provides
    public CoreDataProvider getDataProvider() {
        if (dataProvider == null) {
            dataProvider = new CoreDataProvider(getMainEventBus());
        }
        return dataProvider;
    }


    @Provides
    public PlaceController getPlaceController() {
        if (placeController == null) {
            placeController = new PlaceController(getMainEventBus());
        }
        return placeController;
    }
}
