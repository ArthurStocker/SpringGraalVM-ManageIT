package org.manageit.core.platform;

import java.net.URL;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.graalvm.polyglot.Source;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
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
@ConfigurationProperties(prefix = "core.platform")
public class CPlatform {

  private Map<String, Map<String, Object>> _properties = new HashMap<String, Map<String, Object>>();

  private static final Logger logger = LoggerFactory.getLogger(CPlatform.class);

  /**
   * .
   * 
   * 
   */
  public CPlatform() {
    logger.info("Initializing ManageIT Platform");
  }

  /**
   * Set the value for parameters and permissions.
   * 
   * @param __value
   */
  public void setProperties(Map<String, Map<String, Object>> __values) {
    _properties.putAll(__values);
  }

  /**
   * Get the value for parameters and permissions.
   * 
   * @return true/false
   */
  public Map<String, Map<String, Object>> getProperties() {
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

  /**
   * .
   * 
   * @return
   */
  @Bean
  public CommandLineRunner clrPlatformInitializer(ApplicationContext ctx) {
    return args -> {

      logger.info("Loading ManageIT Platform");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        logger.info(beanName);
      }

    };
  }
}