/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Edit;

import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public abstract class AnnotationCreationEdit extends AnnotationEdit {
       
    private final String annotationType;
    private final Properties props;
    private final List<AnnotationBackReference> backRefs;

    public AnnotationCreationEdit(AnnotatedTextHandler document, String annotationType, Properties props, List<AnnotationBackReference> backRefs) {
        super(document);
        this.annotationType = annotationType;
        this.props = props;
        this.backRefs = backRefs;
    }

    public String getAnnotationType() {
        return annotationType;
    }

    public Properties getProperties() {
        return props;
    }

    public List<AnnotationBackReference> getAnnotationBackReferences() {
        return backRefs;
    }
}
