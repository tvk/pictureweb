package com.senselessweb.pictureweb.datastore.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.senselessweb.pictureweb.commons.util.ExtPreconditions;

public class StoredGeoData implements GeoData {

  private final String latitude;
  private final String longitude;

  @JsonCreator
  public StoredGeoData(
      final @JsonProperty("latitude") String latitude, 
      final @JsonProperty("longitude") String longitude) {
    this.latitude = ExtPreconditions.checkNotBlank(latitude);
    this.longitude = ExtPreconditions.checkNotBlank(longitude);
  }

  @Override
  public String getLatitude() {
    return latitude;
  }

  @Override
  public String getLongitude() {
    return longitude;
  }
}
