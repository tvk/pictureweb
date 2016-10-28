package com.senselessweb.pictureweb.flickr.downloader;

import com.flickr4java.flickr.photosets.Photoset;
import com.google.common.base.Preconditions;

public class AlbumMetadataWrapper {

  private final Photoset photoset;

  public AlbumMetadataWrapper(final Photoset photoset) {
    this.photoset = Preconditions.checkNotNull(photoset);
  }

  public String getId() {
    return photoset.getId();
  }

  public String getTitle() {
    return photoset.getTitle();
  }

  public String getDescription() {
    return photoset.getDescription();
  }

  public String getPrimaryPhoto() {
    return photoset.getPrimaryPhoto().getId();
  }

  @Override
  public String toString() {
    return "Album " + getId();
  }
}
