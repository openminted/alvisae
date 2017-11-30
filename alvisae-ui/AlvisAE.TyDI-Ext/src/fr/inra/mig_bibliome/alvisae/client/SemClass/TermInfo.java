/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.SemClass;

import com.google.gwt.view.client.ProvidesKey;
import fr.inra.mig_bibliome.alvisae.client.data3.TermBasicImpl;

/**
 *
 * @author fpapazian
 */
public class TermInfo implements Comparable<TermInfo> {

    public static final ProvidesKey<TermInfo> KEY_PROVIDER = new ProvidesKey<TermInfo>() {

        @Override
        public Object getKey(TermInfo item) {
            return item == null ? null : item.getId();
        }
    };
    
    private final TermBasicImpl termImpl;

    public TermBasicImpl getTerm() {
        return termImpl;
    }
    
    public TermInfo(TermBasicImpl classImpl) {
        this.termImpl = classImpl;
    }

    public int getId() {
        return getTerm().getTermId();
    }

    public String getSurfaceForm() {
        return getTerm().getSurfaceForm();
    }

    @Override
    public final int compareTo(TermInfo o) {
        return new Integer(this.getId()).compareTo(o.getId());
    }
}
