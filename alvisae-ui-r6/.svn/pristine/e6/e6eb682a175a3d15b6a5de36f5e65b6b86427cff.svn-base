/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.Relation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import fr.inra.mig_bibliome.alvisae.client.data3.UserAuthorizationsImpl.JsArrayIntegerDecorator;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSet;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotationImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author fpapazian
 */
public class AnnotatedTextImpl extends JavaScriptObject implements AnnotatedText {

    private static interface AnnotationFilter {

        public boolean accept(Annotation annotation);
    }

    private static interface SpecifiedAnnotationFilter {

        public boolean accept(AnnotationSet annotationSet, Annotation annotation);
    }

    static interface AnnotationProcessor {

        public boolean process(AnnotationSetImpl annotationSet, AnnotationImpl annotation);
    }

    /**
     * @throws IllegalArgumentException if invalid JSON string or structure not
     * corresponding to AnnotatedText
     */
    public final static AnnotatedTextImpl createFromJSON(String jsonStr) {
        AnnotatedTextImpl result = JsonUtils.safeEval(jsonStr).cast();
        result.addBackRef();
        return result;
    }
    //--------------------------------------------------------------------------

    protected AnnotatedTextImpl() {
    }

    @Override
    public final native DocumentImpl getDocument() /*-{ return this.document; }-*/;

    @Override
    public final native AnnotationSchemaDefinition getAnnotationSchema() /*-{ return this.schema; }-*/;

    @Override
    public final native TaskDefinition getEditedTask() /*-{ if (this.hasOwnProperty('edited_task')) { return this['edited_task']; } else { return null; } }-*/;

    @Override
    public final Set<Integer> getOutdatedAnnotationSetIds() {
        return new HashSet<Integer>( new JsArrayIntegerDecorator(_getOutdatedAnnotationSetIds()));
    }
    
    private final native JsArrayInteger _getOutdatedAnnotationSetIds() /*-{ if (this.hasOwnProperty('outdated')) { return this['outdated']; } else { return []; } }-*/;

    final native AnnotationSetListImpl _getAnnotationSetList() /*-{ return this.annotation; }-*/;

    @Override
    public final List<? extends AnnotationSetImpl> getAnnotationSetList() {
        return new JsArrayDecorator<AnnotationSetImpl>(_getAnnotationSetList());
    }

    final native AnnotationSetInfoListImpl _getAnnotationSetInfoList() /*-{ return this.annotation_sets; }-*/;

    @Override
    public final List<? extends AnnotationSetInfoImpl> getAnnotationSetInfoList() {
        return new JsArrayDecorator<AnnotationSetInfoImpl>(_getAnnotationSetInfoList());
    }

    final void addAdditionalAnnotationSet(AnnotationSetImpl annotationSet) {
        removeBackRef();
        _getAnnotationSetList().addAnnotationSet(annotationSet);
        addBackRef();
    }

    @Override
    public final void scanAnnotations(final AnnotatedText.AnnotationProcessor processor) {
        scanAnnotations(new AnnotatedTextImpl.AnnotationProcessor() {
            @Override
            public boolean process(AnnotationSetImpl annotationSet, AnnotationImpl annotation) {
                return processor.process(annotationSet, annotation);
            }
        });
    }

    public final void scanAnnotations(AnnotatedTextImpl.AnnotationProcessor processor) {
        AnnotationSetListImpl asl = _getAnnotationSetList();
        for (int as = 0; as < asl.length(); as++) {
            AnnotationSetImpl annotationSet = asl.get(as);
            annotationSet.scanAnnotations(processor);
        }
    }

    private final void addBackRef() {
        scanAnnotations(new AnnotatedTextImpl.AnnotationProcessor() {
            @Override
            public boolean process(AnnotationSetImpl annotationSet, AnnotationImpl annotation) {
                annotation.setAnnotatedText(AnnotatedTextImpl.this);
                return true;
            }
        });
    }

    private final AnnotatedTextImpl removeBackRef() {
        final AnnotatedTextImpl[] annotatedText = {null};
        scanAnnotations(new AnnotatedTextImpl.AnnotationProcessor() {
            @Override
            public boolean process(AnnotationSetImpl annotationSet, AnnotationImpl annotation) {
                annotatedText[0] = annotation.getAnnotatedText();
                annotation.setAnnotatedText(null);
                return true;
            }
        });
        return annotatedText[0];
    }

    @Override
    public final String getJSON() {
        String jsonStr = null;
        try {
            removeBackRef();
            jsonStr = new JSONObject(this).toString();
        } finally {
            addBackRef();
        }
        return jsonStr;
    }

    @Override
    public final Collection<Annotation> getAnnotations() {
        return getFilteredAnnotations(null);
    }

    @Override
    public final Annotation getAnnotation(final String annotationId) {
        final List<Annotation> liste = new ArrayList<Annotation>();
        scanAnnotations(new AnnotatedTextImpl.AnnotationProcessor() {
            @Override
            public boolean process(AnnotationSetImpl annotationSet, AnnotationImpl annotation) {
                if (annotationId.equals(annotation.getId())) {
                    liste.add(annotation);
                    return false;
                } else {
                    return true;
                }
            }
        });

        return liste.isEmpty() ? null : liste.get(0);
    }

    public final List<Annotation> getFilteredAnnotations(final AnnotationFilter filter) {
        final List<Annotation> liste = new ArrayList<Annotation>();
        scanAnnotations(new AnnotatedTextImpl.AnnotationProcessor() {
            @Override
            public boolean process(AnnotationSetImpl annotationSet, AnnotationImpl annotation) {
                if (filter == null || filter.accept(annotation)) {
                    liste.add(annotation);
                }
                return true;
            }
        });
        return liste;
    }

