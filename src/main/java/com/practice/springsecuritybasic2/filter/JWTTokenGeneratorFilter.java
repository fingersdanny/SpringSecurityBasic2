package com.practice.springsecuritybasic2.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.practice.springsecuritybasic2.constants.SecurityConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
	// todo : 이렇게 해서 어디에 저장하는데..?
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
			String jwt = Jwts.builder().setIssuer("Eazy Bank").setSubject("JWT Token")
				.claim("username", authentication.getName())
				.claim("authorities", populateAuthorities(authentication.getAuthorities()))
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 30_000))
				.signWith(key).compact();
			response.setHeader(SecurityConstants.JWT_HEADER, jwt);
		}

		filterChain.doFilter(request, response);
	}

	// 이 필터가 jwt가 특정 api path에서만 작동하도록 해줌
	// (로그인 프로세스에서만 jwt를 만들고 이외의 api 호출 시에는 계속해서 불러올 필요가 없기 때문에)
	// 아래 코드는 /user 인 api endpoint일 때 true 이므로 !을 붙여서 항상 false로 만들어주고
	// 그 이외의 path 에서는 정상적으로 작동하게끔 한다.
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !request.getServletPath().equals("/user");
	}

	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
		for (GrantedAuthority authority : collection) {
			authoritiesSet.add(authority.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}
}
