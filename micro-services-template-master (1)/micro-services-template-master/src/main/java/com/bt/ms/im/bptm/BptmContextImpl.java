package com.bt.ms.im.bptm;

import com.bt.util.logging.E2ETransaction;

/**
 * Base implementation of {@link BptmContext}.
 *
 * <p>
 * Used by default by {@link BptmContextHolder} strategies.
 * 
 * @author Suman Mandal
 *
 */
public class BptmContextImpl implements BptmContext {

	private static final long serialVersionUID = 5241837153332715280L;

	private E2ETransaction e2eTxn;

	private String serviceName;

	private String appName;

	public BptmContextImpl() {
	}

	public BptmContextImpl(E2ETransaction e2eTxn, String serviceName, String appName) {
		this.e2eTxn = e2eTxn;
		this.serviceName = serviceName;
		this.appName = appName;
	}

	@Override
	public E2ETransaction getE2eTxn() {
		return e2eTxn;
	}

	@Override
	public void setE2eTxn(E2ETransaction e2eTxn) {
		this.e2eTxn = e2eTxn;
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String getAppName() {
		return this.appName;
	}

	@Override
	public void setAppName(String appName) {
		this.appName = appName;
	}
}
