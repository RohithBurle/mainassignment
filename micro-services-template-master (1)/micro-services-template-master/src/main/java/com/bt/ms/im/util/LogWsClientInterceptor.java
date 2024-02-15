package com.bt.ms.im.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpComponentsConnection;

import com.bt.ms.im.bptm.BptmContextHolder;

/**
 * used for logging the downstream call using ws client
 * 
 * @author Suman Mandal
 * 
 */
@Component
public class LogWsClientInterceptor implements ClientInterceptor {
	
	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	 
	@Autowired
	LogUtil logUtil;

  @Override
  public void afterCompletion(MessageContext arg0, Exception arg1)
      throws WebServiceClientException {
  }

  @Override
  public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		responseLogging(messageContext);
		return true;
  }

  @Override
  public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {

    try {
      StringBuilder sb = new StringBuilder();
      TransportContext context = TransportContextHolder.getTransportContext();
      HttpComponentsConnection connection = (HttpComponentsConnection) context.getConnection();
      try {
        sb.append("2982 URI :: ").append(connection.getUri());
        sb.append(" Method :: POST ");
        Header[] httpHeaders = connection.getHttpPost().getAllHeaders();
        sb.append(" Headers :: ");
        for (Header header : httpHeaders) {
          sb.append(header.getName()).append("=").append(header.getValue()).append(" ");
        }
      } catch (URISyntaxException e) {
				logException(sb, e);
      }

      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      messageContext.getRequest().writeTo(buffer);
      messageContext.getPropertyNames();
      sb.append(" Request body  :: ")
          .append(buffer.toString(java.nio.charset.StandardCharsets.UTF_8.name()));
      String finalLog =logUtil.maskLog(sb.toString());
      BptmContextHolder.getE2ETxn().logMessage("2982", finalLog);
      log.info( finalLog);

    } catch (IOException e) {
      throw new WebServiceIOException("Can not write the SOAP request into the out stream", e);
    }

    return true;
  }

  @Override
  public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		responseLogging(messageContext);
		return true;
  }
  
  protected void  responseLogging(MessageContext messageContext) {
	  try {
	      StringBuilder sb = new StringBuilder();
	      TransportContext context = TransportContextHolder.getTransportContext();
	      HttpComponentsConnection connection = (HttpComponentsConnection) context.getConnection();
	      try {
	        sb.append("2983 URI :: ").append(connection.getUri());
	        sb.append(" Method :: POST ");
	        Header[] httpHeaders = connection.getHttpPost().getAllHeaders();
	        sb.append(" Headers :: ");
	        for (Header header : httpHeaders) {
	          sb.append(header.getName()).append("=").append(header.getValue()).append(" ");
	        }
	      } catch (URISyntaxException e) {
	    	  logException(sb, e);
	      }
	      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	      messageContext.getResponse().writeTo(buffer);
	      sb.append(" Request body  :: ")
	          .append(buffer.toString(java.nio.charset.StandardCharsets.UTF_8.name()).replaceAll("\r\n|[\r\n]", " "));
	      String finalLog =logUtil.maskLog(sb.toString());
	      BptmContextHolder.getE2ETxn().logMessage("2983",finalLog);
	      log.info(finalLog);
	    } catch (IOException e) {
	      throw new WebServiceIOException("Can not write the SOAP response into the out stream", e);
	    }
  }
  
  private void logException(StringBuilder sb, URISyntaxException e) {
		sb.append("Exception cathced in LogWsClientInterceptor :: occured on:: ");
		StackTraceElement[] st = e.getStackTrace();
		sb.append(Arrays.toString(st));
		log.error(sb.toString());
	}
}
