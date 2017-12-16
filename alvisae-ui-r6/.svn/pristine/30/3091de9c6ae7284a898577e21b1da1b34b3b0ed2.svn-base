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

    private static RegExp regex = RegExp.compile("^(https?:/(?:/[^/]+)+/+)(?:projects/+){1}(\\d+)/?$");
    static final String UrlFragmentSeparator = "#";

    public static String stripUrlFragment(String semClassExternalId) {
        int fragPos = semClassExternalId.indexOf(UrlFragmentSeparator);
        if (fragPos >= 0) {
            return semClassExternalId.substring(0, fragPos);
        } else {
            return semClassExternalId;
        }
    }

    public static String cleanUrl(String semClassExternalId) {
        StringBuilder cleaned = new StringBuilder();

        int protocolEndPos = semClassExternalId.indexOf("://") + 3;
        cleaned.append(semClassExternalId.substring(0, protocolEndPos));

        int fragPos = semClassExternalId.indexOf(UrlFragmentSeparator);
        if (fragPos >= 0) {
            cleaned.append(semClassExternalId.substring(protocolEndPos, fragPos).replaceAll("(/+)", "/"))
                    .append(semClassExternalId.substring(fragPos));
        } else {
            cleaned.append(semClassExternalId.substring(protocolEndPos).replaceAll("(/+)", "/")).append("/");
        }
        return cleaned.toString().replaceAll("/+$", "/");
    }

    public static String getResourceExternalId(String baseUrl, Integer projectId) {
        return cleanUrl(baseUrl) + "projects/" + projectId;
    }
    //
    // always with one single trailing slash
    private final String resourceUrl;
    private final int projectId;

    public ResourceLocator(String baseResourceUrl) {
        MatchResult result = regex.exec(cleanUrl(baseResourceUrl));
        if (result == null || result.getGroupCount() != 3) {
            throw new IllegalArgumentException("Not a valid TyDI resource url! " + cleanUrl(baseResourceUrl));
        }
        this.resourceUrl = result.getGroup(1);
        this.projectId = Integer.valueOf(result.getGroup(2));
    }

    public ResourceLocator(String resourceUrl, int projectId) {
        this.resourceUrl = cleanUrl(resourceUrl);
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
