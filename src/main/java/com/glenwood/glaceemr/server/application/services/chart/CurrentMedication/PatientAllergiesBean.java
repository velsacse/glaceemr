package com.glenwood.glaceemr.server.application.services.chart.CurrentMedication;

public class PatientAllergiesBean {
	Integer id;
	Integer severity;
	Integer typeId;
	String typeName;
	String allergyName;
	String allergyCode;
	String codeSystem;
	String drugCategory;
	String reactionTo;
	String modifiedon;
	String onsetDate;
	String createdOn;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSeverity() {
		return severity;
	}

	public void setSeverity(Integer severity) {
		this.severity = severity;
	}

	public Integer gettypeId() {
		return typeId;
	}

	public void settypeId(Integer typeId) {
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

	public String getModifiedon() {
		return modifiedon;
	}

	public void setModifiedon(String modifiedon) {
		this.modifiedon = modifiedon;
	}

	public String getonsetDate() {
		return onsetDate;
	}

	public void setonsetDate(String onsetDate) {
		this.onsetDate = onsetDate;
	}

	public String getcreatedOn() {
		return createdOn;
	}

	public void setcreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	

}