package com.senselessweb.pictureweb.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public class PictureService {

  @ModelAttribute("greetins")
  public String getGreeting() {
    return "ps";
  }
}
