package com.senselessweb.pictureweb.flickr;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.datastore.domain.StoredAuthentication;
import com.senselessweb.pictureweb.datastore.repository.AuthenticationRepository;

@Service
public class AuthenticatedFlickrProvider {

  private static final Log log = LogFactory.getLog(AuthenticatedFlickrProvider.class);
  private final AuthenticationRepository authenticationRepository;
  private final Flickr flickr;

  public AuthenticatedFlickrProvider(AuthenticationRepository authenticationRepository, Flickr flickr) {
    this.authenticationRepository = Preconditions.checkNotNull(authenticationRepository);
    this.flickr = Preconditions.checkNotNull(flickr);
  }

  public Optional<Flickr> getIfAuthenticated() {
    if (loadAuthentication()) {
      RequestContext.getRequestContext().setAuth(this.flickr.getAuth());
      return Optional.of(flickr);
    }
    return Optional.empty();
  }

  public Flickr getAnyway() {
    return flickr;
  }

  public boolean isAuthenticated() {
    return loadAuthentication();
  }

  private boolean loadAuthentication() {
    if (flickr.getAuth() != null) {
      return true;
    }
    final List<StoredAuthentication> authentications = authenticationRepository.findAll();
    if (!authentications.isEmpty()) {
      final StoredAuthentication auth = authentications.iterator().next();
      try {
        this.flickr.setAuth(this.flickr.getAuthInterface().checkToken(auth.getValue(), auth.getSecret()));
      } catch (final FlickrException e) {
        log.error("Error while authenticating", e);
        return false;
      }
      return true;
    }
    return false;
  }

}
