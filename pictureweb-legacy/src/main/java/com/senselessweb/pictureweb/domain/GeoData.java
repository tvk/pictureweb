package com.senselessweb.pictureweb.domain;

import com.senselessweb.java.utils.ExtPreconditions;

public class GeoData {

  private final String latitude;
  private final String longitude;

  public GeoData(final String latitude, final String longitude) {
    this.latitude = ExtPreconditions.checkNotBlank(latitude);
    this.longitude = ExtPreconditions.checkNotBlank(longitude);
  }

  public String getLatitude() {
    return latitude;
  }

  public String getLongitude() {
    return longitude;
  }
}
