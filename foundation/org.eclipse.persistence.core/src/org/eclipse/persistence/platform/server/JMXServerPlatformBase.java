/*******************************************************************************
 * Copyright (c) 1998, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     06/30/2010-2.1.1 Michael O'Brien 
 *       - 316513: Enable JMX MBean functionality for JBoss, Glassfish and WebSphere in addition to WebLogic
 *       Move JMX MBean generic registration code up from specific platforms
 *       see <link>http://wiki.eclipse.org/EclipseLink/DesignDocs/316513</link>
 *     07/15/2010-2.1.1 Michael O'Brien 
 *       - 316513: registration/deregistration mismatch for MBean Object name reg=- and dereg=_ - no more instance already exists on redeploy
 ******************************************************************************/  
package org.eclipse.persistence.platform.server;

import java.lang.management.ManagementFactory;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.services.mbean.MBeanDevelopmentServices;
import org.eclipse.persistence.services.mbean.MBeanRuntimeServicesMBean;
import org.eclipse.persistence.sessions.DatabaseSession;

/**
 * PUBLIC:
 * Subclass of org.eclipse.persistence.platform.server.ServerPlatformBase 
 * in support of the JMXEnabledPlatform interface
 * <p>
 * This is the abstract superclass of all platforms for all servers that contain a subclass
 * that implements the JMXEnabledPlatform interface. 
 * Each DatabaseSession
 * contains an instance of the receiver, to help the DatabaseSession determine:
 * <p><ul>
 * <li> Whether or not to enable JTA (external transaction control)
 * <li> How to register/unregister for runtime services (JMX/MBean)
 * <li> Whether or not to enable runtime services
 * </ul><p>
 * Subclasses already exist to provide configurations for Oc4J, WebLogic, JBoss, NetWeaver, GlassFish and WebSphere.
 * <p>
 * If the versioned platform subclass is JMX enabled by EclipseLink (registers MBeans) then that 
 * server platform must implement the JMXEnabledPlatform interface
 * To provide some different behavior than the provided ServerPlatform(s), we recommend
 * subclassing org.eclipse.persistence.platform.server.JMXServerPlatformBase (or a subclass),
 * and overriding:
 * <ul>
 * <li>JMXEnabledPlatform.prepareServerSpecificServicesMBean()
 * </ul>
 * for the desired behavior.
 *
 * @see org.eclipse.persistence.platform.server.ServerPlatformBase
 * @since EclipseLink 2.1.1
 */
public abstract class JMXServerPlatformBase extends ServerPlatformBase {

    /** This is the prefix for all MBeans that are registered with their specific session name appended */
    public static final String JMX_REGISTRATION_PREFIX = "TopLink:Name=";
    /** The default indexed MBeanServer instance to use when multiple MBeanServer instances exist on the platform - usually only in JBoss */
    public static final int JMX_MBEANSERVER_INDEX_DEFAULT_FOR_MULTIPLE_SERVERS = 0;
    /** This persistence.xml or sessions.xml property is used to override the moduleName */
    protected static final String OVERRIDE_JMX_MODULENAME_PROPERTY = "eclipselink.jmx.moduleName"; 
    /** This persistence.xml or sessions.xml property is used to override the applicationName */
    protected static final String OVERRIDE_JMX_APPLICATIONNAME_PROPERTY = "eclipselink.jmx.applicationName"; 

    /**
     * The following constants and attributes are used to determine the module and application name
     * to satisfy the requirements for 248746 where we provide an identifier pair for JMX sessions.
     * Each application can have several modules.
     * 1) Application name - the persistence unit associated with the session (a 1-1 relationship)
     * 2) Module name - the ejb or war jar name (there is a 1-many relationship for module:session(s)) 
     */
    /** Override by subclass: Search String in application server ClassLoader for the application:persistence_unit name */
    protected static String APP_SERVER_CLASSLOADER_APPLICATION_PU_SEARCH_STRING_PREFIX = "annotation: ";
    protected static String APP_SERVER_CLASSLOADER_APPLICATION_PU_SEARCH_STRING_POSTFIX = "";

