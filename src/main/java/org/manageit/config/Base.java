package org.manageit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 */
@Configuration
@ConfigurationProperties
public class Base {

  private static final Logger logger = LoggerFactory.getLogger(Base.class);

  /**
   * .
   * 
   * 
   */
  public Base() {
    logger.info("Initializing Base");
  }
}