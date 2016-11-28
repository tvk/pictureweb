package com.senselessweb.pictureweb.datastore.client;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

public abstract class AbstractRestClient<T> {
  
  private static final String baseUrl = "http://pictureweb-datastore/";
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
      try {
        final Link next = body.getLink("next");
        // Just "next" does not work. Locally this is something like
        // http://Overlord.fritz.box:8081/storedPhotos?page=1&size=1000
        // which confuses the eureka powered resttemplate.
        // It looks like this is not yet configurable in spring-data-rest:
        // http://stackoverflow.com/questions/34792288/spring-data-rest-links-and-ribbon-client-loadbalancer
        final String href = baseUrl + StringUtils.removeStart(new URL(next.getHref()).getFile(), "/");
        entities.addAll(getPageRecursive(href));
      } catch (final MalformedURLException e) {
        throw new RuntimeException(e);
      }
      
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
