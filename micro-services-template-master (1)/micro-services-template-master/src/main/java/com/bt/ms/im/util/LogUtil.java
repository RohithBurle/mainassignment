package com.bt.ms.im.util;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("log")
@Component
@Getter
@Setter
public class LogUtil {

	private List<String> maskConfigList;

	public String maskLog(String log) {
		String maskLog = (null != log) ? log.replaceAll("\r\n|[\r\n]", " ") : log;
		if (maskConfigList != null && !maskConfigList.isEmpty() && null != maskLog) {
			for (String maskConfig : maskConfigList) {
				String[] maskConfArr = maskConfig.split(",");
				if (maskConfArr.length > 1 && maskConfArr[0].length() > 0 && maskConfArr[1].length() > 0) {
					StringBuilder sb = new StringBuilder();
					sb.append("(");
					sb.append(maskConfArr[0]);
					sb.append(")");
					sb.append("([^");
					sb.append(maskConfArr[1].charAt(0));
					sb.append("]*)");
					sb.append("(");
					sb.append(maskConfArr[1]);
					sb.append(")");
					maskLog = maskLog.replaceAll(sb.toString(), "$1***$3");
				}
			}

		}
		return maskLog;

	}

}