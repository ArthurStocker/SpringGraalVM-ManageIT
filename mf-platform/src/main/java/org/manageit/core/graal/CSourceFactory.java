package org.manageit.core.graal;

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.graalvm.polyglot.Source;
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
@ConfigurationProperties(prefix = "graal.source")
public class CSourceFactory {

  private Map<String, Map<String, Boolean>> _properties = new HashMap<String, Map<String, Boolean>>();

  private static final Logger logger = LoggerFactory.getLogger(CSourceFactory.class);

  /**
   * .
   * 
   * 
   */
  public CSourceFactory() {
    logger.info("Initializing GraalVM Source Factory");
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
  public Function<Map<String, Object>, Source> executeGraalSourceFactory() {
    return __args -> buildGraalSource(__args);
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  @Scope("prototype")
  public Source buildGraalSource(Map<String, Object> __args) {
    Source source = null;
    if (__args != null) {
      // TODO: create Source with options
      try {
        if (__args.get("language") instanceof String && __args.get("url") instanceof URL) {
          source = Source.newBuilder(__args.get("language").toString(), (URL) __args.get("url")).build();
        } else {
          throw new IllegalArgumentException("Language or URL not specified!");
        }
      } catch (Exception e) {
        logger.error("Could not build Graal Source!");
        logger.trace("Trace \n", e);
      }
    }
    return source;
  }
}