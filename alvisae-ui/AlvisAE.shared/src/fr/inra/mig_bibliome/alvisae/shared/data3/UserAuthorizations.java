/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.Set;

/**
 *
 * @author fpapazian
 */
public interface UserAuthorizations  {
    
    /**
     *
     * @return the list of global Authorizations given to the user
     */
    Set<Integer> getGlobalAuths();

    /**
     *
     * @return the list of Authorizations given to the user for the specified campaign
     */
    Set<Integer> getAuthsByCampaign(int campaignId);

    /**
     *
     * @return the list of campaigns for which User's Authorization info has been returned
     * <code>getAuthsByCampaign()</code> must be used to check if the user was given any Authorization for the campaign
     */
    Set<Integer> getCampaigns();
    
}