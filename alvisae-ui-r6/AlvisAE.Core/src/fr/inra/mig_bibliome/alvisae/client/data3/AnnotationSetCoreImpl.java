/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetCore;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetType;

/**
 *
 * @author fpapazian
 */
class AnnotationSetCoreImpl extends JavaScriptObject implements AnnotationSetCore {

    protected AnnotationSetCoreImpl() {
    }

    @Override
    public final native int getId() /*-{ return this.id; }-*/;

    private final native String _getType() /*-{ return this.type; }-*/;
    
    @Override
    public final AnnotationSetType getType() {
        return AnnotationSetType.withName(_getType());
    }

    @Override
    public final native int getTaskId() /*-{ return this.task_id; }-*/;

    @Override
    public final native String getPublicationDate() /*-{ return this.published; }-*/;
    
    @Override
    public final native String getCreationDate() /*-{ return this.created; }-*/;

    @Override
    public final native String getDescription() /*-{ return this.description; }-*/;

    @Override
    public final native int getRevision() /*-{ return this.revision; }-*/;

    @Override
    public final native boolean isHead() /*-{ return this.head; }-*/;
    
    @Override
    public final native int getOwner() /*-{ return this.owner; }-*/;

    @Override
    public final native boolean isInvalidated() /*-{ if (this.hasOwnProperty('invalidated')) { return this.invalidated; } else { return false } }-*/;
    
    private final static String INVALIDFIELD_PREFIX = "invalid field ";

    /**
     * Check that the AnnoationSet parsed from a JSON string conforms to the expected structure 
     * @throws IllegalArgumentException
     */
    public final void checkStructure() {
        Integer id = null;
        try {
            id = getId();
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "AnnotationSet.Id", ex);
        }
        if (id == null) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "-> AnnotationSet.Id should not be empty");
        }
        try {
            AnnotationSetType type = getType();
            if (type == null) {
                throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "-> AnnotationSetType should not be empty");
            }
            try {
                String description = getDescription();
            } catch (Exception ex) {
                throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "Description", ex);
            }
            try {
                int owner = getOwner();
            } catch (Exception ex) {
                throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "Owner", ex);
            }
            try {
                int revision = getRevision();
            } catch (Exception ex) {
                throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "Revision", ex);
            }
            try {
                boolean head = isHead();
            } catch (Exception ex) {
                throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "Head", ex);
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + " in AnnotationSet Id=" + id, ex);
        }
    }
}
