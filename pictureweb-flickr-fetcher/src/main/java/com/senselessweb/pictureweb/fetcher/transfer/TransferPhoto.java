package com.senselessweb.pictureweb.fetcher.transfer;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.flickr4java.flickr.photos.Exif;
import com.flickr4java.flickr.photos.Photo;
import com.senselessweb.pictureweb.datastore.domain.GeoData;
import com.senselessweb.pictureweb.datastore.domain.Tag;

public class TransferPhoto implements com.senselessweb.pictureweb.datastore.domain.Photo {

  private final Photo photo;
  private final Collection<Exif> exif;
  
  public TransferPhoto(final Photo photo, final Collection<Exif> exif) {
    this.photo = photo;
    this.exif = exif;
  }

  @Override
  public String getId() {
    return photo.getId();
  }

  @Override
  public String getTitle() {
    return photo.getTitle();
  }

  @Override
  public String getDescription() {
    return photo.getDescription();
  }

  @Override
  public GeoData getGeo() {
    return photo.getGeoData() == null ? null : new GeoData() {
      
      @Override
      public String getLongitude() {
        return String.valueOf(photo.getGeoData().getLongitude());
      }
      
      @Override
      public String getLatitude() {
        return String.valueOf(photo.getGeoData().getLatitude());
      }
    };
  }

  @Override
  public Date getLastUpdate() {
    return photo.getLastUpdate();
  }

  @Override
  public List<Tag> getTags() {
    return photo.getTags() == null ? null :
      photo.getTags().stream().map(t -> tag(t)).collect(Collectors.toList());
  }

  private Tag tag(com.flickr4java.flickr.tags.Tag t) {
    return new Tag() {
      
      @Override
      public String getValue() {
        return t.getValue();
      }
      
      @Override
      public String getRaw() {
        return t.getRaw();
      }
      
      @Override
      public String getId() {
        return t.getId();
      }
    };
  }

  @Override
  public List<com.senselessweb.pictureweb.datastore.domain.Exif> getExif() {
    return exif == null ? null : exif.stream().map(e -> exif(e)).collect(Collectors.toList());
  }

  private com.senselessweb.pictureweb.datastore.domain.Exif exif(Exif e) {
    return new com.senselessweb.pictureweb.datastore.domain.Exif() {
      
      @Override
      public String getTagspace() {
        return e.getTagspace();
      }
      
      @Override
      public String getTag() {
        return e.getTag();
      }
      
      @Override
      public String getRaw() {
        return e.getRaw();
      }
      
      @Override
      public String getClean() {
        return e.getClean();
      }
    };
  }

}
