package com.glenwood.glaceemr.server.application.services.fax;

import java.util.Date;
public class FaxInboxBean {

	int faxId;
	String transStationId;
	int folderId;
	String faxSubject;
	int faxTypeId;
	int statusId;
	int forwardTo;
	int receiveDate;
	Date modifiedDate;
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	int recepientName;
	String h491020Faxnotes;


	public FaxInboxBean(int faxId, String transStationId, int folderId,
			String faxSubject, int faxTypeId, int statusId, int forwardTo,
			int receiveDate, Date modifiedDate, int recepientName,
			String h491020Faxnotes) {
		super();
		this.faxId = faxId;
		this.transStationId = transStationId;
		this.folderId = folderId;
		this.faxSubject = faxSubject;
		this.faxTypeId = faxTypeId;
		this.statusId = statusId;
		this.forwardTo = forwardTo;
		this.receiveDate = receiveDate;
		this.modifiedDate = modifiedDate;
		this.recepientName = recepientName;
		this.h491020Faxnotes = h491020Faxnotes;
	}
	public int getFaxId() {
		return faxId;
	}
	public void setFaxId(int faxId) {
		this.faxId = faxId;
	}
	public String getTransStationId() {
		return transStationId;
	}
	public void setTransStationId(String transStationId) {
		this.transStationId = transStationId;
	}
	public int getFolderId() {
		return folderId;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
	public String getFaxSubject() {
		return faxSubject;
	}
	public void setFaxSubject(String faxSubject) {
		this.faxSubject = faxSubject;
	}
	public int getFaxTypeId() {
		return faxTypeId;
	}
	public void setFaxTypeId(int faxTypeId) {
		this.faxTypeId = faxTypeId;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public int getForwardTo() {
		return forwardTo;
	}
	public void setForwardTo(int forwardTo) {
		this.forwardTo = forwardTo;
	}
	public int getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(int receiveDate) {
		this.receiveDate = receiveDate;
	}

	public int getRecepientName() {
		return recepientName;
	}
	public void setRecepientName(int recepientName) {
		this.recepientName = recepientName;
	}
	public String getH491020Faxnotes() {
		return h491020Faxnotes;
	}
	public void setH491020Faxnotes(String h491020Faxnotes) {
		this.h491020Faxnotes = h491020Faxnotes;
	}

}

