package org.manageit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@ComponentScan("org.manageit.export.*")
// @PropertySource("${manageit.config.location}")
@ConfigurationProperties(prefix = "export")
@EnableAutoConfiguration
public class Export {

  private static final Logger logger = LoggerFactory.getLogger(Export.class);

  /**
   * .
   * 
   * 
   */
  public Export() {
    logger.info("Initializing Export");
  }
}