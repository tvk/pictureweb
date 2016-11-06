package com.senselessweb.pictureweb.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("map")
public class MapController {

  @GetMapping
  public String get() {
    return "map";
  }

}
