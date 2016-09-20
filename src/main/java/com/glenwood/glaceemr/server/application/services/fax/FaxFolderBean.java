package com.glenwood.glaceemr.server.application.services.fax;

public class FaxFolderBean {

	long totalInbox;
	long unreadInbox;
	long totalSaved;
	long unreadSaved;
	long totalTrash;
	long unreadTrash;
	long outboxFax;
	long sentFax;
	int faxFolderId;
	String outFaxFileName;
	
	public FaxFolderBean(long totalInbox, long unreadInbox, long totalSaved,
			long unreadSaved, long totalTrash, long unreadTrash,
			long outboxFax, long sentFax, int faxFolderId, String outFaxFileName) {
		super();
		this.totalInbox = totalInbox;
		this.unreadInbox = unreadInbox;
		this.totalSaved = totalSaved;
		this.unreadSaved = unreadSaved;
		this.totalTrash = totalTrash;
		this.unreadTrash = unreadTrash;
		this.outboxFax = outboxFax;
		this.sentFax = sentFax;
		this.faxFolderId = faxFolderId;
		this.outFaxFileName = outFaxFileName;
	}
	
	public String getOutFaxFileName() {
		return outFaxFileName;
	}

	public void setOutFaxFileName(String outFaxFileName) {
		this.outFaxFileName = outFaxFileName;
	}

	public FaxFolderBean(String outFaxFileName) {
		super();
		this.outFaxFileName = outFaxFileName;
	}

	public FaxFolderBean() {
		// TODO Auto-generated constructor stub
	}

	public long getTotalInbox() {
		return totalInbox;
	}
	
	public void setTotalInbox(long totalInbox) {
		this.totalInbox = totalInbox;
	}
	
	public long getUnreadInbox() {
		return unreadInbox;
	}
	public void setUnreadInbox(long unreadInbox) {
		this.unreadInbox = unreadInbox;
	}
	
	public long getTotalSaved() {
		return totalSaved;
	}
	
	public void setTotalSaved(long totalSaved) {
		this.totalSaved = totalSaved;
	}
	
	public long getUnreadSaved() {
		return unreadSaved;
	}
	
	public void setUnreadSaved(long unreadSaved) {
		this.unreadSaved = unreadSaved;
	}
	public long getTotalTrash() {
		return totalTrash;
	}
	
	public void setTotalTrash(long totalTrash) {
		this.totalTrash = totalTrash;
	}
	
	public long getUnreadTrash() {
		return unreadTrash;
	}
	
	public void setUnreadTrash(long unreadTrash) {
		this.unreadTrash = unreadTrash;
	}
	
	public long getOutboxFax() {
		return outboxFax;
	}
	
	public void setOutboxFax(long outboxFax) {
		this.outboxFax = outboxFax;
	}
	
	public long getSentFax() {
		return sentFax;
	}
	
	public void setSentFax(long sentFax) {
		this.sentFax = sentFax;
	}
	
}
