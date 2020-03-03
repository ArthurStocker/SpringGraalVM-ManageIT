package org.manageit.config;

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 */
@Configuration
@PropertySource("${manageit.config.location}")
@ConfigurationProperties(prefix = "core")
public class Core {

  //private Map<String, String> _basexparameters = new HashMap<String, String>();

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

  @Bean
  @Scope("singleton")
  public String about() {
    String about = "[" + "/" + " (" + ") ]";
    return about;
  }

  //@Autowired
  //public void test(ConfigurableEnvironment env) {
    //PropertiesConfiguration pc = new PropertiesConfiguration(); // apache commons-configuration
  //}

  /*
   * Properties .... public void setDescriptor(List<CPropertyDescriptor>
   * __descriptors) { _descriptors = __descriptors; } public
   * List<CPropertyDescriptor> getDescriptor() { return _descriptors; }
   * 
   * @Bean public Function<String, CPropertyDescriptor> getDescriptorByName() {
   * return arg -> findDescriptor(arg); }
   * 
   * @Bean
   * 
   * @Scope("prototype") public CPropertyDescriptor findDescriptor(String arg) {
   * logger.info("DESCRIPTOR TYPE: "+arg); CPropertyDescriptor descriptor = null;
   * if (arg != null) { Iterator<CPropertyDescriptor> __descriptors_ =
   * _descriptors.iterator();
   * 
   * while (__descriptors_.hasNext()) { CPropertyDescriptor _descriptor =
   * __descriptors_.next(); logger.info("DESCRIPTOR: "+_descriptor.getName()); if
   * (_descriptor.getName().equals(arg)) { descriptor = _descriptor; } } } return
   * descriptor; }
   * 
   * 
   * 
   * public void setProperty(List<CProperty> __properties) { _properties =
   * __properties; } public List<CProperty> getProperty() { return _properties; }
   */

  /**
   * .
   * 
   * @param extensions
   */
  public void setExtensions(Map<String, Map<String, String>> __extensions) {
    logger.debug("Set File Extension parameters to "+__extensions);
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
  //public void setBaseXParameters(Map<String, String> __options) {
  //  logger.debug("Set BaseX Session parameters to "+__options);
  //  _basexparameters.putAll(__options);
  //}

  /**
   * .
   *
   * @return 
   */
  //public Map<String, String> getBaseXParameters() {
  //  return _basexparameters;
  //}

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
}