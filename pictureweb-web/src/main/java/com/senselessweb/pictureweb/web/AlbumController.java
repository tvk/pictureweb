package com.senselessweb.pictureweb.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.senselessweb.pictureweb.web.service.ClientAlbumService;

@Controller
@RequestMapping("albums")
public class AlbumController {

  private final ClientAlbumService albumService;

  public AlbumController(final ClientAlbumService albumService) {
    this.albumService = albumService;
  }

  @RequestMapping("/{id}")
  public ModelAndView album(final @PathVariable("id") String id) {
    return new ModelAndView("album", "album", albumService.get(id));
  }

  @RequestMapping
  public ModelAndView albums() {
    return new ModelAndView("albums", "albums", albumService.getAlbums());
  }

}
