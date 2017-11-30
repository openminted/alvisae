/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationGroup;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author fpapazian
 */
public class AnnotationGroupImpl extends JavaScriptObject implements AnnotationGroup {

    public static final AnnotationGroupImpl create() {
        return JavaScriptObject.createArray().cast();
    }

    protected AnnotationGroupImpl() {
    }

    private final JsArray<AnnotationReferenceImpl> asArray() {
        return this.cast();
    }

    @Override
    public final ArrayList<AnnotationReference> getComponentRefs() {
        JsArray<AnnotationReferenceImpl> array = this.cast();
        ArrayList<AnnotationReference> liste = new ArrayList<AnnotationReference>();
        for (int i = 0, n = array.length(); i < n; ++i) {
            AnnotationReference aRef = (AnnotationReference) array.get(i);
            liste.add(aRef);
        }
        return liste;
    }

    @Override
    public final native void removeComponent(String componentId) /*-{ 
    for (i in this) { 
    if (this[i].ann_id==componentId) {
    this.splice(i,1); break; 
    } 
    }
    }-*/;

    @Override
    public final void removeComponent(AnnotationReference component) {
        String annotationId = component.getAnnotationId();
        removeComponent(annotationId);
    }

    @Override
    public final void removeComponents(Collection<AnnotationReference> components) {
        for (AnnotationReference c : components) {
            String annotationId = c.getAnnotationId();
            removeComponent(annotationId);
        }
    }

    @Override
    public final void addComponent(AnnotationReference component) {
        /*
        if (!annotatedText.getAnnotatedText().equals(component.getAnnotatedText())) {
        throw new IllegalArgumentException("New component does not belong to the same AnnotatedText as its parent");
        }
         */
        
        asArray().push(AnnotationReferenceImpl.create(component));
    }

    @Override
    public final void addComponents(Collection<AnnotationReference> components) {
        for (AnnotationReference c : components) {
            addComponent(c);
        }
    }

    /**
     * Check that the Group parsed from a JSON string conforms to the expected
     * structure
     *
     * @throws IllegalArgumentException
     */
    public final void checkStructure() {
        for (AnnotationReferenceImpl component : new JsArrayDecorator<AnnotationReferenceImpl>(asArray())) {
            component.checkStructure();
        }
    }
}
