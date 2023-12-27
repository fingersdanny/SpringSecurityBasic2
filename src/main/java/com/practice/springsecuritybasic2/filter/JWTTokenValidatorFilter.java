package com.practice.springsecuritybasic2.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.practice.springsecuritybasic2.constants.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
		if (jwt != null) {
			try {
				SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

				Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(jwt)
					.getBody();

				String username = String.valueOf(claims.get("username"));
				String authorities = (String)claims.get("authorities");
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
					AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (Exception e) {
				throw new BadCredentialsException("Invalid Token received");
			}
		}
		filterChain.doFilter(request, response);
	}

	// JWTTokenGeneratorFilter 와 마찬가지로 이 필터는 /user를 제외한 모든 API endpoint에서 동작해야함
	// 왜냐면 다른 API로 접근할 때 적법한 Security Token을 들고 있는지 확인해야하기 때문,,
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/user");
	}
}
