package com.getEnv;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnvUtil {
	static String zookeeperUrl;

	static String brokersUrl;

	static {
		// get all env
		Map<String, String> envMap = System.getenv();
		Set<Entry<String, String>> envSet = envMap.entrySet();

		String vcapEnv = null;
		for (Entry<String, String> entry : envSet) {
			if (entry.getKey().equals("VCAP_SERVICES")) {
				vcapEnv = entry.getValue();
			}
		}

		Pattern zookeeperPattern = Pattern.compile("zookeeper\":\"(\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+)\"");
		Matcher matcher = zookeeperPattern.matcher(vcapEnv);
		if (matcher.find()) {
			// set zookeeper url
			String zookeeperUrl = matcher.group(1);
		}

		Pattern brokersPattern = Pattern.compile("brokers\":\"(\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+)\"");
		matcher = brokersPattern.matcher(vcapEnv);
		if (matcher.find()) {
			// set kafka broker url
			String brokersUrl = matcher.group(1);
		}
	}

	public static String getZookeeperUrl() {
		return zookeeperUrl;
	}

	public static String getKafkaBrokerUrl() {
		return brokersUrl;
	}

}
