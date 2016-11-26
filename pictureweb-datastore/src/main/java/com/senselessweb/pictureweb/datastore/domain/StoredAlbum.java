package com.senselessweb.pictureweb.datastore.domain;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class StoredAlbum implements Album {

  @Id
  private final String id;

  private final String title;
  private final String description;
  private final String primaryPhotoId;
  private final List<String> photos;

  @JsonCreator
  public StoredAlbum(
      @JsonProperty("id") String id, 
      @JsonProperty("title") String title, 
      @JsonProperty("description") String description, 
      @JsonProperty("primaryPhotoId") String primaryPhotoId, 
      @JsonProperty("photos") final List<String> photos) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.primaryPhotoId = primaryPhotoId;
    this.photos = photos;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getPrimaryPhotoId() {
    return primaryPhotoId;
  }

  @Override
  public List<String> getPhotos() {
    return Lists.newArrayList(photos);
  }

}
