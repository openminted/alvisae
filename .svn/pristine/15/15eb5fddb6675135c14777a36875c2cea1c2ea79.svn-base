/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig.cdxws.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

/**
 * Service used to locate a configuration file associated with a specific
 * instance of the WebServices the config file can be specified by a web param,
 * or via JNDI Custom resource.
 *
 * <u>Web param Configuration</u><br/> Web param name: configFilePath <br/>
 * <u>JNDI Custom resource Configuration</u> <br/> JNDI Name:
 * AlvisAE_WS_Config<br/> Resource Type: java.util.Properties<br/> Factory
 * Class: org.glassfish.resources.custom.factory.PropertiesFactory<br/> <br/>
 * for each deployed WS, a property containing the configuration filepath is
 * added to the custom resource. The property name is base on the WS
 * contextRoot, (slash characters replaced by dots) e.g. for WS deployed at :
 * /alvisae/demo => the property name will be : alvisae.demo.filepath
 */

/*
 * @author fpapazian
 */
public class ConfigResourceLocator {
    //Name of the web context param used to define config file

    private static final String ConfigFilePath_ParamName = "configFilePath";
    //Name of the JNDI custom resource
    private final static String JNDIName = "AlvisAE_WS_Config";
    private final static String PropSuffix = "filepath";
    private static boolean resourceRetrieved = false;
    private static Properties resProps = null;

    /**
     *
     * @param servletContext
     * @return Properties containing the values of the configuration file
     * specified by web param or by the JNDI, or null if none specified
     */
    public static Properties getConfigFile(ServletContext servletContext) {

        String configFilePath = servletContext.getInitParameter(ConfigFilePath_ParamName);
        if (configFilePath == null) {
            System.err.println("ConfigResourceLocator: web-param '" + ConfigFilePath_ParamName + "' not specified for this servlet!\nChecking JNDI...");
            return getConfigFile(servletContext.getContextPath());
        }
        File configFile = new File(configFilePath);
        if (!configFile.canRead()) {
            System.err.println("ConfigResourceLocator: web-param '" + ConfigFilePath_ParamName + "' points to an unreadable config file ( " + configFilePath + " )");
            return null;
        }

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(configFilePath));
        } catch (IOException e) {
            System.err.println("ConfigResourceLocator: web-param '" + ConfigFilePath_ParamName + "' points to an invalid config file ( " + configFilePath + " )");
            return null;
        }
        return properties;
    }

    /**
     *
     * @param contextRoot the context root of the application
     * @return Properties containing the values of the configuration file
     * associated to the specified contextRoot
     */
    public static Properties getConfigFile(String contextRoot) {
        if (!resourceRetrieved) {
            try {
                Context initCtx = new InitialContext();
                Context envCtx = (Context) initCtx.lookup("");
                Object o = envCtx.lookup(JNDIName);
                if (o instanceof Properties) {
                    resProps = (Properties) o;
                }
            } catch (NamingException ex) {
            } finally {
                resourceRetrieved = true;
            }
        }
        if (resProps == null) {
            System.err.println("ConfigResourceLocator: properties '" + JNDIName + "' not found in JNDI!");
            return null;
        } else {
            String propName = contextRoot.replaceFirst("/", "").replaceAll("/", ".") + "." + PropSuffix;
            String filename = resProps.getProperty(propName);
            if (filename == null) {
                System.err.println("ConfigResourceLocator: '" + propName + "' key absent from '" + JNDIName + "'");
                return null;
            } else {
                return getAndCheckConfigFile("ConfigResourceLocator: ", true, filename);
            }
        }
    }

    public static Properties getAndCheckConfigFile(String messagePrefix, boolean verbose, String filename) {
        Properties result = null;
        if (filename == null || filename.trim().isEmpty()) {
            System.err.println(messagePrefix + "Missing configuration file");
            return null;
        } else {
            File f = new File(filename);
            if (f.exists() && f.canRead()) {
                Properties temp = new Properties();
                FileInputStream fis;
                try {
                    fis = new FileInputStream(f);
                    try {
                        temp.load(fis);
                        if (verbose) {
                            System.out.println(messagePrefix + "Loading values from configuration file : " + filename + "  :");
                            temp.list(System.out);
                        }
                        result = temp;
                    } catch (IOException ex) {
                        System.err.println(messagePrefix + "Error while reading configuration file : " + filename);
                        ex.printStackTrace(System.err);
                    } finally {
                        fis.close();
                    }
                } catch (FileNotFoundException ex) {
                    System.err.println(messagePrefix + "Error while opening configuration file : " + filename);
                    ex.printStackTrace(System.err);
                } catch (IOException ex) {
                    System.err.println(messagePrefix + "Error while reading configuration file : " + filename);
                    ex.printStackTrace(System.err);
                }
            } else {
                System.err.println(messagePrefix + "Can not read from configuration file : " + filename);
                return null;
            }
            return result;
        }
    }
}
