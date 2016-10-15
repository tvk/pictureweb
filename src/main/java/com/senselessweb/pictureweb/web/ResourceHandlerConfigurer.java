package com.senselessweb.pictureweb.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.base.Preconditions;
import com.senselessweb.pictureweb.Context;

@Component
public class ResourceHandlerConfigurer extends WebMvcConfigurerAdapter {
	
	private final Context context;
	
	public ResourceHandlerConfigurer(final Context context) {
		this.context = Preconditions.checkNotNull(context);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").addResourceLocations(context.getPicturesPath().toURI().toString());
	}

	
}
 