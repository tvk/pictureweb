package com.senselessweb.pictureweb.flickr.authentication;

import javax.inject.Provider;

import com.flickr4java.flickr.Flickr;

public interface FlickrProvider extends Provider<Flickr> {
	
	public String getUserId();
	
	public boolean isAuthenticated();

}
