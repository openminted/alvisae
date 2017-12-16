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
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDISemClassRef;

/**
 *
 * @author fpapazian
 */
public class TyDISemClassRefImpl implements TyDISemClassRef {

    private static RegExp SemClassExternalIdregex = RegExp.compile("^https?:/(?:/[^/]+)+/+(?:projects/+){1}(\\d+)/semClass/(\\d+)(?:/canonic/(\\d+)/?)?$");

    public static String getSemClassShortExternalId(String baseUrl, Integer projectId, int semClassId) {
        return ResourceLocator.getResourceExternalId(baseUrl, projectId) + "/semClass/" + semClassId;
    }

    public static String getSemClassFullExternalId(String baseUrl, Integer projectId, int semClassId, Integer canonicTermId) {
        String result = getSemClassShortExternalId(baseUrl, projectId, semClassId);
        if (canonicTermId != null) {
            result += "/canonic/" + canonicTermId;
        }
        return result;
    }
   
    public static Integer getSemClassIdFromSemClassExternalId(String semClassExternalId) {
        Integer semClassId = null;
        
        MatchResult result = SemClassExternalIdregex.exec(ResourceLocator.stripUrlFragment(semClassExternalId));
        if (result != null && (result.getGroupCount() == 3 || result.getGroupCount() == 4)) {
            Integer projectId = Integer.valueOf(result.getGroup(1));
            semClassId = Integer.valueOf(result.getGroup(2));
            if (result.getGroupCount() == 4) {
                //    Integer canonicTermId = Integer.valueOf(result.getGroup(3));
            }
        }
        return semClassId;
    }
    //
    private final TyDIResourceRef resRef;
    private final int semClassId;
    private final Integer canonicId;
    private final String canonicLabel;

    public TyDISemClassRefImpl(TyDIResourceRef resRef, int semClassId, Integer canonicId, String canonicLabel) {
        this.resRef = resRef;
        this.semClassId = semClassId;
        this.canonicId = canonicId;
        this.canonicLabel = canonicLabel;
    }
    
    public TyDISemClassRefImpl(TyDIResourceRef resRef, int semClassId, Integer canonicId) {
        this(resRef, semClassId, canonicId, null);
    }

    @Override
    public Integer getTyDISemanticClassId() {
        return semClassId;
    }

    @Override
    public Integer getTyDICanonicTermId() {
        return canonicId;
    }

    @Override
    public String getCanonicLabel() {
        return canonicLabel;
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
    public boolean sameShortSemClassRef(String otherSemClassRef) {
        return otherSemClassRef == null ? false
                : otherSemClassRef.startsWith(
                getSemClassShortExternalId(getTyDIInstanceBaseUrl(), getTyDIProjectId(), getTyDISemanticClassId()));
    }

    @Override
    public String toString() {
        return getSemClassFullExternalId(getTyDIInstanceBaseUrl(), getTyDIProjectId(), getTyDISemanticClassId(), getTyDICanonicTermId());
    }
}
