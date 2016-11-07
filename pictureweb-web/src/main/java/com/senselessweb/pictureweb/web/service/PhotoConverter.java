package com.senselessweb.pictureweb.web.service;

import java.io.File;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.commons.storage.StoredPhotos;
import com.senselessweb.pictureweb.datastore.domain.StoredPhoto;
import com.senselessweb.pictureweb.web.domain.ClientPhoto;

@Service
public class PhotoConverter implements Function<StoredPhoto, ClientPhoto> {

  private final StoredPhotos storedPhotos;

  public PhotoConverter(final StoredPhotos storedPhotos) {
    this.storedPhotos = Preconditions.checkNotNull(storedPhotos);
  }

  @Override
  public ClientPhoto apply(final StoredPhoto picture) {
    return picture == null ? null
        : new ClientPhoto(picture.getTitle(), picture.getDescription(), picture.getGeo(), generateImageUri(storedPhotos.getLarge(picture.getId())),
            generateImageUri(storedPhotos.getMedium(picture.getId())), generateImageUri(storedPhotos.getSmall(picture.getId())));
  }

  private String generateImageUri(final File file) {
    return "/photos" + StringUtils.prependIfMissing(storedPhotos.getGeneratedLocation().relativize(file.toURI()).toASCIIString(), "/");
  }

}
