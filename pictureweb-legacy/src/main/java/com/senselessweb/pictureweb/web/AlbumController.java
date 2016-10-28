package com.senselessweb.pictureweb.web;

import java.util.Collection;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.senselessweb.pictureweb.domain.Album;
import com.senselessweb.pictureweb.service.AlbumService;

@RestController
@RequestMapping("albums")
public class AlbumController {

  private final AlbumService albumService;

  public AlbumController(final AlbumService albumService) {
    this.albumService = albumService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public Collection<Album> getAllAlbums() {
    return albumService.getAllAlbums();
  }
}
