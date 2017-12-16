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
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDITermRef;

/**
 *
 * @author fpapazian
 */
public class TyDITermRefImpl implements TyDITermRef {

    private static RegExp TermExternalIdregex = RegExp.compile("^https?:/(?:/[^/]+)+/+(?:projects/+){1}(\\d+)/term/(\\d+)/?$");

    public static String getTermExternalId(String baseUrl, int projectId, int termId) {
        return ResourceLocator.getResourceExternalId(baseUrl, projectId) + "/term/" + termId;
    }

    public static Integer getTermIdFromTermExternalId(String termExternalId) {
        Integer termId = null;
        MatchResult result = TermExternalIdregex.exec(ResourceLocator.stripUrlFragment(termExternalId));
        if (result != null && result.getGroupCount() == 3) {
            Integer projectId = Integer.valueOf(result.getGroup(1));
            termId = Integer.valueOf(result.getGroup(2));
        }
        return termId;
    }
    //
    private final TyDIResourceRef resRef;
    private final int termId;

    public TyDITermRefImpl(TyDIResourceRef resRef, int termId) {
        this.resRef = resRef;
        this.termId = termId;

    }

    @Override
    public Integer getTyDITermId() {
        return termId;
    }

    @Override
    public String getTyDIInstanceBaseUrl() {
        return resRef.getTyDIInstanceBaseUrl();
    }

    @Override
    public Integer getTyDIProjectId() {
        return resRef.getTyDIProjectId();
    }
    
    @Override
    public String toString() {
        return getTermExternalId(getTyDIInstanceBaseUrl(), getTyDIProjectId(), getTyDITermId());
    }    
}
