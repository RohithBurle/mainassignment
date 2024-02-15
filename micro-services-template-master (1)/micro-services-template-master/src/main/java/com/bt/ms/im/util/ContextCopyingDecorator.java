/** */
package com.bt.ms.im.util;

import com.bt.ms.im.bptm.BptmContextHolder;
import com.bt.util.logging.E2ETransaction;
import org.springframework.core.task.TaskDecorator;

/**
 * Copy the context (Request bean and other ) to the async thread We are only
 * sharing e2eTransactionBean , If any other bean is required , we have to add
 * here.
 * 
 * @author Suman Mandal
 * 
 */
public class ContextCopyingDecorator implements TaskDecorator {
	@Override
	public Runnable decorate(Runnable runnable) {
	//	E2ETransaction e2eTxn = BptmContextHolder.getE2ETxn();
		String serviceName = BptmContextHolder.getServiceName();
		String appName = BptmContextHolder.getAppName();
		//String e2eStr = e2eTxn.toString();
		return () -> {
			try {
				BptmContextHolder.createContext(serviceName, appName, e2eStr);
				runnable.run();
			} finally {
				BptmContextHolder.clearContext();
			}
		};
	}
}
