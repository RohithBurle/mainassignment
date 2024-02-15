package com.bt.ms.im.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** @author Suman Mandal */
public class BaseHttpRequest {

  private Map<String, Object> headers = new HashMap<>();

  public void setHeader(String key, Object value) {
    headers.put(key, value);
  }

  public Optional<Object> getHeaderOptional(String key) {
    return Optional.ofNullable(headers.get(key));
  }

  public Object getHeader(String key) {
    return Optional.ofNullable(headers.get(key));
  }
}
