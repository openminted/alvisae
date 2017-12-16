/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Document.AnnotationDocumentViewMapper;
import fr.inra.mig_bibliome.alvisae.client.AlvisaeCore;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Provides methods to process an AnnotatedText
 *
 * @author fpapazian
 */
public class AnnotatedTextProcessor {

    //
    static class AnnotationFragment implements Comparable<AnnotationFragment> {

        private final Annotation annotation;
        private final Fragment annotationFragment;
        private final Fragment markerFragment;
        private Integer overallSpan = null;
        private final boolean formattingAnnotation;

        private AnnotationFragment(Annotation annotation, Fragment annotationFragment, Fragment markerFragment, boolean isFormattingAnnotation) {
            this.annotation = annotation;
            this.annotationFragment = annotationFragment;
            this.markerFragment = markerFragment;
            this.formattingAnnotation = isFormattingAnnotation;
        }

        AnnotationFragment(Annotation annotation, Fragment annotationFragment, boolean isFormattingAnnotation) {
            this(annotation, FragmentImpl.create(annotationFragment), FragmentImpl.create(annotationFragment), isFormattingAnnotation);
        }

        /**
         * Implements the convenient sorting order for processing (as if
         * fragments where in a tree, with depth-first traversal)
         */
        @Override
        public int compareTo(AnnotationFragment other) {
            //place before the markerFragment which starts first
            int c = Integer.valueOf(this.markerFragment.getStart()).compareTo(other.markerFragment.getStart());

            //both markerFragments start at same position...
            if (c == 0) {
                //place first the zero-length markerFragment
                if (this.markerFragment.getStart() == this.markerFragment.getEnd()) {
                    c = -1;
                } else if (other.markerFragment.getStart() == other.markerFragment.getEnd()) {
                    c = 1;

                } //none of the markerFragments are zero-sized...
                else {

                    //place before the markerFragment belonging to Formatting AnnotationSet, whatever its size
                    if (this.isFormattingAnnotation() && !other.isFormattingAnnotation()) {
                        c = -1;
                    } else if (!this.isFormattingAnnotation() && other.isFormattingAnnotation()) {
                        c = 1;

                    } //none of the markerFragments are Formatting Annotation
                    else {

                        //place before the markerFragment which ends last
                        c = Integer.valueOf(other.markerFragment.getEnd()).compareTo(this.markerFragment.getEnd());
                        if (c == 0) {
                            //if 2 fragments are identical, place before the one belonging to annotation with the overall largest coverage
                            c = this.getOverallSpan().compareTo(other.getOverallSpan());
                            if (c == 0) {
                                //if 2 fragments are identical, place before the one belonging to the older annotation
                                c = Integer.valueOf(this.annotation.getCreationDate()).compareTo(Integer.valueOf(other.annotation.getCreationDate()));
                                if (c == 0) {
                                    //well... just use id to have a constant relative order
                                    c = this.annotation.getId().compareTo(other.annotation.getId());
                                }
                            }
                        }
                    }
                }
            }
            return c;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public Fragment getMarkerFragment() {
            return markerFragment;
        }

        public Fragment getAnnotationFragment() {
            return annotationFragment;
        }

        private Integer getOverallSpan() {
            if (overallSpan == null) {
                List<Fragment> sf = annotation.getTextBinding().getSortedFragments();
                overallSpan = sf.get(sf.size() - 1).getEnd() - sf.get(0).getStart();
            }
            return overallSpan;
        }

        private AnnotationFragment splitAt(int splitOffset) {
            AnnotationFragment rightBud = new AnnotationFragment(this.getAnnotation(), this.getAnnotationFragment(), FragmentImpl.create(splitOffset, this.getMarkerFragment().getEnd()), this.isFormattingAnnotation());
            this.getMarkerFragment().setEnd(splitOffset);
            return rightBud;
        }

        private boolean isFormattingAnnotation() {
            return formattingAnnotation;
        }
    }

    /**
     * Manage the matching between Annotation & Fragment and their
     * representation in the HTML via Markers
     */
    public static class AnnotationFragmentMarkerMapper {

        HashMap<String, ArrayList<Fragment>> fragmentsByAnnotationId = new HashMap<String, ArrayList<Fragment>>();
        HashMap<String, ArrayList<String>> markerIdsByFragmentId = new HashMap<String, ArrayList<String>>();
        HashMap<String, String> fragmentIdByMarkerId = new HashMap<String, String>();
        HashMap<String, String> annotationIdByFragmentId = new HashMap<String, String>();
        private static final String sep = "@";

