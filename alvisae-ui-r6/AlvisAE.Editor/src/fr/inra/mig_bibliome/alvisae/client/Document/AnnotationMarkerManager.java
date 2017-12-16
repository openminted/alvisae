/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import fr.inra.mig_bibliome.alvisae.client.Edit.TextAnnotationCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.data3.*;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages text selection in the document view
 *
 * @author fpapazian
 */
public class AnnotationMarkerManager {

    private static final String DocContainerId = "aae_DocumentContainer";
    private static final String TextContainerId = "aae_DocTextContainer";
    private static final String DocumentCanvasId = "aae_DocumentCanvas";
    private static final String OverlayContainerId = "aae_OverlayContainer";
    private static int instanceNb = 0;

// -----------------------------------------------------------------------------
    private static native String getId(Node node) /*-{ if (node!=null) { return node.id; } else { return null; } }-*/;

    private static native String getTextContent(Node node) /*-{ if (node!=null) { return node.textContent; } else { return null; } }-*/;

// -----------------------------------------------------------------------------
    private static native int getNbRanges(JavaScriptObject ranges) /*-{ return ranges.length; }-*/;

// -----------------------------------------------------------------------------
    private static native DOMRange getRange(JavaScriptObject ranges, int index) /*-{ return ranges[index]; }-*/;

// -----------------------------------------------------------------------------
    private static native JavaScriptObject getSelectedDOMRanges() /*-{ return $wnd._getSelectedRawRanges();  }-*/;

// -----------------------------------------------------------------------------
    //retrieve the "raw" range provided by the browser
    private static native DOMRange newRawRange() /*-{ return new $wnd.RawRange(); }-*/;

// -----------------------------------------------------------------------------
    public static native float getInternetExplorerVersion() /*-{
     var rv = -1;
     if ($wnd.navigator.appName == 'Microsoft Internet Explorer')
     {
     var ua = $wnd.navigator.userAgent;
     var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
     if (re.exec(ua) != null)
     rv = parseFloat( RegExp.$1 );
     }
     return rv;
     }-*/;
// -----------------------------------------------------------------------------

    public static native void restoreNativeSelection() /*-{
     if ($doc.selection) {
     //IE :
     throw ("IE support not implemented yet!!");
     } else {
     $wnd.getSelection().removeAllRanges();
     for (i=0; i<$wnd.backupSelection.length; i++) {
     var range = $wnd.backupSelection[i];
     $wnd.getSelection().addRange(range);
     }
     }
     }-*/;
// -----------------------------------------------------------------------------

    public static native void clearNativeSelection() /*-{
     $wnd.backupSelection = new Array();
     if ($doc.selection) {
     //IE :
     throw ("IE support not implemented yet!!");
     } else {
     $wnd.getSelection().removeAllRanges();
     }
     }-*/;
// -----------------------------------------------------------------------------

    //FIXME : replace by DOM.isOrHasChild()
    public static boolean isDescendantOf(Element testedElmt, Element parentElmt) {
        if ((testedElmt == null) || (parentElmt == null)) {
            return false;
        } else if (testedElmt.equals(parentElmt)) {
            return true;
        } else {
            return isDescendantOf(testedElmt.getParentElement(), parentElmt);
        }
    }
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
    public boolean isTextSelected() {
        JavaScriptObject ranges = getSelectedDOMRanges();
        return getNbRanges(ranges) > 0;
    }

