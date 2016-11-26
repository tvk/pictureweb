package com.senselessweb.pictureweb.datastore.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoredPhoto implements Photo {

  @Id
  private final String id;
  private final String title;
  private final String description;
  private final StoredGeoData geoData;
  private final Date lastUpdate;
  private final List<? extends Tag> tags;
  private final List<? extends Exif> exif;

  @JsonCreator
  public StoredPhoto(
      final @JsonProperty("id") String id, 
      final @JsonProperty("title") String title, 
      final @JsonProperty("description") String description, 
      final @JsonProperty("geoData") StoredGeoData geoData, 
      final @JsonProperty("lastUpdate") Date lastUpdate, 
      final @JsonProperty("tags") List<StoredTag> tags,
      final @JsonProperty("exif") List<StoredExif> exif) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.geoData = geoData;
    this.lastUpdate = lastUpdate;
    this.tags = tags;
    this.exif = exif;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public StoredGeoData getGeo() {
    return geoData;
  }

  @Override
  public Date getLastUpdate() {
    return lastUpdate;
  }

  @Override
  public List<Tag> getTags() {
    return (List<Tag>) tags;
  }

  @Override
  public List<Exif> getExif() {
    return (List<Exif>) exif;
  }
}
