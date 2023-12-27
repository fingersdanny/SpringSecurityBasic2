package com.practice.springsecuritybasic2.config;

import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

public class CsrfConfig {
	public CsrfTokenRequestAttributeHandler requestAttributeHandler() {
		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
		requestHandler.setCsrfRequestAttributeName("_csrf");
		return requestHandler;
	}
}
