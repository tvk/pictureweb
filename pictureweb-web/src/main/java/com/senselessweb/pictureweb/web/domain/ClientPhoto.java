package com.senselessweb.pictureweb.web.domain;

import com.senselessweb.pictureweb.datastore.domain.GeoData;

public class ClientPhoto {

  private final String id; 
  private final String title;
  private final String description;
  private final GeoData geo;
  private final String large;
  private final String medium;
  private final String small;
  private final boolean isComplete;

  public ClientPhoto(final String id, final String title, final String description, final GeoData geo,
      final String large, final String medium, final String small, final boolean isComplete) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.geo = geo;
    this.large = large;
    this.medium = medium;
    this.small = small;
    this.isComplete = isComplete;
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

  public GeoData getGeo() {
    return geo;
  }

  public String getLarge() {
    return large;
  }

  public String getMedium() {
    return medium;
  }

  public String getSmall() {
    return small;
  }

  public boolean isComplete() {
    return isComplete;
  }

}
