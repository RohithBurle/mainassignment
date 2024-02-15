package com.bt.ms.im.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bt.ms.im.annotation.ClientInfo;
import com.bt.ms.im.baseclientexception.BaseClientException;
import com.bt.ms.im.config.AppConstants;
import com.bt.ms.im.entity.ResponseBean;
import com.bt.ms.im.entity.RootExceptionDetails;
import com.bt.ms.im.util.BptmHelper;

/**
 * This aspect will catch any exception thrown while downstream call and return
 * a proper response
 *
 * <p>
 * Asumption
 *
 * <p>
 * downstream call will happen from the @Repository class normal call return
 * type will be ResponseBean<T> async call return type will be
 * CompletableFuture<ResponseBean<T>>
 * 
 * @author Suman Mandal
 * 
 */
@Aspect
@Component
@Order(2)
public class RepoExceptionHandlerAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AppConstants appConstants;

	@Pointcut("within(@org.springframework.stereotype.Repository *) &&  within(com.bt.ms.im..*)")
	public void applicationRepositoryClassPointcut() {
	}

	@Around("applicationRepositoryClassPointcut() ")
	public Object catchExceptionAroundRepositoryCommon(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		ClientInfo client = method.getAnnotation(ClientInfo.class);
		try {
			return joinPoint.proceed();
		} catch (IllegalArgumentException e) {
			log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
			throw e;
		} catch (BaseClientException ex) {
			BptmHelper.logBPTMError(ex);
			log.error("Exception cached in repo aspect :: occurred on :: {} :: Arguments :: {} Exception Details :: {} {}",
					joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), ex.toString(),
					Arrays.toString(ex.getStackTrace()));
			return ResponseBean.errorRes(this.appConstants.getErrorRes().getClientErrorCode(),
					this.appConstants.getErrorRes().getMessage(),
					RootExceptionDetails.of(client.clientSystem(), ex.getReasonCode(), ex.getReasonText()));
		}
//   required if soap call are there
//		catch (SoapMessageCreationException | WebServiceIOException ex) {
//			BptmHelper.logBPTMError(ex);
//			log.error("Exception cached in repo aspect :: occurred on :: {} :: Arguments :: {} Exception Details :: {} {}",
//		joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), ex.toString(),
//		Arrays.toString(ex.getStackTrace()));
//			return ResponseBean.errorRes(this.appConstants.getErrorRes().getClientErrorCode(),
//					this.appConstants.getErrorRes().getMessage(),
//					RootExceptionDetails.of(client.clientSystem(),
//							this.appConstants.getErrorRes().getClientUnavErrorCode(),
//							this.appConstants.getErrorRes().getTempUnavMessage()));
//		} 
		catch (Exception ex) {
			BptmHelper.logBPTMError(ex);
			log.error("Exception cached in repo aspect :: occurred on :: {} :: Arguments :: {} Exception Details :: {} {}",
					joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), ex.toString(),
					Arrays.toString(ex.getStackTrace()));
			return ResponseBean.errorRes(this.appConstants.getErrorRes().getClientErrorCode(),
					this.appConstants.getErrorRes().getMessage(),
					RootExceptionDetails.of(client.clientSystem(),
							this.appConstants.getErrorRes().getClientUnavErrorCode(),
							this.appConstants.getErrorRes().getTempUnavMessage()));
		}
	}

}
