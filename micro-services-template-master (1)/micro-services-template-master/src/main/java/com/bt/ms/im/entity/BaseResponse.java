package com.bt.ms.im.entity;

import java.util.List;

/**
 * Base response class
 *
 * @author Suman Mandal
 */
public class BaseResponse {

  private String code;
  private String message;
  private List<RootExceptionDetails> rootException;

  public static BaseResponse errRes(String code, String message) {
    BaseResponse baseRes = new BaseResponse();
    baseRes.setCode(code);
    baseRes.setMessage(message);
    return baseRes;
  }

  public static BaseResponse errRes(
      String code, String message, List<RootExceptionDetails> rootException) {
    BaseResponse baseRes = new BaseResponse();
    baseRes.setCode(code);
    baseRes.setMessage(message);
    baseRes.setRootException(rootException);
    return baseRes;
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
  public List<RootExceptionDetails> getRootException() {
    return rootException;
  }
  /** @param rootException the rootException to set */
  public void setRootException(List<RootExceptionDetails> rootException) {
    this.rootException = rootException;
  }
}
