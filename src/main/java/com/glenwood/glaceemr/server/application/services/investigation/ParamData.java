package com.glenwood.glaceemr.server.application.services.investigation;


public class ParamData {
	Integer confirmTestStatus;
	Integer prelimStatus;
	Integer resultStatus;
	Integer labStatus;
	String orderedDate;
	String performedDate;
	String resultNotes;
	Integer testDetailId;
	Integer encounterId;
	String drugxml;
	public Integer getConfirmTestStatus() {
		return confirmTestStatus;
	}
	public void setConfirmTestStatus(Integer confirmTestStatus) {
		this.confirmTestStatus = confirmTestStatus;
	}
	public Integer getPrelimStatus() {
		return prelimStatus;
	}
	public void setPrelimStatus(Integer prelimStatus) {
		this.prelimStatus = prelimStatus;
	}
	public Integer getResultStatus() {
		return resultStatus;
	}
	public void setResultStatus(Integer resultStatus) {
		this.resultStatus = resultStatus;
	}
	public Integer getLabStatus() {
		return labStatus;
	}
	public void setLabStatus(Integer labStatus) {
		this.labStatus = labStatus;
	}
	public String getOrderedDate() {
		return orderedDate;
	}
	public void setOrderedDate(String orderedDate) {
		this.orderedDate = orderedDate;
	}
	public String getPerformedDate() {
		return performedDate;
	}
	public void setPerformedDate(String performedDate) {
		this.performedDate = performedDate;
	}
	public String getResultNotes() {
		return resultNotes;
	}
	public void setResultNotes(String resultNotes) {
		this.resultNotes = resultNotes;
	}
	public Integer getTestDetailId() {
		return testDetailId;
	}
	public void setTestDetailId(Integer testDetailId) {
		this.testDetailId = testDetailId;
	}public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public String getDrugxml() {
		return drugxml;
	}
	public void setDrugxml(String drugxml) {
		this.drugxml = drugxml;
	}	
}
