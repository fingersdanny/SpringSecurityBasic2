package com.practice.springsecuritybasic2.config;

import static org.springframework.security.config.Customizer.*;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
		/**
		* 1. 특정 요청 모두 허가 및 권한 부여
		 */
		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers(
					"/notices",
					"/contact",
					"/register")
			.permitAll()
				.requestMatchers(
					"/myAccount",
					"/myBalance",
					"/myLoans",
					"/myCards")
			.authenticated());

		/**
		 * 2. 모든 요청 거부 처리
		 */
		// http.authorizeHttpRequests(requests -> requests
		// 	.anyRequest().denyAll());

		/**
		 * 3. 모든 요청 허가
		 */
		// http.authorizeHttpRequests(requests -> requests
		// 	.anyRequest().permitAll());

		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		return http.build();
	}


	// @Bean
	// public InMemoryUserDetailsManager userDetailsService() {
	// 	/**
	// 	 * 1. 인메모리 유저 권한 설정.  withDefaultPasswordEncoder 사용 (deprecated)
	// 	 */
	// 	// UserDetails admin = User.withDefaultPasswordEncoder()
	// 	// 	.username("admin")
	// 	// 	.password("1234")
	// 	// 	.authorities("admin")
	// 	// 	.build();
	// 	//
	// 	// UserDetails user = User.withDefaultPasswordEncoder()
	// 	// 	.username("user")
	// 	// 	.password("1234")
	// 	// 	.authorities("read")
	// 	// 	.build();
	//
	// 	/**
	// 	 * 2. Custom PasswordEncoder 사용 (권장되는 방식)
	// 	 *  UserDetails는 한번 만들어지고 나면 Setter가 닫혀 있어서 수정이 불가함
	// 	 *  이는 framework 설계 상 임의로 유저에 대한 정보를 바꾸지 않게 하기 위해서
	// 	 *  지금과 같이 구현할 경우 InMemoryUserDetailsManager가 실제 구현체를 담당하며
	// 	 *  UserDetailsService 내부의
	// 	 *  loadUserByUsername(String Username) 메서드를 통해서 AuthenticationProvider를 통해 제공 받은 인증를 통해서 작동함
	// 	 *  UserDetailsService -> UserDetailsManager를 통해서 유저 정보를 가져올 수 있다.
	// 	 */
	// 	UserDetails admin = User.withUsername("admin")
	// 		.username("admin")
	// 		.password("1234")
	// 		.authorities("admin")
	// 		.build();
	//
	// 	UserDetails user = User.withUsername("user")
	// 		.username("user")
	// 		.password("1234")
	// 		.authorities("read")
	// 		.build();
	// 	return new InMemoryUserDetailsManager(admin, user);
	// }

	/**
	 * Principal -> Authentication -> UsernamePasswordAuthenticationToken
	 * AuthenticationProvider, AuthenticationProviderManager에서 사용되어 모든 인증이 성공적이었는지 확인할 때 반환 타입
	 *
	 * UserDetails -> User
	 * UserDetailsManager, UserDetailsService에서 유저 정보를 불러 오기 위해 사용
	 */

	/**
	 * 실제 클라우드 환경의 (AWS RDS)에 연결하고 JdbcUserDetailsManager를 통해서 관리
	 // * @param dataSource
	 */
	// @Bean
	// public UserDetailsService userDetailsService(DataSource dataSource) {
	// 	return new JdbcUserDetailsManager(dataSource);
	// }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
