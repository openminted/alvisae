/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.core.client.GWT;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationStatusChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextProcessor.AnnotationFragmentMarkerMapper;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import java.util.*;

/**
 * This class is associated to one specific view, and keep track of the mapping
 * between widget and the actual data.
 *
 * @author fpapazian
 */
public class AnnotationDocumentViewMapper {

    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    private static HashMap<DocumentView, AnnotationDocumentViewMapper> mapperByDocView = new HashMap<DocumentView, AnnotationDocumentViewMapper>();
    private static HashMap<AnnotatedText, AnnotationDocumentViewMapper> mapperByDocument = new HashMap<AnnotatedText, AnnotationDocumentViewMapper>();

    public static AnnotationDocumentViewMapper newMapper(AnnotatedTextHandler annotatedDoc, DocumentView docView) {
        AnnotationDocumentViewMapper mapper = new AnnotationDocumentViewMapper(annotatedDoc);
        mapperByDocView.put(docView, mapper);
        mapperByDocument.put(annotatedDoc.getAnnotatedText(), mapper);
        return mapper;
    }

    public static AnnotationDocumentViewMapper getMapper(DocumentView docView) {
        return mapperByDocView.get(docView);
    }

    public static AnnotationDocumentViewMapper getMapper(AnnotatedText annotatedDoc) {
        return mapperByDocument.get(annotatedDoc);
    }
    private final AnnotatedTextHandler annotatedText;
    private AnnotationFragmentMarkerMapper annFragMarkmap = new AnnotationFragmentMarkerMapper();
    private HashSet<String> veiledAnnotation = new HashSet<String>();
    private final AnnotatedTextHandler annotatedTextHandler;

    public AnnotationDocumentViewMapper(AnnotatedTextHandler annotatedDoc) {
        this.annotatedText = annotatedDoc;
        annotatedTextHandler = AnnotatedTextHandler.getHandler(annotatedText.getAnnotatedText());
    }

    // -------------------------------------------------------------------------
    public AnnotatedTextImpl getAnnotatedText() {
        return annotatedText.getAnnotatedText();
    }

    public AnnotatedTextHandler getAnnotatedTextHandler() {
        return annotatedText;
    }

    public String getAnnotationIdFromMakerId(String markerId) {
        return annFragMarkmap.getAnnotationIdForMaker(markerId);
    }

    public ArrayList<String> getMarkerIdsFromAnnotationId(String annotationId) {
        return annFragMarkmap.getMarkerIdsForAnnotation(annotationId);
    }

    public Set<String> getAnnotationIds() {
        return annFragMarkmap.getAnnotationIds();
    }

    public Annotation getAnnotation(String annotationId) {
        return annotatedTextHandler.getAnnotation(annotationId);
    }

    public ArrayList<String> getMarkerIdsForFragment(String annotationId, Fragment fragment) {
        return annFragMarkmap.getMarkerIdsForFragment(annotationId, fragment);
    }

    public Fragment getFragmentForMarker(String markerId) {
        return annFragMarkmap.getFragmentForMarker(markerId);
    }
    //==========================================================================

    public boolean isVeiled(String annotationId) {
        return veiledAnnotation.contains(annotationId);
    }

    public void setVeiled(String annotationId) {
        veiledAnnotation.add(annotationId);
        injector.getMainEventBus().fireEvent(new AnnotationStatusChangedEvent(annotationId));
    }

    public void setUnveiled(String annotationId) {
        veiledAnnotation.remove(annotationId);
        injector.getMainEventBus().fireEvent(new AnnotationStatusChangedEvent(annotationId));
    }

    public boolean toggleVeiledStatus(String annotationId) {
        boolean veiledStatus = isVeiled(annotationId);
        if (veiledStatus) {
            setUnveiled(annotationId);
        } else {
            setVeiled(annotationId);
        }
        return !veiledStatus;
    }

    public Set<String> getVeiledAnnotationIds() {
        return veiledAnnotation;
    }

    //==========================================================================
    public List<Annotation> getReferencesToAnnotation(String annotationId) {
        return annotatedTextHandler.getReferencesToAnnotation(annotationId);
    }

    public boolean isFormattingAnnotation(String annotationId) {
        return annotatedTextHandler.isFormattingAnnotation(annotationId);
    }

