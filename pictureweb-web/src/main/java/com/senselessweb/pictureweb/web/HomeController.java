package com.senselessweb.pictureweb.web;

import java.util.Collections;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.senselessweb.pictureweb.web.service.ClientAlbumService;

@Controller
@RequestMapping("/")
public class HomeController {
  
  private final ClientAlbumService albumService;
  
  public HomeController(final ClientAlbumService albumService) {
    this.albumService = albumService;
  }

  @RequestMapping
  public ModelAndView home() {
    return new ModelAndView("home", Collections.singletonMap("photo", albumService.getRandomPhoto()));
  }
}
