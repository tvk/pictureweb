package com.senselessweb.pictureweb.config;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.commons.storage.StoredPhotos;

@Service
public class ResourceHandlerConfigurer extends WebMvcConfigurerAdapter {

  private final StoredPhotos storedPhotos;

  public ResourceHandlerConfigurer(StoredPhotos storedPhotos) {
    this.storedPhotos = Preconditions.checkNotNull(storedPhotos);
  }

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {

    registry.addResourceHandler("/assets/**").addResourceLocations("classpath:assets/");
    registry.addResourceHandler("/images/**").addResourceLocations("classpath:images/");
    registry.addResourceHandler("/photos/**").addResourceLocations(storedPhotos.getGeneratedLocation().toString());
  }
}