    public final List<SpecifiedAnnotation> getFilteredSpecifiedAnnotations(final SpecifiedAnnotationFilter filter) {
        final List<SpecifiedAnnotation> liste = new ArrayList<SpecifiedAnnotation>();
        scanAnnotations(new AnnotatedTextImpl.AnnotationProcessor() {
            @Override
            public boolean process(AnnotationSetImpl annotationSet, AnnotationImpl annotation) {
                if (filter == null || filter.accept(annotationSet, annotation)) {
                    liste.add(new SpecifiedAnnotationImpl(annotation, annotationSet));
                }
                return true;
            }
        });
        return liste;
    }

    @Override
    public final Collection<Annotation> getAnnotations(final String type) {
        if (type == null) {
            throw new NullPointerException("Annotation type should not be null");
        }
        return getFilteredAnnotations(new AnnotationFilter() {
            @Override
            public boolean accept(Annotation annotation) {
                return type.equals(annotation.getAnnotationType());
            }
        });
    }

    @Override
    public final List<Annotation> getAnnotations(final AnnotationKind kind) {
        if (kind == null) {
            throw new NullPointerException("AnnotationKind should not be null");
        }
        return getFilteredAnnotations(new AnnotationFilter() {
            @Override
            public boolean accept(Annotation annotation) {
                return kind.equals(annotation.getAnnotationKind());
            }
        });
    }

    public final List<SpecifiedAnnotation> getSpecifiedAnnotations(final AnnotationKind kind) {
        if (kind == null) {
            throw new NullPointerException("AnnotationKind should not be null");
        }
        return getFilteredSpecifiedAnnotations(new SpecifiedAnnotationFilter() {
            @Override
            public boolean accept(AnnotationSet annotationSet, Annotation annotation) {
                return kind.equals(annotation.getAnnotationKind());
            }
        });
    }

    private final Collection<Fragment> getBoundedFragments(Collection<Fragment> fragments) {
        int maxPos = getDocument().getContents().length() - 1;
        for (Fragment f : fragments) {
            if (f.getStart() < 0) {
                f.setStart(0);
            } else if (f.getStart() >= maxPos) {
                f.setStart(maxPos);
            }
            if (f.getEnd() < 0) {
                f.setEnd(0);
            } else if (f.getEnd() >= maxPos) {
                f.setEnd(maxPos);
            }
        }
        return fragments;
    }

    public final AnnotationImpl createLooseTextAnnotation(String id, String type, Collection<Fragment> fragments, Properties props, List<AnnotationBackReference> backRefs) {
        AnnotationImpl annotation = AnnotationImpl.create(id, AnnotationKind.TEXT, type);
        annotation.getTextBinding().addFragments(getBoundedFragments(fragments));
        annotation.setProperties(props);
        annotation.setSourceAnnotations(backRefs);
        annotation.setAnnotatedText(this);
        return annotation;
    }

    public final AnnotationImpl createLooseGroupAnnotation(String id, String type, Collection<AnnotationReference> components, Properties props, List<AnnotationBackReference> backRefs) {
        AnnotationImpl annotation = AnnotationImpl.create(id, AnnotationKind.GROUP, type);
        annotation.getAnnotationGroup().addComponents(components);
        annotation.setProperties(props);
        annotation.setSourceAnnotations(backRefs);
        annotation.setAnnotatedText(this);
        return annotation;
    }

    public final AnnotationImpl createLooseRelationAnnotation(String id, String type, Map<String, AnnotationReference> arguments, Properties props, List<AnnotationBackReference> backRefs) {
        AnnotationImpl annotation = AnnotationImpl.create(id, AnnotationKind.RELATION, type);
        Relation relation = annotation.getRelation();
        for (Entry<String, AnnotationReference> e : arguments.entrySet()) {
            relation.setArgument(e.getKey(), e.getValue(), true);
        }
        annotation.setProperties(props);
        annotation.setSourceAnnotations(backRefs);
        annotation.setAnnotatedText(this);
        return annotation;
    }
    private final static String INVALIDFIELD_PREFIX = "AnnotatedText : invalid field ";

    /**
     * Check that the annotatedText parsed from a JSON string conforms to the
     * expected structure
     *
     * @throws IllegalArgumentException
     */
    public final void checkStructure() {
        //
        DocumentImpl document;
        try {
            document = getDocument();
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "Document", ex);
        }
        if (document == null) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "-> Document should not be empty");
        }
        document.checkStructure();
        //
        AnnotationSetListImpl annotationSets = null;
        try {
            annotationSets = _getAnnotationSetList();
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "AnnotationSets", ex);
        }
        if (annotationSets == null) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "-> AnnotationSets should not be empty");
        }

        for (AnnotationSetImpl annotationSet : new JsArrayDecorator<AnnotationSetImpl>(annotationSets)) {
            annotationSet.checkStructure();
            for (AnnotationImpl annotation : annotationSet.getTextAnnotations()) {
                annotation.checkStructure();
            }
            for (AnnotationImpl annotation : annotationSet.getGroups()) {
                annotation.checkStructure();
            }
            for (AnnotationImpl annotation : annotationSet.getRelations()) {
                annotation.checkStructure();
            }
        }
        //
        AnnotationSetInfoListImpl annotationSetInfos = null;
        try {
            annotationSetInfos = _getAnnotationSetInfoList();
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "AnnotationSetInfos", ex);
        }
        if (annotationSetInfos == null) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "-> AnnotationSetInfos should not be empty");
        }
        for (AnnotationSetInfoImpl annotationSet : new JsArrayDecorator<AnnotationSetInfoImpl>(annotationSetInfos)) {
            annotationSet.checkStructure();
        }
        //
        try {
            AnnotationSchemaDefinition schema = getAnnotationSchema();
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "Schema", ex);
        }
    }
}
