package com.senselessweb.pictureweb.datastore.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoredExif implements Exif {

  private final String tag;
  private final String tagspace;
  private final String raw;
  private final String clean;

  @JsonCreator
  public StoredExif(
      final @JsonProperty("id") String tag, 
      final @JsonProperty("tagspace") String tagspace, 
      final @JsonProperty("raw") String raw, 
      final @JsonProperty("clean") String clean) {
    this.tag = tag;
    this.tagspace = tagspace;
    this.raw = raw;
    this.clean = clean;
  }

  @Override
  public String getTag() {
    return tag;
  }

  @Override
  public String getTagspace() {
    return tagspace;
  }

  @Override
  public String getRaw() {
    return raw;
  }

  @Override
  public String getClean() {
    return clean;
  }

}