    //return all text node ranges covered by the current text selection
    private static ArrayList<DOMRange> _getSelectedRanges(String docContainerId) {
        ArrayList<DOMRange> result = new ArrayList<DOMRange>();

        //retrieve raw DOM ranges
        JavaScriptObject ranges = getSelectedDOMRanges();
        Element docContainer = Document.get().getElementById(docContainerId);
        ArrayList<DOMRange> rangeList = new ArrayList<DOMRange>();
        for (int n = 0; n < getNbRanges(ranges); n++) {
            DOMRange r = getRange(ranges, n);
            //GWT.log(">" + String.valueOf(r.getStartOffset()) + " : "+ String.valueOf(r.getEndOffset()) + " <");

            //check if the range is inside the document panel
            if (isDescendantOf(r.getCommonAncestorContainer(), docContainer)) {
                rangeList.add(r);
            }
        }

        //convert to the corresponding list of range in text nodes
        for (DOMRange r : rangeList) {

            //TODO : properly handle start or end selection within other than text node (e.g. image)

            //TODO : adjacent selection within the same node should be merged

            //TODO : nested selections should be ignored

            DOMRange anchor = newRawRange();

            anchor.setEndOffset(r.getEndOffset());

            Node startTextNode;
            if (r.getStartNode().getNodeType() == Node.TEXT_NODE) {
                startTextNode = r.getStartNode();
                anchor.setStartNode(startTextNode);
                anchor.setStartContainerId(getId(startTextNode.getParentNode()));
                anchor.setStartOffset(r.getStartOffset());

            } else if (r.getStartNode().getNodeType() == Node.ELEMENT_NODE) {
                startTextNode = r.getStartNode().getChild(r.getStartOffset());
                if (startTextNode.getNodeType() == Node.TEXT_NODE) {
                    anchor.setStartNode(startTextNode);
                    anchor.setStartContainerId(getId(startTextNode.getParentNode()));
                    anchor.setStartOffset(0);
                } else {
                    startTextNode = _getNextTextNode(startTextNode, false, r.getCommonAncestorContainer(), null);
                    if (startTextNode != null) {
                        anchor.setStartNode(startTextNode);
                        anchor.setStartContainerId(getId(startTextNode.getParentNode()));
                        anchor.setStartOffset(0);
                    }
                }
            } else {
                startTextNode = null;
                anchor.setStartNode(startTextNode);
                anchor.setStartContainerId("?");
                anchor.setStartOffset(-1);
            }

            Node endTextNode;
            if (r.getEndNode().getNodeType() == Node.TEXT_NODE) {
                endTextNode = r.getEndNode();
                anchor.setEndNode(endTextNode);
                anchor.setEndContainerId(getId(endTextNode.getParentNode()));
                anchor.setEndOffset(r.getEndOffset());
            } else if (r.getEndNode().getNodeType() == Node.ELEMENT_NODE) {

                //FIXME : it seems there is a bug on FF when triple-clicking to select the entire sentence text. Then, the node offset index seems incorrect
                //idem on Chrome

                //GWT.log(" TT: eoff=" + r.getEndOffset() + " soff=" + r.getStartOffset() + " eNdVal=" + r.getEndNode().getNodeValue()  + " eNdChild=" + r.getEndNode().getChildCount());
                endTextNode = r.getEndNode().getChild(r.getEndOffset());

                if (endTextNode.getNodeType() == Node.TEXT_NODE) {
                    anchor.setEndNode(endTextNode);
                    anchor.setEndContainerId(getId(endTextNode.getParentNode()));
                    anchor.setEndOffset(endTextNode.getNodeValue().length());
                } else {
                    endTextNode = _getPreviousTextNode(endTextNode, false, r.getCommonAncestorContainer());
                    if (endTextNode != null) {
                        anchor.setEndNode(endTextNode);
                        anchor.setEndContainerId(getId(endTextNode.getParentNode()));
                        anchor.setEndOffset(endTextNode.getNodeValue().length());
                    }
                }

            } else {
                endTextNode = null;
                anchor.setEndNode(endTextNode);
                anchor.setEndContainerId("?");
                anchor.setEndOffset(-1);
            }

            if (anchor.getStartContainerId().equals("?")) {
                anchor.setStartContainerId(anchor.getEndContainerId());
                anchor.setStartNode(endTextNode);
                if (endTextNode != null) {
                    anchor.setStartOffset(0);
                }
            }
            if (anchor.getEndContainerId().equals("?")) {
                anchor.setEndContainerId(anchor.getStartContainerId());
                anchor.setEndNode(startTextNode);
                if (startTextNode != null) {
                    anchor.setEndOffset(startTextNode.getNodeValue().length());
                }
            }

            if (anchor.getStartNode() != anchor.getEndNode()) {

                //A part of the selection span accross multiple nodes

                //keep the end of the selection as a separate anchor limited to the ending text node
                DOMRange lastanchor = newRawRange();
                lastanchor.setStartNode(anchor.getEndNode());
                lastanchor.setStartContainerId(anchor.getEndContainerId());
                lastanchor.setStartOffset(0);
                lastanchor.setEndNode(anchor.getEndNode());
                lastanchor.setEndContainerId(anchor.getEndContainerId());
                lastanchor.setEndOffset(anchor.getEndOffset());

                //store the beginning of the selection as a separate anchor limited to the starting text node
                anchor.setEndNode(anchor.getStartNode());
                anchor.setEndContainerId(anchor.getStartContainerId());
                anchor.setEndOffset(anchor.getEndNode().getNodeValue().length());
                if (anchor.getStartOffset() != anchor.getEndOffset()) {
                    anchor.setText(anchor.getStartNode().getNodeValue().substring(anchor.getStartOffset(), anchor.getEndOffset()));
                    result.add(anchor);
                }

                //find the text nodes inbetween and create new anchors for them
                boolean again = true;
                while (again) {
                    Node nestedNode = _getNextTextNode(anchor.getStartNode(), false, r.getCommonAncestorContainer(), lastanchor.getStartNode());
                    if (nestedNode == null) {
                        again = false;
                    } else if (nestedNode == anchor.getEndNode()) {
                        again = false;
                    } else {
                        anchor = newRawRange();
                        anchor.setStartNode(nestedNode);
                        anchor.setStartContainerId(getId(nestedNode.getParentNode()));
                        anchor.setStartOffset(0);
                        anchor.setEndNode(nestedNode);
                        anchor.setEndContainerId(getId(nestedNode.getParentNode()));
                        anchor.setEndOffset(nestedNode.getNodeValue().length());

                        if ((anchor.getStartOffset() != anchor.getEndOffset()) && (!anchor.getStartContainerId().equals(docContainerId)) && (anchor.getStartNode() != lastanchor.getStartNode())) {
                            anchor.setText(anchor.getStartNode().getNodeValue().substring(anchor.getStartOffset(), anchor.getEndOffset()));
                            result.add(anchor);
                        }
                    }
                }

                //store the end of the selection
                if (lastanchor.getStartOffset() != lastanchor.getEndOffset()) {
                    lastanchor.setText(lastanchor.getStartNode().getNodeValue().substring(lastanchor.getStartOffset(), lastanchor.getEndOffset()));
                    result.add(lastanchor);
                }

            } else if (anchor.getStartOffset() != anchor.getEndOffset()) {
                //keep only ranges of at leat 1 character long
                anchor.setText(anchor.getStartNode().getNodeValue().substring(anchor.getStartOffset(), anchor.getEndOffset()));
                result.add(anchor);
            }

        }
        rangeList.clear();

        return result;
    }

