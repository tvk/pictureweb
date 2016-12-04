package com.senselessweb.pictureweb.web.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.senselessweb.pictureweb.datastore.client.AlbumService;
import com.senselessweb.pictureweb.web.domain.ClientAlbum;
import com.senselessweb.pictureweb.web.domain.ClientPhoto;

@Service(ClientAlbumService.beanName)
public class ClientAlbumService {

  public static final String beanName = "albums";

  private static final Set<String> hiddenAlbums = Sets.newHashSet(
      "72157647261406841", "72157645129642036", "72157644769041219", "72157652634775562");


  private final AlbumService albumService;
  private final AlbumConverter albumConverter;

  public ClientAlbumService(final AlbumService albumService,
      final AlbumConverter albumConverter) {
    this.albumService = Preconditions.checkNotNull(albumService);
    this.albumConverter = Preconditions.checkNotNull(albumConverter);
  }

  public List<ClientAlbum> getAlbums() {
    return albumService.getAll().stream()
        .filter(a -> !hiddenAlbums.contains(a.getId()))
        .map(albumConverter).collect(Collectors.toList());
  }
  
  public ClientPhoto getRandomPhoto() {
    final List<String> photos = Lists.newArrayList();
    albumService.getAll().stream()
        .filter(a -> !hiddenAlbums.contains(a.getId()))
        .forEach(a -> photos.addAll(a.getPhotos()));
    photos.removeAll(AlbumConverter.hiddenPictures);
    Collections.shuffle(photos);
    return albumConverter.convertPicture(photos.iterator().next());
  }

  public List<ClientAlbum> getAlbumsWithGeo() {
    return albumService.getAll().stream()
        .filter(a -> !hiddenAlbums.contains(a.getId()))
        .map(albumConverter)
        .filter(a -> a.getGeo() != null).collect(Collectors.toList());
  }

  public ClientAlbum get(String id) {
    return albumConverter.apply(albumService.get(id));
  }

}
