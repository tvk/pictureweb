package com.senselessweb.pictureweb.datastore.domain;

import java.util.Date;
import java.util.List;

public interface Photo {

  public String getId();

  public String getTitle();

  public String getDescription();

  public GeoData getGeo();

  public Date getLastUpdate();

  public List<Tag> getTags();

  public List<Exif> getExif();
  
}
