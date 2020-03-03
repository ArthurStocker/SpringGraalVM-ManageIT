package org.manageit.runner;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 */
@ComponentScan("org.manageit.*")
@SpringBootApplication
public class Main{

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  /**
   * ManageIT main method. This invokes the run() method of the SpringApplication
   * core class with the ManageIT Main class as Parameter.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(Main.class);
    application.addInitializers(new ApplicationInitializer());
    application.run(args);
  }

  /**
   * .
   * 
   * @return
   */
  // @Bean
  // public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
  //    return args -> {

  //      System.out.println("Let's inspect the beans provided by Spring Boot:");

  //      String[] beanNames = ctx.getBeanDefinitionNames();
  //      Arrays.sort(beanNames);
  //      for (String beanName : beanNames) {
  //        logger.info(beanName);
  //      }

  //    };
  // }
}