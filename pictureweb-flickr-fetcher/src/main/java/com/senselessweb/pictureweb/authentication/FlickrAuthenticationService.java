package com.senselessweb.pictureweb.authentication;

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
import com.senselessweb.pictureweb.datastore.client.AuthenticationService;
import com.senselessweb.pictureweb.datastore.domain.Authentication;

@Service
public class FlickrAuthenticationService {

  private final AuthenticatedFlickrProvider flickr;
  private final Provider<AtomicReference<Token>> requestTokenHolder;
  private final AuthenticationService authenticationRepository;

  public FlickrAuthenticationService(
      final AuthenticatedFlickrProvider flickr,
      final @Qualifier("requestToken") Provider<AtomicReference<Token>> requestToken,
      final AuthenticationService authenticationRepository) {
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
    this.authenticationRepository.save(authentication(accessToken.getToken(), accessToken.getSecret()));
    requestTokenHolder.get().set(null);
    return flickr.isAuthenticated();
  }

  private Authentication authentication(String token, String secret) {
    return new Authentication() {
      
      @Override
      public String getId() {
        return null;
      }
      
      @Override
      public String getValue() {
        return token;
      }
      
      @Override
      public String getSecret() {
        return secret;
      }
    };
  }
}
