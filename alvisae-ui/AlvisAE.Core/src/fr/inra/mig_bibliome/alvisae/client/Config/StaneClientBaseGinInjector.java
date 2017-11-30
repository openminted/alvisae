/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import fr.inra.mig_bibliome.alvisae.client.data.CoreDataProvider;

/**
 * Gin Injector used to retrieve "Global" instances
 * @author fpapazian
 */
@GinModules(StaneClientBaseGinModule.class)
public interface StaneClientBaseGinInjector extends Ginjector {

    public CoreDataProvider getCoreDataProvider();

    public EventBus getMainEventBus();

    public PlaceController getPlaceController();
}
