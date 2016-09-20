package com.glenwood.glaceemr.server.application.services.fax;

import java.util.Date;

public class FaxOutboxBean {

	int faxId;
	String faxSubject;
	String faxFileName;
	int folderId;
	Date scheduleTime;
	int statusId;
	String recipientName;
	int forwardedToUserId;
	int createdBy;
	Date createdDate;
	String billingCode;
	int modifiedByUserId;
	String senderName;
	String outboxThumbnail;
	String recipientNumber;
	String faxAttachments;
	public FaxOutboxBean(int faxId, String faxSubject, String faxFileName,
			int folderId, Date scheduleTime, int statusId,
			String recipientName, int forwardedToUserId, int createdBy,
			Date createdDate, String billingCode, int modifiedByUserId,
			String senderName, String outboxThumbnail, String recipientNumber,
			String faxAttachments) {
		super();
		this.faxId = faxId;
		this.faxSubject = faxSubject;
		this.faxFileName = faxFileName;
		this.folderId = folderId;
		this.scheduleTime = scheduleTime;
		this.statusId = statusId;
		this.recipientName = recipientName;
		this.forwardedToUserId = forwardedToUserId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.billingCode = billingCode;
		this.modifiedByUserId = modifiedByUserId;
		this.senderName = senderName;
		this.outboxThumbnail = outboxThumbnail;
		this.recipientNumber = recipientNumber;
		this.faxAttachments = faxAttachments;
	}
	public int getFaxId() {
		return faxId;
	}
	public void setFaxId(int faxId) {
		this.faxId = faxId;
	}
	public String getFaxSubject() {
		return faxSubject;
	}
	public void setFaxSubject(String faxSubject) {
		this.faxSubject = faxSubject;
	}
	public String getFaxFileName() {
		return faxFileName;
	}
	public void setFaxFileName(String faxFileName) {
		this.faxFileName = faxFileName;
	}
	public int getFolderId() {
		return folderId;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
	public Date getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public int getForwardedToUserId() {
		return forwardedToUserId;
	}
	public void setForwardedToUserId(int forwardedToUserId) {
		this.forwardedToUserId = forwardedToUserId;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getBillingCode() {
		return billingCode;
	}
	public void setBillingCode(String billingCode) {
		this.billingCode = billingCode;
	}
	public int getModifiedByUserId() {
		return modifiedByUserId;
	}
	public void setModifiedByUserId(int modifiedByUserId) {
		this.modifiedByUserId = modifiedByUserId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getOutboxThumbnail() {
		return outboxThumbnail;
	}
	public void setOutboxThumbnail(String outboxThumbnail) {
		this.outboxThumbnail = outboxThumbnail;
	}
	public String getRecipientNumber() {
		return recipientNumber;
	}
	public void setRecipientNumber(String recipientNumber) {
		this.recipientNumber = recipientNumber;
	}
	public String getFaxAttachments() {
		return faxAttachments;
	}
	public void setFaxAttachments(String faxAttachments) {
		this.faxAttachments = faxAttachments;
	}

}
