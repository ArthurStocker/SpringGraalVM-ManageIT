package org.manageit.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
@ComponentScan("org.manageit.event.*")
@ConfigurationProperties(prefix = "event")
@EnableAutoConfiguration
public class Event {

  private Map<String, Map<String, String>> _properties = new HashMap<String, Map<String, String>>();

  private static final Logger logger = LoggerFactory.getLogger(Event.class);

  /**
   * .
   * 
   * 
   */
  public Event() {
    logger.info("Initializing Event handler");
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
}