/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.UIObject;

/**
 *
 * @author fpapazian
 */
public class Blinker extends Timer {
    private final int[] steps;
    private final UIObject uio;
    private int step = -1;
    private Command executeWhenFinish = null;

    public Blinker(UIObject uio, int[] steps) {
        this.uio = uio;
        this.steps = steps;
    }

    public Blinker(UIObject uio) {
        this(uio, new int[]{120, 40, 100, 40, 100});
    }
    
    @Override
    public void run() {
        step++;
        if (step < steps.length) {
            uio.setVisible(!uio.isVisible());
            schedule(steps[step]);
        } else if (executeWhenFinish!=null) {
            executeWhenFinish.execute();
        }
    }

    public void start(Command executeWhenFinish) {
        this.executeWhenFinish = executeWhenFinish;
        step = -1;
        run();
    }

    public void start() {
        start(null);
    }

}
