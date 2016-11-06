package com.senselessweb.pictureweb.commons.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class ExtPreconditions {

	public static File checkIsFile(final File file) {

		if (file != null && file.isFile()) {
			return file;
		} else {
			throw new IllegalArgumentException("File \"" + file + "\" is not a file");
		}
	}

	public static File checkIsDirectory(final File file) {

		if (file != null && file.isDirectory()) {
			return file;
		} else {
			throw new IllegalArgumentException("File \"" + file + "\" is not a directory");
		}
	}

	public static String checkNotBlank(final String string) {
		return checkNotBlank(string, "Param must not be blank");
	}

	public static String checkNotBlank(final String string, final String message) {

		if (StringUtils.isBlank(string)) {
			throw new IllegalArgumentException(message);
		} else {
			return string;
		}
	}

}
