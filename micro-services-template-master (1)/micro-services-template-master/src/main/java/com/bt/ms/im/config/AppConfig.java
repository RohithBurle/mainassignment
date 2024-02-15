package com.bt.ms.im.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bt.ms.im.util.LoggingRequestInterceptor;

/**
 * adding logging interceptor
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {
  @Autowired LoggingRequestInterceptor loggingInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loggingInterceptor);
  }
}
