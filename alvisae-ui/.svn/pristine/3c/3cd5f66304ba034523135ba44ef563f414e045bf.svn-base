/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.Date;

/**
 * Annotation Campaign
 * @author fpapazian
 */
public interface Campaign {
    /**
     * @return the Id of this Annotation Campaign
     */
    int getId();

    /**
     *
     * @return human readable short name of this Annotation Campaign
     */
    String getDisplayName();

    /**
     *
     * @return url to this campaign guidelines
     */
    String getGuidelinesUrl();
    
    /**
     *
     * @return human readable short description of this Annotation Campaign
     */
    @Deprecated
    String getDescription();

    /**
     *
     * @return true if the Campaign is currently open to user to edition text annotations
     */
    @Deprecated
    boolean isOpen();

    /**
     *
     * @return the stating date of this Annotation Campaign
     */
    @Deprecated
    Date getStartingDate();

    /**
     *
     * @return the planned ending date of this Annotation Campaign
     */
    @Deprecated
    Date getPlannedEndDate();

    /**
     *
     * @return the Id of the user administering this campaign
     */
    @Deprecated
    String getAdminUserId();

}
