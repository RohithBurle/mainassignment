/** */
package com.bt.ms.im.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Application wise constant , will fetch the value from constants.properties
 * file
 *
 */
@Configuration
@PropertySource("classpath:constants.properties")
@PropertySource(value = "file:./config/constants.properties", ignoreResourceNotFound=true)
@ConfigurationProperties(prefix = "app")
public class AppConstants {

	public static class ErrorRes {
		private String message;
		private String internalErrorCode;
		private String clientErrorCode;
		private String validationfailedErrorCode;
		private String clientUnavErrorCode;
		private String tempUnavMessage;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getInternalErrorCode() {
			return internalErrorCode;
		}

		public void setInternalErrorCode(String internalErrorCode) {
			this.internalErrorCode = internalErrorCode;
		}

		public String getClientErrorCode() {
			return clientErrorCode;
		}

		public void setClientErrorCode(String clientErrorCode) {
			this.clientErrorCode = clientErrorCode;
		}

		public String getValidationfailedErrorCode() {
			return validationfailedErrorCode;
		}

		public void setValidationfailedErrorCode(String validationfailedErrorCode) {
			this.validationfailedErrorCode = validationfailedErrorCode;
		}

		public String getClientUnavErrorCode() {
			return clientUnavErrorCode;
		}

		public void setClientUnavErrorCode(String clientUnavErrorCode) {
			this.clientUnavErrorCode = clientUnavErrorCode;
		}

		public String getTempUnavMessage() {
			return tempUnavMessage;
		}

		public void setTempUnavMessage(String tempUnavMessage) {
			this.tempUnavMessage = tempUnavMessage;
		}

	}

	public static class Header {
		private String accept;

		public String getAccept() {
			return accept;
		}

		public void setAccept(String accept) {
			this.accept = accept;
		}
	}

	private ErrorRes errorRes;
	private Header header;
	private String strTrue;
	private String strFalse;

	public String getStrTrue() {
		return strTrue;
	}

	public void setStrTrue(String strTrue) {
		this.strTrue = strTrue;
	}

	public String getStrFalse() {
		return strFalse;
	}

	public void setStrFalse(String strFalse) {
		this.strFalse = strFalse;
	}

	public ErrorRes getErrorRes() {
		return errorRes;
	}

	public void setErrorRes(ErrorRes errorRes) {
		this.errorRes = errorRes;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

}
