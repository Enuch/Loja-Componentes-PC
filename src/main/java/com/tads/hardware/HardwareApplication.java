package com.tads.hardware;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.tads.hardware.service.FileStorageService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class HardwareApplication implements CommandLineRunner, WebMvcConfigurer {

	@Resource
	FileStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(HardwareApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.deledeAll();
		storageService.init();
		
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/")
		.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
	}
}
