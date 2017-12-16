/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText.AnnotationProcessor;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSet;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetType;
import fr.inra.mig_bibliome.alvisae.shared.data3.ExtendedAnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationObsolescenceStatus;
import java.util.*;
import java.util.Map.Entry;

/**
 * Frontal used to access the underlying Data Model. Shared by every views
 * displaying the same AnnotatedText).
 *
 * @author fpapazian
 */
public class AnnotatedTextHandler implements ExtendedAnnotatedText {

    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);

    private static class DocHandlerParams {

        Integer userId;
        Integer campaignId;
        Integer documentId;
        AnnotatedTextHandler handler;

        public DocHandlerParams(Integer userId, Integer campaignId, Integer documentId, AnnotatedTextHandler handler) {
            this.userId = userId;
            this.campaignId = campaignId;
            this.documentId = documentId;
            this.handler = handler;
        }
    }
    private static HashMap<AnnotatedTextImpl, DocHandlerParams> handlerByDocument = new HashMap<AnnotatedTextImpl, DocHandlerParams>();

    /**
     *
     * @throws IllegalArgumentException if the structure of the specified
     * annotatedDoc is invalid
     */
    public static AnnotatedTextHandler createHandler(int userId, int campaignId, AnnotatedTextImpl annotatedDoc) {
        if (annotatedDoc == null) {
            throw new IllegalArgumentException("AnnotatedText should not be null!");
        }
        releaseHandler(annotatedDoc);
        annotatedDoc.checkStructure();
        AnnotatedTextHandler handler = new AnnotatedTextHandler(userId, annotatedDoc);
        handlerByDocument.put(annotatedDoc, new DocHandlerParams(userId, campaignId, annotatedDoc.getDocument().getId(), handler));
        return handler;
    }

    public static AnnotatedTextHandler getHandler(AnnotatedTextImpl annotatedDoc) {
        DocHandlerParams handlerParam = handlerByDocument.get(annotatedDoc);
        return handlerParam.handler;
    }

    public static void releaseHandler(AnnotatedTextImpl annotatedDoc) {
        handlerByDocument.remove(annotatedDoc);
    }
    // -------------------------------------------------------------------------
    private final int currentUserId;
    private AnnotatedTextImpl annotatedText;
    private final HashMap<String, Annotation> annotationById = new HashMap<String, Annotation>();
    //current User's Annotation Set (i.e. the only one editable)
    private AnnotationSetImpl editableUsersAnnotationSet = null;
    private AnnotationSetImpl formattingAnnotationSet = null;
    private final HashMap<Integer, HashSet<String>> annotationIdByAnnotationSetId = new HashMap<Integer, HashSet<String>>();
    private final HashMap<String, Integer> annotationSetIdByAnnotationId = new HashMap<String, Integer>();
    private final HashSet<Integer> referenceableAnnotationSetIds = new HashSet<Integer>();
    private final HashSet<Integer> invalidatedAnnotationSetIds = new HashSet<Integer>();
    private final HashMap<Integer, AnnotationSetImpl> loadedAnnotationSets = new HashMap<Integer, AnnotationSetImpl>();
    private AdditionalAnnotationSetRequestHandler additionalAnnSetRequestHnd = null;

    private AnnotatedTextHandler(Integer userId, AnnotatedTextImpl annotatedDoc) {
        this.currentUserId = userId != null ? userId : injector.getCoreDataProvider().getRequestManager().getCurrentUserId();;
        this.annotatedText = annotatedDoc;
        init();
    }

    private void init() {

        annotationIdByAnnotationSetId.clear();
        formattingAnnotationSet = null;
        editableUsersAnnotationSet = null;
        referenceableAnnotationSetIds.clear();

        TaskDefinition taskDef = annotatedText.getEditedTask();
        AnnotationSetListImpl asl = annotatedText._getAnnotationSetList();
        for (int i = 0; i < asl.length(); i++) {
            AnnotationSetImpl as = asl.get(i);

            loadedAnnotationSets.put(as.getId(), as);
            if (as.isInvalidated()) {
                invalidatedAnnotationSetIds.add(as.getId());
            }

            HashSet<String> annIdSet = new HashSet<String>();
            annotationIdByAnnotationSetId.put(as.getId(), annIdSet);

            if (AnnotationSetType.UserAnnotation.equals(as.getType()) && (as.getOwner() == currentUserId)) {
                //the editable AnnotationSet is the one corresponding to the currently edited Task; RootTask is never editable
                if (taskDef == null || (taskDef.getId() == as.getTaskId() && taskDef.getPrecedencelevel() > 0)) {
                    editableUsersAnnotationSet = as;
                }
                //the Annotations of the user's Annotation SetAnnotation Set are referenceable
                referenceableAnnotationSetIds.add(as.getId());
            } else if (AnnotationSetType.HtmlAnnotation.equals(as.getType())) {
                formattingAnnotationSet = as;
            } else if (taskDef == null || (taskDef.getReviewedTask() != null && taskDef.getReviewedTask() == as.getTaskId())) {
                //the Annotations of the reviewed Annotation Set are referenceable
                referenceableAnnotationSetIds.add(as.getId());
            }
        }

        if (editableUsersAnnotationSet == null) {
            //FIXME Not I18N
            GWT.log("WARNING: Missing User's Annotation Set!");
        }

        annotatedText.scanAnnotations(new AnnotationProcessor() {
            @Override
            public boolean process(AnnotationSet annotationSet, Annotation annotation) {
                annotationIdByAnnotationSetId.get(annotationSet.getId()).add(annotation.getId());
                annotationSetIdByAnnotationId.put(annotation.getId(), annotationSet.getId());
                annotationById.put(annotation.getId(), annotation);
                return true;
            }
        });

    }

    public AnnotatedTextImpl getAnnotatedText() {
        return annotatedText;
    }

    public Collection<Annotation> getAnnotations() {
        return new ArrayList<Annotation>(annotationById.values());
    }

    public List<Annotation> getReferenceableAnnotations() {
        ArrayList<Annotation> result = new ArrayList<Annotation>();
        for (Annotation a : annotationById.values()) {
            if (isReferenceableAnnotation(a.getId())) {
                result.add(a);
            }
        }
        return result;
    }

    public Map<AnnotationReference, Annotation> getReferenceableAnnotationRefs() {
        Map<AnnotationReference, Annotation> result = new HashMap<AnnotationReference, Annotation>();
        for (Annotation a : annotationById.values()) {
            String annotationId = a.getId();
            Integer annotationSetId = annotationSetIdByAnnotationId.get(annotationId);
            if (annotationSetId != null && referenceableAnnotationSetIds.contains(annotationSetId)) {
                result.put(new SetSafeAnnotationReference(annotationId, annotationSetId), a);
            }
        }
        return result;
    }

    public boolean isReferenceableAnnotation(String annotationId) {
        Integer annotationSetId = annotationSetIdByAnnotationId.get(annotationId);
        return annotationSetId != null && referenceableAnnotationSetIds.contains(annotationSetId);
    }

    /**
     *
     * @param annotationId
     * @return the ObsolescenceStatus of the specified Annotation: OUTDATED, if
     * Annotation belongs to invalidated (i.e. non-Head) AnnotationSet;
     * OUTDATEDREF, if one of the components/arguments of the Annotation is
     * invalid; OUTDATEDBACKREF, if one of the Source Annotations of the
     * Annotation is invalid; UPTODATE, otherwise
     */
    public AnnotationObsolescenceStatus getAnnotationObsolescenceStatus(AnnotationReference annotationRef, Integer userAnnotationSetId) {
        String annotationId = annotationRef.getAnnotationId();
        Integer annotationSetId = annotationRef.getAnnotationSetId() != null ? annotationRef.getAnnotationSetId() : userAnnotationSetId;
        AnnotationObsolescenceStatus obsolescenceStatus = AnnotationObsolescenceStatus.UPTODATE;
        AnnotationSet annotationSet = getAnnotationSet(annotationSetId);
        if ((annotationSet != null) && (annotationSet.isInvalidated() && !annotationSetId.equals(userAnnotationSetId))) {
            obsolescenceStatus = AnnotationObsolescenceStatus.OUTDATED;
        } else if ((annotationSet == null) && getAnnotatedText().getOutdatedAnnotationSetIds().contains(annotationSetId)) {
            obsolescenceStatus = AnnotationObsolescenceStatus.OUTDATED;
        } else {
            Annotation annotation = getAnnotation(annotationId);
            if (annotation != null) {

                switch (annotation.getAnnotationKind()) {
                    case GROUP:
                        for (AnnotationReference compRef : annotation.getAnnotationGroup().getComponentRefs()) {
                            if (!AnnotationObsolescenceStatus.UPTODATE.equals(getAnnotationObsolescenceStatus(compRef, userAnnotationSetId))) {
                                obsolescenceStatus = AnnotationObsolescenceStatus.OUTDATEDREF;
                                break;
                            }
                        }
                        break;
                    case RELATION:
                        for (AnnotationReference argRef : annotation.getRelation().getRolesArguments().values()) {
                            if (!AnnotationObsolescenceStatus.UPTODATE.equals(getAnnotationObsolescenceStatus(argRef, userAnnotationSetId))) {
                                obsolescenceStatus = AnnotationObsolescenceStatus.OUTDATEDREF;
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }

                if (AnnotationObsolescenceStatus.UPTODATE.equals(obsolescenceStatus)) {
                    if (annotation.getSourceAnnotations() != null) {
                        for (AnnotationBackReference backRef : annotation.getSourceAnnotations().getAnnotationBackReferences()) {
                            if (!AnnotationObsolescenceStatus.UPTODATE.equals(getAnnotationObsolescenceStatus(backRef, userAnnotationSetId))) {
                                obsolescenceStatus = AnnotationObsolescenceStatus.OUTDATEDBACKREF;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return obsolescenceStatus;
    }

    public List<Annotation> getAnnotationsForAnnSets(Set<Integer> annotationSetIds) {
        ArrayList<Annotation> result = new ArrayList<Annotation>();
        AnnotationSetListImpl asl = annotatedText._getAnnotationSetList();
        for (int i = 0; i < asl.length(); i++) {
            AnnotationSetImpl as = asl.get(i);
            if (annotationSetIds == null || annotationSetIds.contains(as.getId())) {
                result.addAll(as.getTextAnnotations());
                result.addAll(as.getGroups());
                result.addAll(as.getRelations());
            }
        }
        return result;
    }

    @Override
    public Annotation getAnnotation(String annotationId) {
        return annotationById.get(annotationId);
    }

    public Integer getAnnotationSetId(String annotationId) {
        return annotationSetIdByAnnotationId.get(annotationId);
    }

    public AnnotationReference getAnnotationReference(String annotationId) {
        int currUsersAnnSetId = getEditableUsersAnnotationSet().getId();
        Integer annotationSetId = annotationSetIdByAnnotationId.get(annotationId);
        //specify AnnotationSet id only if it is different than user's current one
        return AnnotationReferenceImpl.create(annotationId, (annotationSetId != currUsersAnnSetId) ? annotationSetId : null);
    }

    /**
     *
     * @param annotationId
     * @return true if the specified annotation is a Formatting annotation
     */
    public boolean isFormattingAnnotation(String annotationId) {
        return formattingAnnotationSet != null && annotationIdByAnnotationSetId.get(formattingAnnotationSet.getId()).contains(annotationId);
    }

    public AnnotationSetImpl getFormattingAnnotationSet() {
        return formattingAnnotationSet;
    }

    public AnnotationSetImpl getEditableUsersAnnotationSet() {
        return editableUsersAnnotationSet;
    }

    public Set<Integer> getLoadedAnnotationSetIds() {
        return loadedAnnotationSets.keySet();
    }

    public Set<Integer> getInvalidatedAnnotationSetIds() {
        return invalidatedAnnotationSetIds;
    }

    public AnnotationSet getAnnotationSet(int annotatioSetId) {
        return loadedAnnotationSets.get(annotatioSetId);
    }

    public AnnotationSetImpl getAnnotationSetIdForUserTask(int userId, int taskId) {
        AnnotationSetImpl result = null;
        AnnotationSetListImpl asl = annotatedText._getAnnotationSetList();
        for (int i = 0; i < asl.length(); i++) {
            AnnotationSetImpl as = asl.get(i);
            if (AnnotationSetType.UserAnnotation.equals(as.getType()) && (as.getOwner() == userId) && as.getTaskId() == taskId) {
                result = as;
                break;
            }
        }
        return result;
    }
    //==========================================================================

    public final String getEditedUsersAnnotationAsCSV() {
        final StringBuilder result = new StringBuilder();

        result.append(AnnotationImpl.getCSV(null));
        getEditableUsersAnnotationSet().scanAnnotations(new AnnotatedTextImpl.AnnotationProcessor() {
            @Override
            public boolean process(AnnotationSetImpl annotationSet, AnnotationImpl annotation) {
                result.append(annotation.getCSV());
                return true;
            }
        });
        return result.toString();
    }

    //==========================================================================
    public static interface AdditionalAnnotationSetRequestHandler {

        public void requestAdditionalAnnotationSet(AnnotatedTextHandler annotatedTextHandler, int annotationSetId);
    }

    public void setAdditionalAnnotationSetRequestHandler(AdditionalAnnotationSetRequestHandler additionalAnnSetRequestHnd) {
        this.additionalAnnSetRequestHnd = additionalAnnSetRequestHnd;
    }

    public void requestAdditionalAnnotationSet(int annotationSetId) {
        if (additionalAnnSetRequestHnd != null) {
            additionalAnnSetRequestHnd.requestAdditionalAnnotationSet(this, annotationSetId);
        }
    }
    //==========================================================================

    public void addAdditionalAnnotationSet(AnnotationSetImpl annotationSet) {
        annotatedText.addAdditionalAnnotationSet(annotationSet);
        init();
    }

    //==========================================================================
    @Override
    public Annotation createTextAnnotation(String id, String type, Collection<Fragment> fragments, Properties props, List<AnnotationBackReference> backRefs) {
        if (getAnnotation(id) != null) {
            throw new IllegalArgumentException("this Id is already used for an existing Annotation");
        }
        AnnotationImpl annotation = annotatedText.createLooseTextAnnotation(id, type, fragments, props, backRefs);
        editableUsersAnnotationSet.addTextAnnotation(annotation);
        annotationById.put(annotation.getId(), annotation);
        annotationSetIdByAnnotationId.put(annotation.getId(), editableUsersAnnotationSet.getId());
        return annotation;
    }

    @Override
    public Annotation createGroupAnnotation(String id, String type, Collection<AnnotationReference> components, Properties props, List<AnnotationBackReference> backRefs) {
        if (getAnnotation(id) != null) {
            throw new IllegalArgumentException("this Id is already used for an existing Annotation");
        }
        AnnotationImpl annotation = annotatedText.createLooseGroupAnnotation(id, type, components, props, backRefs);
        editableUsersAnnotationSet.addGroupAnnotation(annotation);
        annotationById.put(annotation.getId(), annotation);
        annotationSetIdByAnnotationId.put(annotation.getId(), editableUsersAnnotationSet.getId());
        return annotation;
    }

    @Override
    public Annotation createRelationAnnotation(String id, String type, Map<String, AnnotationReference> arguments, Properties props, List<AnnotationBackReference> backRefs) {
        if (getAnnotation(id) != null) {
            throw new IllegalArgumentException("this Id is already used for an existing Annotation");
        }
        AnnotationImpl annotation = annotatedText.createLooseRelationAnnotation(id, type, arguments, props, backRefs);
        editableUsersAnnotationSet.addRelationAnnotation(annotation);
        annotationById.put(annotation.getId(), annotation);
        annotationSetIdByAnnotationId.put(annotation.getId(), editableUsersAnnotationSet.getId());
        return annotation;
    }

    @Override
    public boolean removeAnnotation(String annotationId) {
        if (annotationId == null) {
            throw new NullPointerException("annotationId should not be null");
        }

        if (getAnnotation(annotationId) != null) {
            //checking for references to this Annotation before performing the removal
            if (!getReferencesToAnnotation(annotationId, true).isEmpty()) {
                return false;
            }
            switch (getAnnotation(annotationId).getAnnotationKind()) {
                case TEXT:
                    editableUsersAnnotationSet.removeTextAnnotation(annotationId);
                    break;
                case GROUP:
                    editableUsersAnnotationSet.removeGroupAnnotation(annotationId);
                    break;
                case RELATION:
                    editableUsersAnnotationSet.removeRelationAnnotation(annotationId);
                    break;

            }
            annotationById.remove(annotationId);

            return true;
        } else {
            throw new IllegalArgumentException("the Annotation does not belong to this AnnotatedText");
        }
    }

    @Override
    public void fragmentsAdditionToAnnotation(String annotationId, Collection<Fragment> fragments) {
        if (annotationId == null) {
            throw new NullPointerException("annotationId should not be null");
        } else if (fragments == null) {
            throw new NullPointerException("fragments should not be null");
        }
        Annotation annotation = getAnnotation(annotationId);
        if (annotation != null) {
            if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                annotation.getTextBinding().addFragments(fragments);
            } else {
                throw new IllegalArgumentException("Fragment can only be added to TEXT Annotation");
            }
        } else {
            throw new IllegalArgumentException("the Annotation does not belong to this AnnotatedText");
        }
    }

    @Override
    public boolean fragmentsSubstractionToAnnotation(String annotationId, Collection<Fragment> fragments) {
        if (annotationId == null) {
            throw new NullPointerException("annotationId should not be null");
        } else if (fragments == null) {
            throw new NullPointerException("fragments should not be null");
        }
        Annotation annotation = getAnnotation(annotationId);
        if (annotation != null) {
            if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                return annotation.getTextBinding().removeFragments(fragments);
            } else {
                throw new IllegalArgumentException("Fragment can only be removed from TEXT Annotation");
            }
        } else {
            throw new IllegalArgumentException("the Annotation does not belong to this AnnotatedText");
        }
    }

    public List<Annotation> getReferencesToAnnotation(String annotationId, boolean fastFail) {
        if (annotationId == null) {
            throw new NullPointerException("annotationId should not be null");
        }
        List<Annotation> result = new ArrayList<Annotation>();

        Annotation annotation = getAnnotation(annotationId);
        if (annotation == null) {
            throw new IllegalArgumentException("the Annotation does not belong to this AnnotatedText");
        }

        MainAnnotationLoop:
        for (Annotation a : getAnnotations()) {
            if (annotation.equals(a)) {
                continue;
            }

            switch (a.getAnnotationKind()) {
                case GROUP:
                    for (AnnotationReference component : a.getAnnotationGroup().getComponentRefs()) {
                        if (annotationId.equals(component.getAnnotationId())) {
                            result.add(a);
                            if (fastFail) {
                                break MainAnnotationLoop;
                            } else {
                                continue MainAnnotationLoop;
                            }
                        }
                    }
                    break;
                case RELATION:
                    for (Entry<String, AnnotationReference> e : a.getRelation().getRolesArguments().entrySet()) {
                        if (annotationId.equals(e.getValue().getAnnotationId())) {
                            result.add(a);
                            if (fastFail) {
                                break MainAnnotationLoop;
                            } else {
                                continue MainAnnotationLoop;
                            }
                        }
                    }
                    break;
            }
        }
        return result;
    }

    @Override
    public List<Annotation> getReferencesToAnnotation(String annotationId) {
        return getReferencesToAnnotation(annotationId, false);
    }

    @Override
    public boolean isEqualToOrReferencedBy(String annotationId, String referentAnnId) {
        boolean isEqualOrReferenced = annotationId.equals(referentAnnId);

        if (!isEqualOrReferenced) {
            Annotation referent = getAnnotation(referentAnnId);
            if (referent == null) {
                throw new IllegalArgumentException("the Annotation does not belong to this AnnotatedText");
            }

            switch (referent.getAnnotationKind()) {
                case TEXT:
                    isEqualOrReferenced = false;
                    break;
                case GROUP:
                    for (AnnotationReference component : referent.getAnnotationGroup().getComponentRefs()) {
                        isEqualOrReferenced = isEqualToOrReferencedBy(annotationId, component.getAnnotationId());
                        if (isEqualOrReferenced) {
                            break;
                        }
                    }
                    break;
                case RELATION:
                    for (AnnotationReference argument : referent.getRelation().getRolesArguments().values()) {
                        isEqualOrReferenced = isEqualToOrReferencedBy(annotationId, argument.getAnnotationId());
                        if (isEqualOrReferenced) {
                            break;
                        }
                    }
                    break;
            }
        }
        return isEqualOrReferenced;
    }

    public Annotation addAnnotation(String annotationId, String annotationType, Collection<Fragment> targets, Properties props, List<AnnotationBackReference> backRefs) {
        Annotation annotation = createTextAnnotation(annotationId, annotationType, targets, props, backRefs);
        //annotationSaver.addAnnotation(annotation);
        return annotation;
    }

    public boolean removeAnnotation(Annotation annotation) {
        boolean result = removeAnnotation(annotation.getId());
        if (result) {
            //annotationSaver.removeAnnotation(annotation);
        }
        return result;
    }

    public void addAnnotationFragments(String annotationId, List<Fragment> fragments) {
        fragmentsAdditionToAnnotation(annotationId, fragments);
        //annotationSaver.updateAnnotation(getAnnotation(annotationId));
    }

    public boolean removeAnnotationFragment(String annotationId, List<Fragment> fragments) {
        boolean result = fragmentsSubstractionToAnnotation(annotationId, fragments);
        if (result) {
            //annotationSaver.updateAnnotation(getAnnotation(annotationId));
        }
        return result;
    }

    public void setAnnotationFragment(String annotationId, Collection<Fragment> fragments) {
        getAnnotation(annotationId).getTextBinding().setFragments(fragments);
        //annotationSaver.updateAnnotation(getAnnotation(annotationId));
    }

    public Annotation modifyAnnotationType(String id, String newType) {
        Annotation annotation = getAnnotation(id);
        ((AnnotationImpl) annotation).setAnnotationType(newType);
        return annotation;
    }

    //==========================================================================
    public Annotation addGroup(String annotationId, String groupType, List<AnnotationReference> components, Properties props, List<AnnotationBackReference> backRefs) {
        Annotation annotation = createGroupAnnotation(annotationId, groupType, components, props, backRefs);
        //annotationSaver.addAnnotation(annotation);
        return annotation;
    }

    public Annotation addGroup(String groupType, List<AnnotationReference> components, Properties props, List<AnnotationBackReference> backRefs) {
        String annotationId = getNewAnnotationId();
        Annotation annotation = addGroup(annotationId, groupType, components, props, backRefs);
        //annotationSaver.addAnnotation(annotation);
        return annotation;
    }

    public boolean removeGroup(Annotation annotation) {
        return removeAnnotation(annotation);
    }

    public Annotation modifyGroup(String id, String newType, List<AnnotationReference> newComponents) {
        Annotation annotation = getAnnotation(id);
        for (AnnotationReference c : annotation.getAnnotationGroup().getComponentRefs()) {
            annotation.getAnnotationGroup().removeComponent(c.getAnnotationId());
        }
        for (AnnotationReference c : newComponents) {
            annotation.getAnnotationGroup().addComponent(c);
        }
        ((AnnotationImpl) annotation).setAnnotationType(newType);
        //annotationSaver.updateAnnotation(annotation);
        return annotation;
    }
    //==========================================================================

    public boolean removeRelation(Annotation annotation) {
        return removeAnnotation(annotation);
    }

    public Annotation addRelation(String annotationId, String relationType, Map<String, AnnotationReference> argumentRoleMap, Properties props, List<AnnotationBackReference> backRefs) {
        Annotation annotation = createRelationAnnotation(annotationId, relationType, argumentRoleMap, props, backRefs);
        //annotationSaver.addAnnotation(annotation);
        return annotation;
    }

    public Annotation addRelation(String relationType, Map<String, AnnotationReference> argumentRoleMap, Properties props, List<AnnotationBackReference> backRefs) {
        String annotationId = getNewAnnotationId();
        Annotation annotation = addRelation(annotationId, relationType, argumentRoleMap, props, backRefs);
        //annotationSaver.addAnnotation(annotation);
        return annotation;
    }

    public Annotation modifyRelation(String id, String newRelType, Map<String, AnnotationReference> newArgumentRoleMap) {
        Annotation annotation = getAnnotation(id);
        ((RelationImpl) annotation.getRelation()).setRolesArguments(newArgumentRoleMap);
        ((AnnotationImpl) annotation).setAnnotationType(newRelType);
        //annotationSaver.updateAnnotation(annotation);
        return annotation;
    }

    //==========================================================================
    public boolean replaceProperty(Annotation annotation, String key, List<String> newValues) {
        Properties props = annotation.getProperties();
        if (props.hasKey(key)) {
            props.removeKey(key);
        }
        if (newValues != null && !newValues.isEmpty()) {
            for (String value : newValues) {
                props.addValue(key, value);
            }
        }

        //annotationSaver.updateAnnotation(annotation);
        return true;
    }

    public boolean replaceProperty(Annotation annotation, String key, int propIndex, String oldValue, String newValue) {
        Properties props = annotation.getProperties();
        if (oldValue == null) {
            props.addValue(key, newValue);
        } else if (newValue == null) {
            props.removeValue(key, propIndex);
        } else {
            props.replaceValue(key, propIndex, newValue);
        }

        //annotationSaver.updateAnnotation(annotation);
        return true;
    }
    //==========================================================================

    /**
     * code reused from :
     * http://stackoverflow.com/questions/105034/how-to-create-a-guid-uuid-in-javascript
     *
     * @return an rfc4122 version 4 compliant UUID
     */
    public static native String generateUUID() /*-{ return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {var r = Math.random()*16|0,v=c=='x'?r:r&0x3|0x8;return v.toString(16);}); }-*/;

    @Override
    public final String getNewAnnotationId() {
        // AnnotationId must be:
        //   - unique (to avoid collision if 2 client instances use same authentication)
        //   - locally generated (because the server is not always available, if we allow offline processing)
        return generateUUID();
    }
}
