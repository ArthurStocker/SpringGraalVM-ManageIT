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

package org.manageit.core.connectors.basex;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.basex.api.client.ClientQuery;
import org.basex.api.client.ClientSession;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * Spring Boot BaseX connector class.
 *
 * @author Arthur Stocker
 */
@Scope("prototype")
@Component("CConnector")
public class CConnector implements ResourceLoader {

  private String _started;

  private static final Logger logger = LoggerFactory.getLogger(CConnector.class);

  public CConnector() {
    logger.info("Initializing BaseX Connector");

    _started = ", started on " + (new Date());
  }

  /**
   * Build BaseX Query {@link org.basex.api.client.ClientSession}.
   * 
   * @param resourceURI  - URI to the resource including: host, port, user and
   *                     password, or {@code null}
   * @param basexSession - ClientSession to be used, or {@code null}
   * 
   * @return a {@code ClientQuery} if it's built without errors, or {@code null}
   *         otherwise
   */
  private ClientQuery buildQuery(URI __resourceURI, ClientSession __basexSession) {
    logger.debug("[Connector - buildQuery] Build BaseX Query");

    ClientQuery __query = null;

    // TODO build query from resourceString
    try {
      // TODO replace by the querystring built from resourceString
      __query = __basexSession.query("declare variable $a as xs:int external; 1 to $a");
      // TODO bind variables extracted from resourceString
      __query.bind("$a", "5");
    } catch (IOException __e) {
      logger.error("Connector build BaseX query failed: " + __e.getMessage());
      logger.trace("Trace \n", __e);
    }

    return __query;
  }

  /**
   * Get a BaseX ClientSession, which maight be used in a Scope
   * {@link org.basex.api.client.ClientSession}.
   * 
   * @param resourceURI - URI to the resource including: host, port, user and
   *                    password, or {@code null}
   * 
   * @return a {@code ClientSession} if it's connected without errors, or
   *         {@code null} otherwise
   */
  private ClientSession getSession(URI __resourceURI) {
    logger.debug("[Connector - getSession] Get BaseX Session");

    ClientSession __basexSession = null;
    Map<String, String> __parameters = new HashMap<String, String>();

    // TODO resolve resourceURI to host, port and user ....

    try {
      if (__parameters.containsKey("host") && __parameters.containsKey("port") && __parameters.containsKey("user")
          && __parameters.containsKey("password")) {
        logger.debug("[Connector - getSession] Connect to BaseX Server " + __parameters.toString());
        __basexSession = new ClientSession(__parameters.get("host"), Integer.parseInt(__parameters.get("port")),
            __parameters.get("user"), __parameters.get("password"));
        logger.trace("[Connector - getSession] BaseX Session-Object " + __basexSession);
      }
    } catch (Exception __e) {
      logger.error("Connector connection to BaseX Server failed with: " + __e.getMessage());
      logger.trace("Trace \n", __e);
    }
    return __basexSession;
  }

  /**
   * Get a resource from the database, which maight be used in a Scope
   * 
   * @param resourceString - String to the resource including: host, port, user
   *                       and password, or {@code null}
   * 
   * @return a {@code Resource} if it's exising and queried without errors, or
   *         {@code ByteArrayResource} with default properties otherwise
   */
  public Resource getResource(String __resourceString) {
    logger.debug("[Connector - getResource] Get Basex Resource");

    URI __resourceURI = null;
    try {
      __resourceURI = new URI(__resourceString);
    } catch (URISyntaxException __e) {
      logger
          .error("Connector resource string is not valid, cannot convert resource string to URI: " + __e.getMessage());
      logger.trace("Trace \n", __e);
    }

    logger.trace("[Connector - getResource] Load Resource\n\tHOST " + __resourceURI.getHost() + "\n\tPORT "
        + __resourceURI.getPort() + "\n\tPATH " + __resourceURI.getPath() + "\n\tQUERY " + __resourceURI.getQuery()
        + "\n\tFRAGMENT " + __resourceURI.getFragment());

    byte[] __byteArray = null;

    try {
      ClientSession __basexSession = getSession(__resourceURI);

      if (__basexSession != null) {
        ClientQuery __query = buildQuery(__resourceURI, __basexSession);

        __byteArray = __query.execute().getBytes();
        logger.trace("[Connector - getResource] Query result\n" + new String(__byteArray));

        __basexSession.close();
      }
    } catch (IOException __e) {
      logger.error("Connector query for resource form BaseX Server failed! " + __e.getMessage());
      logger.trace("Trace\n", __e);
    }

    if (__byteArray != null) {
      return new ByteArrayResource(__byteArray);
    }

    String __propertiesString = "" + "manageit.properties.loaded=false\n"
        + "manageit.properties.connector=BaseXConnector\n" + "";
    return new ByteArrayResource(__propertiesString.getBytes());
  }

  /**
   * Expose the ClassLoader used by this ResourceLoader.
   * <p>
   * Clients which need to access the ClassLoader directly can do so in a uniform
   * manner with the ResourceLoader, rather than relying on the thread context
   * ClassLoader.
   * 
   * @return the ClassLoader (only {@code null} if even the system ClassLoader
   *         isn't accessible)
   * @see org.springframework.util.ClassUtils#getDefaultClassLoader()
   * @see org.springframework.util.ClassUtils#forName(String, ClassLoader)
   */
  @Override
  public ClassLoader getClassLoader() {
    // TODO Auto-generated method stub
    return null; // _applicationContext.getClassLoader();
  }

  /**
   * Get a string representation from this class with time loaded
   * 
   * @return String
   */
  @Override
  public String toString() {
    return super.toString() + _started;
  }
}