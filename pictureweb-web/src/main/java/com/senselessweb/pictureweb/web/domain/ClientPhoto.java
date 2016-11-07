package com.senselessweb.pictureweb.web.domain;

import com.senselessweb.pictureweb.datastore.domain.StoredGeoData;

public class ClientPhoto {

  private final String title;
  private final String description;
  private final StoredGeoData geo;
  private final String large;
  private final String medium;
  private final String small;

  public ClientPhoto(final String title, final String description, final StoredGeoData geo,
      final String large, final String medium, final String small) {
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
    return large;
  }

  public String getMedium() {
    return medium;
  }

  public String getSmall() {
    return small;
  }

}
