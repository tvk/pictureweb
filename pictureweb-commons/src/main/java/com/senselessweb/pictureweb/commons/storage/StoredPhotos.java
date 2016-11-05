package com.senselessweb.pictureweb.commons.storage;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class StoredPhotos {

  private final File storageBase = new File("/home/thomas/development/java/picture-web/data/"); // TODO
  private final File storageOriginals = new File(storageBase, "originals");
  private final File storageGenerated = new File(storageBase, "generated");

  public File getOriginal(final String photoId) {
    final File location = new File(storageOriginals, StringUtils.substring(photoId, 0, 5) + File.separator);
    if (!location.isDirectory()) {
      location.mkdirs();
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

  private File getStoredFile(final String photoId, final String prefix) {
    final File location = new File(storageGenerated, StringUtils.substring(photoId, 0, 5) + File.separator);
    if (!location.isDirectory()) {
      location.mkdirs();
    }
    return new File(location, photoId + "@" + prefix + ".jpg");
  }

}
