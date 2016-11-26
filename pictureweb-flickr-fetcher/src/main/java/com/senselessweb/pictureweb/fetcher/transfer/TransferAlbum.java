package com.senselessweb.pictureweb.fetcher.transfer;

import java.util.List;

import com.flickr4java.flickr.photosets.Photoset;
import com.senselessweb.pictureweb.datastore.domain.Album;

public class TransferAlbum implements Album {
  
  private final Photoset photoset;
  private final List<String> photos;

  public TransferAlbum(Photoset photoset, List<String> photos) {
    this.photoset = photoset;
    this.photos = photos;
  }

  @Override
  public String getId() {
    return photoset.getId();
  }

  @Override
  public String getTitle() {
    return photoset.getTitle();
  }

  @Override
  public String getDescription() {
    return photoset.getDescription();
  }

  @Override
  public String getPrimaryPhotoId() {
    return photoset.getPrimaryPhoto().getId();
  }

  @Override
  public List<String> getPhotos() {
    return photos;
  }

}
