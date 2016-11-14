package com.senselessweb.pictureweb.datastore.domain;

public class StoredExif {

  private final String tag;
  private final String tagspace;
  private final String raw;
  private final String clean;

  public StoredExif(String tag, String tagspace, String raw, String clean) {
    this.tag = tag;
    this.tagspace = tagspace;
    this.raw = raw;
    this.clean = clean;
  }

  public String getTag() {
    return tag;
  }

  public String getTagspace() {
    return tagspace;
  }

  public String getRaw() {
    return raw;
  }

  public String getClean() {
    return clean;
  }

}
