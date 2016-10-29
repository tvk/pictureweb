package com.senselessweb.pictureweb.fetcher.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.senselessweb.pictureweb.datastore.domain.StoredAlbum;
import com.senselessweb.pictureweb.datastore.repository.AlbumRepository;
import com.senselessweb.pictureweb.flickr.AuthenticatedFlickrProvider;

@Service
public class AlbumFetcher extends AbstractFetcher {

  private static final Log log = LogFactory.getLog(AlbumFetcher.class);
  private final AlbumRepository albumRepository;

  public AlbumFetcher(final AuthenticatedFlickrProvider flickrProvider, final AlbumRepository albumRepository) {
    super(flickrProvider);
    this.albumRepository = albumRepository;
  }

  @Override
  protected void doFetch(Flickr flickr, String userId) throws FlickrException {
    log.debug("Fetching albums");
    final Photosets photosets = flickr.getPhotosetsInterface().getList(userId, 500, 1, null);
    photosets.getPhotosets().forEach(s -> storeAlbum(s));
  }

  private void storeAlbum(final Photoset photoset) {
    final StoredAlbum album = new StoredAlbum(photoset.getId(), photoset.getTitle(),
        photoset.getDescription(), photoset.getPrimaryPhoto().getId());
    albumRepository.save(album);
  }

}
