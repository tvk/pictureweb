package com.senselessweb.pictureweb.datastore.domain;

import org.springframework.data.annotation.Id;

public class StoredAlbum {

  @Id
  private final String id;

  private final String title;
  private final String description;
  private final String primaryPhotoId;

  public StoredAlbum(String id, String title, String description, String primaryPhotoId) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.primaryPhotoId = primaryPhotoId;
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

}
