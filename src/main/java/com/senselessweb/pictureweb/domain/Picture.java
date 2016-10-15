package com.senselessweb.pictureweb.domain;

import java.util.Optional;

import com.senselessweb.java.utils.ExtPreconditions;

public class Picture {

	private final String url;
	
	public Picture(final String url) {
		this.url = ExtPreconditions.checkNotBlank(url);
	}
	
	public String getUrl() {
		return url;
	}

	public Optional<GeoData> getGeoData() {
		// TODO Auto-generated method stub
		return null;
	}
}
