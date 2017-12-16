/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

public class DOMRangeImpl extends JavaScriptObject implements DOMRange {

    protected DOMRangeImpl() {
    }

    public final native String getStartContainerId() /*-{ return this.startContainer; }-*/;

    public final native Node getStartNode() /*-{ return this.startNode; }-*/;

    public final native int getStartOffset() /*-{ return this.startOffset; }-*/;

    public final native String getEndContainerId() /*-{ return this.endContainer; }-*/;

    public final native int getEndOffset() /*-{ return this.endOffset; }-*/;

    public final native String getText() /*-{ return this.text; }-*/;

    public final native Node getEndNode() /*-{ return this.endNode; }-*/;

    public final native Element getCommonAncestorContainer() /*-{ return this.commonAncestorContainer; }-*/;

    public final native void setEndContainerId(String endContainerId) /*-{ this.endContainer=endContainerId; }-*/;

    public final native void setEndNode(Node endNode) /*-{ this.endNode=endNode; }-*/;

    public final native void setEndOffset(int endOffset) /*-{ this.endOffset=endOffset; }-*/;

    public final native void setStartContainerId(String startContainerId) /*-{ this.startContainer=startContainerId; }-*/;

    public final native void setStartNode(Node startNode) /*-{ this.startNode=startNode; }-*/;

    public final native void setStartOffset(int startOffset) /*-{ this.startOffset=startOffset; }-*/;

    public final native void setText(String text) /*-{ this.text=text; }-*/;

    public final native void setCommonAncestorContainer(Node commonAncestorContainer) /*-{ this.commonAncestorContainer=commonAncestorContainer; }-*/;
}
