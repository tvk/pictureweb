package com.senselessweb.pictureweb.commons.storage;

import java.io.File;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class StoredPhotos {

  private static final Log log = LogFactory.getLog(StoredPhotos.class);
  private final File storageBase;
  private final File storageOriginals;
  private final File storageGenerated;

  public StoredPhotos() {
    final File storage = new File(StringUtils.isBlank(System.getenv("pictureweb_storage_development")) ? 
        "/data/storage/" : System.getenv("pictureweb_storage_development"));
    log.debug("Storage folder determined to: " + storage);
    this.storageBase = storage;
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
