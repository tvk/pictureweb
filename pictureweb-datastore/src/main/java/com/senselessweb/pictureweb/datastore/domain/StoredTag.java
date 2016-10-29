package com.senselessweb.pictureweb.datastore.domain;

public class StoredTag {

  private final String id;
  private final String value;
  private final String raw;

  public StoredTag(String id, String value, String raw) {
    this.id = id;
    this.value = value;
    this.raw = raw;
  }

  public String getId() {
    return id;
  }

  public String getValue() {
    return value;
  }

  public String getRaw() {
    return raw;
  }

}
