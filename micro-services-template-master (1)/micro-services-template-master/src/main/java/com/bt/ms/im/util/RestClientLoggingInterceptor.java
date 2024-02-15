package com.bt.ms.im.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import com.bt.ms.im.bptm.BptmContextHolder;
import com.bt.util.logging.E2ETransaction;
import com.bt.ms.im.util.LogUtil;

/**
 * used for logging the downstream call using restTemplate
 *@author Suman Mandal
 *
 */
@Component
public class RestClientLoggingInterceptor implements ClientHttpRequestInterceptor {

	@Autowired
	LogUtil logUtil;
	
	@Value("${app.e2eData.headerKey:E2EDATA}")
	private String e2eDataHeader;
	
	@Value("${app.e2eData.fromValue:MS}")
	private String e2eDataFrom;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    logRequest(request, body);
    ClientHttpResponse response = execution.execute(request, body);
    logResponse(response);
    return response;
  }

	private void logRequest(HttpRequest request, byte[] body) {
		request.getHeaders().set(e2eDataHeader, BptmContextHolder.getE2ETxn().toString());
		request.getHeaders().set(AppConstantsUtil.E2EDATA_FROM, e2eDataFrom);
		StringBuilder sb = new StringBuilder();
		sb.append("2982 URI ::").append(request.getURI());
		sb.append(" Method ::").append(request.getMethod());
		sb.append(" Headers ::").append(request.getHeaders());
		sb.append(" Request body ::").append(new String(body, StandardCharsets.UTF_8));
		String finalLog = logUtil.maskLog(sb.toString());
		E2ETransaction e2eTxn = BptmContextHolder.getE2ETxn();
		if (e2eTxn != null) {
			e2eTxn.logMessage("2982", finalLog);
		}
		log.info(finalLog);
	}

  private void logResponse(ClientHttpResponse response) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("2983 Response :").append(response.getStatusCode());
    sb.append(" Status text :").append(response.getStatusText());
    sb.append(" Headers :").append(response.getHeaders());
    sb.append(" Response body  :: ")
        .append(StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()).replaceAll("\r\n|[\r\n]", " "));
    String finalLog =logUtil.maskLog(sb.toString());
    log.info(finalLog);
    E2ETransaction e2eTxn  =  BptmContextHolder.getE2ETxn();
    if( e2eTxn != null ) {      	
    	e2eTxn.logMessage("2983", finalLog);

    	String e2eNewData = BptmContextHolder.getE2ETxn().toString() ; 
    	List<String> headervalueList = response.getHeaders().get(e2eDataHeader);
    	if(!CollectionUtils.isEmpty(headervalueList)) {
    		e2eNewData = headervalueList.get(0);
    	}
    	e2eTxn.endOutboundCall(e2eNewData);
    }
  }
}
