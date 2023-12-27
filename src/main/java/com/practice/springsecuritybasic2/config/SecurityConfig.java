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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.practice.springsecuritybasic2.filter.CsrfCookieFilter;
import com.practice.springsecuritybasic2.filter.RequestValidationBeforeFilter;

@Configuration
@EnableWebMvc
@EnableWebSecurity
public class SecurityConfig {
	/**
	 *
	 * Spring Security 버전 3.1.0 부터 모든 설정을 Lambda DSL로 하도록 변경..
	 * http.csrf(htttpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
	 */

	/**
	 * JWT 기반 filterChain
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.securityContext(securityContextfConfigurer -> securityContextfConfigurer
				.requireExplicitSave(false))
			// 세션 사용 X
			.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.cors(corsConfigurer -> corsConfigurer.configurationSource(new CorsConfig()));
		http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer
				.csrfTokenRequestHandler(new CsrfConfig().requestAttributeHandler())
				.ignoringRequestMatchers("/contact", "/register")
				/** withHttpOnlyFalse 를 쓰는 이유
				 *  쿠키 기반의 세션을 설정한다고 할 때 (OAuth나 JWT 토큰을 사용하지 않는다고 치면)
				 *  csrfTokenRepository에 'XSRF-TOKEN'이라는 쿠키에 csrf 토큰을 유지합니다.
				 *
				 */

				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
			.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
			.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class);

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
				"/myCards",
				"/user")
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


	/**
	 * Session 사용 예시
	 */
	// @Bean
	// SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	// 	http.securityContext(securityContextfConfigurer -> securityContextfConfigurer
	// 		.requireExplicitSave(false))
	// 		.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
	// 			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
	// 	http.cors(corsConfigurer -> corsConfigurer.configurationSource(new CorsConfig()));
	// 	http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer
	// 			.csrfTokenRequestHandler(new CsrfConfig().requestAttributeHandler())
	// 			.ignoringRequestMatchers("/contact", "/register")
	// 			/** withHttpOnlyFalse 를 쓰는 이유
	// 			 *  쿠키 기반의 세션을 설정한다고 할 때 (OAuth나 JWT 토큰을 사용하지 않는다고 치면)
	// 			 *  csrfTokenRepository에 'XSRF-TOKEN'이라는 쿠키에 csrf 토큰을 유지합니다.
	// 			 *
	// 			 */
	//
	// 			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
	// 		.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
	// 		.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class);
	//
	// 	/**
	// 	* 1. 특정 요청 모두 허가 및 권한 부여
	// 	 */
	// 	http.authorizeHttpRequests((requests) -> requests
	// 			.requestMatchers(
	// 				"/notices",
	// 				"/contact",
	// 				"/register")
	// 		.permitAll()
	// 			.requestMatchers(
	// 				"/myAccount",
	// 				"/myBalance",
	// 				"/myLoans",
	// 				"/myCards",
	// 				"/user")
	// 		.authenticated());
	//
	// 	/**
	// 	 * 2. 모든 요청 거부 처리
	// 	 */
	// 	// http.authorizeHttpRequests(requests -> requests
	// 	// 	.anyRequest().denyAll());
	//
	// 	/**
	// 	 * 3. 모든 요청 허가
	// 	 */
	// 	// http.authorizeHttpRequests(requests -> requests
	// 	// 	.anyRequest().permitAll());
	//
	// 	http.formLogin(withDefaults());
	// 	http.httpBasic(withDefaults());
	// 	return http.build();
	// }


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

	/**
	 * Encoding = 단순한 변환 작업임 (다시 돌릴 수 있다.)
	 * Encryption = 키를 포함한 암호화 작업 (비밀을 보장함 * 복호화를 통해서 돌릴 수 있음)
	 * Hashing = 데이터를 해시 값으로 변환 시킴 (원본을 보지 않고도 값을 비교할 수 있음, 돌릴 수 없음)
	 *
	 * BCryptPasswordEncoder 는 매번 새로운 해시 값을 뱉는다.
	 *
	 * Security에서 다양한 PasswordEncoder 구현체를 제공하기 때문에 Spring Security를 사용하는 이점이 있음
	 * 이 중에서 NoOpPasswordEncoder나 StandardPasswordEncoder등의 구현체는 실제로 운영환경에서 쓰지 말아야함
	 * 왜냐면 이 구현체들은 운영환경에서 사용할 정도로 높은 보안을 제공해주지 않기 때문에,,
	 *
	 * Argon2PasswordEncoder;
	 * 더 최신의 해싱 알고리즘을 사용할 수록 보안성은 올라가지만, 실제 어플리케이션 구동 과정 속에서 로그인 시간이 더
	 * 늘어나거나 할 수 있다.
	 * 이러한 이유로 BCryptPasswordEncoder가 자주 사용된다.
	 *
	 * BCryptPasswordEncoder를 쓰고 나면 DB에 암호화된 해시 값으로 저장된 상태로 비밀번호가 존재해야한다.
	 */

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
