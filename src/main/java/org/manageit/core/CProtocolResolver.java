/*
 * Copyright 2018-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.manageit.core;

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
 * A configurable custom class for protocol-specific resource handling.
 *
 * <p>
 * Used as an SPI for a {@link ResourceLoader}, allowing for custom configurable
 * protocols to be handled without subclassing the loader implementation (or
 * application context implementation).
 * </p>
 * <p>
 * Parameters need tobe configured through properties in the
 * application.properties file. {@code repository.connector.class} has to be set
 * to the full qualified name of the class to use as, and providing the
 * ResourceLoder interface. {@code repository.redirect} is to be set to [all or
 * scheme] and defines if resources without a scheme should be passed to the
 * custom ResourceLoader. {@code repository.scheme} is the comma separated list
 * of scheme to be passed to the custom ResourceLoader.
 * </p>
 * 
 * 
 * @author Arthur Stocker
 * @see org.springframework.core.io.ProtocolResolver
 */
@Lazy(false)
@Primary
public class CProtocolResolver implements ProtocolResolver {

  private String SCHEME;
  private String REDIRECT;
  private String CLASSNAME;

  private String _configured;

  private Environment _environment;
  private ResourceLoader _databaseConnector;
  private ConfigurableApplicationContext _applicationContext;

  private static final Logger logger = LoggerFactory.getLogger(CProtocolResolver.class);

  public CProtocolResolver(ConfigurableApplicationContext __applicationContext) {
    logger.info("Initializing ProtocolResolver");
    _applicationContext = __applicationContext;
    logger.trace("[ProtocolResolver - init] ApplicationContext " + _applicationContext);
    _environment = _applicationContext.getEnvironment();
    logger.trace("[ProtocolResolver - init] Environment " + _environment);

    CLASSNAME = _environment.getProperty("repository.connector.class");
    logger.debug("[ProtocolResolver - init] CLASSNAME " + CLASSNAME);
    REDIRECT = _environment.getProperty("repository.redirect");
    logger.debug("[ProtocolResolver - init] REDIRECT " + REDIRECT);
    SCHEME = _environment.getProperty("repository.scheme");
    logger.debug("[ProtocolResolver - init] SCHEME " + SCHEME);

    // configured = "CLASSNAME,REDIRECT,SCHEME" if not empty
    if (CLASSNAME != null && !CLASSNAME.toLowerCase().equals("")) {
      _configured = _configured + "CLASSNAME,";
    }
    if (REDIRECT != null && !REDIRECT.toLowerCase().equals("")) {
      _configured = _configured + "REDIRECT,";
    }
    if (SCHEME != null && !SCHEME.toLowerCase().equals("")) {
      _configured = _configured + "SCHEME";
    }

    String initmsg = "ProtocolResolver initialized with scheme " + SCHEME + ", redirecting " + REDIRECT
        + ", using connector class " + CLASSNAME;

    if (_configured.indexOf("CLASSNAME") == -1 || _configured.indexOf("REDIRECT") == -1
        || _configured.indexOf("SCHEME") == -1) {
      logger.error(initmsg + ". Configuration is not valid, bypass custom protocols and resource loader !");
    } else {
      logger.info(initmsg);
    }
  }

  /**
   * Get the custom resource loader bean from the defined class.
   * 
   * @return a corresponding {@code ResourceLoader} if the class and bean exists,
   *         or {@code null} otherwise
   */
  private ResourceLoader getConector() {
    logger.debug("[ProtocolResolver - getConector] Get Database Connector");

    ResourceLoader __databaseConnector = null;

    if (_configured.indexOf("CLASSNAME") != -1) {
      try {
        logger.debug("[ProtocolResolver - getConector] Use ClassName " + CLASSNAME);
        __databaseConnector = (ResourceLoader) _applicationContext.getBean(Class.forName(CLASSNAME));
      } catch (ClassNotFoundException __e) {
        logger.error("[ProtocolResolver - getConector] Connector Class not found: " + __e.getMessage());
        logger.trace("Trace \n", __e);
      } catch (BeansException __e) {
        logger
            .error("[ProtocolResolver - getConector] Connector Bean not found: " + CLASSNAME + " " + __e.getMessage());
        logger.trace("Trace \n", __e);
      }
    }

    logger.debug("[ProtocolResolver - getConector] Connector " + __databaseConnector);

    return __databaseConnector;
  }

  /**
   * Resolve the given location against the defined resource loader if it's
   * configured protocol matches.
   * 
   * @param location       the user-specified resource location
   * @param resourceLoader the associated resource loader
   * @return a corresponding {@code Resource} handle if the given location matches
   *         this resolver's protocol, or {@code null} otherwise
   */
  public Resource resolve(String __resourceString, ResourceLoader __resourceLoader) {
    logger.debug("[ProtocolResolver - resolve] Resolve path to resource");

    URI __resourceURI = null;
    try {
      __resourceURI = new URI(__resourceString);
    } catch (URISyntaxException __e) {
      logger.error(
          "[ProtocolResolver - resolve] Malformed string, cannot convert resourceString to URI : " + __e.getMessage());
      logger.trace("Trace \n", __e);
    }

    logger.trace("[ProtocolResolver - resolve] Resource Location\n\tSCHEME " + __resourceURI.getScheme()
        + "\n\tSCHEMESPECIFICPART " + __resourceURI.getSchemeSpecificPart() + "\n\tHOST " + __resourceURI.getHost()
        + "\n\tPORT " + __resourceURI.getPort() + "\n\tPATH " + __resourceURI.getPath());

    Resource resource;

    // check if the connector is initialized
    if (_databaseConnector == null) {
      _databaseConnector = getConector();
    }

    if (_databaseConnector != null && _configured.indexOf("REDIRECT") != -1 && _configured.indexOf("SCHEME") != -1) {
      if (__resourceURI.getPath() != null
          && ((__resourceURI.getScheme() != null && SCHEME.indexOf(__resourceURI.getScheme()) != -1)
              || (__resourceURI.getScheme() == null && REDIRECT.toLowerCase().equals("all")))) {
        logger.debug("[ProtocolResolver - resolve] Use ResourceLoader " + _databaseConnector + ", for SCHEME "
            + __resourceURI.getScheme());
        resource = _databaseConnector.getResource(__resourceString);
        if (resource.exists()) {
          return resource;
        }
      }
    }

    logger.trace("[ProtocolResolver - resolve] Use ResourceLoader " + __resourceLoader);

    return null;
  }
}