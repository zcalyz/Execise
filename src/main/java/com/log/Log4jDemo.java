package com.log;

import org.apache.log4j.Logger;

public class Log4jDemo {
	public static Logger log = Logger.getLogger(Log4jDemo.class);
	
	public static void main(String[] args) {
		log.warn("warn");
		log.debug("debug");
		log.info("info");
//		#[%d{yy/MM/dd HH:mm:ss:SSS}]
	}
}
