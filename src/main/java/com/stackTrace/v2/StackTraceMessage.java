package com.stackTrace.v2;

/**
 * 
 * @author Chenzhai
 *
 */
public class StackTraceMessage {
	public String transactionId;
	
	public String callRelation;
	
	public long time;
	
	public StackTraceMessage() {
		// TODO Auto-generated constructor stub
	}

	public StackTraceMessage(String transactionId, String callRelation,
			long time) {
		super();
		this.transactionId = transactionId;
		this.callRelation = callRelation;
		this.time = time;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCallRelation() {
		return callRelation;
	}

	public void setCallRelation(String callRelation) {
		this.callRelation = callRelation;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
	
}
