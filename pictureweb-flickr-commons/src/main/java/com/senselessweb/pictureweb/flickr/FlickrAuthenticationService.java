package com.senselessweb.pictureweb.flickr;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Provider;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.auth.Permission;
import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.datastore.domain.StoredAuthentication;
import com.senselessweb.pictureweb.datastore.repository.AuthenticationRepository;

@Service
public class FlickrAuthenticationService {

  private final AuthenticatedFlickrProvider flickr;
  private final Provider<AtomicReference<Token>> requestTokenHolder;
  private final AuthenticationRepository authenticationRepository;

  public FlickrAuthenticationService(
      final AuthenticatedFlickrProvider flickr,
      final @Qualifier("requestToken") Provider<AtomicReference<Token>> requestToken,
      final AuthenticationRepository authenticationRepository) {
    this.flickr = Preconditions.checkNotNull(flickr);
    this.requestTokenHolder = Preconditions.checkNotNull(requestToken);
    this.authenticationRepository = Preconditions.checkNotNull(authenticationRepository);
  }

  @Bean("requestToken")
  @Scope(value = "session")
  public AtomicReference<Token> requestToken() {
    return new AtomicReference<Token>();
  }

  public Optional<String> generateAuthenticationUrl(final URL callbackUrl) {
    if (flickr.isAuthenticated()) {
      return Optional.empty();
    }
    if (requestTokenHolder.get().get() == null) {
      final Token requestToken = flickr.getAnyway().getAuthInterface().getRequestToken(callbackUrl.toString());
      this.requestTokenHolder.get().set(requestToken);
    }

    return Optional.of(flickr.getAnyway().getAuthInterface().getAuthorizationUrl(
        requestTokenHolder.get().get(), Permission.READ));
  }

  public boolean verify(final String verifier) {
    final Token token = requestTokenHolder.get().get();
    if (token == null) {
      throw new IllegalStateException("No requestToken set. Please restart authentication");
    }
    final Token accessToken = flickr.getAnyway().getAuthInterface().getAccessToken(
        requestTokenHolder.get().get(), new Verifier(verifier));
    this.authenticationRepository.save(new StoredAuthentication(accessToken.getToken(), accessToken.getSecret()));
    requestTokenHolder.get().set(null);
    return flickr.isAuthenticated();
  }

}
