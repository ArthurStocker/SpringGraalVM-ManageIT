package org.manageit.config;

import java.net.URL;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
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
// @PropertySource("${manageit.config.location}")
@ConfigurationProperties(prefix = "core")
@EnableAutoConfiguration
public class Core {

  // private Map<String, String> _basexparameters = new HashMap<String, String>();

  private Map<String, Map<String, String>> _extensions = new HashMap<String, Map<String, String>>();

  private Engine _engine;
  private Map<String, Map<String, String>> _enginedefaults = new HashMap<String, Map<String, String>>();

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
   * .
   * 
   * @param extensions
   */
  public void setExtensions(Map<String, Map<String, String>> __extensions) {
    logger.debug("Set File Extension parameters to " + __extensions);
    _extensions.putAll(__extensions);
  }

  /**
   * .
   * 
   * @return
   */
  public Map<String, Map<String, String>> getExtensions() {
    return _extensions;
  }

  /**
   * .
   * 
   * @param options
   */
  public void setEngineParameters(Map<String, Map<String, String>> __options) {
    _enginedefaults.putAll(__options);
  }

  /**
   * .
   *
   * @return
   */
  public Map<String, Map<String, String>> getEngineParameters() {
    return _enginedefaults;
  }

  /**
   * Get the main Graal Engine, which maight be used in a Scope
   * {@link org.graalvm.polyglot.Context}.
   * 
   * @return Engine
   */
  @Bean
  @Primary
  @Scope("singleton")
  public Engine getEngine() {
    if (_engine == null)
      _engine = buildGraalEngine(_enginedefaults.get("sys"));
    return _engine;
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  public Function<Map<String, String>, Engine> executeGraalEngineFactory() {
    return __args -> buildGraalEngine(__args);
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  @Scope("prototype")
  public Engine buildGraalEngine(Map<String, String> __args) {
    Engine engine = null;
    if (__args != null) {
      // TODO: create Engine with options
      try {
        engine = Engine.newBuilder().build();
      } catch (Exception e) {
        logger.error("Could not build Graal Engine!");
        logger.trace("Trace \n", e);
      }
    }
    return engine;
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  public Function<Map<String, String>, Context> executeGraalContextFactory() {
    return __args -> buildGraalContext(__args);
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  @Scope("prototype")
  public Context buildGraalContext(Map<String, String> __args) {
    Context context = null;
    if (__args != null) {
      // TODO: create Context with options
      try {
        context = Context.newBuilder().allowAllAccess(true).engine(getEngine()).build(); // .allowIO(true)
      } catch (Exception e) {
        logger.error("Could not build Graal Context!");
        logger.trace("Trace \n", e);
      }
    }
    return context;
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  public Function<Map<String, Object>, Source> executeGraalSourceFactory() {
    return __args -> buildGraalSource(__args);
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  @Scope("prototype")
  public Source buildGraalSource(Map<String, Object> __args) {
    Source source = null;
    if (__args != null) {
      // TODO: create Source with options
      try {
        if (__args.get("language") instanceof String && __args.get("url") instanceof URL) {
          source = Source.newBuilder(__args.get("language").toString(), (URL) __args.get("url")).build();
        } else {
          throw new IllegalArgumentException("Language or URL not specified!");
        }
      } catch (Exception e) {
        logger.error("Could not build Graal Source!");
        logger.trace("Trace \n", e);
      }
    }
    return source;
  }

  /**
   * .
   * 
   * @return
   */
  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      System.out.println("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        logger.info(beanName);
      }

    };
  }
}