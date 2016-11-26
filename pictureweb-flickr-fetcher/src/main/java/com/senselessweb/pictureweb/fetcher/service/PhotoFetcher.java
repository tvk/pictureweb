package com.senselessweb.pictureweb.fetcher.service;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Exif;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.google.common.collect.Sets;
import com.senselessweb.pictureweb.authentication.AuthenticatedFlickrProvider;
import com.senselessweb.pictureweb.datastore.client.PhotoService;
import com.senselessweb.pictureweb.fetcher.transfer.TransferPhoto;

@Service
public class PhotoFetcher extends AbstractFetcher {

  private static final Log log = LogFactory.getLog(PhotoFetcher.class);
  private static final Set<String> extra = Sets.newHashSet("geo", "original_format", "last_update", "machine_tags");
  private final PhotoService photoService;

  public PhotoFetcher(final AuthenticatedFlickrProvider flickrProvider, final PhotoService photoService) {
    super(flickrProvider);
    this.photoService = photoService;
  }

  @Override
  protected void doFetch(final Flickr flickr, final String userId) throws FlickrException {
    doFetch(flickr, userId, 1);
  }

  private void doFetch(final Flickr flickr, final String userId, final int page) throws FlickrException {
    log.debug("Fetching photos");
    final PhotoList<Photo> photos = flickr.getPeopleInterface().getPhotos(userId, null, null, null, null, null, null, null, extra, 500, page);
    photos.forEach(p -> storePhoto(flickr, p));
    if (photos.getPage() < photos.getPages()) {
      doFetch(flickr, userId, page + 1);
    }
  }

  private void storePhoto(final Flickr flickr, final Photo photo) {
    try {
      final com.senselessweb.pictureweb.datastore.domain.Photo stored = this.photoService.get(photo.getId());
      if (stored == null || stored.getLastUpdate().before(photo.getLastUpdate())) {
        final Collection<Exif> exif = flickr.getPhotosInterface().getExif(photo.getId(), photo.getSecret());
        photoService.save(new TransferPhoto(photo, exif));
      }
    } catch (final FlickrException e) {
      log.error("Could not store photo", e);
    }
  }
}
