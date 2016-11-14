package com.senselessweb.pictureweb.downloader.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.Size;
import com.google.common.util.concurrent.Futures;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.senselessweb.pictureweb.commons.storage.StoredPhotos;
import com.senselessweb.pictureweb.flickr.AuthenticatedFlickrProvider;

@Service
@DefaultProperties(
      commandProperties = 
          @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "600000"),
      threadPoolProperties = {
          @HystrixProperty(name = "coreSize", value = "3"),
          @HystrixProperty(name = "maxQueueSize", value = "100000"),
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

  @HystrixCommand(threadPoolKey = "downloadOriginal", fallbackMethod="fallback")
  public Future<File> downloadOriginal(final Photo photo) {
    return download(photo, Size.ORIGINAL, storageService.getOriginal(photo.getId()), "original");
  }

  @HystrixCommand(threadPoolKey = "downloadSmall", fallbackMethod="fallback")
  public Future<File> downloadSmall(final Photo photo) {
    return download(photo, Size.SMALL, storageService.getSmall(photo.getId()), "small");
  }

  @HystrixCommand(threadPoolKey = "downloadMedium", fallbackMethod="fallback")
  public Future<File> downloadMedium(final Photo photo) {
    return download(photo, Size.MEDIUM, storageService.getMedium(photo.getId()), "medium");
  }

  @HystrixCommand(threadPoolKey = "downloadLarge", fallbackMethod="fallback")
  public Future<File> downloadLarge(final Photo photo) {
    return download(photo, Size.LARGE_1600, storageService.getLarge(photo.getId()), "large");
  }

  private Future<File> download(final Photo photo, final int size, final File target, final String sizeString) {

    return new AsyncResult<File>() {

      @Override
      public File invoke() {
        log.info("Starting download of photo " + photo.getId() + " in size " + sizeString + " to: " + target);
        final Flickr f = flickr.getIfAuthenticated().orElseThrow(() -> new IllegalStateException("Not authenticated"));
        try {
          try (final InputStream is = f.getPhotosInterface().getImageAsStream(photo, size)) {
            try (final FileOutputStream fos = new FileOutputStream(target)) {
              IOUtils.copy(is, fos);
              log.info("Finished download of photo " + photo.getId() + " in size " + sizeString + " to: " + target);
              return target;
            }
          }
        } catch (final Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
  
  @HystrixCommand
  public Future<File> fallback(final Photo photo, final Throwable t) {
    log.error("Error while downloading photo " + photo.getId(), t);
    return Futures.immediateFailedFuture(t);
  }

  
}
