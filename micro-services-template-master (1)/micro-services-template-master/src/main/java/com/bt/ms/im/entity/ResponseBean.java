package com.bt.ms.im.entity;

import com.bt.ms.im.config.AppConstants;
import com.bt.ms.im.service.SpringApplicationContextHolder;

import java.util.ArrayList;
import java.util.List;

/*
 * @param <T>
 * 
 * @author Suman Mandal
 */
public class ResponseBean<T> {

	private T data;
	private ResponseStatus status;
	private String code;
	private String message;
	private List<RootExceptionDetails> rootExceptions;

	public enum ResponseStatus {
		SUCCESS, FAILURE, PER_FAIL
	}

	public ResponseBean() {
	}

	public ResponseBean(T data) {
		super();
		this.data = data;
	}

	/**
	 * This method will create a success response and . set the data there
	 *
	 * @param t
	 * @return
	 */
	public static <T> ResponseBean<T> of(T t) {
		ResponseBean<T> rb = new ResponseBean<>(t);
		rb.setStatus(ResponseStatus.SUCCESS);
		return rb;
	}

	/**
	 * This method will return error response
	 *
	 * @param t
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseBean errorRes(ResponseBean resBean) {
		ResponseBean rb = new ResponseBean();
		rb.setStatus(resBean.getStatus());
		rb.setCode(resBean.getCode());
		rb.setMessage(resBean.getMessage());
		rb.setRootException(resBean.getRootExceptions());
		return rb;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> ResponseBean<T> errorRes(ResponseBean resBean, Class T) {
		ResponseBean<T> rb = new ResponseBean<>();
		rb.setStatus(resBean.getStatus());
		rb.setCode(resBean.getCode());
		rb.setMessage(resBean.getMessage());
		rb.setRootException(resBean.getRootExceptions());
		return rb;
	}

	@SuppressWarnings({ "rawtypes" })
	public static ResponseBean errorRes() {
		return errorRes(getAppConstant().getErrorRes().getClientErrorCode(),
				getAppConstant().getErrorRes().getMessage());
	}

	@SuppressWarnings({ "rawtypes" })
	public static ResponseBean errorRes(String code, String message) {
		return errorRes(code, message, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseBean errorRes(String code, String message, RootExceptionDetails rootException) {
		ResponseBean rb = new ResponseBean();
		rb.setStatus(ResponseStatus.FAILURE);
		rb.setCode(code);
		rb.setMessage(message);
		if (rootException != null) {
			List<RootExceptionDetails> list = new ArrayList<>();
			list.add(rootException);
			rb.setRootException(list);
		}
		return rb;
	}

	public static <T> ResponseBean<T> errorRes(@SuppressWarnings("rawtypes") Class T, String code, String message,
			RootExceptionDetails rootException) {
		ResponseBean<T> rb = new ResponseBean<>();
		rb.setStatus(ResponseStatus.FAILURE);
		rb.setCode(code);
		rb.setMessage(message);
		if (rootException != null) {
			List<RootExceptionDetails> list = new ArrayList<>();
			list.add(rootException);
			rb.setRootException(list);
		}
		return rb;
	}

	public boolean isFailed() {
		if (ResponseStatus.FAILURE.equals(this.status)) {
			return true;
		}
		return false;
	}

	public boolean isSuccess() {
		if (ResponseStatus.SUCCESS.equals(this.status)) {
			return true;
		}
		return false;
	}

	public boolean isPertialfail() {
		if (ResponseStatus.PER_FAIL.equals(this.status)) {
			return true;
		}
		return false;
	}

	/** @return the data */
	public T getData() {
		return data;
	}

	/** @param data the data to set */
	public void setData(T data) {
		this.data = data;
	}

	/** @return the status */
	public ResponseStatus getStatus() {
		return status;
	}

	/** @param status the status to set */
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	/** @return the code */
	public String getCode() {
		return code;
	}

	/** @param code the code to set */
	public void setCode(String code) {
		this.code = code;
	}

	/** @return the message */
	public String getMessage() {
		return message;
	}

	/** @param message the message to set */
	public void setMessage(String message) {
		this.message = message;
	}

	/** @return the rootException */
	public List<RootExceptionDetails> getRootExceptions() {
		return rootExceptions;
	}

	/** @param rootException the rootException to set */
	public void setRootException(List<RootExceptionDetails> rootExceptions) {
		this.rootExceptions = rootExceptions;
	}

	public static AppConstants getAppConstant() {
		return SpringApplicationContextHolder.getBean(AppConstants.class);
	}
}
