package com.bt.ms.im.bptm;

import com.bt.ms.im.util.BptmHelper;
import com.bt.util.logging.E2ETransaction;
import java.util.UUID;
import org.springframework.util.Assert;

/**
 * A <code>ThreadLocal</code>-based implementation of
 * {@link BptmContextHolderStrategy}.
 * 
 * @see java.lang.ThreadLocal
 * 
 * @author Suman Mandal
 * 
 */
final class ThreadLocalBptmContextHolderStrategy implements BptmContextHolderStrategy {

	private static final ThreadLocal<BptmContext> contextHolder = new ThreadLocal<>();

	@Override
	public void clearContext() {
		contextHolder.remove();
	}

	@Override
	public BptmContext getContext() {
		BptmContext ctx = contextHolder.get();

		if (ctx == null) {
			ctx = createContext("DEFAULT", "MICRO-SERVICES-TEMPLATE");
			contextHolder.set(ctx);
		}
		Assert.notNull(ctx, "Only non-null BptmContext instances are permitted");
		return ctx;
	}

	@Override
	public void setContext(BptmContext context) {
		Assert.notNull(context, "Only non-null BptmContext instances are permitted");
		contextHolder.set(context);
	}

	@Override
	public BptmContext createContext(String serviceName, String appName) {
		serviceName = serviceName.toUpperCase();
		appName = appName.toUpperCase();
		UUID uuid = UUID.randomUUID();
		int transactionId = uuid.variant();
		StringBuilder sb = new StringBuilder();
		sb.append("busProcOriginator=").append(appName);
		sb.append(",busProcType=MNP,busProcID=").append(transactionId);
		sb.append(",busTxnType=").append(serviceName);

		E2ETransaction e2eTxn = new E2ETransaction(sb.toString(), appName, serviceName + "-HTTP-Access",
				BptmHelper.getBptmKey());
		BptmContext context = new BptmContextImpl(e2eTxn, serviceName, appName);
		setContext(context);
		return context;
	}

	@Override
	public BptmContext createContext(String serviceName, String appName, String e2eData) {
		serviceName = serviceName.toUpperCase();
		appName = appName.toUpperCase();
		E2ETransaction e2eTxn = new E2ETransaction(e2eData, appName, serviceName + "-HTTP-Access",
				BptmHelper.getBptmKey());
		BptmContext context = new BptmContextImpl(e2eTxn, serviceName, appName);
		setContext(context);
		return context;
	}
}
