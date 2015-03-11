package com.jeans.tinyitsm.util;

import org.apache.logging.log4j.LogManager;

public class LoggerUtil {

	public static void info(Object message) {
		LogManager.getLogger().info(message);
	}

	public static void error(Throwable e) {
		LogManager.getLogger().error(e.getMessage() + ". 起因: " + e.getCause());
	}
}
