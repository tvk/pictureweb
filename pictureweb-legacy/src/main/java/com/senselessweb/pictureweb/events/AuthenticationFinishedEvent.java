package com.senselessweb.pictureweb.events;

import org.springframework.context.ApplicationEvent;

import com.flickr4java.flickr.Flickr;

public class AuthenticationFinishedEvent extends ApplicationEvent {

  private static final long serialVersionUID = -3337907744286217187L;

  public AuthenticationFinishedEvent(final Flickr flickr) {
    super(flickr);
  }

}
