package com.senselessweb.pictureweb.web.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.datastore.repository.AlbumRepository;
import com.senselessweb.pictureweb.web.domain.ClientAlbum;

@Service(AlbumService.beanName)
public class AlbumService {

  public static final String beanName = "albums";

  private final AlbumRepository albumRepository;
  private final AlbumConverter albumConverter;

  public AlbumService(final AlbumRepository albumRepository,
      final AlbumConverter albumConverter) {
    this.albumRepository = Preconditions.checkNotNull(albumRepository);
    this.albumConverter = Preconditions.checkNotNull(albumConverter);
  }

  public Collection<ClientAlbum> getAlbums() {
    return albumRepository.findAll().stream().map(albumConverter).collect(Collectors.toList());
  }

  public Collection<ClientAlbum> getAlbumsWithGeo() {
    return albumRepository.findAll().stream().map(albumConverter)
        .filter(a -> a.getGeo() != null).collect(Collectors.toList());
  }

}
