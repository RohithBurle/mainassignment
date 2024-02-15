/** */
package com.bt.ms.im.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will contain downstream information .
 *
 * <p>Which system is called and what method
 * 
 * @author Suman Mandal
 * 
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientInfo {
  String clientSystem();

  String reqType() default "REQ";

  String compTxnName();
}
