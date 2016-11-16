package com.glenwood.glaceemr.server.application.services.chart.CurrentMedication;

import java.util.Date;

public class PatientAllergiesBean {
	int id;
	int severity;
	int typeId;
	String typeName;
	String allergyName;
	String allergyCode;
	String codeSystem;
	String drugCategory;
	String reactionTo;
	String modifiedby;
	Date modifiedon;
	String onsetDate;
	String createdBy;
	Date createdOn;
	
	public PatientAllergiesBean(){}
	
	public PatientAllergiesBean(Integer id,Integer severity,Integer typeId,String typeName,String allergyName,String allergyCode,String codeSystem,String drugCategory,String reactionTo,String modifiedby,Date modifiedon,String onsetDate,String createdBy,Date createdOn){
		this.id=id;
		this.severity=severity;
		this.typeId=typeId;
		this.typeName=typeName;
		this.allergyName=allergyName;
		this.allergyCode=allergyCode;
		this.codeSystem=codeSystem;
		this.drugCategory=drugCategory;
		this.reactionTo=reactionTo;
		this.modifiedby=modifiedby;
		this.modifiedon=modifiedon;
		this.onsetDate=onsetDate;
		this.createdBy=createdBy;
		this.createdOn=createdOn;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public int gettypeId() {
		return typeId;
	}

	public void settypeId(int typeId) {
		this.typeId = typeId;
	}

	public String gettypeName() {
		return typeName;
	}

	public void settypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getallergyName() {
		return allergyName;
	}

	public void setallergyName(String allergyName) {
		this.allergyName = allergyName;
	}

	public String getallergyCode() {
		return allergyCode;
	}

	public void setallergyCode(String allergyCode) {
		this.allergyCode = allergyCode;
	}

	public String getcodeSystem() {
		return codeSystem;
	}

	public void setcodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getdrugCategory() {
		return drugCategory;
	}

	public void setdrugCategory(String drugCategory) {
		this.drugCategory = drugCategory;
	}

	public String getreactionTo() {
		return reactionTo;
	}

	public void setreactionTo(String reactionTo) {
		this.reactionTo = reactionTo;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Date getModifiedon() {
		return modifiedon;
	}

	public void setModifiedon(Date modifiedon) {
		this.modifiedon = modifiedon;
	}

	public String getonsetDate() {
		return onsetDate;
	}

	public void setonsetDate(String onsetDate) {
		this.onsetDate = onsetDate;
	}

	public String getcreatedBy() {
		return createdBy;
	}

	public void setcreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getcreatedOn() {
		return createdOn;
	}

	public void setcreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	

}
