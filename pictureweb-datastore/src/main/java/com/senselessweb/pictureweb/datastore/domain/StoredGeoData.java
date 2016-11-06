package com.senselessweb.pictureweb.datastore.domain;

import com.senselessweb.pictureweb.commons.util.ExtPreconditions;

public class StoredGeoData {

  private final String latitude;
  private final String longitude;

  public StoredGeoData(final String latitude, final String longitude) {
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
