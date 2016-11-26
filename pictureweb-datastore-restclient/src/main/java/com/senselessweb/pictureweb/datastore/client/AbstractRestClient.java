package com.senselessweb.pictureweb.datastore.client;

import java.net.URI;
import java.util.Collection;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

public abstract class AbstractRestClient<T> {
  
  private static final String baseUrl = "http://localhost:8081/";
  private final RestTemplate restTemplate;
  
  public AbstractRestClient(final RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public final Collection<T> getAll() {
    return getPageRecursive(baseUrl + getEntityName() + "?size=1000");
  }

  private Collection<T> getPageRecursive(final String uri) {

    final PagedResources<T> body = restTemplate.exchange(uri,
        HttpMethod.GET, HttpEntity.EMPTY, getCollectionType()).getBody();
    
    final Collection<T> entities = Lists.newArrayList(body.getContent());
    if (body.hasLink("next")) {
      final Link next = body.getLink("next");
      entities.addAll(getPageRecursive(next.getHref()));
    }
    return entities;
  }


  public final T get(final String id) {
    final T entity = restTemplate.exchange(URI.create(baseUrl + getEntityName() + "/" + id),
        HttpMethod.GET, HttpEntity.EMPTY,
        getEntityType()).getBody();
    return entity;
  }
  
  public void save(T entity) {
    restTemplate.postForObject(URI.create(baseUrl + getEntityName()), entity, Void.class);
  }
  
  protected abstract ParameterizedTypeReference<PagedResources<T>> getCollectionType();

  protected abstract ParameterizedTypeReference<T> getEntityType();
  
  protected abstract String getEntityName();
}
