package com.senselessweb.pictureweb.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class ExposeBeansConfiguration {

  @Bean
  public InternalResourceViewResolver resourceViewResolver() {
    final InternalResourceViewResolver bean = new InternalResourceViewResolver();
    bean.setExposedContextBeanNames("pictureService");
    return bean;
  }
}
