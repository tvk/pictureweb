package com.senselessweb.pictureweb.flickr.authentication;

import java.net.MalformedURLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flickr4java.flickr.FlickrException;
import com.google.common.base.Preconditions;

@RestController
public class FlickrAuthCallbackController {

  static final String FLICKR_CALLBACK_URL_REQUEST_MAPPING = "flickr/callback";

  private final FlickrAuthService flickrAuthService;

  public FlickrAuthCallbackController(final FlickrAuthService flickrAuthService) throws MalformedURLException {
    this.flickrAuthService = Preconditions.checkNotNull(flickrAuthService);
  }

  @RequestMapping(FLICKR_CALLBACK_URL_REQUEST_MAPPING)
  @ResponseStatus(HttpStatus.OK)
  public void receiveCallback(final @RequestParam("oauth_token") String oauthToken,
      final @RequestParam("oauth_verifier") String oauthVerifier) throws FlickrException {
    this.flickrAuthService.setVerifier(oauthVerifier);
  }
}
