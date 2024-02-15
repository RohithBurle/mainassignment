package com.bt.ms.im.config;



import com.bt.ms.im.util.RestClientLoggingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

  @Autowired RestClientLoggingInterceptor logInter;

  @Value("${connection.timeout:5}")
  private int connectTimeout;

  @Value("${read.timeout:10}")
  private int readTimeout;
  
  @Value("${downstream.apigw.cert_alias: NA}")
  private String alias;
  
  @Value("${app.identity_location: NA}")
  private String identityLocation;
  
  @Value("${app.trust_location: NA}")
  private String trustLocation;
  
  @Value("${app.keystore_password: NA}")
  private String keyStorePassword;
  
  @Value("${app.truststore_password: NA}")
  private String trustStorePassword;
  
  private static final String DEFAULT_PRPERTY_VALUE="NA";


  @Bean
  public RestTemplate msRestTemplate() throws Exception {
	  return genericTemplate(connectTimeout, readTimeout, alias);
  }
  
  public RestTemplate genericTemplate (int connectionTimeOut, int readTimeout, String alias) throws Exception{
	  Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
	    converter.setSupportDtd(true);
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getInterceptors().add(logInter);
	    HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory();
	    httpFactory.setConnectTimeout(connectionTimeOut * 1000);
	    httpFactory.setReadTimeout(readTimeout * 1000);
	    if(!identityLocation.trim().equals(DEFAULT_PRPERTY_VALUE) && !trustLocation.trim().equals(DEFAULT_PRPERTY_VALUE)) {
		SSLContext sslContext = SSLContextBuilder.create()
				.loadKeyMaterial(ResourceUtils.getFile(identityLocation), keyStorePassword.toCharArray(),
						keyStorePassword.toCharArray(), new PrivateKeyStrategy() {
							@Override
							public String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket) {
								return alias;
							}
						})
				.loadTrustMaterial(ResourceUtils.getFile(trustLocation), trustStorePassword.toCharArray()).build();
		HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
		httpFactory.setHttpClient(client);
	    }
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpFactory));
	    restTemplate.getMessageConverters().add(0, converter);
	    return restTemplate;
  }

  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
      ObjectMapper jacksonObjectMapper) {
    MappingJackson2HttpMessageConverter converter =
        new MappingJackson2HttpMessageConverter(jacksonObjectMapper);
    converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
    return converter;
  }
 
}
