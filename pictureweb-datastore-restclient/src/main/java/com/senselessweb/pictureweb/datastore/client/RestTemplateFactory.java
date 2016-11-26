package com.senselessweb.pictureweb.datastore.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.module.mrbean.MrBeanModule;

@Configuration
public class RestTemplateFactory implements BeanPostProcessor {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    
    if (bean instanceof RestTemplate) {
      // spring-data-rest attached a seperate message converter in a postprocessor. That's why we have 
      // to do this here instead of in the @Bean method.
      attachMrBeanModule((RestTemplate) bean);
    }
    
    return bean;
  }

  private void attachMrBeanModule(final RestTemplate bean) {
      bean.getMessageConverters().stream()
        .filter(mc -> mc instanceof MappingJackson2HttpMessageConverter)
        .map(mc -> (MappingJackson2HttpMessageConverter) mc)
        .forEach(mc -> mc.getObjectMapper().registerModule(new MrBeanModule()));
  }
}