        private String getFragmentId(String annotationId, int start, int end) {
            return annotationId + sep + start + sep + end;
        }

        private String getFragmentId(String annotationId, Fragment f) {
            return getFragmentId(annotationId, f.getStart(), f.getEnd());
        }

        private Fragment getFragment(String fragmentId) {
            String[] coord = fragmentId.split(sep);
            if (coord.length == 3) {
                return FragmentImpl.create(Integer.valueOf(coord[1]), Integer.valueOf(coord[2]));
            } else {
                return null;
            }
        }

        public void setMapping(AnnotationFragment annotationFragment, String markerId) {
            String annotationId = annotationFragment.getAnnotation().getId();
            int start = annotationFragment.getAnnotationFragment().getStart();
            int end = annotationFragment.getAnnotationFragment().getEnd();

            boolean fragmentAdded = false;
            ArrayList<Fragment> fragments = fragmentsByAnnotationId.get(annotationId);
            if (fragments == null) {
                fragments = new ArrayList<Fragment>();
                fragmentsByAnnotationId.put(annotationId, fragments);
                fragmentAdded = fragments.add(FragmentImpl.create(start, end));
            }
            String fragmentId = getFragmentId(annotationId, start, end);
            ArrayList<String> markers = markerIdsByFragmentId.get(fragmentId);
            if (markers == null) {
                // only add fragment here to avoid redondancy (and cannot use linked-Set to ensure uniqueness because equals can not be overriden for Javascript Objects...)
                if (!fragmentAdded) {
                    fragments.add(FragmentImpl.create(start, end));
                }

                markers = new ArrayList<String>();
                markerIdsByFragmentId.put(fragmentId, markers);
            }
            markers.add(markerId);

            //reverse mappings
            annotationIdByFragmentId.put(fragmentId, annotationId);
            fragmentIdByMarkerId.put(markerId, fragmentId);
        }

        public Set<String> getAnnotationIds() {
            return fragmentsByAnnotationId.keySet();
        }

        public String getAnnotationIdForMaker(String markerId) {
            return annotationIdByFragmentId.get(fragmentIdByMarkerId.get(markerId));
        }

        public ArrayList<String> getMarkerIdsForAnnotation(String annotationId) {
            ArrayList<String> result = new ArrayList<String>();
            ArrayList<Fragment> fragments = fragmentsByAnnotationId.get(annotationId);
            if (fragments != null) {
                for (Fragment f : fragments) {
                    result.addAll(markerIdsByFragmentId.get(getFragmentId(annotationId, f)));
                }
            }
            return result;
        }

        public Fragment getFragmentForMarker(String markerId) {
            return getFragment(fragmentIdByMarkerId.get(markerId));
        }

        public ArrayList<String> getMarkerIdsForFragment(String annotationId, Fragment fragment) {
            ArrayList<String> result = new ArrayList<String>();
            ArrayList<String> markers = markerIdsByFragmentId.get(getFragmentId(annotationId, fragment));
            if (markers != null) {
                result.addAll(markers);
            }
            return result;
        }
    }
    // -------------------------------------------------------------------------

    /**
     * Insert a string into the Net Text after the specified character position,
     * and update the Annotations coordinates consequently
     */
    public static final void insertTextAfterCharacter(AnnotatedText annotatedText, String textToInsert, int characterPosition) {
        String oldtxt = annotatedText.getDocument().getContents();
        String newtxt = oldtxt.substring(0, characterPosition + 1) + textToInsert + oldtxt.substring(characterPosition + 1);

        int length = textToInsert.length();

        for (Annotation annotation : annotatedText.getAnnotations()) {
            if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                for (Fragment frament : annotation.getTextBinding().getFragments()) {
                    int start = frament.getStart();
                    if (start >= characterPosition) {
                        frament.setStart(start + length);
                    }
                    int end = frament.getEnd();
                    if (end >= characterPosition) {
                        frament.setEnd(end + length);
                    }
                }
            }
        }
        annotatedText.getDocument().setContents(newtxt);
    }
    public static final String ANNOTATONID_ATTRNAME = "aae_annid";
    public static final String MAKERORDVAL_ATTRNAME = "aae_ord";

