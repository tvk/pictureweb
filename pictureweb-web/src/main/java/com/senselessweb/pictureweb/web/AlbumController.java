package com.senselessweb.pictureweb.web;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.senselessweb.pictureweb.datastore.client.AlbumService;
import com.senselessweb.pictureweb.web.service.AlbumConverter;

@Controller
@RequestMapping("albums")
public class AlbumController {

  private final AlbumService albumRepository;
  private final AlbumConverter albumConverter;

  public AlbumController(AlbumService albumRepository, AlbumConverter albumConverter) {
    this.albumRepository = albumRepository;
    this.albumConverter = albumConverter;
  }

  @RequestMapping("/{id}")
  public ModelAndView album(final @PathVariable("id") String id) {
    return new ModelAndView("album", "album", albumConverter.apply(albumRepository.get(id)));
  }

  @RequestMapping
  public ModelAndView albums() {
    return new ModelAndView("albums", "albums", albumRepository.getAll().stream().map(albumConverter).collect(Collectors.toList()));
  }

}
