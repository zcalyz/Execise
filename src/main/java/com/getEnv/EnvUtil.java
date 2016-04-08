package com.getEnv;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class EnvUtil {
	static String zookeeperUrl;

	static String brokersUrl;

	static {
		// get all environment variables
		Map<String, String> envMap = System.getenv();
		Set<Entry<String, String>> envSet = envMap.entrySet();
		//get kafka env
		String vcapEnv = null;
		for (Entry<String, String> entry : envSet) {
			if (entry.getKey().equals("VCAP_SERVICES")) {
				vcapEnv = entry.getValue();
			}
		}
		// set zookeeper url
		String zookeeperUrl = getValueByKey(vcapEnv, "zookeeper");
		// set kafka broker url
		String brokers = getValueByKey(vcapEnv, "brokers");
	}

	private static String getValueByKey(String jsonStr, String key) {
		Pattern pattern = Pattern.compile(key + "\":\"(.*?)\"");
		Matcher matcher = pattern.matcher(jsonStr);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	public static String getZookeeperUrl() {
		return zookeeperUrl;
	}

	public static String getKafkaBrokerUrl() {
		return brokersUrl;
	}

}