    /**
     * Appends a new Element child representing the specified Fragment
     *
     * @return the newly created Element
     */
    private static Element addElement(AnnotatedTextHandler annotationModificationHandler, AnnotationFragmentMarkerMapper annFragMarkmap, Element ancestorElt, AnnotationFragment currentFrag, int ordVal) {

        Annotation annotation = currentFrag.getAnnotation();
        Fragment fragment = currentFrag.getMarkerFragment();
        String tagName;
        //If no annotation associated just create a plain DIV element
        String type = annotation != null ? annotation.getAnnotationType() : "div";
        //HTML Annotation keept their original tag kind
        boolean isHtml = annotation == null || annotationModificationHandler.isFormattingAnnotation(annotation.getId());
        if (isHtml) {
            tagName = type.toUpperCase();
        } //... others Annotation are translated into SPAN tag
        else {
            tagName = "SPAN";
        }
        Element newElt = Document.get().createElement(tagName);

        if (!isHtml) {
            //reference attributes
            String uniqueId = HTMLPanel.createUniqueId();
            newElt.setId(uniqueId);

            newElt.setAttribute(ANNOTATONID_ATTRNAME, String.valueOf(annotation.getId()));
            newElt.setAttribute("aae_frgstart", String.valueOf(fragment.getStart()));
            newElt.setAttribute("aae_frgend", String.valueOf(fragment.getEnd()));
            newElt.setAttribute("aae_anntype", type);
            newElt.setAttribute(MAKERORDVAL_ATTRNAME, String.valueOf(ordVal));

            //display related attributes
            newElt.addClassName(type);
            newElt.addClassName(GlobalStyles.RegularAnnotation);
            newElt.addClassName(GlobalStyles.HoveredAnnotation);
            newElt.setAttribute(GlobalStyles.SelectedAttr, "false");

            annFragMarkmap.setMapping(currentFrag, uniqueId);
        }
        if (annotation != null) {
            //FIXME: Surely not used anymore: What is the purpose of storing the properties as marker's attributes anyway?

            //stored properties
            for (String key : annotation.getProperties().getKeys()) {
                StringBuilder values = new StringBuilder();
                for (String v : annotation.getProperties().getValues(key)) {
                    values.append(v).append("\t");
                }
                newElt.setAttribute(key, values.toString());
            }
        }
        ancestorElt.appendChild(newElt);
        return newElt;
    }

