package com.senselessweb.pictureweb.eureka;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class PictureWebEurekaApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(PictureWebEurekaApplication.class).web(true).run(args);
  }
}