package com.bt.ms.im.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.bt.ms.im.util.ContextCopyingDecorator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;

/**
 * Defination of ThreadPoolTaskExecutor , that will be used in the async call
 *
 * @author Suman Mandal
 */
@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

	@Value("${async.pool.threadPrefix:MS-}")
	private String threadNamePrefix;
	
	@Autowired
	private BeanFactory beanFactory;
	
  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix(threadNamePrefix);
    executor.setKeepAliveSeconds(5);
    // to get the request context while async call
    executor.setTaskDecorator(new ContextCopyingDecorator());
    executor.initialize();
    return  new LazyTraceExecutor(beanFactory, executor);
  }


}