    /**
     * Produces the HTML representation of the Annotation contained in the
     * Annotated Text Crossing boundaries is resolved by splitting the Fragment
     * closer to the tree leaf.
     *
     * @return a markerFragment of Document containing the Annotated document
     * ready for display
     */
    public static final Element getMarkedDoc(AnnotatedTextHandler annotatedTextHnd, String textContainerId) {

        AlvisaeCore.speedTracerlog("getMarkedDoc - start...");

        AnnotationFragmentMarkerMapper annFragMarkmap = new AnnotationFragmentMarkerMapper();

        //1- keep only annotation with TextBinding
        List<AnnotationFragment> annotationFragments = new ArrayList<AnnotationFragment>();
        for (Annotation annotation : annotatedTextHnd.getAnnotations()) {
            if (annotation.getAnnotationKind().equals(AnnotationKind.TEXT)) {
                for (Fragment fragment : annotation.getTextBinding().getFragments()) {
                    annotationFragments.add(new AnnotationFragment(annotation, fragment, annotatedTextHnd.isFormattingAnnotation(annotation.getId())));
                }
            }
        }

        //2- sort fragments from the root of the DOM to the leaf
        Collections.sort(annotationFragments);

        List<AnnotationFragment> covering = new ArrayList<AnnotationFragment>();

        //3- in case of overlapping, split the fragments further from the root to eliminate overlapping
        for (int currentIndex = 0; currentIndex < annotationFragments.size(); currentIndex++) {
            AnnotationFragment currentFragment = annotationFragments.get(currentIndex);

            int currentStartOffset = currentFragment.getMarkerFragment().getStart();
            //remove closed markerFragment
            for (AnnotationFragment ancestorFragment : new ArrayList<AnnotationFragment>(covering)) {
                int ancestorEnd = ancestorFragment.getMarkerFragment().getEnd();
                if (ancestorEnd <= currentStartOffset) {
                    //markerFragment is now closed, remove it from list
                    covering.remove(ancestorFragment);
                }
            }

            boolean needToSortAgain = false;
            int currentEndOffset = currentFragment.getMarkerFragment().getEnd();
            //check if there is the ancestor markerFragment is overlapping the current one
            //FIXME : actually only the closest ancestor can have boundary crossing; the loop is not really necessary
            for (int ancestorIndex = covering.size() - 1; ancestorIndex >= 0; ancestorIndex--) {
                AnnotationFragment ancestorFragment = covering.get(ancestorIndex);
                int ancestorEnd = ancestorFragment.getMarkerFragment().getEnd();
                //check if ancestor markerFragment will end before the current one
                if (ancestorEnd < currentEndOffset) {
                    //overlaping situation : the current has to be split in order to avoid boundary crossing
                    AnnotationFragment newFragment = currentFragment.splitAt(ancestorEnd);

                    annotationFragments.add(currentIndex + 1, newFragment);
                    currentEndOffset = currentFragment.getMarkerFragment().getEnd();
                    //since a new markerFragment has been added...
                    // * need to sort again the end of the list,
                    needToSortAgain = true;
                    // * and adjust loop counter to process the current one that has just been splitted
                    currentIndex--;
                    break;
                }
            }

            //stack the current markerFragment in the covering list
            covering.add(currentFragment);

            //sort again global fragments list, including the newly created ones, so they can be processed later by the enclosing loop
            if (needToSortAgain) {
                //FIXME : for later optimisations, only the end of the list need to be sorted again...
                Collections.sort(annotationFragments);
            }
        }

        //4- create the final html from the list of non-overlapping fragments
        //StringBuilder html = new StringBuilder();

        //root container for produced HTML
        Element container = Document.get().createDivElement();

        String leTexte = annotatedTextHnd.getAnnotatedText().getDocument().getContents();

        //create a markerFragment for to create the englobing DIV element
        annotationFragments.add(0, new AnnotationFragment(null, FragmentImpl.create(0, leTexte.length()), true));

        //create a dummy markerFragment to have one more loop round to flush processing
        annotationFragments.add(new AnnotationFragment(null, FragmentImpl.create(leTexte.length(), leTexte.length()), false));
        Iterator<AnnotationFragment> iterator = annotationFragments.iterator();

        //character position of the text already flushed in the HTML representation
        int textOffset = -1;

        //stack of the ancestor of the current markerFragment
        List<AnnotationFragment> ancestors = new ArrayList<AnnotationFragment>();

        //Parent of the current markerFragment
        AnnotationFragment ancestorFrag = null;
        //Parent of the current element
        Element ancestorElt = container;

        int fragOrdVal = 0;
        while (iterator.hasNext()) {
            fragOrdVal++;
            AnnotationFragment currentFrag = iterator.next();

            if (ancestorFrag == null) {
                //no previous ancestor, just create the first element
                ancestorElt = addElement(annotatedTextHnd, annFragMarkmap, ancestorElt, currentFrag, fragOrdVal);
                //html.append("<").append(currentFrag.getAnnotation().getId()).append(">");
                textOffset = 0;
            } else {
                int currentStart = currentFrag.getMarkerFragment().getStart();

                //remove every closed element from the ancestor stack
                for (int ancestorIndex = ancestors.size() - 1; ancestorIndex >= 0; ancestorIndex--) {
                    AnnotationFragment ancestorFragment = ancestors.get(ancestorIndex);
                    int ancestorEnd = ancestorFragment.getMarkerFragment().getEnd();

                    //previous element is not ancestor of the current one
                    if (currentStart >= ancestorEnd) {

                        if (ancestorEnd > textOffset) {
                            //insert right part of text into the closed element
                            String txt = leTexte.substring(textOffset, ancestorEnd);
                            ancestorElt.appendChild(Document.get().createTextNode(txt));
                            //html.append(txt);
                            textOffset = ancestorEnd;
                        }
                        ancestors.remove(ancestorIndex);
                        //html.append("</").append(ancestorFragment.getAnnotation().getId()).append(">");

                        ancestorElt = ancestorElt.getParentElement();
                    }
                }

                int ancestorStart = ancestorFrag.getMarkerFragment().getStart();

                //the new markerFragment start further than its parent :
                if (currentStart > ancestorStart) {
                    //must insert a text node to the left to fill the gap
                    if (currentStart > textOffset) {
                        String txt = annotatedTextHnd.getAnnotatedText().getDocument().getContents().substring(textOffset, currentStart);
                        ancestorElt.appendChild(Document.get().createTextNode(txt));
                        //html.append(txt);
                        textOffset = currentStart;
                    }
                }

                //is this the dummy markerFragment used to flush?
                if (currentFrag.getAnnotation() == null) {
                    continue;
                }
                //create element for current markerFragment
                ancestorElt = addElement(annotatedTextHnd, annFragMarkmap, ancestorElt, currentFrag, fragOrdVal);
                //html.append("<").append(currentFrag.getAnnotation().getId()).append(">");

            }
            ancestorFrag = currentFrag;
            ancestors.add(currentFrag);

        }

        //FIXME inline elements (SPAN) should not be parents of block element (DIV); hence 
        //5- replace annotation SPAN by DIV

        AnnotationDocumentViewMapper.getMapper(annotatedTextHnd.getAnnotatedText()).setAnnotationFragmentMarkerMapper(annFragMarkmap);
        container.setId(textContainerId);
        AlvisaeCore.speedTracerlog("getMarkedDoc - END.");
        return container;
    }

