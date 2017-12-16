/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Conso;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams.TaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientGinInjector;

/**
 * @author fpapazian
 */
public class AnnotationSetConsoActivity extends AbstractActivity implements AnnotationSetConsoView.Presenter {

    private static final StaneClientGinInjector injector = GWT.create(StaneClientGinInjector.class);
    private final TaskReviewParams params;
    private AnnotationSetConsoViewImpl annotationSetConsoView;

    public AnnotationSetConsoActivity(AnnotationSetConsoPlace place) {
        this.params = place.getParams();
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {
        //TODO since the Asynch loading can take some time, a wait dialog should appear on screen to make user more comfortable

        //Async loading to enable fast startup of the default activity
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                Window.alert("Unable to load AnnotationSet Consolidation view.");
                throw new UnsupportedOperationException("Unable to load AnnotationSet Consolidation view.");
            }

            @Override
            public void onSuccess() {
                Scheduler.get().scheduleDeferred(new Command() {
                    @Override
                    public void execute() {
                        annotationSetConsoView = new AnnotationSetConsoViewImpl();
                        annotationSetConsoView.setParams(params);
                        annotationSetConsoView.setPresenter(AnnotationSetConsoActivity.this);
                        containerWidget.setWidget(annotationSetConsoView.asWidget());

                        annotationSetConsoView.proceed();
                    }
                });

            }
        });
    }

    /**
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        //Alert only if modifications were performed in the review
        return !annotationSetConsoView.canCloseView() ? "Do you want to leave and lose unsaved modifications in the Review?" : null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Navigate to a new Place in the browser
     */
    @Override
    public void goTo(Place place) {
        injector.getPlaceController().goTo(place);
    }
}
