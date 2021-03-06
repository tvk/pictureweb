package com.senselessweb.pictureweb.commons.storage;

import java.io.File;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class StoredPhotos {

  private final File storageBase = new File("/var/local/pictureweb/images");
  private final File storageOriginals;
  private final File storageGenerated;

  public StoredPhotos() {
    this.storageOriginals = new File(storageBase, "originals");
    this.storageGenerated = new File(storageBase, "generated");
  }

  public File getOriginal(final String photoId) {
    final File location = new File(storageOriginals, StringUtils.substring(photoId, 0, 5) + File.separator);
    if (!location.isDirectory()) {
      if (!location.mkdirs()) {
        throw new RuntimeException("Could not create directory " + location);
      }
    }
    return new File(location, photoId + ".jpg");
  }

  public File getSmall(final String photoId) {
    return getStoredFile(photoId, "small");
  }

  public File getMedium(final String photoId) {
    return getStoredFile(photoId, "medium");
  }

  public File getLarge(final String photoId) {
    return getStoredFile(photoId, "large");
  }
  
  public boolean isComplete(final String photoId) {
    return getLarge(photoId).isFile() && getMedium(photoId).isFile() && 
           getSmall(photoId).isFile() && getOriginal(photoId).isFile();
    
  }

  private File getStoredFile(final String photoId, final String prefix) {
    final File location = new File(storageGenerated, StringUtils.substring(photoId, 0, 5) + File.separator);
    if (!location.isDirectory()) {
      if (!location.mkdirs()) {
        throw new RuntimeException("Could not create directory " + location);
      }
    }
    return new File(location, photoId + "@" + prefix + ".jpg");
  }

  public URI getGeneratedLocation() {
    return storageGenerated.toURI();
  }

}
