package com.senselessweb.pictureweb.fetcher.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.google.common.collect.Sets;
import com.senselessweb.pictureweb.datastore.domain.StoredGeoData;
import com.senselessweb.pictureweb.datastore.domain.StoredPhoto;
import com.senselessweb.pictureweb.datastore.domain.StoredTag;
import com.senselessweb.pictureweb.datastore.repository.PhotoRepository;
import com.senselessweb.pictureweb.flickr.AuthenticatedFlickrProvider;

@Service
public class PhotoFetcher extends AbstractFetcher {

  private static final Log log = LogFactory.getLog(PhotoFetcher.class);
  private static final Set<String> extra = Sets.newHashSet("geo", "original_format", "last_update", "machine_tags");
  private final PhotoRepository photoRepository;

  public PhotoFetcher(final AuthenticatedFlickrProvider flickrProvider, final PhotoRepository photoRepository) {
    super(flickrProvider);
    this.photoRepository = photoRepository;
  }

  @Override
  protected void doFetch(final Flickr flickr, final String userId) throws FlickrException {
    doFetch(flickr, userId, 1);
  }

  private void doFetch(final Flickr flickr, final String userId, final int page) throws FlickrException {
    log.debug("Fetching albums");
    final PhotoList<Photo> photos = flickr.getPeopleInterface().getPhotos(userId, null, null, null,
        null, null, null, null, extra, 500, page);
    photos.forEach(p -> storePhoto(p));
    if (photos.getPage() < photos.getPages()) {
      doFetch(flickr, userId, page + 1);
    }
  }

  private void storePhoto(final Photo photo) {

    final StoredPhoto stored = this.photoRepository.findOne(photo.getId());
    if (stored == null || stored.getLastUpdate().before(photo.getLastUpdate())) {
      photoRepository.save(toStoredPhoto(photo));
    }
  }

  private StoredPhoto toStoredPhoto(final Photo photo) {

    final StoredGeoData geodata = photo.getGeoData() != null ? new StoredGeoData(
        String.valueOf(photo.getGeoData().getLatitude()),
        String.valueOf(photo.getGeoData().getLongitude())) : null;

    final List<StoredTag> tags = photo.getTags().stream()
        .map(t -> new StoredTag(t.getId(), t.getValue(), t.getRaw())).collect(Collectors.toList());

    return new StoredPhoto(photo.getId(), photo.getTitle(), photo.getDescription(), geodata, photo.getLastUpdate(), tags);
  }

}
