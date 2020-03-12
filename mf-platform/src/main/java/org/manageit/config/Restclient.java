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
@ComponentScan("org.manageit.restclient.*")
// @PropertySource("${manageit.config.location}")
@ConfigurationProperties(prefix = "restclient")
@EnableAutoConfiguration
public class Restclient {

  private static final Logger logger = LoggerFactory.getLogger(Restclient.class);

  /**
   * .
   * 
   * 
   */
  public Restclient() {
    logger.info("Initializing Restclient");
  }
}