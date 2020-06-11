package org.manageit.core.graal;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.graalvm.polyglot.Engine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("${manageit.config.location}")
@ConfigurationProperties(prefix = "graal.engine")
public class CEngineFactory {

  private Engine _engine;
  private Map<String, Map<String, Boolean>> _properties = new HashMap<String, Map<String, Boolean>>();

  private static final Logger logger = LoggerFactory.getLogger(CEngineFactory.class);

  /**
   * .
   * 
   * 
   */
  public CEngineFactory() {
    logger.info("Initializing GraalVM Engine Factory");
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
   * Get the main Graal Engine, which maight be used in a Scope
   * {@link org.graalvm.polyglot.Context}.
   * 
   * @return Engine
   */
  @Bean
  @Primary
  @Scope("singleton")
  public Engine getEngine() {
    if (_engine == null)
      _engine = buildGraalEngine(_properties);
    return _engine;
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  public Function<Map<String, Map<String, Boolean>>, Engine> executeGraalEngineFactory() {
    return __args -> buildGraalEngine(__args);
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  @Scope("prototype")
  public Engine buildGraalEngine(Map<String, Map<String, Boolean>> __args) {
    Engine engine = null;
    if (__args != null) {
      // TODO: create Engine with options
      try {
        engine = Engine.newBuilder().build();
      } catch (Exception e) {
        logger.error("Could not build Graal Engine!");
        logger.trace("Trace \n", e);
      }
    }
    return engine;
  }
}