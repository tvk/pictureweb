package com.senselessweb.pictureweb.authentication;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;

@RestController
@RequestMapping("flickr/auth/")
public class FlickrAuthenticationController {

  private final FlickrAuthenticationService authenticationService;

  public FlickrAuthenticationController(final FlickrAuthenticationService authenticationService) {
    this.authenticationService = Preconditions.checkNotNull(authenticationService);
  }

  @RequestMapping("init")
  public String getAuthUrl(final HttpServletRequest request) throws MalformedURLException {
    // TODO Obviously shitty solution!
    return authenticationService.generateAuthenticationUrl(
        new URL("https://www.senselessweb.com/flickr/auth/callback")).orElse(null);
  }

  @RequestMapping("callback")
  public String callback(final @RequestParam("oauth_verifier") String oauthVerifier) {
    return String.valueOf(authenticationService.verify(oauthVerifier));
  }

}