package com.senselessweb.pictureweb.downloader.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.flickr4java.flickr.photos.Photo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class DownloadPhotoService {

  @HystrixCommand
  public void download(final Photo photo, final int size, final File target) {
    
    flickr.getPhotosInterface().getImageAsStream(photo, size)
  }
}
