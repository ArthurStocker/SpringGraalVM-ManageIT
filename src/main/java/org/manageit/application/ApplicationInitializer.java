/*
 * Copyright 2018-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.manageit.application;

import org.manageit.core.*;
import org.springframework.core.Ordered;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Callback for initializing a Spring {@link ConfigurableApplicationContext}
 * prior to being {@linkplain ConfigurableApplicationContext#refresh()
 * refreshed}.
 *
 * @author Arthur Stocker
 * @see org.springframework.context.ApplicationContextInitializer
 */
public class ApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

  /**
   * Initialize the given application context.
   * 
   * @param applicationContext the application to configure
   */
  @Override
  public void initialize(ConfigurableApplicationContext __applicationContext) {
    __applicationContext.addProtocolResolver(new CProtocolResolver(__applicationContext));
  }

  /**
   * Get the order value of this object.
   * <p>
   * Higher values are interpreted as lower priority. As a consequence, the object
   * with the lowest value has the highest priority (somewhat analogous to Servlet
   * {@code load-on-startup} values).
   * <p>
   * Same order values will result in arbitrary sort positions for the affected
   * objects.
   * 
   * @return the order value
   * @see org.springframework.core.Ordered
   * @see org.springframework.core.Ordered#HIGHEST_PRECEDENCE
   * @see org.springframework.core.Ordered#LOWEST_PRECEDENCE
   */
  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE + 100;
  }
}