package com.senselessweb.pictureweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling
@EnableHystrix
@EnableEurekaClient
public class PictureWebFlickrFetchApplication {

  public static void main(String[] args) {
    SpringApplication.run(PictureWebFlickrFetchApplication.class, args);
  }

  @Bean
  public TaskScheduler taskScheduler() {
    final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(4);
    return scheduler;
  }
}
