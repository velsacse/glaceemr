package com.glenwood.glaceemr.server.application.services.portal.portalMessages;

public class PortalMessagesDetailsBean {

	Integer totalMessageThreads;
	
	long totalInboxMessages;
	
	long totalSentMessages;
	
	long totalMessages;
	
	
	public Integer getTotalMessageThreads() {
		return totalMessageThreads;
	}
	public void setTotalMessageThreads(Integer totalMessageThreads) {
		this.totalMessageThreads = totalMessageThreads;
	}
	public long getTotalInboxMessages() {
		return totalInboxMessages;
	}
	public void setTotalInboxMessages(long totalInboxMessages) {
		this.totalInboxMessages = totalInboxMessages;
	}
	public long getTotalSentMessages() {
		return totalSentMessages;
	}
	public void setTotalSentMessages(long totalSentMessages) {
		this.totalSentMessages = totalSentMessages;
	}
	public long getTotalMessages() {
		return totalMessages;
	}
	public void setTotalMessages(long totalMessages) {
		this.totalMessages = totalMessages;
	}
	
}
