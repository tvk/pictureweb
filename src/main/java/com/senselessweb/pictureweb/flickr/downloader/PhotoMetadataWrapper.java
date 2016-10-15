package com.senselessweb.pictureweb.flickr.downloader;

import java.util.Collection;
import java.util.Date;

import com.flickr4java.flickr.photos.Exif;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.tags.Tag;
import com.google.common.base.Preconditions;

public class PhotoMetadataWrapper {
	
	private final Photo photo;
	private final Collection<Exif> exif;
	
	public PhotoMetadataWrapper(final Photo photo, final Collection<Exif> exif) {
		this.exif = exif;
		this.photo = Preconditions.checkNotNull(photo);
	}
	
    public String getId() {
        return photo.getId();
    }
	
    public String getTitle() {
        return photo.getTitle();
    }
    
    public String getDescription() {
        return photo.getDescription();
    }
    
    public GeoData getGeo() {
        return photo.getGeoData();
    }
	
    public Date getLastUpdate() {
        return photo.getLastUpdate();
    }    

    public Collection<Tag> getTags() {
        return photo.getTags();
    }
    
    public Collection<Exif> getExif() {
		return exif;
	}
}
