package org.manageit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 */
//@Configuration
@PropertySource("${elixir.config.location}")
@ConfigurationProperties(prefix = "elixir")
public class ElixirApp {

  private static final Logger logger = LoggerFactory.getLogger(ElixirApp.class);

  /**
   * .
   * 
   * 
   */
  public ElixirApp() {
    logger.info("Initializing ElixirApp");
  }
}