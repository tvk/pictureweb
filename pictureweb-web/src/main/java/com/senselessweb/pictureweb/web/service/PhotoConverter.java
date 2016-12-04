package com.senselessweb.pictureweb.web.service;

import java.io.File;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.commons.storage.StoredPhotos;
import com.senselessweb.pictureweb.datastore.domain.Photo;
import com.senselessweb.pictureweb.web.domain.ClientPhoto;

@Service
public class PhotoConverter implements Function<Photo, ClientPhoto> {

  private final StoredPhotos storedPhotos;

  public PhotoConverter(final StoredPhotos storedPhotos) {
    this.storedPhotos = Preconditions.checkNotNull(storedPhotos);
  }

  @Override
  public ClientPhoto apply(final Photo photo) {
    return photo == null ? null : new ClientPhoto(photo.getId(),
        photo.getTitle(), photo.getDescription(), photo.getGeo(), 
            generateImageUri(storedPhotos.getLarge(photo.getId())),
            generateImageUri(storedPhotos.getMedium(photo.getId())), 
            generateImageUri(storedPhotos.getSmall(photo.getId())),
            storedPhotos.isComplete(photo.getId()));
  }

  private String generateImageUri(final File file) {
    return "/photos" + StringUtils.prependIfMissing(storedPhotos.getGeneratedLocation().relativize(file.toURI()).toASCIIString(), "/");
  }

}
