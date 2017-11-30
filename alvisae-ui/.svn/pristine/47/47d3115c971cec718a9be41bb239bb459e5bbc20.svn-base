/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.Extension;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDIResourceRef;

/**
 *
 * @author fpapazian
 */
public class ResourceLocator implements TyDIResourceRef {

    private static RegExp regex = RegExp.compile("^(https?:/(?:/[^/]+)+/)(?:projects/+){1}(\\d+)/?$");

    public static String getResourceExternalId(String baseUrl, Integer projectId) {
        return baseUrl.replaceAll("/+$", "") + "/projects/" + projectId;
    }
    //
    // always with one single trailing slash
    private final String resourceUrl;
    private final int projectId;

    public ResourceLocator(String baseResourceUrl) {
        MatchResult result = regex.exec(baseResourceUrl);
        if (result == null || result.getGroupCount() != 3) {
            throw new IllegalArgumentException("Not a valid TyDI resource url! " + baseResourceUrl);
        }
        this.resourceUrl = result.getGroup(1);
        this.projectId = Integer.valueOf(result.getGroup(2));
    }

    public ResourceLocator(String resourceUrl, int projectId) {
        this.resourceUrl = resourceUrl.replaceAll("/+$", "") + "/";
        this.projectId = projectId;
    }

    @Override
    public String getTyDIInstanceBaseUrl() {
        return resourceUrl;
    }

    @Override
    public Integer getTyDIProjectId() {
        return projectId;
    }

    @Override
    public String toString() {
        return getResourceExternalId(getTyDIInstanceBaseUrl(), getTyDIProjectId());
    }
}
