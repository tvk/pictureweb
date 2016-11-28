package com.senselessweb.pictureweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.senselessweb.pictureweb.commons.util.ExtPreconditions;

@Configuration
public class WebConfiguration {

  public static final String googleMapsApiKeyEnvVar = "pictureweb_googleMapsApiKey";
  public static final String googleMapsJsUrlBeanName = "googleMapsJsUrl";

  @Bean(googleMapsJsUrlBeanName)
  public String bean() {
    final String apiKey = ExtPreconditions.checkNotBlank(System.getenv(googleMapsApiKeyEnvVar));
    return "https://maps.googleapis.com/maps/api/js?key=" + apiKey + "&callback=initMap";
  }
}