    /**
     *
     * @param annotation
     * @return the text corresponding to concatenated fragments of the specified
     * Annotation
     */
    public static String getAnnotationText(Annotation annotation) {
        return annotation.getAnnotationText("¦");
    }

    public static String getBriefId(String id) {
        int l = id.length();
        if (l > 15) {
            id = id.substring(0, 5) + "..." + id.substring(l - 5);
        }
        return id;
    }

    //==========================================================================
    public static List<Fragment> getOccurences(AnnotatedTextHandler annotatedTextHnd, Fragment coveredFragment, String pattern) {
        List<Fragment> result = new ArrayList<Fragment>();
        if (pattern != null && !pattern.isEmpty()) {
            String content = annotatedTextHnd.getAnnotatedText().getDocument().getContents();
            int end = 0;
            int patternLen = pattern.length();
            while (true) {
                int pos = content.indexOf(pattern, end);
                if (pos < 0) {
                    break;
                }
                end = pos + patternLen;
                if (coveredFragment != null) {
                    if (pos < coveredFragment.getStart() || end > coveredFragment.getEnd()) {
                        continue;
                    }
                }
                result.add(FragmentImpl.create(pos, end));
            }
        }
        return result;
    }
    //==========================================================================

    /**
     *
     * @return This first enclosing tag element that correspond to a Annotation
     * marker
     */
    public static Element getFirstEnclosingMarkerElement(Element element, String textContainerId) {
        Element result = element;
        while (result != null && result.getAttribute(ANNOTATONID_ATTRNAME).isEmpty()) {
            result = result.getParentElement();
            if (result != null && result.getId().equals(textContainerId)) {
                result = null;
            }
        }
        return result;
    }

    public static String getAnnotationIdFromMarker(Element element) {
        return element == null || element.getAttribute(ANNOTATONID_ATTRNAME).isEmpty() ? null : element.getAttribute(ANNOTATONID_ATTRNAME);
    }

    /**
     *
     * @return The list of all Annotation markers enclosing the specified
     * element
     */
    public static List<Element> getAscendantMarkerElements(Element element, String textContainerId) {
        Element elt = element;
        List<Element> ascendants = new ArrayList<Element>();

        while (elt != null) {
            if (!elt.getAttribute(ANNOTATONID_ATTRNAME).isEmpty()) {
                ascendants.add(elt);
            }
            elt = elt.getParentElement();
            if (elt != null && elt.hasAttribute("id") && elt.getId().equals(textContainerId)) {
                elt = null;
            }
        }
        return ascendants;
    }

    /**
     *
     * @return the CSV representation of the
     */
    public static String getAnnotationsAsCSV() {
        StringBuilder result = new StringBuilder();
        return result.toString();
    }

    //#######################################################################
    private static native String midBlend(String backgroundColor) /*-{
     var c = new $wnd.Color(backgroundColor);
     var w = new $wnd.Color("white");
     var pallet = c.blend(w, 5);
     return pallet[2].hex();
     }-*/;

    public static String getSampleCStyleSheet(AnnotationSchemaDefinition schema) {
        StringBuilder sheet = new StringBuilder();
        int i = 0;
        for (String typeName : schema.getAnnotationTypes()) {

            sheet.append(".").append(typeName).append(":not([").append(GlobalStyles.VeiledAttr).append("])").append(" {\n");
            String color = schema.getAnnotationTypeDefinition(typeName).getColor();
            sheet.append("  background-color: ").append(color).append(";");
            sheet.append("}\n");

            String ncolor = midBlend(color);
            sheet.append(".").append(typeName).append("[").append(GlobalStyles.SelectedAttr).append("='false']").append(":not([").append(GlobalStyles.VeiledAttr).append("])").append(" {\n");
            sheet.append("  background-color: ").append(ncolor).append(";");
            sheet.append("}\n");

        }
        return sheet.toString();
    }
}