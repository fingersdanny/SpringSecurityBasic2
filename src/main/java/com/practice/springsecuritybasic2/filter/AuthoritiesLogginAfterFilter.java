package com.practice.springsecuritybasic2.filter;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class AuthoritiesLogginAfterFilter implements Filter {
	//todo : AOP 로 변경해서 모든 filter에 로거 달기
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			LOG.info("User " + authentication.getName() + " is successfully authenticated and "
				+ "has the authorities " + authentication.getAuthorities().toString());
		}
		chain.doFilter(request, response);
	}

	private final Logger LOG =
		Logger.getLogger(AuthoritiesLogginAfterFilter.class.getName());
}