    /** Override by subclass: Search String in application server session for ejb modules */
    protected static String APP_SERVER_CLASSLOADER_MODULE_EJB_SEARCH_STRING_PREFIX = ".jar/";
    /** Override by subclass: Search String in application server session for war modules */
    protected static String APP_SERVER_CLASSLOADER_MODULE_WAR_SEARCH_STRING_PREFIX = ".war/";
    protected static String APP_SERVER_CLASSLOADER_MODULE_EJB_WAR_SEARCH_STRING_POSTFIX = "";    
    
    
    /** Cache the ServerPlatform MBeanServer for performance */
    private MBeanServer mBeanServer = null;
    
    /** cache the RuntimeServices MBean - during platform construction for JMXEnabledPlatform implementors */
    private MBeanRuntimeServicesMBean runtimeServicesMBean = null;

    /** moduleName determination is available during MBean registration only */
    private String moduleName = null;

    /** applicationName determination is available during MBean registration only */
    private String applicationName = null;
    
    /**
     * INTERNAL:
     * Default Constructor: Initialize so that runtime services and JTA are enabled. 
     */
    public JMXServerPlatformBase(DatabaseSession newDatabaseSession) {
        super(newDatabaseSession);
    }
    
    /** 
     * INTERNAL:
     * Return the MBeanServer to be used for MBean registration and deregistration.<br>
     * This MBeanServer reference is lazy loaded and cached on the platform.<br>
     * There are multiple ways of getting the MBeanServer<br>
     * <p>
     * 1) MBeanServerFactory static function - working for all 4 server WebLogic, WebSphere, JBoss and Glassfish in a generic way<br>
     *   - JBoss is the only server to return 2 MBeanServers in the List<br>
     * 2) ManagementFactory static function - what is the difference in using this one over the one returning a List of servers<br>
     * 3) JNDI lookup<br>
     * 4) Direct server specific native API<br></p>
     * We are using method (1)<br>
     * 
     * @return the JMX specification MBeanServer
     */
    public MBeanServer getMBeanServer() {
        // lazy initialize the MBeanServer reference
        if(null == mBeanServer) {
            try {
                // Attempt to get the first MBeanServer we find - usually there is only one - when agentId == null we return a List of them
                List<MBeanServer> mBeanServerList = MBeanServerFactory.findMBeanServer(null);                
                if(null == mBeanServerList || mBeanServerList.isEmpty()) {
                    // Unable to acquire a JMX specification List of MBeanServer instances
                    // TODO: Warning required
                    // Try alternate static method
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                    if(null == mBeanServer) {
                        AbstractSessionLog.getLog().log(SessionLog.INFO, 
                            "jmx_mbean_runtime_services_registration_mbeanserver_print",
                            mBeanServer, mBeanServer.getMBeanCount(), mBeanServer.getDefaultDomain(), 0);
                    } else {
                        // TODO: Warning required
                    }
                } else {
                    // Use the first MBeanServer by default - there may be multiple domains each with their own MBeanServer
                    mBeanServer = mBeanServerList.get(JMX_MBEANSERVER_INDEX_DEFAULT_FOR_MULTIPLE_SERVERS);
                    if(mBeanServerList.size() > 1) {
                        // There are multiple MBeanServerInstances - ust warn for now
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, 
                                "jmx_mbean_runtime_services_registration_encountered_multiple_mbeanserver_instances",
                                mBeanServerList.size(), JMX_MBEANSERVER_INDEX_DEFAULT_FOR_MULTIPLE_SERVERS, mBeanServer);
                        // IE: for JBoss we need to verify we are using the correct MBean server of the two (default, null)
                        // Check the domain if it is non-null - avoid using this server
                        int index = 0;
                        for(MBeanServer anMBeanServer : mBeanServerList) {
                            AbstractSessionLog.getLog().log(SessionLog.INFO, 
                                    "jmx_mbean_runtime_services_registration_mbeanserver_print",
                                    anMBeanServer, anMBeanServer.getMBeanCount(), anMBeanServer.getDefaultDomain(), index);
                            if(null != anMBeanServer.getDefaultDomain()) {
                                mBeanServer = anMBeanServer;
                                AbstractSessionLog.getLog().log(SessionLog.WARNING, 
                                        "jmx_mbean_runtime_services_switching_to_alternate_mbeanserver",
                                        mBeanServer, index);                                                                
                            }
                            index++;
                        }
                    } else {
                        AbstractSessionLog.getLog().log(SessionLog.INFO, 
                                "jmx_mbean_runtime_services_registration_mbeanserver_print",
                                mBeanServer, mBeanServer.getMBeanCount(), mBeanServer.getDefaultDomain(), 0);
                    }
                }                
            } catch (Exception e) {
                // TODO: Warning required
                e.printStackTrace();
            }
        }
        return mBeanServer;
    } 
    
    /**
     * INTERNAL: serverSpecificRegisterMBean(): Server specific implementation of the
     * creation and deployment of the JMX MBean to provide runtime services for my
     * databaseSession.
     *
     * Default is to do nothing. This should be subclassed if required.
     * For platform classes that override the JMXEnabledPlatform - the services MBean 
     * is created at platform construction for use during MBean registration here.
     *
     * @return void
     * @see #isRuntimeServicesEnabled()
     * @see #disableRuntimeServices()
     * @see #registerMBean()
     */
    public void serverSpecificRegisterMBean() {        
        // get and cache module and application name during registration
        MBeanServer mBeanServerRuntime = getMBeanServer();      
        ObjectName name = null;      
        String sessionName = getMBeanSessionName();
        if (null != sessionName && (shouldRegisterDevelopmentBean || shouldRegisterRuntimeBean)) {
                // Attempt to register new mBean with the server
                if (null != mBeanServerRuntime && shouldRegisterDevelopmentBean) {
                    try {
                        name = new ObjectName(JMX_REGISTRATION_PREFIX + "Development-" + sessionName + ",Type=Configuration");
                    } catch (MalformedObjectNameException mne) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", mne);
                    } catch (Exception exception) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", exception);
                    }

                    // Currently the to be deprecated development MBean is generic to all server platforms
                    MBeanDevelopmentServices developmentMBean = new MBeanDevelopmentServices(getDatabaseSession());
                    ObjectInstance info = null;
                    try {
                        info = mBeanServerRuntime.registerMBean(developmentMBean, name);
                    } catch(InstanceAlreadyExistsException iaee) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", iaee);
                    } catch (MBeanRegistrationException registrationProblem) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", registrationProblem);
                    } catch (Exception e) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", e);
                    }
                    AbstractSessionLog.getLog().log(SessionLog.FINEST, "registered_mbean", info);
                }

                if (null != mBeanServerRuntime && shouldRegisterRuntimeBean) {
                    try {
                        name = new ObjectName(JMX_REGISTRATION_PREFIX + "Session(" + sessionName + ")");                        
                    } catch (MalformedObjectNameException mne) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", mne);
                    } catch (Exception exception) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", exception);
                    }
                    
                    ObjectInstance runtimeInstance = null;
                    try {
                        // The cached runtimeServicesMBean is a server platform specific instance
                        runtimeInstance = mBeanServerRuntime.registerMBean(runtimeServicesMBean, name);
                        setRuntimeServicesMBean(runtimeServicesMBean);
                    } catch(InstanceAlreadyExistsException iaee) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", iaee);
                    } catch (MBeanRegistrationException registrationProblem) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", registrationProblem);
                    } catch (Exception e) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_registering_mbean", e);
                    }
                    AbstractSessionLog.getLog().log(SessionLog.FINEST, "registered_mbean", runtimeInstance);          
                }
        }
    }
    
    /**
     * INTERNAL: 
     * serverSpecificUnregisterMBean(): Server specific implementation of the
     * de-registration of the JMX MBean from its server during session logout.
     *
     * @return void
     * @see #isRuntimeServicesEnabled()
     * @see #disableRuntimeServices()
     */
    public void serverSpecificUnregisterMBean() {
        MBeanServer mBeanServerRuntime = getMBeanServer();      
        ObjectName name = null;      
        String sessionName = getMBeanSessionName();
        if (null != sessionName && (shouldRegisterDevelopmentBean || shouldRegisterRuntimeBean)) {
            try {
                
                // Attempt to register new mBean with the server
                if (shouldRegisterDevelopmentBean) {
                    try {
                        name = new ObjectName(JMX_REGISTRATION_PREFIX + "Development-" + sessionName + ",Type=Configuration");                        
                    } catch (MalformedObjectNameException mne) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_unregistering_mbean", mne);
                    } catch (Exception exception) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_unregistering_mbean", exception);
                    }

                    AbstractSessionLog.getLog().log(SessionLog.FINEST, "unregistering_mbean", name);
                    try {
                        mBeanServerRuntime.unregisterMBean(name);
                    } catch(InstanceNotFoundException inf) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_unregistering_mbean", inf);
                    } catch (MBeanRegistrationException mbre) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_unregistering_mbean", mbre);                        
                    }                                        
                }

                if (shouldRegisterRuntimeBean) {
                    try {                        
                        name = new ObjectName(JMX_REGISTRATION_PREFIX + "Session(" + sessionName + ")");                        
                    } catch (MalformedObjectNameException mne) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_unregistering_mbean", mne);
                    } catch (Exception exception) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_unregistering_mbean", exception);
                    }

                    AbstractSessionLog.getLog().log(SessionLog.FINEST, "unregistering_mbean", name);
                    try {
                        mBeanServerRuntime.unregisterMBean(name);
                    } catch(InstanceNotFoundException inf) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_unregistering_mbean", inf);
                    } catch (MBeanRegistrationException registrationProblem) {
                        AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_unregistering_mbean", registrationProblem);
                    }                              
                }
            } catch (Exception exception) {
                AbstractSessionLog.getLog().log(SessionLog.WARNING, "problem_unregistering_mbean", exception);
            } finally {
                // de reference the mbean
                this.setRuntimeServicesMBean(null);
            }
        }
    }    
    
    /**
     * Remove JMX reserved characters from the session name
     * @param aSession
     * @return
     */
    protected String getMBeanSessionName() {
        // Check for a valid session - should never occur though        
        if(null != getDatabaseSession() && null != getDatabaseSession().getName()) {
            // remove any JMX reserved characters when the session name is file:/drive:/directory
            return getDatabaseSession().getName().replaceAll("[=,:]", "_");
        } else {
            AbstractSessionLog.getLog().log(SessionLog.WARNING, "session_key_for_mbean_name_is_null");
            return null;
        }
    }
    
    /**
     * INTERNAL:
     * Return the cached server specific services MBean 
     */
    protected MBeanRuntimeServicesMBean getRuntimeServicesMBean() {
        return runtimeServicesMBean;
    }

    /**
     * INTERNAL:
     * Set the cached server specific services MBean
     * @param runtimeServicesMBean
     */
    protected void setRuntimeServicesMBean(MBeanRuntimeServicesMBean runtimeServicesMBean) {
        this.runtimeServicesMBean = runtimeServicesMBean;
    }

    /**
     * INTERNAL:
     * @param enableDefault
     * @return
     */
    protected String getModuleName(boolean enableDefault) {
        //Object substring = getModuleOrApplicationName();
        if(null == this.moduleName) {
            // Get property from persistence.xml or sessions.xml
            this.moduleName = (String)getDatabaseSession().getProperty(OVERRIDE_JMX_MODULENAME_PROPERTY);
        }        
        // if there is no function or property override then answer "unknown" as a default for platforms that do not implement getModuleName()
        if(null == this.moduleName && enableDefault) {
            this.moduleName = DEFAULT_SERVER_NAME_AND_VERSION;
        }
        return this.moduleName;
    }
    
    /**
     * INTERNAL: 
     * getModuleName(): Answer the name of the context-root of the application that this session is associated with.
     * Answer "unknown" if there is no module name available.
     * Default behavior is to return "unknown" - we override this behavior here for JBoss.
     * 
     * There are 4 levels of implementation.
     * 1) use the property override jboss.moduleName, or
     * 2) perform a reflective jboss.work.executeThreadRuntime.getModuleName() call, or
     * 3) extract the moduleName:persistence_unit from the server specific classloader string representation, or
     * 3) defer to superclass - usually return "unknown"
     *
     * @return String moduleName
     */
    @Override
    public String getModuleName() {
        return getModuleName(true);
    }
    
    /**
     * INTERNAL;
     * @param aName
     */
    protected void setModuleName(String aName) {
        this.moduleName = aName;
    }
    
    /**
     * INTERNAL:
     * Lazy initialize the application name by
     * first checking for a persistence.xml property override and then
     * deferring to a default name in the absence of a platfrom override of this function
     * @param enableDefault
     * @return
     */
    protected String getApplicationName(boolean enableDefault) {
        //Object substring = getModuleOrApplicationName();
        if(null == this.applicationName) {
            // Get property from persistence.xml or sessions.xml
            this.applicationName = (String)getDatabaseSession().getProperty(OVERRIDE_JMX_APPLICATIONNAME_PROPERTY);
        }
        // if there is no function or property override then answer "unknown" as a default for platforms that do not implement getApplicationName()
        if(null == this.applicationName && enableDefault) {
            this.applicationName = DEFAULT_SERVER_NAME_AND_VERSION;
        }
        return this.applicationName;
    }
    
    /**
     * INTERNAL: 
     * getApplicationName(): Answer the name of the module (EAR name) that this session is associated with.
     * Answer "unknown" if there is no application name available.
     * Default behavior is to return "unknown" 
     * 
     * There are 4 levels of implementation.
     * 1) use the property override weblogic.applicationName, or
     * 2) perform a reflective weblogic.work.executeThreadRuntime.getApplicationName() call, or
     * 3) extract the moduleName:persistence_unit from the application classloader string representation, or
     * 3) defer to this superclass - usually return "unknown"
     *
     * @return String applicationName
     * @see JMXEnabledPlatform 
     */
    public String getApplicationName() {
        return getApplicationName(true);
    }
    
    /**
     * INTERNAL:
     * @param aName
     */
    public void setApplicationName(String aName) {
        this.applicationName = aName;
    }

    /**
     * INTERNAL:
     * Get the applicationName and moduleName from the application server
     * @return
     */
    protected void initializeApplicationNameAndModuleName() {
        String databaseSessionName = getDatabaseSession().getName();
        String classLoaderName = getDatabaseSession().getPlatform().getConversionManager().getLoader().toString();
        // Get property from persistence.xml or sessions.xml
        String jpaModuleName = getModuleName(false);
        String jpaApplicationName = getApplicationName(false);     
        
        if (jpaModuleName == null) {
            String subString = databaseSessionName.substring(databaseSessionName.indexOf(
                    APP_SERVER_CLASSLOADER_MODULE_EJB_SEARCH_STRING_PREFIX) + 
                    APP_SERVER_CLASSLOADER_MODULE_EJB_SEARCH_STRING_PREFIX.length());
            if(null != subString) {
                setModuleName(subString);
            } else {
                subString = databaseSessionName.substring(databaseSessionName.indexOf(
                        APP_SERVER_CLASSLOADER_MODULE_WAR_SEARCH_STRING_PREFIX) + 
                        APP_SERVER_CLASSLOADER_MODULE_WAR_SEARCH_STRING_PREFIX.length());
                setModuleName(subString);
            }
            
            if(null != jpaModuleName && jpaModuleName.indexOf(APP_SERVER_CLASSLOADER_MODULE_EJB_WAR_SEARCH_STRING_POSTFIX) > -1) {
                jpaModuleName = jpaModuleName.substring(0,
                        jpaModuleName.indexOf(APP_SERVER_CLASSLOADER_MODULE_EJB_WAR_SEARCH_STRING_POSTFIX)); 
            }
        }

        // Get the application name from the ClassLoader, it is also present in the session name
        if (jpaApplicationName == null) {
            jpaApplicationName = classLoaderName.substring(classLoaderName.indexOf(
                        APP_SERVER_CLASSLOADER_APPLICATION_PU_SEARCH_STRING_PREFIX) + 
                        APP_SERVER_CLASSLOADER_APPLICATION_PU_SEARCH_STRING_PREFIX.length());
            if(null != jpaApplicationName && jpaApplicationName.indexOf(APP_SERVER_CLASSLOADER_APPLICATION_PU_SEARCH_STRING_POSTFIX) > -1) {
                jpaApplicationName = jpaApplicationName.substring(0,
                        jpaApplicationName.indexOf(APP_SERVER_CLASSLOADER_APPLICATION_PU_SEARCH_STRING_POSTFIX)); 
            }
            if(null != jpaApplicationName) {
                jpaApplicationName = jpaApplicationName.replaceAll("[=,:] ", "_");
            }
            setApplicationName(jpaApplicationName);
        }
        // Final check for null values - incorporated into the get functions in these logs
        AbstractSessionLog.getLog().log(SessionLog.FINEST, "mbean_get_application_name", 
                getDatabaseSession().getName(), getApplicationName());
        AbstractSessionLog.getLog().log(SessionLog.FINEST, "mbean_get_module_name", 
                getDatabaseSession().getName(), getModuleName());
    }
    
}
