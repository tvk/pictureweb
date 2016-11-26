package com.senselessweb.pictureweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.senselessweb.pictureweb.web.service.ClientAlbumService;

@Configuration
public class ExposedBeansConfiguration {

  @Bean
  public InternalResourceViewResolver internalResourceViewResolver() {
    final InternalResourceViewResolver bean = new InternalResourceViewResolver();
    bean.setExposedContextBeanNames(
        ClientAlbumService.beanName,
        WebConfiguration.googleMapsJsUrlBeanName);
    return bean;
  }

}
