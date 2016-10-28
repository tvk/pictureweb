package com.senselessweb.pictureweb.flickr.downloader;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.senselessweb.pictureweb.Context;
import com.senselessweb.pictureweb.events.AuthenticationFinishedEvent;
import com.senselessweb.pictureweb.flickr.authentication.FlickrProvider;

/**
 * TODO: Delete deleted photos
 */
@Service
public class PhotoDownloader implements ApplicationListener<AuthenticationFinishedEvent> {

  private static final Log log = LogFactory.getLog(PhotoDownloader.class);
  private static final Set<String> extra = Sets.newHashSet("geo", "original_format", "last_update", "machine_tags");
  private final Context context;
  private final FlickrProvider flickr;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public PhotoDownloader(final FlickrProvider flickr, final Context context) {
    this.context = Preconditions.checkNotNull(context);
    this.flickr = Preconditions.checkNotNull(flickr);
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

  }

  @Override
  public void onApplicationEvent(AuthenticationFinishedEvent event) {
    fetchUserPhotos();
  }

  @Async
  @Scheduled(fixedRate = 60 * 60 * 1000)
  public void fetchUserPhotos() {
    if (!flickr.isAuthenticated()) {
      return;
    }
    log.debug("Fetching user photos");
    fetchUserPhotos(1);
  }

  public void fetchUserPhotos(final int page) {

    try {

      final PhotoList<Photo> photos = flickr.get().getPeopleInterface().getPhotos(flickr.getUserId(), null, null, null,
          null, null, null, null, extra, 500, page);

      photos.stream().map(p -> new StoredPhoto(context, p)).filter(p -> p.needsRefresh()).forEach(p -> queue(p));

      if (photos.getPage() < photos.getPages()) {
        fetchUserPhotos(page + 1);
      }
    } catch (final FlickrException e) {
      log.error("Error while fetching photos", e);
    }
  }

  private void queue(StoredPhoto p) {
    log.debug("Queuing " + p.getPhoto().getId());
    try {
      new DownloadPhotoCommand(flickr, objectMapper, p).queue();
    } catch (final Exception e) {
      log.error("Could not queue download command", e);
    }
  }
}
