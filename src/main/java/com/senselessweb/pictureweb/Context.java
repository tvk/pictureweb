package com.senselessweb.pictureweb;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Component;

@Component
public class Context {

	public File getStoragePath() {
		return new File("/media/thomas/Daten/Development/pictureweb/data/");
	}
	
	public File getPicturesPath() {
		return new File(getStoragePath(), "pictures");
	}
	
	public File getAlbumsPath() {
		return new File(getStoragePath(), "albums");
	}

	public File getConfigPath() {
		final File path = new File(getStoragePath(), "config");
		if (!path.isDirectory() && !path.mkdirs()) {
			throw new IllegalStateException("Could not create config directory");
		}
		return path;
	}

	public URL getBaseUrl() {
		try {
			return new URL("http://localhost:8080");
		} catch (final MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

}
