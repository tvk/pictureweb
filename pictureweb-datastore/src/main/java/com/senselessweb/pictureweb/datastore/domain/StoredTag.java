package com.senselessweb.pictureweb.datastore.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoredTag implements Tag {

  private final String id;
  private final String value;
  private final String raw;

  @JsonCreator
  public StoredTag(
      final @JsonProperty("id") String id, 
      final @JsonProperty("value") String value, 
      final @JsonProperty("raw") String raw) {
    this.id = id;
    this.value = value;
    this.raw = raw;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getRaw() {
    return raw;
  }

}