    public Fragment getSelectionBoundariesFragment(AnnotatedTextHandler annotatedTextHnd) {
        Fragment result = null;
        ArrayList<DOMRange> ranges = getSelectedRanges();
        List<Fragment> fragments = computeTargets(ranges);
        if (fragments != null && !fragments.isEmpty()) {
            int start = annotatedTextHnd.getAnnotatedText().getDocument().getContents().length();
            int end = 0;
            for (Fragment frag : fragments) {
                start = Math.min(start, frag.getStart());
                end = Math.max(end, frag.getEnd());
            }
            result = FragmentImpl.create(start, end);
        }
        return result;
    }

// -----------------------------------------------------------------------------
    private static class sizeResult {

        public int size = 0;
        public final Node stopNode;
        public boolean finished = false;

        public sizeResult(Node stopNode) {
            this.stopNode = stopNode;
        }
    }

    private static int getTextSizeFromNodeToNode(Node rootNode, Node stopNode) {
        sizeResult result = new sizeResult(stopNode);
        _getTextSizeFromNodeToNode(rootNode, result);
        return result.size;
    }

    private static void _getTextSizeFromNodeToNode(Node rootNode, sizeResult result) {
        if (rootNode == result.stopNode) {
            result.finished = true;
        } else if (rootNode.getNodeType() == Node.TEXT_NODE) {
            result.size += rootNode.getNodeValue().length();
        } else {
            for (int i = 0; i < rootNode.getChildCount(); i++) {
                _getTextSizeFromNodeToNode(rootNode.getChild(i), result);
                if (result.finished) {
                    break;
                }
            }
        }
    }

// -----------------------------------------------------------------------------
    private static native AnnotationMarkerIpml createAnnotationMarkerIpml() /*-{
     return new $wnd.AnnotationMarkerIpml();
     }-*/;

    private static AnnotationMarkerIpml _RangeToMarkerCoordinates(DOMRange range, Node textContainer) {
        if (textContainer != null) {
            AnnotationMarkerIpml anchor = createAnnotationMarkerIpml();
            anchor.setNode(null);
            anchor.setContainerId(getId(textContainer));

            //offset are relative to the text container, not absolute
            int textBefore = getTextSizeFromNodeToNode(textContainer, range.getStartNode());
            anchor.setStartCharacterOffset(textBefore + range.getStartOffset());
            anchor.setEndCharacterOffset(textBefore + range.getEndOffset());
            anchor.setText(getTextContent(textContainer).substring(anchor.getStartCharacterOffset(), anchor.getEndCharacterOffset()));

            return anchor;
        }
        return null;
    }

// -----------------------------------------------------------------------------
    private static Node _getNextTextNode(Node startNode, boolean includingStartNode, Node rootNode, Node stopNode) {
        if (startNode == stopNode) {
            return null;
        } else if ((startNode.getNodeType() == Node.TEXT_NODE) && (includingStartNode)) {
            return startNode;
        } else if (startNode.getFirstChild() != null) {
            return _getNextTextNode(startNode.getFirstChild(), true, rootNode, stopNode);
        } else if (startNode.getNextSibling() != null) {
            return _getNextTextNode(startNode.getNextSibling(), true, rootNode, stopNode);
        } else {
            while ((startNode != rootNode) && (startNode.getParentNode() != null) && (startNode.getParentNode().getNextSibling() == null)) {
                startNode = startNode.getParentNode();
            }
            if ((startNode != rootNode) && (startNode.getParentNode() != null) && (startNode.getParentNode().getNextSibling() != null)) {
                return _getNextTextNode(startNode.getParentNode().getNextSibling(), true, rootNode, stopNode);
            } else {
                return null;
            }
        }
    }

