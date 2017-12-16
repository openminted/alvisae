/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationSingleValuePropertyEdit;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationGroup;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TermAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Extension.TyDIResRefPropVal;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import fr.inra.mig_bibliome.alvisae.shared.data3.Relation;
import fr.inra.mig_bibliome.alvisae.shared.data3.SourceAnnotations;
import fr.inra.mig_bibliome.alvisae.shared.data3.TextBinding;
import java.util.List;

/**
 * Wrapper used for communication between AlvisAE Editor and TyDI-extension
 *
 * @author fpapazian
 */
public class TermAnnotationBox implements Annotation, TermAnnotation {

    private final AnnotatedTextHandler document;
    private final Annotation annotation;
    private String surfaceForm = null;
    private String lemma = null;
    private final String tyDITermRefPropName;
    private final String tyDIClassRefPropName;
    private final List<String> tyDIConceptRefPropNames;

    public TermAnnotationBox(AnnotatedTextHandler document, Annotation annotation, String tyDITermRefPropName, String tyDIClassRefPropName, List<String> tyDIConceptRefPropNames) {
        this.document = document;
        this.annotation = annotation;
        this.tyDITermRefPropName = tyDITermRefPropName;
        this.tyDIClassRefPropName = tyDIClassRefPropName;
        this.tyDIConceptRefPropNames = tyDIConceptRefPropNames;
    }

    @Override
    public String getSurfaceForm() {
        return surfaceForm == null ? getAnnotationText("") : surfaceForm;
    }

    @Override
    public void setSurfaceForm(String surfaceForm) {
        this.surfaceForm = surfaceForm;
    }

    @Override
    public String getLemma() {
        //FIXME lemma may be computed from included Lemma preannotations, or from the value of a specific this annotation property
        return lemma == null ? getAnnotationText("") : lemma;
    }

    @Override
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    @Override
    public boolean isTyDIClassOrTermGenerator() {
        return tyDITermRefPropName != null || tyDIClassRefPropName != null;
    }

    @Override
    public String getTermExternalId() {
        //shortcut to the property that contains the external Id of the Term corresponding to this annotation
        if (tyDITermRefPropName != null) {
            List<String> vals = annotation.getProperties().getValues(tyDITermRefPropName);
            return vals != null && !vals.isEmpty() ? vals.get(0) : "";
        } else {
            return null;
        }
    }

    //make the property change as an undoable edit
    private void editProperty(String propName, String newValue) {
        List<String> propval = annotation.getProperties().getValues(propName);
        String oldValues = propval == null || propval.isEmpty() ? null : propval.get(0);
        String newValues = null;
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValues = newValue.trim();
        }
        AnnotationSingleValuePropertyEdit propedit = new AnnotationSingleValuePropertyEdit(document, annotation, propName, 0, oldValues, newValues);
        propedit.redo();
    }

    @Override
    public void setTermExternalId(String termExternalId) {
        if (tyDITermRefPropName != null) {
            editProperty(tyDITermRefPropName, termExternalId);
        }
    }

    @Override
    public String getSemClassExternalId() {
        String semClassExternalId = null;
        //shortcut to the property that contains the external Id of the Semantic class corresponding to this annotation
        if (tyDIClassRefPropName != null) {
            List<String> vals = annotation.getProperties().getValues(tyDIClassRefPropName);
            semClassExternalId = vals != null && !vals.isEmpty() ? vals.get(0) : null;
        }
        if (semClassExternalId == null) {
            //return first value that contains a Concept link
            if (tyDIConceptRefPropNames != null && !tyDIConceptRefPropNames.isEmpty()) {
                for (String propName : tyDIConceptRefPropNames) {
                    List<String> vals = annotation.getProperties().getValues(propName);
                    if (vals != null && !vals.isEmpty()) {
                        semClassExternalId = vals.get(0);
                        break;
                    }
                }
            }
        }
        return semClassExternalId;
    }

    @Override
    public void setTyDIResourceRef(TyDIResRefPropVal resourceRef) {
        if (tyDIClassRefPropName != null) {
            editProperty(tyDIClassRefPropName, resourceRef.toUrlWithFragment());
        }
    }

    @Override
    public String getId() {
        return annotation.getId();
    }

    @Override
    public AnnotatedText getAnnotatedText() {
        return annotation.getAnnotatedText();
    }

    @Override
    public AnnotationKind getAnnotationKind() {
        return annotation.getAnnotationKind();
    }

    @Override
    public String getAnnotationType() {
        return annotation.getAnnotationType();
    }

    @Override
    public TextBinding getTextBinding() {
        return annotation.getTextBinding();
    }

    @Override
    public AnnotationGroup getAnnotationGroup() {
        return annotation.getAnnotationGroup();
    }

    @Override
    public Relation getRelation() {
        return annotation.getRelation();
    }

    @Override
    public Properties getProperties() {
        return annotation.getProperties();
    }

    @Override
    public void setProperties(Properties props) {
        annotation.setProperties(props);
    }

    @Override
    public String getAnnotationText(String fragmentSeparator) {
        return annotation.getAnnotationText(fragmentSeparator);
    }

    @Override
    public SourceAnnotations getSourceAnnotations() {
        return annotation.getSourceAnnotations();
    }

    @Override
    public SourceAnnotations setSourceAnnotations(List<AnnotationBackReference> backRefs) {
        return annotation.setSourceAnnotations(backRefs);
    }

    @Override
    public int getCreationDate() {
        return annotation.getCreationDate();
    }
}
