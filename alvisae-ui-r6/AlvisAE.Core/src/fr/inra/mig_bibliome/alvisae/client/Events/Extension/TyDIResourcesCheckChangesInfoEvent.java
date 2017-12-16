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
 * Event fired to request if the specified TyDI resources are unchanged (should be replied with a TyDIVersionedResourcesUnchangedInfoEvent)
 * @author fpapazian
 */
public class TyDIResourcesCheckChangesInfoEvent extends TyDIVersionedResourcesInfoEvent {
    
    public TyDIResourcesCheckChangesInfoEvent(List<CheckedSemClassImpl> resRef) {
        super(resRef);
    }
}
