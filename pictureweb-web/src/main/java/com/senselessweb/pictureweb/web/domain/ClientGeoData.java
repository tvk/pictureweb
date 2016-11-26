package com.senselessweb.pictureweb.web.domain;

import com.senselessweb.pictureweb.datastore.domain.GeoData;

public class ClientGeoData {

  private final GeoData geo;

  public ClientGeoData(final GeoData geo) {
    this.geo = geo;
  }

  public double getLat() {
    return Double.parseDouble(geo.getLatitude());
  }

  public double getLng() {
    return Double.parseDouble(geo.getLongitude());
  }

}
