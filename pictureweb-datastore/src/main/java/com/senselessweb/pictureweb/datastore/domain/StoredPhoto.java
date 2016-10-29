package com.senselessweb.pictureweb.datastore.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class StoredPhoto {

  @Id
  private final String id;
  private final String title;
  private final String description;
  private final StoredGeoData geoData;
  private final Date lastUpdate;
  private final List<StoredTag> tags;
  private boolean complete = false;

  public StoredPhoto(String id, String title, String description, StoredGeoData geoData, Date lastUpdate, final List<StoredTag> tags) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.geoData = geoData;
    this.lastUpdate = lastUpdate;
    this.tags = tags;
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

  public StoredGeoData getGeo() {
    return geoData;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public List<StoredTag> getTags() {
    return tags;
  }

  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }

  /*
   * public Collection<Exif> getExif() { return exif; }
   */

}
