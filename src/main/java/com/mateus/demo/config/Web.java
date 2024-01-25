package com.mateus.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class Web implements WebMvcConfigurer {

	public void addCorsMappings(CorsRegistry registry){
		registry.addMapping("/**");
	}

}
