package com.practice.springsecuritybasic2.config;

import static org.springframework.security.config.Customizer.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/**
		* 1. 특정 요청 모두 허가 및 권한 부여
		 */
		// http.authorizeHttpRequests((requests) -> requests
		// 		.requestMatchers(
		// 			"/notices",
		// 			"/contact")
		// 	.permitAll()
		// 		.requestMatchers(
		// 			"/myAccount",
		// 			"/myBalance",
		// 			"/myLoans",
		// 			"/myCards")
		// 	.authenticated());

		/**
		 * 2. 모든 요청 거부 처리
		 */
		// http.authorizeHttpRequests(requests -> requests
		// 	.anyRequest().denyAll());

		/**
		 * 3. 모든 요청 허가
		 */
		http.authorizeHttpRequests(requests -> requests
			.anyRequest().permitAll());

		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		return http.build();
	}
}
