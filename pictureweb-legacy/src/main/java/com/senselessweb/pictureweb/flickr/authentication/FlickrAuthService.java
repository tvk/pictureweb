package com.senselessweb.pictureweb.flickr.authentication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.google.common.base.Preconditions;
import com.senselessweb.java.utils.ExtPreconditions;
import com.senselessweb.pictureweb.Context;
import com.senselessweb.pictureweb.events.AuthenticationFinishedEvent;

@Service
public class FlickrAuthService implements FlickrProvider {

  private static final Log log = LogFactory.getLog(FlickrAuthService.class);

  private static final String APIKEY = "pictureweb.flickr.apikey";
  private static final String APISECRET = "pictureweb.flickr.apisecret";
  private static final String ACCESSTOKEN_VALUE = "pictureweb.accesstoken.value";
  private static final String ACCESSTOKEN_SECRET = "pictureweb.accesstoken.secret";

  private final ApplicationEventPublisher publisher;
  private final Flickr flickr;
  private final AuthInterface authInterface;
  private final File configFile;
  private final URL baseUrl;

  private Token requestToken;
  private Token accessToken;

  public FlickrAuthService(final ApplicationEventPublisher publisher, final Context context)
      throws MalformedURLException {

    this.publisher = Preconditions.checkNotNull(publisher);
    this.baseUrl = Preconditions.checkNotNull(context.getBaseUrl());
    this.configFile = new File(context.getConfigPath(), "auth.properties");

    final String apiKey = ExtPreconditions.checkNotBlank(System.getenv(APIKEY),
        "No api key given. Please ensure that the environment variable \"" + APIKEY + "\".");
    final String apiSecret = ExtPreconditions.checkNotBlank(System.getenv(APISECRET),
        "No api secret given. Please ensure that the environment variable \"" + APISECRET + "\".");
    this.flickr = new Flickr(apiKey, apiSecret, new REST());
    this.authInterface = flickr.getAuthInterface();
  }

  @PostConstruct
  public void startAuthentication() throws IOException, FlickrException {

    this.accessToken = readAccessToken();
    if (this.accessToken != null) {
      log.debug("Using access token from storage");
      this.finishAuthentication();
    } else {
      log.debug("No stored access token, will generate callback url");
      generateCallbackUrl();
    }
  }

  private void generateCallbackUrl() throws MalformedURLException {
    requestToken = authInterface
        .getRequestToken(new URL(baseUrl, FlickrAuthCallbackController.FLICKR_CALLBACK_URL_REQUEST_MAPPING).toString());

    final String authUrl = authInterface.getAuthorizationUrl(requestToken, Permission.READ);
    log.debug("==== Please open this url in a browser: ====");
    log.debug(authUrl);
  }

  private Token readAccessToken() throws IOException {
    if (configFile.isFile()) {
      try (final FileReader reader = new FileReader(configFile)) {

        final Properties props = new Properties();
        props.load(reader);
        if (props.containsKey(ACCESSTOKEN_VALUE) && props.containsKey(ACCESSTOKEN_SECRET)) {
          return new Token(props.getProperty(ACCESSTOKEN_VALUE), props.getProperty(ACCESSTOKEN_SECRET));
        }
      }
    }
    return null;
  }

  public void setVerifier(final String verifier) throws FlickrException {

    final Verifier theVerifier = new Verifier(verifier);
    this.accessToken = authInterface.getAccessToken(requestToken, theVerifier);
    try {
      this.storeAccessToken();
    } catch (final IOException e) {
      log.error("Storing access token failed", e);
    }
    this.finishAuthentication();
  }

  private void finishAuthentication() throws FlickrException {
    this.flickr.setAuth(authInterface.checkToken(this.accessToken));
    log.debug("Authentication finished");
    this.publisher.publishEvent(new AuthenticationFinishedEvent(this.flickr));
  }

  private void storeAccessToken() throws IOException {
    final Properties props = new Properties();
    props.put(ACCESSTOKEN_VALUE, this.accessToken.getToken());
    props.put(ACCESSTOKEN_SECRET, this.accessToken.getSecret());
    try (final FileOutputStream fos = new FileOutputStream(configFile)) {
      props.store(fos, null);
    }
    log.debug("Access token successfully stored to " + configFile);
  }

  @Override
  public String getUserId() {
    return get().getAuth().getUser().getId();
  }

  @Override
  public boolean isAuthenticated() {
    return flickr.getAuth() != null;
  }

  public Flickr get() {
    if (this.flickr.getAuth() == null) {
      throw new IllegalStateException("Authentication process is not completed yet");
    }
    RequestContext.getRequestContext().setAuth(this.flickr.getAuth());
    return this.flickr;
  }
}
