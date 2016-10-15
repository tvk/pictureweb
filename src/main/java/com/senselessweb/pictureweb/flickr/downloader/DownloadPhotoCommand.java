package com.senselessweb.pictureweb.flickr.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flickr4java.flickr.photos.Exif;
import com.flickr4java.flickr.photos.Size;
import com.google.common.base.Preconditions;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.senselessweb.pictureweb.flickr.authentication.FlickrAuthService;
import com.senselessweb.pictureweb.flickr.authentication.FlickrProvider;

public class DownloadPhotoCommand extends HystrixCommand<Void> {

	private static final Log log = LogFactory.getLog(FlickrAuthService.class);
	private final ObjectMapper objectMapper;
	private final StoredPhoto photo;
	private final FlickrProvider flickr;

	public DownloadPhotoCommand(final FlickrProvider flickr, final ObjectMapper objectMapper, final StoredPhoto photo) {

		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("DownloadGroup"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("DownloadPhoto"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter()
						.withExecutionTimeoutEnabled(false))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.defaultSetter().withCoreSize(5)
						.withMaxQueueSize(Integer.MAX_VALUE).withQueueSizeRejectionThreshold(Integer.MAX_VALUE)));

		this.flickr = Preconditions.checkNotNull(flickr);
		this.objectMapper = Preconditions.checkNotNull(objectMapper); 
		this.photo = Preconditions.checkNotNull(photo);
	}

	@Override
	protected Void run() {
		
		final String photoId = photo.getPhoto().getId();
		log.debug("Downloading " + photoId);
		
		if (!photo.isUp2Date()) {
			downloadPhoto(Size.ORIGINAL, photo.getOriginal());
			downloadPhoto(Size.LARGE_1600, photo.getHighResolution());
			downloadPhoto(Size.MEDIUM_640, photo.getMediumResolution());
			downloadPhoto(Size.SMALL_320, photo.getLowResolution());
		} else { 
			downloadPhotoIfNecessary(Size.ORIGINAL, photo.getOriginal());
			downloadPhotoIfNecessary(Size.LARGE_1600, photo.getHighResolution());
			downloadPhotoIfNecessary(Size.MEDIUM_640, photo.getMediumResolution());
			downloadPhotoIfNecessary(Size.SMALL_320, photo.getLowResolution());
		}
		
		downloadMetadata();
		log.debug("Downloaded " + photoId);		
		return null;
	}

	private void downloadPhotoIfNecessary(final int size, final File file) {
		if (!file.isFile()) {
			downloadPhoto(size, file);
		}
	}

	private void downloadMetadata() {
		try {
			final Collection<Exif> exif = flickr.get().getPhotosInterface().getExif(photo.getPhoto().getId(), photo.getPhoto().getSecret());
			objectMapper.writeValue(photo.getMetadata(), new PhotoMetadataWrapper(photo.getPhoto(), exif));
		} catch (final Exception e) {
			log.error("Could not download metadata", e);
			photo.getMetadata().delete();
		}
	}

	private void downloadPhoto(final int size, final File file) {
		try {
			try (final InputStream is = flickr.get().getPhotosInterface().getImageAsStream(photo.getPhoto(), size)) {
				photo.getHighResolution().getParentFile().mkdirs();
				try (final FileOutputStream fos = new FileOutputStream(file)) {
					IOUtils.copy(is, fos);
				}
			}
		} catch (final Exception e) {
			log.error("Could not download photo", e);
			file.delete();
		}
	}
}
