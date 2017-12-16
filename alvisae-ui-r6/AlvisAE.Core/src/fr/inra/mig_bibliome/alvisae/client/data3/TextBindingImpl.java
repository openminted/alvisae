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
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.TextBinding;
import java.util.*;

/**
 *
 * @author fpapazian
 */
public class TextBindingImpl extends JavaScriptObject implements TextBinding {

    public static final TextBindingImpl create() {
        return JavaScriptObject.createArray().cast();
    }

    protected TextBindingImpl() {
    }

    private final JsArray<FragmentImpl> asArray() {
        return this.cast();
    }

    @Override
    public final List<Fragment> getFragments() {
        JsArray<FragmentImpl> array = this.cast();
        List<Fragment> liste = new ArrayList<Fragment>();
        for (int i = 0, n = array.length(); i < n; ++i) {
            @SuppressWarnings("unchecked")
            Fragment support = (Fragment) array.get(i);
            liste.add(FragmentImpl.create(support));
        }
        return liste;
    }

    @Override
    public final List<Fragment> getSortedFragments() {
        List<Fragment> liste = new ArrayList<Fragment>(getFragments());
        Collections.sort(liste, FragmentImpl.COMPARATOR2);
        return liste;
    }

    private final void _addFragment(Fragment fragment) {
        asArray().push(FragmentImpl.create(fragment));
    }

    private final void clearFragments() {
        asArray().setLength(0);
    }

    @Override
    public final void addFragment(Fragment fragment) {
        List<Fragment> unmerged = getFragments();
        unmerged.add(FragmentImpl.create(fragment));
        List<Fragment> merged = mergeOverlappingFragments(unmerged);
        clearFragments();
        _addFragments(merged);
    }

    //FIXME could be replaced by inplace merging to avoid list copy
    public static List<Fragment> mergeOverlappingFragments(List<Fragment> unmerged) {
        if (unmerged.size() > 1) {
            //sort text segments before merging
            Collections.sort(unmerged, FragmentImpl.COMPARATOR);

            ArrayList<Fragment> merged = new ArrayList<Fragment>();

            //merge adjacent, or overlapping, fragment
            Fragment previous = unmerged.get(0);
            merged.add(previous);
            Fragment current;
            for (int i = 1; i < unmerged.size(); i++) {
                current = unmerged.get(i);
                if ((current.getStart() <= previous.getEnd()) && (current.getEnd() > previous.getEnd())) {
                    previous.setEnd(current.getEnd());
                } else if ((current.getStart() >= previous.getStart()) && (current.getEnd() <= previous.getEnd())) {
                } else {
                    merged.add(current);
                    previous = current;
                }
            }
            return merged;
        } else {
            return unmerged;
        }
    }

    @Override
    public final boolean removeFragment(Fragment toBeRemoved) {
        List<Fragment> frag = new ArrayList<Fragment>();
        frag.add(toBeRemoved);
        return removeFragments(frag);
    }

    private final native void _removeFragment(int start, int end) /*-{ for (i in this) { f=this[i]; if ((f[0]==start)&&(f[1]==end)) { f.splice(i,1); break; } } }-*/;

    public final void _addFragments(Collection<Fragment> fragments) {
        for (Fragment f : fragments) {
            _addFragment(f);
        }
    }

    @Override
    public final void addFragments(Collection<Fragment> fragments) {
        List<Fragment> unmerged = getFragments();
        for (Fragment fragment : fragments) {
            unmerged.add(FragmentImpl.create(fragment));
        }
        List<Fragment> merged = mergeOverlappingFragments(unmerged);
        clearFragments();
        _addFragments(merged);
    }

    @Override
    public final boolean removeFragments(Collection<Fragment> fragments) {
        boolean result = true;
        if (!fragments.isEmpty()) {
            List<Fragment> previousFrags = getFragments();

            List<Fragment> toKeep = null;
            for (Fragment toBeRemoved : fragments) {

                Collections.sort(previousFrags, FragmentImpl.COMPARATOR);
                toKeep = new ArrayList<Fragment>();

                for (Fragment fragment : previousFrags) {
                    if ((toBeRemoved.getEnd() < fragment.getStart()) || (toBeRemoved.getStart() > fragment.getEnd())) {
                        //no overlapping: keep the exisitng fragment
                        toKeep.add(FragmentImpl.create(fragment));

                    } else if ((toBeRemoved.getStart() <= fragment.getStart()) && (toBeRemoved.getEnd() >= fragment.getEnd())) {
                        //completely remove any fragment identical or included to the one to be removed
                    } else if ((toBeRemoved.getStart() > fragment.getStart()) && (toBeRemoved.getEnd() < fragment.getEnd())) {
                        //the fragment to be removed is included in the existing fragment; then split the existing
                        toKeep.add(FragmentImpl.create(fragment.getStart(), toBeRemoved.getStart()));
                        toKeep.add(FragmentImpl.create(toBeRemoved.getEnd(), fragment.getEnd()));

                    } else if ((toBeRemoved.getStart() <= fragment.getStart()) && (toBeRemoved.getEnd() < fragment.getEnd())) {
                        //the fragment to be removed covers the left part of the existing fragment; keep only the right part of existing fragment
                        toKeep.add(FragmentImpl.create(toBeRemoved.getEnd(), fragment.getEnd()));

                    } else if ((toBeRemoved.getStart() > fragment.getStart()) && (toBeRemoved.getEnd() >= fragment.getEnd())) {
                        //the fragment to be removed covers the right part of the existing fragment; keep only the left part of existing fragment
                        toKeep.add(FragmentImpl.create(fragment.getStart(), toBeRemoved.getStart()));

                    }
                }
                previousFrags = toKeep;
            }
            if (toKeep.isEmpty()) {
                result = false;
            } else {
                clearFragments();
                _addFragments(toKeep);
            }
        }
        return result;
    }

    @Override
    public final void setFragments(Collection<Fragment> fragments) {
        clearFragments();
        addFragments(fragments);
    }  
    
    public static final Comparator<TextBinding> COMPARATOR = new Comparator<TextBinding>() {

        @Override
        public int compare(TextBinding tb1, TextBinding tb2) {
            int c = 0;
            if (tb1!=null && tb2 !=null) {
                List<Fragment> sf1 = tb1.getSortedFragments();
                List<Fragment> sf2 = tb2.getSortedFragments();
                
                //place before the Text binding which starts first
                c = Integer.valueOf(sf1.get(0).getStart()).compareTo(Integer.valueOf(sf2.get(0).getStart()));
                if (c == 0) {
                    c = Integer.valueOf(sf1.get(sf1.size()-1).getEnd()).compareTo(Integer.valueOf(sf2.get(sf2.size()-1).getEnd()));
                }
            }
            return c;
        }
    };

    private final static String INVALIDFIELD_PREFIX = "Invalid field ";

    /**
     * Check that the TextBinding parsed from a JSON string conforms to the
     * expected structure
     *
     * @throws IllegalArgumentException
     */
    public final void checkStructure() {
        List<Fragment> frags = null;
        try {
            frags = getFragments();
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "Fragments", ex);
        }
        if (frags.isEmpty()) {
            throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "-> Fragment should not be empty");
        }
        for (Fragment f : frags) {
            try {
                int start = f.getStart();
            } catch (Exception ex) {
                throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "Framgent.Start", ex);
            }
            try {
                int end = f.getEnd();
            } catch (Exception ex) {
                throw new IllegalArgumentException(INVALIDFIELD_PREFIX + "Framgent.End", ex);
            }
        }
    }

}
