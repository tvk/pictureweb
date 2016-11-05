package com.senselessweb.pictureweb.downloader.service;

import java.io.File;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.google.common.util.concurrent.Futures;
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

  @Scheduled(fixedDelay = 60 * 1000)
  public void check() {

    final Observable<Photo> incompletes = Observable.from(repository.findByIncompleteTrue()).map(p -> toPhoto(p));

    final Observable<Future<File>> checkOriginals = incompletes.map(p -> check(p, storage.getOriginal(p.getId()), ph -> downloader.downloadOriginal(ph)));
    final Observable<Future<File>> checkSmalls = incompletes.map(p -> check(p, storage.getSmall(p.getId()), ph -> downloader.downloadSmall(ph)));
    final Observable<Future<File>> checkMediums = incompletes.map(p -> check(p, storage.getMedium(p.getId()), ph -> downloader.downloadMedium(ph)));
    final Observable<Future<File>> checkLarges = incompletes.map(p -> check(p, storage.getLarge(p.getId()), ph -> downloader.downloadLarge(ph)));

    Observable.zip(incompletes, checkOriginals, checkSmalls, checkMediums, checkLarges, (p, o, s, m, l) -> checkResult(p, o, s, m, l)).subscribe();
  }

  private Void checkResult(final Photo photo, final Future<File> original, final Future<File> small, final Future<File> medium, final Future<File> large) {
    try {
      if (isUp2Date(photo, original.get()) && isUp2Date(photo, small.get()) && isUp2Date(photo, medium.get()) && isUp2Date(photo, large.get())) {
        log.info("Completed " + photo.getId());
      }
    } catch (final Exception e) {
      log.error(e);
    }
    return null;
  }

  private Photo toPhoto(StoredPhoto p) {
    final PhotosInterface photosInterface = authenticatedFlickr.getIfAuthenticated().get().getPhotosInterface();
    try {
      return photosInterface.getPhoto(p.getId());
    } catch (final FlickrException e) {
      throw new IllegalStateException("Could not get original", e);
    }
  }

  private Future<File> check(final Photo photo, final File target, Function<Photo, Future<File>> downloader) {
    return isUp2Date(photo, target) ? Futures.immediateFuture(target) : downloader.apply(photo);
  }

  private boolean isUp2Date(final Photo photo, final File target) {
    return target.isFile();
  }

}