    public Annotation addAnnotation(String annotationId, String annotationType, Collection<Fragment> targets, Properties props, List<AnnotationBackReference> backRefs) {
        return annotatedTextHandler.addAnnotation(annotationId, annotationType, targets, props, backRefs);
    }

    public boolean removeAnnotation(Annotation annotation) {
        return annotatedTextHandler.removeAnnotation(annotation);
    }

    public void addAnnotationFragments(String annotationId, List<Fragment> fragments) {
        annotatedTextHandler.addAnnotationFragments(annotationId, fragments);
    }

    public boolean removeAnnotationFragment(String annotationId, List<Fragment> fragments) {
        return annotatedTextHandler.removeAnnotationFragment(annotationId, fragments);
    }

    public void setAnnotationFragment(String annotationId, Collection<Fragment> fragments) {
        annotatedTextHandler.setAnnotationFragment(annotationId, fragments);
    }

    public Annotation modifyAnnotationType(String id, String newAnnotationType) {
        return annotatedTextHandler.modifyAnnotationType(id, newAnnotationType);
    }
    //==========================================================================
    public Annotation addGroup(String annotationId, String groupType, List<AnnotationReference> components, Properties props, List<AnnotationBackReference> backRefs) {
        return annotatedTextHandler.addGroup(annotationId, groupType, components, props, backRefs);
    }

    public Annotation addGroup(String groupType, List<AnnotationReference> components, Properties props, List<AnnotationBackReference> backRefs) {
        return annotatedTextHandler.addGroup(groupType, components, props, backRefs);
    }

    public boolean removeGroup(Annotation annotation) {
        return annotatedTextHandler.removeGroup(annotation);
    }

    public Annotation modifyGroup(String id, String newType, List<AnnotationReference> newComponents) {
        return annotatedTextHandler.modifyGroup(id, newType, newComponents);
    }
    //==========================================================================

    public boolean removeRelation(Annotation annotation) {
        return annotatedTextHandler.removeRelation(annotation);
    }

    public Annotation addRelation(String annotationId, String relationType, Map<String, AnnotationReference> argumentRoleMap, Properties props, List<AnnotationBackReference> backRefs) {
        return annotatedTextHandler.addRelation(annotationId, relationType, argumentRoleMap, props, backRefs);
    }

    public Annotation addRelation(String relationType, Map<String, AnnotationReference> argumentRoleMap, Properties props, List<AnnotationBackReference> backRefs) {
        return annotatedTextHandler.addRelation(relationType, argumentRoleMap, props, backRefs);
    }

    public Annotation modifyRelation(String id, String newRelType, Map<String, AnnotationReference> newArgumentRoleMap) {
        return annotatedTextHandler.modifyRelation(id, newRelType, newArgumentRoleMap);
    }

    //==========================================================================
    public boolean replaceProperty(Annotation annotation, String key, List<String> newValues) {
        return annotatedTextHandler.replaceProperty(annotation, key, newValues);
    }

    public boolean replaceProperty(Annotation annotation, String key, int propIndex, String oldValue, String newValue) {
        return annotatedTextHandler.replaceProperty(annotation, key, propIndex, oldValue, newValue);
    }

    //==========================================================================
    public void setAnnotationFragmentMarkerMapper(AnnotationFragmentMarkerMapper annFragMarkmap) {
        this.annFragMarkmap = annFragMarkmap;
    }

    // -------------------------------------------------------------------------
    public String exportAnchorMarkers() {
        StringBuilder r = new StringBuilder();

        r.append("<Annotations>\n");
        for (String aId : getAnnotationIds()) {
            Annotation a = getAnnotation(aId);
            r.append("  <Annotation id='").append(aId).append("' type='").append(a.getAnnotationType()).append("'>\n");
            //PROTOBREAK
            /*
             for (Fragment t : a.getTargets()) {
             r.append("    <Target>\n");
             r.append("      <containerId>").append(t.getContainerId()).append("</containerId>\n");
             r.append("      <startOffset>").append(t.getStartCharacterOffset()).append("</startOffset>\n");
             r.append("      <endOffset>").append(t.getEndCharacterOffset()).append("</endOffset>\n");
             r.append("    </Target>\n");
             }
             *
             */
            r.append("  </Annotation>\n");
        }
        r.append("\n</Annotations>");
        return r.toString();
    }
}
