package com.senselessweb.pictureweb.datastore.domain;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.google.common.collect.Lists;

public class StoredAlbum {

  @Id
  private final String id;

  private final String title;
  private final String description;
  private final String primaryPhotoId;
  private final List<String> photos;

  public StoredAlbum(String id, String title, String description, String primaryPhotoId, final List<String> photos) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.primaryPhotoId = primaryPhotoId;
    this.photos = photos;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getPrimaryPhotoId() {
    return primaryPhotoId;
  }

  public List<String> getPhotos() {
    return Lists.newArrayList(photos);
  }

}
