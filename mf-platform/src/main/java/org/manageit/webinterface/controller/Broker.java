package org.manageit.webinterface.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 */
@Controller
@ComponentScan(basePackages = { "org.manageit.webapplication.*" })
public class Broker {

  private static final Logger logger = LoggerFactory.getLogger(Broker.class);

  /*
   * @RequestMapping(value = "/ex/foos/{id}", method = GET)
   * 
   * @ResponseBody public String getFoosBySimplePathWithPathVariable(
   * 
   * @PathVariable("id") long id) { return "Get a specific Foo with id=" + id; }
   */

  /**
   * .
   * 
   * @return
   */
  @RequestMapping(value = "/{module}/{member}", method = RequestMethod.GET) // produces = "text/plain"
  public ResponseEntity<String> dispatch(HttpServletRequest request, @PathVariable("module") String module,
      @PathVariable("member") String member) {

    // TODO: dynamically get the function name from the request object

    String response = "Hello World";

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Refresh", "1");

    ResponseEntity<String> result = new ResponseEntity<String>(response, responseHeaders, HttpStatus.OK);

    return result; // new ResponseEntity<Object>(response, responseHeaders, HttpStatus.OK);
  }
}