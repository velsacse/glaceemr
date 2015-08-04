package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "patient_encounter_type")
public class PatientEncounterType {
    
	@Column(name="groupid")
	private Short groupid;
    @Id
	@Column(name="categoryid")
	private Short categoryid;

	@Column(name="encounter_typename")
	private String encounterTypename;

	@Column(name="isactive")
	private Boolean isactive;

	@Column(name="plantype")
	private Boolean planType;
	
	public Boolean getPlanType() {
		return planType;
	}

	public void setPlanType(Boolean planType) {
		this.planType = planType;
	}

	public Short getGroupid() {
		return groupid;
	}

	public Short getCategoryid() {
		return categoryid;
	}

	public String getEncounterTypename() {
		return encounterTypename;
	}

	public Boolean getIsactive() {
		return isactive;
	}

	public void setGroupid(Short groupid) {
		this.groupid = groupid;
	}

	public void setCategoryid(Short categoryid) {
		this.categoryid = categoryid;
	}

	public void setEncounterTypename(String encounterTypename) {
		this.encounterTypename = encounterTypename;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	
	
}