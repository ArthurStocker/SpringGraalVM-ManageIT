package org.manageit.repository;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.env.Environment;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 */
@Lazy(false)
@Primary
public class CProtocolResolver implements ProtocolResolver {

  private String SCHEME;
  private String REDIRECT;
  private String CLASSNAME;

  private Environment _environment;
  private ResourceLoader _baseConnector;
  private ConfigurableApplicationContext _applicationContext;

  private static final Logger logger = LoggerFactory.getLogger(CProtocolResolver.class);

  public CProtocolResolver(ConfigurableApplicationContext __applicationContext) {
    logger.info("Initializing ProtocolResolver");
    _applicationContext = __applicationContext;
    logger.trace("[ProtocolResolver - init] ApplicationContext "+_applicationContext);
    _environment = _applicationContext.getEnvironment();
    logger.trace("[ProtocolResolver - init] Environment "+_environment);

    CLASSNAME = _environment.getProperty("database.connector.class");
    logger.debug("[ProtocolResolver - init] CLASSNAME "+CLASSNAME);
    REDIRECT = _environment.getProperty("database.redirect");
    logger.debug("[ProtocolResolver - init] REDIRECT "+REDIRECT);
    SCHEME = _environment.getProperty("database.scheme");
    logger.debug("[ProtocolResolver - init] SCHEME "+SCHEME);

    logger.info("ProtocolResolver initialized with scheme "+SCHEME+", redirecting "+REDIRECT+", using connector class "+CLASSNAME);
  }

  private ResourceLoader getConector() {
    logger.debug("[ProtocolResolver - getConector] Get Database Connector");

    ResourceLoader __baseConnector = null;

    try {
      logger.debug("[ProtocolResolver - getConector] Use ClassName "+CLASSNAME);
      __baseConnector = (ResourceLoader) _applicationContext.getBean(Class.forName(CLASSNAME));
    } catch (ClassNotFoundException __e) {
      logger.error("[ProtocolResolver - getConector] BaseConnector Class not found: "+CLASSNAME+" "+__e.getMessage());
      logger.trace("Trace \n", __e);
    } catch (BeansException __e) {
      logger.error("[ProtocolResolver - getConector] BaseConnector Bean not found: "+CLASSNAME+" "+__e.getMessage());
      logger.trace("Trace \n", __e);
    }

    logger.debug("[ProtocolResolver - getConector] BaseConnector " + __baseConnector);

    return __baseConnector;
  }

  public Resource resolve(String __resourceString, ResourceLoader __resourceLoader) {
    logger.debug("[ProtocolResolver - resolve] Resolve path to resource");

    URI __resourceURI = null;
    try {
      __resourceURI = new URI(__resourceString);
    } catch (URISyntaxException __e) {
      logger.error("[ProtocolResolver - resolve] Malformed string, cannot convert resourceString to URI : "+__e.getMessage());
      logger.trace("Trace \n", __e);
    }
    
    logger.trace("[ProtocolResolver - resolve] Resource Location\n\tSCHEME "+__resourceURI.getScheme()+"\n\tSCHEMESPECIFICPART "+__resourceURI.getSchemeSpecificPart()+"\n\tHOST "+__resourceURI.getHost()+"\n\tPORT "+__resourceURI.getPort()+"\n\tPATH "+__resourceURI.getPath());

    Resource resource;

    // check if baseconnector is initialized
    if(_baseConnector == null) {
      _baseConnector = getConector();
    }

    if(__resourceURI.getPath() != null && ((__resourceURI.getScheme() != null && SCHEME.indexOf(__resourceURI.getScheme()) != -1) || (__resourceURI.getScheme() == null && (REDIRECT.toLowerCase().equals("all") || REDIRECT.toLowerCase().equals("relative"))))){
      logger.debug("[ProtocolResolver - resolve] Use ResourceLoader "+_baseConnector+", for SCHEME "+__resourceURI.getScheme());
      resource = _baseConnector.getResource(__resourceString);
      if(resource.exists()){
        return resource;
      }
    }

    logger.trace("[ProtocolResolver - resolve] Use ResourceLoader "+__resourceLoader);

    return null;
  }
}