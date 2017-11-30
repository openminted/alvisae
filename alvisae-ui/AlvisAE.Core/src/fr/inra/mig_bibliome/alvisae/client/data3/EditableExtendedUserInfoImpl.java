/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.view.client.ProvidesKey;

/**
 *
 * @author fpapazian
 */
public class EditableExtendedUserInfoImpl extends ExtendedUserInfoImpl {
    
    public static final ProvidesKey<EditableExtendedUserInfoImpl> KEY_PROVIDER = new ProvidesKey<EditableExtendedUserInfoImpl>() {

        @Override
        public Object getKey(EditableExtendedUserInfoImpl item) {
            return item == null ? null : item.getId();
        }
    };
    
    public static final native EditableExtendedUserInfoImpl create() /*-{
    u = {};
    u.id=-1;
    u.login='new_user';
    u.is_admin=false;
    u.persited=false;
    u.password=''
    return u;
    }-*/;

    public static final EditableExtendedUserInfoImpl create(ExtendedUserInfoImpl source) {
        EditableExtendedUserInfoImpl copy = create();
        copy.setId(source.getId()); 
        copy.setDisplayName(source.getDisplayName()); 
        copy.setAdmin(source.isAdmin()); 
        copy.setPersisted(true);
        return copy;
    }
    
    protected EditableExtendedUserInfoImpl() {
    }

    public final native boolean isPersisted() /*-{ if (this.hasOwnProperty('persited')) { return this.persited; } else { return true; } }-*/;    

    public final native void setPersisted(boolean persited) /*-{ this.persited = persited; }-*/;    
    
    public final native void setAdmin(boolean isAdmin) /*-{ this.is_admin = isAdmin; }-*/;    

    public final native void setDisplayName(String name) /*-{ this.login = name; }-*/;    

    public final native String getPassword() /*-{ return this.password; }-*/;    
    
    public final native void setPassword(String password) /*-{ this.password = password; }-*/;    

    private final native void setId(int id) /*-{ this.id = id; }-*/;    
}
