package com.stackTrace.v2;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Chenzhai
 *
 */
public class ThreadTransactionRelation {
	
	private static final Map<String, String> relations= new HashMap<String, String>();
	
	public static String agentId = null;
	
	public static void addElement(String threadName, String transactionId, String agentId){
		relations.put(threadName, transactionId);
		if(ThreadTransactionRelation.agentId == null){
			ThreadTransactionRelation.agentId = agentId;
		}
	}
	
	public static String getTransactionId(String threadName){
		return relations.get(threadName);
	}
}
