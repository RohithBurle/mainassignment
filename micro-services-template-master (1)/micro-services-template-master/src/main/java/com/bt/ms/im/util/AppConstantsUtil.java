package com.bt.ms.im.util;


public class AppConstantsUtil {

	  	public static final String E2EDATA_HEADER = "E2EDATA";
		public static final String E2EDATA_TO = "To";
		public static final String E2EDATA_FROM = "From";
		public static final String EXCEPTION_MSG = "Exception catched in repo aspect :: occured on::";
		public static final String ARGS = " :: Arguments :: ";
		public static final String EXCEPTION_MESSAGE = "Exception occured and catched in ResponseExceptionHandlerAdvice";
		public static final String HEADERS = " Headers :: ";
		public static final String METHOD_POST = " Method :: POST ";
		public static final String URI = "URI :: ";
		public static final String REQUEST_BODY = " Request body  :: ";
		public static final String SOAP_ERROR = "Can not write the SOAP response into the out stream";
	  
	  private AppConstantsUtil() {
	    throw new IllegalStateException("Utility class");
	  }
	}