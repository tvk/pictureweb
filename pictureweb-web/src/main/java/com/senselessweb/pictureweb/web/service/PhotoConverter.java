package com.senselessweb.pictureweb.web.service;

import java.util.function.Function;

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
    return picture == null ? null : new ClientPhoto(picture.getTitle(), picture.getDescription(), picture.getGeo(),
        storedPhotos.getLarge(picture.getId()), storedPhotos.getMedium(picture.getId()),
        storedPhotos.getSmall(picture.getId()));
  }

}
