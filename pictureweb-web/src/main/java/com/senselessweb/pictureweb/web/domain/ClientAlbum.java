package com.senselessweb.pictureweb.web.domain;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientAlbum {

  private final String id;
  private final String title;
  private final String description;
  private final ClientPhoto primaryPhoto;
  private final ClientGeoData geo;
  private final Supplier<Collection<ClientPhoto>> photos;

  public ClientAlbum(final String id, final String title, final String description, 
      final ClientPhoto primaryPhoto, final ClientGeoData geo, 
      final Supplier<Collection<ClientPhoto>> photos) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.primaryPhoto = primaryPhoto;
    this.geo = geo;
    this.photos = photos;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public ClientPhoto getPrimaryPhoto() {
    return primaryPhoto;
  }

  public ClientGeoData getGeo() {
    return geo;
  }
  
  public boolean isPrimaryPhotoIsComplete() {
    return getPrimaryPhoto() != null && getPrimaryPhoto().isComplete();
  }

  public Collection<ClientPhoto> getPhotos() {
    return photos.get();
  }

  public String getLink() {
    return "/albums/" + id;
  }

}
