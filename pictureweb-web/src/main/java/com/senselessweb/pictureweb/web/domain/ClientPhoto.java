package com.senselessweb.pictureweb.web.domain;

import java.io.File;

import com.senselessweb.pictureweb.datastore.domain.StoredGeoData;

public class ClientPhoto {

  private final String title;
  private final String description;
  private final StoredGeoData geo;
  private final File large;
  private final File medium;
  private final File small;

  public ClientPhoto(String title, String description, StoredGeoData geo, File large, File medium, File small) {
    this.title = title;
    this.description = description;
    this.geo = geo;
    this.large = large;
    this.medium = medium;
    this.small = small;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public StoredGeoData getGeo() {
    return geo;
  }

  public String getLarge() {
    return large.getName();
  }

  public String getMedium() {
    return medium.getName();
  }

  public String getSmall() {
    return small.getName();
  }

}
