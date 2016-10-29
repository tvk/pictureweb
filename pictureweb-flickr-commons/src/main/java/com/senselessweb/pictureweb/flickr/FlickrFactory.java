package com.senselessweb.pictureweb.flickr;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.senselessweb.java.utils.ExtPreconditions;

@Service
public class FlickrFactory {

  private static final String APIKEY = "pictureweb.flickr.apikey";
  private static final String APISECRET = "pictureweb.flickr.apisecret";

  @Bean
  public Flickr flickr() {
    final String apiKey = ExtPreconditions.checkNotBlank(System.getenv(APIKEY),
        "No api key given. Please ensure that the environment variable \"" + APIKEY + "\".");
    final String apiSecret = ExtPreconditions.checkNotBlank(System.getenv(APISECRET),
        "No api secret given. Please ensure that the environment variable \"" + APISECRET + "\".");
    return new Flickr(apiKey, apiSecret, new REST());
  }
}
