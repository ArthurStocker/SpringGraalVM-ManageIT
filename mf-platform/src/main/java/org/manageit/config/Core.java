package org.manageit.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
@ConfigurationProperties(prefix = "core")
@EnableAutoConfiguration
public class Core {

  private Map<String, Map<String, String>> _properties = new HashMap<String, Map<String, String>>();

  private static final Logger logger = LoggerFactory.getLogger(Core.class);

  /**
   * .
   * 
   * 
   */
  public Core() {
    logger.info("Initializing Core");
  }

  /**
   * Set the value for parameters and permissions.
   * 
   * @param __value
   */
  public void setProperties(Map<String, Map<String, String>> __values) {
    _properties.putAll(__values);
  }

  /**
   * Get the value for parameters and permissions.
   * 
   * @return properties
   */
  public Map<String, Map<String, String>> getProperties() {
    return _properties;
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  public CommandLineRunner clrCoreInitializer(ApplicationContext ctx) {
    return args -> {

      logger.info("Loading Core");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        logger.info(beanName);
      }

    };
  }
}