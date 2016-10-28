package com.senselessweb.pictureweb.flickr.downloader;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photosets.Photosets;
import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.Context;
import com.senselessweb.pictureweb.events.AlbumsChangedEvent;
import com.senselessweb.pictureweb.events.AuthenticationFinishedEvent;
import com.senselessweb.pictureweb.flickr.authentication.FlickrProvider;

/**
 * TODO: Delete deleted photos
 */
@Service
public class AlbumDownloader implements ApplicationListener<AuthenticationFinishedEvent> {

  private static final Log log = LogFactory.getLog(PhotoDownloader.class);

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Context context;
  private final FlickrProvider flickr;
  private final ApplicationEventPublisher publisher;

  public AlbumDownloader(final FlickrProvider flickr, final Context context,
      final ApplicationEventPublisher publisher) {
    this.context = Preconditions.checkNotNull(context);
    this.flickr = Preconditions.checkNotNull(flickr);
    this.publisher = Preconditions.checkNotNull(publisher);
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

  }

  @Override
  public void onApplicationEvent(AuthenticationFinishedEvent event) {
    downloadAlbums();
  }

  @Async
  @Scheduled(fixedRate = 60 * 60 * 1000)
  public void downloadAlbums() {

    if (!flickr.isAuthenticated()) {
      return;
    }

    try {
      downloadAlbums(1);
      publisher.publishEvent(new AlbumsChangedEvent());
    } catch (FlickrException e) {
      log.error("Could not download albums", e);
    }
  }

  private void downloadAlbums(final int page) throws FlickrException {
    log.debug("Downloading albums (Page " + page + ")");
    final Photosets photosets = flickr.get().getPhotosetsInterface().getList(flickr.getUserId(), 5, page, null);
    photosets.getPhotosets().stream().map(photoset -> new AlbumMetadataWrapper(photoset)).forEach(w -> write(w));
    if (photosets.getPage() < photosets.getPages()) {
      downloadAlbums(page + 1);
    }
  }

  private void write(final AlbumMetadataWrapper album) {
    log.debug("Writing album " + album);
    final File file = new File(context.getAlbumsPath(), album.getId() + ".properties");
    file.getParentFile().mkdirs();
    try {
      objectMapper.writeValue(file, album);
    } catch (final Exception e) {
      log.error("Could not write album " + album, e);
    }
  }

}
