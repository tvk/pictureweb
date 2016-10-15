package com.senselessweb.pictureweb.flickr.downloader;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flickr4java.flickr.photos.Photo;
import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.Context;

public class StoredPhoto {

	private final Photo photo;
	private final Context context;

	public StoredPhoto(final Context context, final Photo photo) {
		this.context = Preconditions.checkNotNull(context);
		this.photo = Preconditions.checkNotNull(photo);
	}

	public Photo getPhoto() {
		return photo;
	}

	public File getOriginal() {
		return generatePath(context.getPicturesPath(), photo, null, "jpg");
	}

	public File getLowResolution() {
		return generatePath(context.getPicturesPath(), photo, "low", "jpg");
	}

	public File getMediumResolution() {
		return generatePath(context.getPicturesPath(), photo, "medium", "jpg");
	}

	public File getHighResolution() {
		return generatePath(context.getPicturesPath(), photo, "high", "jpg");
	}

	public File getMetadata() {
		return generatePath(context.getPicturesPath(), photo, "metadata", "properties");
	}

	public JsonNode getMetadataAsJson() {
		if (getMetadata().isFile()) {
			try {
				return new ObjectMapper().readTree(getMetadata());
			} catch (final Exception e) {
				throw new IllegalArgumentException("Can not read metadata");
			}
		} else {
			return null;
		}
	}

	private File generatePath(File picturesDir, Photo p, final String suffix, String extension) {
		final File folder = new File(picturesDir, StringUtils.substring(p.getId(), 0, 4) + File.separator + p.getId());
		final String filename = p.getId() + (suffix == null ? "" : ("-" + suffix)) + "." + extension;
		return new File(folder, filename);
	}

	public boolean needsRefresh() {
		if (!getLowResolution().isFile()) {
			return true;
		}
		if (!getMediumResolution().isFile()) {
			return true;
		}
		if (!getHighResolution().isFile()) {
			return true;
		}
		return !isUp2Date();
	}

	public boolean isUp2Date() {
		final JsonNode metadata = getMetadataAsJson();
		if (metadata == null) {
			return false;
		}
		final long storedLastUpdate = metadata.get("lastUpdate").asLong();
		final long newLastUpdate = photo.getLastUpdate().getTime();
		return storedLastUpdate >= newLastUpdate;
	}
}
