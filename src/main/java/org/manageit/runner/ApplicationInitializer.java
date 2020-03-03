package org.manageit.runner;

import org.manageit.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class ApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  /**
   * .
   * 
   * @param ConfigurableApplicationContext 
   * @return
   */
  @Override
  public void initialize(ConfigurableApplicationContext __applicationContext) {
    __applicationContext.addProtocolResolver(new CProtocolResolver(__applicationContext));
  }

  /**
   * .
   * 
   * @return
   */
  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE + 100;
  }
}