    private static Node _getPreviousTextNode(Node startNode, boolean includingStartNode, Node rootNode) {
        if ((startNode.getNodeType() == Node.TEXT_NODE) && (includingStartNode)) {
            return startNode;
        } else if (startNode.getLastChild() != null) {
            return _getPreviousTextNode(startNode.getLastChild(), true, rootNode);
        } else if (startNode.getPreviousSibling() != null) {
            return _getPreviousTextNode(startNode.getPreviousSibling(), true, rootNode);
        } else if ((startNode != rootNode) && (startNode.getParentNode() != null) && (startNode.getParentNode().getPreviousSibling() != null)) {
            return _getPreviousTextNode(startNode.getParentNode().getPreviousSibling(), true, rootNode);
        } else {
            return null;
        }
    }
    // -------------------------------------------------------------------------
    private int numOffset = 0;
    //TODO : handle discountinuous id after marker removal
    private AnnotatedTextHandler sourceDoc;
    public HashMap<String, String> htmlFragmentsByContainerIds = new HashMap<String, String>();
    private ArrayList<DOMRange> lastSelectedRanges = null;
    private List<Fragment> lastSelectedTargets = null;
    private final int instanceNum;

    public AnnotationMarkerManager() {
        instanceNb++;
        instanceNum = instanceNb;
    }

    public ArrayList<DOMRange> getSelectedRanges() {
        if (lastSelectedRanges == null) {
            lastSelectedRanges = _getSelectedRanges(getDocContainerId());
        }
        return lastSelectedRanges;
    }

    /**
     * Clear the stored text selection
     */
    public void clearLastSelection() {
        lastSelectedRanges = null;
        lastSelectedTargets = null;
    }

    // -------------------------------------------------------------------------
    public String getDocContainerId() {
        return DocContainerId + ":" + instanceNum + ".";
    }

    public String getTextContainerId() {
        return TextContainerId + ":" + instanceNum + ".";
    }

    public String getDocumentCanvasId() {
        return DocumentCanvasId + ":" + instanceNum + ".";
    }

    public String getOverlayContainerId() {
        return OverlayContainerId + ":" + instanceNum + ".";
    }

    // -------------------------------------------------------------------------
    // globally create all annotation Markers for the selected ranges
    public void createMarkersForTargets(AnnotatedTextHandler annotatedDoc, AnnotationSchemaDefinition params, String newAnnotationType, List<Fragment> targets, Properties props) {
        String annotationId = annotatedDoc.getNewAnnotationId();

        //store the new annotation id and its targets
        TextAnnotationCreationEdit annCreate = new TextAnnotationCreationEdit(annotatedDoc, annotationId, newAnnotationType, targets, props, null);
        annCreate.redo();

    }

    public void refreshDocument() {
        Element newDoc = AnnotatedTextProcessor.getMarkedDoc(sourceDoc, getTextContainerId());

        Element textContainer = Document.get().getElementById(getTextContainerId());
        if (textContainer != null) {
            textContainer.removeFromParent();
        }
        Element docContainer = Document.get().getElementById(getDocContainerId());
        docContainer.appendChild(newDoc);
     }

    // -------------------------------------------------------------------------
    public void reset(AnnotatedTextHandler sourceDoc) {
        if (sourceDoc != null) {
            numOffset = sourceDoc.getAnnotations().size();

        } else {
            numOffset = 0;

        }
        this.sourceDoc = sourceDoc;
    }

    // -------------------------------------------------------------------------
    public List<Fragment> computeTargets(List<DOMRange> ranges) {

        if ((ranges == null) || (ranges.isEmpty())) {
            return null;
        } else {
            if (lastSelectedTargets != null && ranges == lastSelectedRanges) {
                return lastSelectedTargets;
            } else {
                ArrayList<Fragment> unmerged = new ArrayList<Fragment>();
                Element docContainer = Document.get().getElementById(getTextContainerId());
                for (DOMRange r : ranges) {
                    AnnotationMarkerIpml anchor = _RangeToMarkerCoordinates(r, docContainer);
                    Fragment t = FragmentImpl.create(anchor.getStartCharacterOffset(), anchor.getEndCharacterOffset());
                    unmerged.add(t);

                }
                List<Fragment> merged = TextBindingImpl.mergeOverlappingFragments(unmerged);
                lastSelectedTargets = merged;
                return merged;
            }
        }
    }
}
//--========================================================================--//

