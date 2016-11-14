package com.senselessweb.pictureweb.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ErrorHandler {

  private static final Log log = LogFactory.getLog(ErrorHandler.class);

  @ExceptionHandler(NoHandlerFoundException.class)
  public void handleException(final NoHandlerFoundException e) {
    log.error("Handling exception", e);

  }

}
