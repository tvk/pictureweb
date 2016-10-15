package com.senselessweb.pictureweb.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.senselessweb.pictureweb.Context;
import com.senselessweb.pictureweb.domain.Album;
import com.senselessweb.pictureweb.events.AlbumsChangedEvent;
import com.senselessweb.pictureweb.flickr.downloader.PhotoDownloader;

@Service
public class AlbumService {
	
	private static final Log log = LogFactory.getLog(PhotoDownloader.class);

	private final AtomicReference<Map<String, Album>> cache = new AtomicReference<Map<String, Album>>();
	private final PictureService pictureService;
	private final Context context;
	
	public AlbumService(final Context context, final PictureService pictureService) {
		this.context = Preconditions.checkNotNull(context);
		this.pictureService = Preconditions.checkNotNull(pictureService);
	}

	public Collection<Album> getAllAlbums() {
		return cache.get().values();
	}
	
	@EventListener(AlbumsChangedEvent.class)
	public void rebuildCache() {
		
		final FileFilter filter = (f) -> f.isFile() && FilenameUtils.isExtension(f.getName(), "properties"); 
		final Map<String, Album> albums = Lists.newArrayList(context.getAlbumsPath().listFiles(filter)).stream()
				.map(f -> readJson(f))
				.filter(j -> j != null)
				.map(p -> new Album(p))
				.collect(Collectors.toMap(a -> a.getId(), a -> a));
		this.cache.set(albums);
	}

	private JsonElement readJson(final File f) {
		try {
			try (final FileReader r = new FileReader(f)) {
				return new JsonParser().parse(r);
			}
		} catch (final Exception e) {
			log.error("Could not read album properties file", e);
			return null;
		}
	}
}
