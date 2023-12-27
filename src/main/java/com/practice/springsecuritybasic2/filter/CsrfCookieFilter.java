package com.practice.springsecuritybasic2.filter;

import java.io.IOException;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CsrfCookieFilter extends OncePerRequestFilter {
	// 로그인 시 CsrfToken을 클라이언트에서 알 수 있는 방법이 없음. 따라서 이런 필터를 통해서 클라이언트가 사용할 토큰 정보를 담아서 보내야함
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		if (null != csrfToken.getHeaderName()) {
			response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
		}
		filterChain.doFilter(request, response);
	}
}
