package com.stackTrace.v0307;

public class StackTraceMessage {
	protected String transactionId;
	
	protected String callRelation;
	
	protected String agentId;
	
	protected long interval;
	
	protected String timestamp;
	
	public StackTraceMessage() {
	}

	public StackTraceMessage(String transactionId, String callRelation, String agentId, long interval, long timestamp) {
		this.transactionId = transactionId;
		this.callRelation = callRelation;
		this.agentId = agentId;
		this.interval = interval;
//		this.timestamp = DateUtil.getDateAndTimeStr(timestamp);
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

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timeStamp) {
//		this.timestamp = DateUtil.getDateAndTimeStr(timeStamp);
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
}
