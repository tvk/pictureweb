package com.senselessweb.pictureweb.web.service;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.senselessweb.pictureweb.commons.storage.StoredPhotos;
import com.senselessweb.pictureweb.datastore.domain.StoredAlbum;
import com.senselessweb.pictureweb.datastore.domain.StoredPhoto;
import com.senselessweb.pictureweb.datastore.repository.PhotoRepository;
import com.senselessweb.pictureweb.web.domain.ClientAlbum;
import com.senselessweb.pictureweb.web.domain.ClientGeoData;
import com.senselessweb.pictureweb.web.domain.ClientPhoto;

@Service
public class AlbumConverter implements Function<StoredAlbum, ClientAlbum> {

  private final PhotoConverter photoConverter;
  private final PhotoRepository photoRepository;
  private final StoredPhotos storedPhotos;

  public AlbumConverter(final PhotoConverter photoConverter, final PhotoRepository photoRepository,
      final StoredPhotos storedPhotos) {
    this.photoConverter = photoConverter;
    this.photoRepository = photoRepository;
    this.storedPhotos = storedPhotos;
  }

  @Override
  public ClientAlbum apply(final StoredAlbum album) {
    return new ClientAlbum(album.getId(), album.getTitle(), album.getDescription(), 
        convertPicture(album.getPrimaryPhotoId()), storedPhotos.isComplete(album.getPrimaryPhotoId()),
        findGeoData(album), convertPictures(album));
  }

  private Collection<ClientPhoto> convertPictures(StoredAlbum album) {
    return album.getPhotos().stream()
        .filter(p -> storedPhotos.isComplete(p))
        .map(p -> convertPicture(p))
        .collect(Collectors.toList());
  }

  private ClientGeoData findGeoData(final StoredAlbum album) {
    if (album.getPrimaryPhotoId() != null) {
      final StoredPhoto primaryPhoto = photoRepository.findOne(album.getPrimaryPhotoId());
      if (primaryPhoto != null && primaryPhoto.getGeo() != null) {
        return new ClientGeoData(primaryPhoto.getGeo());
      }
    }

    return null;
  }

  private ClientPhoto convertPicture(final String photoId) {
    return StringUtils.isBlank(photoId) ? null : photoConverter.apply(photoRepository.findOne(photoId));
  }
}
