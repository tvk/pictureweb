package com.senselessweb.pictureweb.datastore.domain;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoredAuthentication implements Authentication {

  @Id
  private final String id;
  private final String value;
  private final String secret;

  @JsonCreator
  public StoredAuthentication(
      final @JsonProperty("id") String id, 
      final @JsonProperty("value") String value, 
      final @JsonProperty("secret") String secret) {
    this.id = id;
    this.value = value;
    this.secret = secret;
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
  public String getSecret() {
    return secret;
  }
}
