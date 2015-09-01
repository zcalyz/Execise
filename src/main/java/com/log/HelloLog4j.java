package com.log;

import org.apache.log4j.Logger;

public class HelloLog4j {
	private static Logger  logger = Logger.getLogger(HelloLog4j.class.getName());
	
	public static void main(String[] args) {
		logger.debug("This is a log");
		logger.info("info log");
		logger.error("error log");
		logger.warn("warn log");
	}
}
