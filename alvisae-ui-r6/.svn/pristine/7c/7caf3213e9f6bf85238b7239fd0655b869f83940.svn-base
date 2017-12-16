/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationOptionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.ApplicationOptionChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.data.AuthenticationInfoImpl;

/**
 *
 * @author fpapazian
 */
public class ApplicationOptions {
    
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    

    public static class PersistedOptionHandler implements ApplicationOptionChangedEventHandler {

        private final String dimensionUniqueName;

        public PersistedOptionHandler(String dimensionUniqueName) {
            this.dimensionUniqueName = dimensionUniqueName;
        }

        public void persistValue(Integer dimensionValue) {
            Integer oldValue = ApplicationOptions.getNonNullOption(dimensionUniqueName);
            ApplicationOptions.setOption(dimensionUniqueName, dimensionValue);
            injector.getMainEventBus().fireEvent(new ApplicationOptionChangedEvent(dimensionUniqueName, oldValue, dimensionValue));
        }
        
        public Integer getValue() {
            return ApplicationOptions.getNonNullOption(dimensionUniqueName);
        }

        public Integer getValue(Integer defaultValue) {
            return ApplicationOptions.getNonNullOption(dimensionUniqueName, defaultValue);
        }

        public void valueChanged(Integer oldValue, Integer newValue) {
        }

        @Override
        public void onApplicationOptionChanged(ApplicationOptionChangedEvent event) {
            if (dimensionUniqueName.equals(event.getOptionName())) {
                valueChanged(event.getOldValue(), event.getNewValue());
            }
        }
    }
    //
    private static final String OptionPrefix = "aae.prefs.";
    private static final String OptionLoginSuffix = ".loginName";
    private static final String OptionTokenSuffix = ".token";

    private static String getPrefix() {
        return OptionPrefix;
    }

    //some options are specific to each instance of AlvisAE
    private static String getInstanceSpecificPrefix() {
        return OptionPrefix + GWT.getModuleBaseURL();
    }
    //
    private final String serverSpecificPrefix;

    //login option are specifix to the client instance and to the remote server
    public ApplicationOptions(String serverSpecificPrefix) {
        this.serverSpecificPrefix = serverSpecificPrefix;
    }

    private String getInstServSpecificPrefix() {
        return getInstanceSpecificPrefix() + ':' + serverSpecificPrefix;
    }

    public String getLastLoginName() {
        String lastLoginName = null;
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String pref = getInstServSpecificPrefix();
            lastLoginName = store.getItem(pref + OptionLoginSuffix);
        }
        return lastLoginName;
    }

    public void setSuccessfullSignIn(AuthenticationInfoImpl authInfo) {
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String pref = getInstServSpecificPrefix();
            store.setItem(pref + OptionLoginSuffix, authInfo.getName());
            store.setItem(pref + OptionTokenSuffix, authInfo.getBase64EncodedBasicAuthToken());
        }
    }

    public boolean hasSavedCredential() {
        return getAuthenticationInfo() != null;
    }

    public AuthenticationInfoImpl getAuthenticationInfo() {
        AuthenticationInfoImpl result = null;
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String pref = getInstServSpecificPrefix();
            String name = store.getItem(pref + OptionLoginSuffix);
            String token = store.getItem(pref + OptionTokenSuffix);
            result = AuthenticationInfoImpl.getForUserToken(name, token);
        }
        return result;
    }

    public void setUnSuccessfullSignIn() {
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String pref = getInstServSpecificPrefix();
            store.removeItem(pref + OptionTokenSuffix);
        }
    }

    public static String getNonNullOption(String key, String defaultValue) {
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String prefix = getInstanceSpecificPrefix() + "." + key;
            return store.getItem(prefix);
        } else {
            return defaultValue;
        }
    }

    public static boolean setOption(String key, String value) {
        Storage store = Storage.getLocalStorageIfSupported();
        if (store != null) {
            String prefix = getInstanceSpecificPrefix() + "." + key;
            if (value == null) {
                store.removeItem(prefix);
            } else {
                store.setItem(prefix, value);
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean setOption(String key, Integer value) {
        return setOption(key, value.toString());
    }

    public static Integer getNonNullOption(String key, Integer defaultValue) {
        String strVal = getNonNullOption(key, (String) null);
        if (strVal != null) {
            try {
                return Integer.valueOf(strVal);
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Integer getNonNullOption(String key) {
        return getNonNullOption(key, (Integer) null);
    }
}
