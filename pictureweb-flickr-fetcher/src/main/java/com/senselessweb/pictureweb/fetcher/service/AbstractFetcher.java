package com.senselessweb.pictureweb.fetcher.service;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.senselessweb.pictureweb.flickr.AuthenticatedFlickrProvider;

public abstract class AbstractFetcher {

  private static final Log log = LogFactory.getLog(AbstractFetcher.class);
  private final AuthenticatedFlickrProvider flickrProvider;

  public AbstractFetcher(final AuthenticatedFlickrProvider flickrProvider) {
    this.flickrProvider = flickrProvider;
  }

  @Scheduled(fixedRate = 30 * 1000)
  public void fetch() {

    final Optional<Flickr> flickr = flickrProvider.getIfAuthenticated();
    if (!flickr.isPresent()) {
      log.warn("Not authenticated yet. Can't fetch anything");
      return;
    }

    try {
      doFetch(flickr.get(), flickr.get().getAuth().getUser().getId());
    } catch (final FlickrException e) {
      log.warn("Error while fetching", e);
    }
  }

  protected abstract void doFetch(Flickr flickr, String userId) throws FlickrException;
}
