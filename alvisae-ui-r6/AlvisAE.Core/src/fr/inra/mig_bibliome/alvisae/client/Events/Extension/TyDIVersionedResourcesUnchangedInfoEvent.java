/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Events.Extension;

import fr.inra.mig_bibliome.alvisae.client.data3.Extension.CheckedSemClassImpl;
import java.util.List;

/**
 * Event fired to signal that the specified TyDI resources were not changed (reply to TyDIResourcesCheckChangesInfoEvent)
 * @author fpapazian
 */
public class TyDIVersionedResourcesUnchangedInfoEvent extends TyDIVersionedResourcesInfoEvent {
    
    public TyDIVersionedResourcesUnchangedInfoEvent(List<CheckedSemClassImpl> resRef) {
        super(resRef);
    }
}
