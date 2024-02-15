package com.bt.ms.im.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.bt.ms.im.bptm.BptmContextHolder;
import com.bt.util.logging.E2ETransaction;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * logging the 2981,2984
 * 
 * @author Suman Mandal
 * 
 */
@Component
public class LoggingRequestInterceptor implements HandlerInterceptor {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${spring.service.start.url:/ms/}")
	private String startUrl;
	
	@Autowired
	LogUtil logUtil;

	private static ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse res, Object handler) throws Exception {
		String url = request.getRequestURI();
		String e2edata = null;
		if (url.contains("v1" + startUrl)) {

			StringBuilder sb = new StringBuilder();
			sb.append("2981 uri = ");
			sb.append(request.getRequestURI());
			sb.append("?");
			sb.append(request.getQueryString());
			sb.append(" Method= ");
			sb.append(request.getMethod());
		      
			sb.append(" RequestHeaders=");
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				sb.append(headerName);
				sb.append("=");
				sb.append(request.getHeader(headerName));
				sb.append(" ");
				if (AppConstantsUtil.E2EDATA_HEADER.equalsIgnoreCase(headerName)) {
					e2edata = request.getHeader(headerName);
				}
			}
			sb.append("payload=");
			sb.append(getPayload(request));
			String[] urlArr = url.split("v1" + startUrl);
			String serviceName = url;
			if (urlArr.length > 1) {
				String[] splitArr = urlArr[1].split("/");
				serviceName = splitArr[0];
			}
			if (e2edata != null) {
				BptmContextHolder.createContext(serviceName + "-" + request.getMethod(), appName, e2edata);
			} else {
				BptmContextHolder.createContext(serviceName + "-" + request.getMethod(), appName);
			}
			E2ETransaction e2eTxn = BptmContextHolder.getE2ETxn();
			e2eTxn.startInboundCall();
			String finalLog = logUtil.maskLog(sb.toString());
			e2eTxn.logMessage("2981", finalLog);
			log.info(finalLog);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse responseWrapper, Object handler,
			@Nullable Exception ex) throws Exception {
		String url = request.getRequestURI();
		if (url.contains("v1" + startUrl)) {
			E2ETransaction e2eTxn = BptmContextHolder.getE2ETxn();
			responseWrapper.addHeader(AppConstantsUtil.E2EDATA_HEADER, e2eTxn.toString());
			StringBuilder sb = new StringBuilder();
			sb.append(" Status Code :: ");
			sb.append(responseWrapper.getStatus());
			sb.append(System.lineSeparator());
			sb.append(" ResponseHeaders :: ");
			for (String headerName : responseWrapper.getHeaderNames()) {
				sb.append(headerName);
				sb.append("=");
				sb.append(responseWrapper.getHeader(headerName));
				sb.append(" ");
			}
			sb.append(System.lineSeparator());
			String payLoad = getPayload(responseWrapper);
			if (payLoad != null) {

				sb.append(" Body :: ");
				sb.append(payLoad);
			}

			String finalLog = logUtil.maskLog(sb.toString());
			e2eTxn.logMessage("2984", finalLog);
			log.info(finalLog);
			e2eTxn.endInboundCall();
			BptmContextHolder.clearContext();

		}
	}

	private String getPayload(final HttpServletResponse response) {
		String responseBody = null;
		if (response instanceof ContentCachingResponseWrapper) {
			final ContentCachingResponseWrapper res = (ContentCachingResponseWrapper) response;
			try {
				responseBody = IOUtils.toString(res.getContentInputStream(), response.getCharacterEncoding());
				res.copyBodyToResponse();
			} catch (IOException e) {
				StringBuilder sb = new StringBuilder();
				sb.append("Exception cathced in LoggingRequestInterceptor :: occured on:: ");
				StackTraceElement[] st = e.getStackTrace();
				sb.append(Arrays.toString(st));
				log.error(sb.toString());
			}
		}
		return responseBody;
	}

	private String getPayload(final HttpServletRequest request) throws IOException {
		if (request instanceof ResettableRequestServletWrapper) {
			final ResettableRequestServletWrapper resettableRequest = (ResettableRequestServletWrapper) request;
			final String requestBody = IOUtils.toString(resettableRequest.getReader());

			resettableRequest.resetInputStream();
			return requestBody.substring(0, Math.min(1024, requestBody.length())); // only log the first 1kB
		} else {
			return "[unknown]";
		}
	}
}
