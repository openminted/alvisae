/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import java.util.Comparator;

/**
 *
 * @author fpapazian
 */
public class FragmentImpl extends JavaScriptObject implements Fragment {

    public static final FragmentImpl create(int start, int end) {
        FragmentImpl result = JavaScriptObject.createArray().cast();
        result.setStart(start);
        result.setEnd(end);
        return result;
    }

    public static final FragmentImpl create(Fragment fragment) {
        return create(fragment.getStart(), fragment.getEnd());
    }

    protected FragmentImpl() {
    }

    @Override
    public final native int getStart() /*-{ return this[0]; }-*/;

    @Override
    public final native void setStart(int start) /*-{ this[0]=start; }-*/;

    @Override
    public final native int getEnd() /*-{ return this[1]; }-*/;

    @Override
    public final native void setEnd(int end) /*-{ this[1]=end; }-*/;

    //Note: Can not override JavaScriptObject equals() since its final
    @Override
    public final boolean isSame(Fragment other) {
        return other!=null && getStart()==other.getStart() && getEnd()==other.getEnd();
    }
    
    public static final Comparator<Fragment> COMPARATOR = new Comparator<Fragment>() {

        @Override
        public int compare(Fragment t1, Fragment t2) {
            //place before the fragment which starts first
            int c = Integer.valueOf(t1.getStart()).compareTo(Integer.valueOf(t2.getStart()));
            if (c == 0) {
                //place after the fragment which ends last
                c = Integer.valueOf(t1.getEnd()).compareTo(Integer.valueOf(t2.getEnd()));
            }
            return c;
        }
    };
    
    public static final Comparator<Fragment> COMPARATOR2 = new Comparator<Fragment>() {

        @Override
        public int compare(Fragment t1, Fragment t2) {
            //place before the fragment which starts first
            int c = Integer.valueOf(t1.getStart()).compareTo(t2.getStart());
            if (c == 0) {
                //place before the fragment which ends last
                c = Integer.valueOf(t2.getEnd()).compareTo(t1.getEnd());
            }
            return c;
        }
    };
    
}
