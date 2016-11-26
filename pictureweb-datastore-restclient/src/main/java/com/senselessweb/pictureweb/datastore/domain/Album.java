package com.senselessweb.pictureweb.datastore.domain;

import java.util.List;

public interface Album {

  public String getId();

  public String getTitle();

  public String getDescription();

  public String getPrimaryPhotoId();

  public List<String> getPhotos();

}
