package com.bt.ms.im.aop;

import com.bt.ms.im.annotation.ClientInfo;
import com.bt.ms.im.bptm.BptmContextHolder;
import com.bt.util.logging.E2ETransaction;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Logging is done using this aspect , specially the BPTM log in the controller
 * and repo(while doing any client call)
 *
 *@author Suman Mandal
 *
 */
@Aspect
@Component
@Order(1)
public class LoggingAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}

	@Pointcut(" within(@org.springframework.web.bind.annotation.RestController *) &&  within(com.bt.ms.im..*)")
	public void controllerPointcut() {
	}

	public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		try {
			Object result = joinPoint.proceed();
			log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), result);
			return result;
		} catch (IllegalArgumentException e) {
			log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

			throw e;
		}
	}

	@Pointcut("within(@org.springframework.stereotype.Repository *) &&  within(com.bt.ms.im..*)")
	public void applicationRepositoryPackagePointcut() {

	}

	@Around("applicationRepositoryPackagePointcut() ")
	public Object logAroundRepository(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		ClientInfo client = method.getAnnotation(ClientInfo.class);

		E2ETransaction masterE2ETx = BptmContextHolder.getE2ETxn();
		if (masterE2ETx != null) {

			if (client != null) {
				masterE2ETx.startOutboundCall(client.clientSystem(), client.reqType(), client.compTxnName());
			} else {
				masterE2ETx.startOutboundCall("SOME_CLIENT", "HTTP", "login");
			}
		}

		try {
			return joinPoint.proceed();
		} catch (IllegalArgumentException e) {
			log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

			throw e;
		}
	}
}
