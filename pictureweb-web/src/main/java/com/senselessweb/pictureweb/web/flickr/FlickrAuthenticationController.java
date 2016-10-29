package com.senselessweb.pictureweb.web.flickr;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.flickr.FlickrAuthenticationService;

@RestController
@RequestMapping("flickr/auth/")
public class FlickrAuthenticationController {

  private final FlickrAuthenticationService authenticationService;

  public FlickrAuthenticationController(final FlickrAuthenticationService authenticationService) {
    this.authenticationService = Preconditions.checkNotNull(authenticationService);
  }

  @RequestMapping("init")
  public String getAuthUrl(final HttpServletRequest request) throws MalformedURLException {
    final String callbackUrl = StringUtils.removeEnd(request.getRequestURL().toString(), "init") + "callback";
    return authenticationService.generateAuthenticationUrl(new URL(callbackUrl)).orElse(null);
  }

  @RequestMapping("callback")
  public String callback(final @RequestParam("oauth_verifier") String oauthVerifier) {
    return String.valueOf(authenticationService.verify(oauthVerifier));
  }

}
