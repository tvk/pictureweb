package com.senselessweb.pictureweb;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableScheduling
public class PictureWebApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PictureWebApplication.class, args);
	}
	
	@Bean
	public SimpleApplicationEventMulticaster applicationEventMulticaster() {
		final SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
		multicaster.setTaskExecutor(taskExecutor());
		return multicaster;
	}
	
	@Bean
	public Executor taskExecutor() {
		return new ThreadPoolTaskExecutor();
	}
}
