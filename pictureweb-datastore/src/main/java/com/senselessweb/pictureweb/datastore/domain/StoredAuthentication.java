package com.senselessweb.pictureweb.datastore.domain;

public class StoredAuthentication {

  private final String value;
  private final String secret;

  public StoredAuthentication(String value, String secret) {
    this.value = value;
    this.secret = secret;
  }

  public String getValue() {
    return value;
  }

  public String getSecret() {
    return secret;
  }
}
