package com.senselessweb.pictureweb.web.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.senselessweb.pictureweb.datastore.client.AlbumService;
import com.senselessweb.pictureweb.web.domain.ClientAlbum;

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

  public Collection<ClientAlbum> getAlbums() {
    return albumService.getAll().stream()
        .filter(a -> !hiddenAlbums.contains(a.getId()))
        .map(albumConverter).collect(Collectors.toList());
  }

  public Collection<ClientAlbum> getAlbumsWithGeo() {
    return albumService.getAll().stream()
        .filter(a -> !hiddenAlbums.contains(a.getId()))
        .map(albumConverter)
        .filter(a -> a.getGeo() != null).collect(Collectors.toList());
  }

}
