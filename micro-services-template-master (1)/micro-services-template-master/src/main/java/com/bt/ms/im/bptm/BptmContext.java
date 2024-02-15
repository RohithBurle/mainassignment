package com.bt.ms.im.bptm;

import com.bt.util.logging.E2ETransaction;
import java.io.Serializable;

/** @author Suman Mandal */
public interface BptmContext extends Serializable {

  E2ETransaction getE2eTxn();

  void setE2eTxn(E2ETransaction e2eTxn);

  String getServiceName();

  void setServiceName(String serviceName);

  String getAppName();

  void setAppName(String appName);
}
