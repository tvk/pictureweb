package com.senselessweb.pictureweb.datastore.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.senselessweb.pictureweb.datastore.domain.Authentication;

@Service
public class AuthenticationService extends AbstractRestClient<Authentication> {
  
  public AuthenticationService(final RestTemplate restTemplate) {
    super(restTemplate);
  }
  
  @Override
  protected String getEntityName() {
    return "storedAuthentications";
  }

  @Override
  protected ParameterizedTypeReference<PagedResources<Authentication>> getCollectionType() {
    return new ParameterizedTypeReference<PagedResources<Authentication>>() {};
  }

  @Override
  protected ParameterizedTypeReference<Authentication> getEntityType() {
    return new ParameterizedTypeReference<Authentication>() {};
  }

}
