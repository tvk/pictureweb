package com.senselessweb.pictureweb.web.service;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.senselessweb.pictureweb.commons.storage.StoredPhotos;
import com.senselessweb.pictureweb.datastore.client.PhotoService;
import com.senselessweb.pictureweb.datastore.domain.Album;
import com.senselessweb.pictureweb.datastore.domain.Photo;
import com.senselessweb.pictureweb.web.domain.ClientAlbum;
import com.senselessweb.pictureweb.web.domain.ClientGeoData;
import com.senselessweb.pictureweb.web.domain.ClientPhoto;

@Service
public class AlbumConverter implements Function<Album, ClientAlbum> {

  private final PhotoConverter photoConverter;
  private final PhotoService photoService;
  private final StoredPhotos storedPhotos;

  public AlbumConverter(final PhotoConverter photoConverter, final PhotoService photoService,
      final StoredPhotos storedPhotos) {
    this.photoConverter = photoConverter;
    this.photoService = photoService;
    this.storedPhotos = storedPhotos;
  }

  @Override
  public ClientAlbum apply(final Album album) {
    return new ClientAlbum(album.getId(), album.getTitle(), album.getDescription(), 
        convertPicture(album.getPrimaryPhotoId()), 
        findGeoData(album), convertPictures(album));
  }

  private Supplier<Collection<ClientPhoto>> convertPictures(Album album) {
    return new Supplier<Collection<ClientPhoto>>() {
      @Override
      public Collection<ClientPhoto> get() {
        return album.getPhotos().stream()
            .filter(p -> storedPhotos.isComplete(p))
            .map(p -> convertPicture(p))
            .collect(Collectors.toList());
      }
    };
    
  }

  private ClientGeoData findGeoData(final Album album) {
    if (album.getPrimaryPhotoId() != null) {
      final Photo primaryPhoto = photoService.get(album.getPrimaryPhotoId());
      if (primaryPhoto != null && primaryPhoto.getGeo() != null) {
        return new ClientGeoData(primaryPhoto.getGeo());
      }
    }

    return null;
  }

  private ClientPhoto convertPicture(final String photoId) {
    return StringUtils.isBlank(photoId) ? null : photoConverter.apply(photoService.get(photoId));
  }
}
