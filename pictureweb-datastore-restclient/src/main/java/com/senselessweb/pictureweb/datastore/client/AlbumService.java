package com.senselessweb.pictureweb.datastore.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.senselessweb.pictureweb.datastore.domain.Album;

@Service
public class AlbumService extends AbstractRestClient<Album> {

  public AlbumService(final RestTemplate restTemplate) {
    super(restTemplate);
  }
  
  @Override
  protected String getEntityName() {
    return "storedAlbums";
  }

  @Override
  protected ParameterizedTypeReference<PagedResources<Album>> getCollectionType() {
    return new ParameterizedTypeReference<PagedResources<Album>>() {};
  }

  @Override
  protected ParameterizedTypeReference<Album> getEntityType() {
    return new ParameterizedTypeReference<Album>() {};
  }

}
