package com.senselessweb.pictureweb.fetcher.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
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
    photosets.getPhotosets().forEach(s -> storeAlbum(s, fetchPhotos(flickr, s.getId(), 1)));
  }

  private List<String> fetchPhotos(final Flickr flickr, final String photosetId, final int page) {
    try {
      final PhotoList<Photo> photolist = flickr.getPhotosetsInterface().getPhotos(photosetId, 100, page);
      final List<String> ids = photolist.stream().map(p -> p.getId()).collect(Collectors.toList());
      if (photolist.getPage() < photolist.getPages()) {
        ids.addAll(fetchPhotos(flickr, photosetId, page + 1));
      }
      return ids;
    } catch (final FlickrException e) {
      log.error("Could not fetch photos of album " + photosetId, e);
      return Collections.emptyList();
    }
  }

  private void storeAlbum(final Photoset photoset, final List<String> photos) {
    final StoredAlbum album = new StoredAlbum(photoset.getId(), photoset.getTitle(),
        photoset.getDescription(), photoset.getPrimaryPhoto().getId(), photos);
    albumRepository.save(album);
  }

}
