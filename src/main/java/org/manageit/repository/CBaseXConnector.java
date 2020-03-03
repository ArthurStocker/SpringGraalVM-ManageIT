package org.manageit.repository;

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
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 */
@Scope("prototype")
@Repository("BaseXConnector")
public class CBaseXConnector implements ResourceLoader {

  private String _started;

  private static final Logger logger = LoggerFactory.getLogger(CBaseXConnector.class);

  public CBaseXConnector() {
    logger.info("Initializing BaseXConnector");

    _started = ", started on "+(new Date());
  }

  /**
   * Build BaseX Query {@link org.basex.api.client.ClientSession}.
   * 
   * @return
   */
  private ClientQuery buildQuery(URI __resourceURI, ClientSession __basexSession) {
    logger.debug("[BaseXClient - buildQuery] Build BaseX Query");

    ClientQuery __query = null;

    // TODO build query from resourceString
    try {
      // TODO replace by the querystring built from resourceString
      __query = __basexSession.query("declare variable $a as xs:int external; 1 to $a");
      // TODO bind variables extracted from resourceString
      __query.bind("$a", "5");
    } catch (IOException __e) {
      logger.error("BaseXClient build query failed: "+__e.getMessage());
      logger.trace("Trace \n", __e);
    }

    return __query;
  }

  /**
   * Get a BaseX ClientSession, which maight be used in a Scope
   * {@link org.basex.api.client.ClientSession}.
   * 
   * @param resourceString - [host, port, user, password] as String or [null]
   * 
   * @return ClientSession
   */
  private ClientSession getSession(URI __resourceURI) {
    logger.debug("[BaseXClient - getSession] Get BaseX Session");

    ClientSession __basexSession = null;
    Map<String, String> __parameters = new HashMap<String, String>();

    // TODO resolve resourceURI to host, port and user ....

    try {
      if (__parameters.containsKey("host") && __parameters.containsKey("port") && __parameters.containsKey("user") && __parameters.containsKey("password")) {
        logger.debug("[BaseXClient - buildSession] Connect to Server "+__parameters.toString());
        __basexSession = new ClientSession(
          __parameters.get("host"),
          Integer.parseInt(__parameters.get("port")),
          __parameters.get("user"),
          __parameters.get("password")
        );
        logger.trace("[BaseXClient - buildSession] Session-Object "+__basexSession);
      }
    } catch (Exception __e) {
      logger.error("BaseXClient connection failed with: "+__e.getMessage());
      logger.trace("Trace \n", __e);
    }
    return __basexSession;
  }

  /**
   * Get a resource from the database, which maight be used in a Scope
   * 
   * @param resourceString - [host, port, user, password] as URI or [null]
   * 
   * @return Resource
   */
  public Resource getResource(String __resourceString) {
    logger.debug("[BaseXConnector - getResource] Get Basex Resource");

    URI __resourceURI = null;
    try {
      __resourceURI = new URI(__resourceString);
    } catch (URISyntaxException __e) {
      logger.error("[BaseXConnector - getResource] Malformed string, cannot convert resourceString to URI : "+__e.getMessage());
      logger.trace("Trace \n", __e);
    }

    logger.trace("[BaseXConnector - getResource] Load Resource\n\tHOST " +__resourceURI.getHost()+"\n\tPORT "+__resourceURI.getPort()+"\n\tPATH "+__resourceURI.getPath()+"\n\tQUERY "+__resourceURI.getQuery()+"\n\tFRAGMENT "+__resourceURI.getFragment());

    byte[] __byteArray = null;

    try{
      ClientSession __basexSession = getSession(__resourceURI);

      if(__basexSession != null){
        ClientQuery __query = buildQuery(__resourceURI, __basexSession);

        __byteArray = __query.execute().getBytes();
        logger.trace("[BaseXConnector - getResource] Query result\n"+new String(__byteArray));

        __basexSession.close();
      }
    } catch (IOException __e) {
      logger.error("BaseXConnector query resource failed! "+__e.getMessage());
      logger.trace("Trace\n", __e);
    }

    if(__byteArray != null){
      return new ByteArrayResource(__byteArray);
    }

    String __dummy = "" +
    "manageit.properties.load=true\n" +
    "manageit.properties.from=dummy\n" +
    "";
    return new ByteArrayResource(__dummy.getBytes());
  }

  /**
	 * Expose the ClassLoader used by this ResourceLoader.
	 * <p>Clients which need to access the ClassLoader directly can do so
	 * in a uniform manner with the ResourceLoader, rather than relying
	 * on the thread context ClassLoader.
	 * @return the ClassLoader
	 * (only {@code null} if even the system ClassLoader isn't accessible)
	 * @see org.springframework.util.ClassUtils#getDefaultClassLoader()
	 * @see org.springframework.util.ClassUtils#forName(String, ClassLoader)
	 */
  @Override
  public ClassLoader getClassLoader() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Get a string representation from this class with time loaded
   * 
   * @return String
   */
  @Override
  public String toString(){
    return super.toString()+_started;
  }
}