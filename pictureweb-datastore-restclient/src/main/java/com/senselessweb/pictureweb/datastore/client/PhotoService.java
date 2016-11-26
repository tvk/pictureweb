package com.senselessweb.pictureweb.datastore.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.senselessweb.pictureweb.datastore.domain.Photo;

@Service
public class PhotoService extends AbstractRestClient<Photo> {

  public PhotoService(final RestTemplate restTemplate) {
    super(restTemplate);
  }
  
  @Override
  protected String getEntityName() {
    return "storedPhotos";
  }

  @Override
  protected ParameterizedTypeReference<PagedResources<Photo>> getCollectionType() {
    return new ParameterizedTypeReference<PagedResources<Photo>>() {};
  }

  @Override
  protected ParameterizedTypeReference<Photo> getEntityType() {
    return new ParameterizedTypeReference<Photo>() {};
  }

}
