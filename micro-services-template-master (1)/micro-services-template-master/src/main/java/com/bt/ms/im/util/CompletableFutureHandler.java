package com.bt.ms.im.util;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bt.ms.im.entity.ResponseBean;

public class CompletableFutureHandler {

	private static final Logger log = LoggerFactory.getLogger(CompletableFutureHandler.class);

	/**
	 * Its a safe getter for CompletableFuture If any exception thrown , catch it
	 * and return error response
	 *
	 *@author Suman Mandal
	 *
	 * @param rb
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ResponseBean<T> safeGet(CompletableFuture<ResponseBean<T>> rb) {
		ResponseBean<T> response = null;
		ResponseBean<T> defalutResponse = new ResponseBean<>();
		defalutResponse.setStatus(ResponseBean.ResponseStatus.FAILURE);
		defalutResponse.setCode("01");
		try {
			response = rb.getNow(defalutResponse);
			return response;
		} catch (Exception ex) {
			log.error("Exception thrown in CompletableFutureHandler.safeGet :: {}",
					Arrays.toString(ex.getStackTrace()));
			return ResponseBean.errorRes();
		}
	}
}
