package com.practice.springsecuritybasic2.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

public class CorsConfig implements CorsConfigurationSource {
	// @Override
	// public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	// 	CorsConfiguration config = new CorsConfiguration();
	// 	config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
	// 	config.setAllowedMethods(Collections.singletonList("*"));
	// 	config.setAllowCredentials(true);
	// 	config.setAllowedHeaders(Collections.singletonList("*"));
	// 	config.setMaxAge(3600L);
	// 	return config;
	// }

	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Collections.singletonList("*"));
		config.setExposedHeaders(Arrays.asList("Authorization"));
		config.setMaxAge(3600L);
		return config;
	}
}
