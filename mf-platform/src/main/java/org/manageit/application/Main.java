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

import org.manageit.config.*;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Spring Boot Application entry class.
 *
 * @author Arthur Stocker
 */
@SpringBootApplication
public class Main {

  /**
   * ManageIT main method. This invokes the run() method of the
   * SpringApplicationBuilder boot class with the Main class as Parameter.
   *
   * @param args command line arguments
   * @see org.springframework.boot.builder.SpringApplicationBuilder
   */
  public static void main(String[] args) {
    SpringApplicationBuilder builder = new SpringApplicationBuilder();

    SpringApplicationBuilder parent = builder.parent(Core.class).web(WebApplicationType.NONE);
    parent.application().addInitializers(new ApplicationInitializer());

    SpringApplicationBuilder child = parent.child(Event.class).web(WebApplicationType.NONE);
    child.application().addInitializers(new ApplicationInitializer());

    SpringApplicationBuilder sibling = child.sibling(Webinterface.class).web(WebApplicationType.SERVLET);
    sibling.application().addInitializers(new ApplicationInitializer());

    sibling.run(args);
  }
}