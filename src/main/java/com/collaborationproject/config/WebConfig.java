package com.collaborationproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


//alternative to dispatcher-servlet
@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.collaborationproject")
public class WebConfig extends WebMvcConfigurerAdapter{
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/resources/**")
		.addResourceLocations("/resources/");
	}
	
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver getCommonsMultipartResolver(){
		CommonsMultipartResolver commonsMultipartResolver= new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxUploadSize(307200);
		return commonsMultipartResolver;
	}
}
