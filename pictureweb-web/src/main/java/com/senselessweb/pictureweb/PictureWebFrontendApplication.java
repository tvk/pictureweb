package com.senselessweb.pictureweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class PictureWebFrontendApplication {

  public static void main(String[] args) {
    SpringApplication.run(PictureWebFrontendApplication.class, args);
  }
}