package com.bt.ms.im.util;

import java.io.IOException;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Filter to help logging interceptor
 * @author Suman Mandal
 * 
 */
@Component
public class LoggingFilter implements Filter {
	
	@Value("${spring.service.start.url:/ms/}")
	private String startUrl;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("########## Initiating LoggingFilter ##########");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String uri = request.getRequestURI();
		if (uri.contains(startUrl)) {

			HttpServletResponse response = (HttpServletResponse) servletResponse;
			HttpServletRequest requestWrapper = new ResettableRequestServletWrapper(request);
			HttpServletResponse responseWrapper =  (HttpServletResponse) new ContentCachingResponseWrapper(response);

			filterChain.doFilter(requestWrapper, responseWrapper);
		} else {
			filterChain.doFilter(servletRequest, servletResponse);
		}

	}

	@Override
	public void destroy() {
	}
}
