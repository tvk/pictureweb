package com.senselessweb.pictureweb.downloader.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.Size;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.senselessweb.pictureweb.commons.storage.StoredPhotos;
import com.senselessweb.pictureweb.flickr.AuthenticatedFlickrProvider;

@Service
@DefaultProperties(
      commandProperties = @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "600000"),
      threadPoolProperties = {
          @HystrixProperty(name = "maxQueueSize", value = "100000"),
          @HystrixProperty(name = "coreSize", value = "3"),
          @HystrixProperty(name = "queueSizeRejectionThreshold", value = "100000")
      })
public class AsyncDownloader {

  private static final Log log = LogFactory.getLog(AsyncDownloader.class);
  private final AuthenticatedFlickrProvider flickr;
  private final StoredPhotos storageService;

  public AsyncDownloader(final AuthenticatedFlickrProvider flickr, final StoredPhotos storageService) {
    this.flickr = flickr;
    this.storageService = storageService;
  }

  @Async
  @HystrixCommand(threadPoolKey = "downloadOriginal")
  public Future<File> downloadOriginal(final Photo photo) {
    return download(photo, Size.ORIGINAL, storageService.getOriginal(photo.getId()));
  }

  @Async
  @HystrixCommand(threadPoolKey = "downloadSmall")
  public Future<File> downloadSmall(final Photo photo) {
    return download(photo, Size.SMALL, storageService.getSmall(photo.getId()));
  }

  @Async
  @HystrixCommand(threadPoolKey = "downloadMedium")
  public Future<File> downloadMedium(final Photo photo) {
    return download(photo, Size.MEDIUM, storageService.getMedium(photo.getId()));
  }

  @Async
  @HystrixCommand(threadPoolKey = "downloadLarge")
  public Future<File> downloadLarge(final Photo photo) {
    return download(photo, Size.LARGE_1600, storageService.getLarge(photo.getId()));
  }

  private Future<File> download(final Photo photo, final int size, final File target) {

    return new AsyncResult<File>() {

      @Override
      public File invoke() {
        log.info("Starting download of photo " + photo.getId() + " in size " + size);
        final Flickr f = flickr.getIfAuthenticated().orElseThrow(() -> new IllegalStateException("Not authenticated"));
        try {
          try (final InputStream is = f.getPhotosInterface().getImageAsStream(photo, size)) {
            try (final FileOutputStream fos = new FileOutputStream(target)) {
              IOUtils.copy(is, fos);
              return target;
            }
          }
        } catch (final Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
}
