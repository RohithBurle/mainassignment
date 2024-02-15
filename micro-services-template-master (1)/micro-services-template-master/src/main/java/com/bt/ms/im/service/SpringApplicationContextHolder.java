package com.bt.ms.im.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


@Service
public class SpringApplicationContextHolder implements ApplicationContextAware {

  private static ApplicationContext applicationContext = null;

  public static <T> T getBean(Class<T> beanClass) {
    return applicationContext.getBean(beanClass);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
	  SpringApplicationContextHolder.applicationContext = applicationContext;
  }
}
