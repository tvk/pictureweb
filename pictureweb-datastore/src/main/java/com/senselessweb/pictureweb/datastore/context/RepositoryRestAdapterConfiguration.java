package com.senselessweb.pictureweb.datastore.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.senselessweb.pictureweb.datastore.domain.StoredAlbum;
import com.senselessweb.pictureweb.datastore.domain.StoredAuthentication;
import com.senselessweb.pictureweb.datastore.domain.StoredPhoto;

@Configuration
public class RepositoryRestAdapterConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
      objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      objectMapper.setSerializationInclusion(Include.NON_NULL);
    }
    
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
      config.exposeIdsFor(StoredAlbum.class, StoredPhoto.class, StoredAuthentication.class);
    }

}