package com.bt.ms.im.bptm;

//import com.bt.util.logging.E2ETransaction;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.bt.util.logging.E2ETransaction;

/**
 * 
 * Holds the BPTM context
 * 
 * @author Suman Mandal
 * 
 */
public class BptmContextHolder {
	public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
	public static final String SYSTEM_PROPERTY = "app.bptm.strategy";
	private static String strategyName = System.getProperty(SYSTEM_PROPERTY);
	private static BptmContextHolderStrategy strategy;

	static {
		initialize();
	}

	/** Explicitly clears the context value from the current thread. */
	public static void clearContext() {
		strategy.clearContext();
	}

	/**
	 * Obtain the current <code>BptmContext</code>.
	 *
	 * @return the Bptm context (never <code>null</code>)
	 */
	public static BptmContext getContext() {
		return strategy.getContext();
	}

	public static E2ETransaction getE2ETxn() {
		return strategy.getContext().getE2eTxn();
	}

	public static String getServiceName() {
		return strategy.getContext().getServiceName();
	}

	public static String getAppName() {
		return strategy.getContext().getAppName();
	}

	private static void initialize() {
		if (!StringUtils.hasText(strategyName)) {
			strategyName = MODE_THREADLOCAL;
		}

		if (strategyName.equals(MODE_THREADLOCAL)) {
			strategy = new ThreadLocalBptmContextHolderStrategy();
		} else {
			try {
				Class<?> clazz = Class.forName(strategyName);
				Constructor<?> customStrategy = clazz.getConstructor();
				strategy = (BptmContextHolderStrategy) customStrategy.newInstance();
			} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException
					| InstantiationException ex) {
				ReflectionUtils.handleReflectionException(ex);
			}
		}
	}

	/**
	 * Associates a new <code>BptmContext</code> with the current thread of
	 * execution.
	 *
	 * @param context the new <code>BptmContext</code> (may not be
	 *                <code>null</code>)
	 */
	public static void setContext(BptmContext context) {
		strategy.setContext(context);
	}

	/**
	 * Changes the preferred strategy. Do <em>NOT</em> call this method more than
	 * once for a given JVM, as it will re-initialize the strategy and adversely
	 * affect any existing threads using the old strategy.
	 *
	 * @param strategyName the fully qualified class name of the strategy that
	 *                     should be used.
	 */
	public static void setStrategyName(String strategyName) {
		BptmContextHolder.strategyName = strategyName;
		initialize();
	}

	/**
	 * Allows retrieval of the context strategy. See SEC-1188.
	 *
	 * @return the configured strategy for storing the security context.
	 */
	public static BptmContextHolderStrategy getContextHolderStrategy() {
		return strategy;
	}

	/**
	 * Delegates the creation of a new, empty context to the configured strategy.
	 */
	public static BptmContext createContext(String serviceName, String appName) {
		return strategy.createContext(serviceName, appName);
	}

	/**
	 * Delegates the creation of a new, empty context to the configured strategy.
	 */
	public static BptmContext createContext(String serviceName, String appName, String e2eData) {
		return strategy.createContext(serviceName, appName, e2eData);
	}

}
