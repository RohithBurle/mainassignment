package com.bt.ms.im.util;

import com.bt.ms.im.bptm.BptmContextHolder;
import com.bt.util.logging.E2ETransaction;
import com.bt.util.logging.LoggingFramework;
import com.bt.util.logging.LoggingFrameworkProperties;
import com.bt.util.logging.MessagesBundle;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class BptmHelper {

	private static volatile MessagesBundle mb = null;

	/** @return MessageBundle and maintain singleton pattern */
	public static MessagesBundle getBptmKey() {
		if (mb == null) {
			synchronized (MessagesBundle.class) {
				if (mb == null) {
					Class<BptmHelper> clazz = BptmHelper.class;
					InputStream inputStream = clazz.getResourceAsStream("/logHandling.properties");
					try {
						System.out.println("Container Name :: " + InetAddress.getLocalHost().getHostName());
						LoggingFrameworkProperties.putProgrammatic("appServerName",
								InetAddress.getLocalHost().getHostName());
					} catch (UnknownHostException e) {
						LoggingFrameworkProperties.putProgrammatic("appServerName", "DefaultServer");
					}

					LoggingFramework.initialise(inputStream);
					MessagesBundle mbobj = new MessagesBundle("LogMessage");
					mb = mbobj;
				}
			}
		}
		return mb;
	}

	public static void logBPTMError(Exception ex) {
		E2ETransaction masterE2ETx = (E2ETransaction) BptmContextHolder.getContext();
		StringWriter sw = new StringWriter();
		sw.append(" Exception occured :: ");
		ex.printStackTrace(new PrintWriter(sw));
		masterE2ETx.logMessage("004", sw.toString());
	}
}
