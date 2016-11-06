package com.senselessweb.pictureweb.web.domain;

import com.senselessweb.pictureweb.datastore.domain.StoredGeoData;

public class ClientGeoData {

  private final StoredGeoData geo;

  public ClientGeoData(final StoredGeoData geo) {
    this.geo = geo;
  }

  public double getLat() {
    return Double.parseDouble(geo.getLatitude());
  }

  public double getLng() {
    return Double.parseDouble(geo.getLongitude());
  }

}
