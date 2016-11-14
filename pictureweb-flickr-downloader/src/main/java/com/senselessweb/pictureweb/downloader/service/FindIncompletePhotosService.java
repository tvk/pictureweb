package com.senselessweb.pictureweb.downloader.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.senselessweb.pictureweb.commons.storage.StoredPhotos;
import com.senselessweb.pictureweb.datastore.domain.StoredPhoto;
import com.senselessweb.pictureweb.datastore.repository.PhotoRepository;
import com.senselessweb.pictureweb.flickr.AuthenticatedFlickrProvider;

import rx.Observable;

@Service
public class FindIncompletePhotosService {

  private static final Log log = LogFactory.getLog(FindIncompletePhotosService.class);
  private final PhotoRepository repository;
  private final StoredPhotos storage;
  private final AsyncDownloader downloader;
  private final AuthenticatedFlickrProvider authenticatedFlickr;

  public FindIncompletePhotosService(PhotoRepository repository, StoredPhotos storage, AsyncDownloader downloadService,
      AuthenticatedFlickrProvider authenticatedFlickr) {
    this.repository = repository;
    this.storage = storage;
    this.downloader = downloadService;
    this.authenticatedFlickr = authenticatedFlickr;
  }

  @Scheduled(fixedDelay = 60 * 1000, initialDelay = 0)
  public void check() {

    Observable.from(repository.findAll())
        .filter(p -> !storage.isComplete(p.getId()))
        .map(p -> toPhoto(p))
        .doOnNext(p -> log.info("Found incomplete photo: " + p.getId()))
        .doOnNext(p -> downloader.downloadOriginal(p))
        .doOnNext(p -> downloader.downloadSmall(p))
        .doOnNext(p -> downloader.downloadMedium(p))
        .doOnNext(p -> downloader.downloadLarge(p))
        .count()
        .subscribe(c -> log.info("Triggered download of " + c + " photos"));
  }

  private Photo toPhoto(StoredPhoto p) {
    final PhotosInterface photosInterface = authenticatedFlickr.getIfAuthenticated().get().getPhotosInterface();
    try {
      return photosInterface.getPhoto(p.getId());
    } catch (final FlickrException e) {
      throw new IllegalStateException("Could not get original", e);
    }
  }
}
