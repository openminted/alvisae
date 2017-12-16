/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3.validation;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationReferenceImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.JsArrayStringDecorator;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.ConsolidableAnnotation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class ConsolidableAnnotationImpl extends JavaScriptObject implements ConsolidableAnnotation {

    protected ConsolidableAnnotationImpl() {
    }

    @Override
    public final AnnotationReference getAnnotationRef() {
        return _getAnnotationRef();
    }

    private final native AnnotationReferenceImpl _getAnnotationRef() /*-{ return this.ann_ref; }-*/;

    @Override
    public final native int getAdjudicationLevel() /*-{ return this.level; }-*/;

    @Override
    public final List<String> getReferencedAnnotationIds() {
        return new ArrayList<String>( new JsArrayStringDecorator( _getReferencedAnnotationIds())  );
    }

    private final native JsArrayString _getReferencedAnnotationIds() /*-{ return this.referenced; }-*/;

    @Override
    public final AnnotationKind getAnnotationKind() {
        return AnnotationKind.values()[_getKind()];
    }

    private final native int _getKind() /*-{ return this.kind; }-*/;
}
