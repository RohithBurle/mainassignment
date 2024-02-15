package com.bt.ms.im.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.http.HttpStatusCode;

import com.bt.ms.im.exception.StandardError;
import com.bt.ms.im.exception.handler.GenericBusinessExceptionDetails;
import com.bt.ms.im.exception.handler.GenericStandardExceptionDetails;
import com.bt.ms.im.exception.handler.businessexception.BusinessException;
import com.bt.ms.im.exception.handler.standardexception.BadRequestException;
import com.bt.ms.im.exception.handler.standardexception.ForbiddenException;
import com.bt.ms.im.exception.handler.standardexception.InternalServerException;
import com.bt.ms.im.exception.handler.standardexception.ResourceNotFoundException;
import com.bt.ms.im.exception.handler.standardexception.UnauthorizedException;
import com.bt.ms.im.util.AppConstantsUtil;
import com.bt.ms.im.util.BptmHelper;

/**
 * @author Suman Mandal
 *
 */
@RestControllerAdvice
public class ResponseExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

  private static final String INTERNAL_ERROR_MESSAGE = "Internal Server Error";
  private static final String INTERNAL_ERROR_CODE = "01";
  private static final String exceptionMessage = AppConstantsUtil.EXCEPTION_MESSAGE + " :: {}";

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @ExceptionHandler(InternalServerException.class)
  public final ResponseEntity<Object> handleInternalServerException(
      InternalServerException ex, WebRequest request) {
    log.error(exceptionMessage, Arrays.toString(ex.getStackTrace()));
    BptmHelper.logBPTMError(ex);
    GenericStandardExceptionDetails exception =
        new GenericStandardExceptionDetails(ex.getCode(), INTERNAL_ERROR_MESSAGE);

    return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BusinessException.class)
  public final ResponseEntity<Object> handleBusinessException(
      BusinessException ex, WebRequest request) {
    log.error(exceptionMessage, Arrays.toString(ex.getStackTrace()));
    BptmHelper.logBPTMError(ex);
    GenericBusinessExceptionDetails exception =
        new GenericBusinessExceptionDetails(
            ex.getErrorCode(), INTERNAL_ERROR_MESSAGE, ex.getRootException());

    return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public final ResponseEntity<Object> handleUnauthorizedException(
      UnauthorizedException ex, WebRequest request) {
    log.error(exceptionMessage, Arrays.toString(ex.getStackTrace()));
    BptmHelper.logBPTMError(ex);
    GenericStandardExceptionDetails exception =
        new GenericStandardExceptionDetails(ex.getCode(), ex.getErrorMessage());

    return new ResponseEntity<>(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(BadRequestException.class)
  public final ResponseEntity<Object> handleBadRequestException(
      BadRequestException ex, WebRequest request) {
    log.error(exceptionMessage, Arrays.toString(ex.getStackTrace()));
    BptmHelper.logBPTMError(ex);
    GenericStandardExceptionDetails exception =
        new GenericStandardExceptionDetails(ex.getCode(), ex.getErrorMessage());

    return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ForbiddenException.class)
  public final ResponseEntity<Object> handleForbiddenException(
      ForbiddenException ex, WebRequest request) {
    log.error(exceptionMessage, Arrays.toString(ex.getStackTrace()));
    BptmHelper.logBPTMError(ex);
    GenericStandardExceptionDetails exception =
        new GenericStandardExceptionDetails(ex.getCode(), ex.getErrorMessage());

    return new ResponseEntity<>(exception, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public final ResponseEntity<Object> handleResourceNotFoundException(
      ResourceNotFoundException ex, WebRequest request) {
    log.error(exceptionMessage, Arrays.toString(ex.getStackTrace()));
    BptmHelper.logBPTMError(ex);
    GenericStandardExceptionDetails exception =
        new GenericStandardExceptionDetails(ex.getCode(), ex.getErrorMessage());

    return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
    log.error(exceptionMessage, Arrays.toString(ex.getStackTrace()));
    BptmHelper.logBPTMError(ex);
    GenericStandardExceptionDetails exception =
        new GenericStandardExceptionDetails(INTERNAL_ERROR_CODE, INTERNAL_ERROR_MESSAGE);

    return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }



  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      @Nullable Object body,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error(exceptionMessage, Arrays.toString(ex.getStackTrace()));
    BptmHelper.logBPTMError(ex);
    if (ex instanceof HttpRequestMethodNotSupportedException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR405_61);
    } else if (ex instanceof HttpMediaTypeNotSupportedException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR415_68);
    } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR415_68);
    } else if (ex instanceof MissingPathVariableException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_20);
    } else if (ex instanceof MissingServletRequestParameterException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_29);
    } else if (ex instanceof ServletRequestBindingException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_29);
    } else if (ex instanceof ConversionNotSupportedException) {
      body = new GenericStandardExceptionDetails(INTERNAL_ERROR_CODE, INTERNAL_ERROR_MESSAGE);
    } else if (ex instanceof TypeMismatchException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_29);
    } else if (ex instanceof HttpMessageNotReadableException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_22);
    } else if (ex instanceof HttpMessageNotWritableException) {
      body = new GenericStandardExceptionDetails(INTERNAL_ERROR_CODE, INTERNAL_ERROR_MESSAGE);
    } else if (ex instanceof MethodArgumentNotValidException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_22);
    } else if (ex instanceof MissingServletRequestPartException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_29);
    } else if (ex instanceof BindException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_29);
    } else if (ex instanceof NoHandlerFoundException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR404_60);
    } else if (ex instanceof MethodArgumentTypeMismatchException) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_29);
 
    }
    if (body == null) {
      body = new GenericStandardExceptionDetails(StandardError.ERR400_29);
    }
    return new ResponseEntity<>(body, headers, status);
  }
}
