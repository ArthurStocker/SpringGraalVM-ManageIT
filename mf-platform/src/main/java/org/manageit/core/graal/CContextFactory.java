package org.manageit.core.graal;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.graalvm.polyglot.Context;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 */
@Configuration
@ComponentScan("org.manageit.core.*")
@ConfigurationProperties(prefix = "graal.context")
public class CContextFactory {

  private Map<String, Map<String, Boolean>> _properties = new HashMap<String, Map<String, Boolean>>();

  private static final Logger logger = LoggerFactory.getLogger(CContextFactory.class);

  /**
   * .
   * 
   * 
   */
  public CContextFactory() {
    logger.info("Initializing GraalVM Context Factory");
  }

  /**
   * Set the value for parameters and permissions.
   * 
   * @param __value
   */
  public void setProperties(Map<String, Map<String, Boolean>> __values) {
    _properties.putAll(__values);
  }

  /**
   * Get the value for parameters and permissions.
   * 
   * @return true/false
   */
  public Map<String, Map<String, Boolean>> getProperties() {
    return _properties;
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  public Function<Map<String, Map<String, Boolean>>, Context> executeGraalContextFactory() {
    return __args -> buildGraalContext(__args);
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  @Scope("prototype")
  public Context buildGraalContext(Map<String, Map<String, Boolean>> __args) {
    Context context = null;
    if (__args != null) {
      // TODO: create Context with options
      try {
        context = Context.newBuilder().allowAllAccess(true).engine(new CEngineFactory().getEngine()).build(); // .allowIO(true)
      } catch (Exception e) {
        logger.error("Could not build Graal Context!");
        logger.trace("Trace \n", e);
      }
    }
    return context;
  }
}