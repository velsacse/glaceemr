package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="patient_allergies")
public class PatientAllergies {
	
	@Id
	@Column(name="patallerg_id")
	public Integer patAllergId;
	
	@Column(name="patallerg_chartid")
	public Integer patAllergChartId;
	
	@Column(name="patallerg_encounterid")
	public Integer patAllergEncounterId;
	
	@Column(name="patallerg_typeid")
	public Integer patAllergTypeId;
	
	@Column(name="patallerg_allergicto")
	public String patAllergAllergicTo;
	
	@Column(name="patallerg_reactionto")
	public String patAllergReactionTo;
	
	@Column(name="patallerg_onsetdate")
	public String patAllergOnsetDate;
	
	@Column(name="patallerg_status")
	public Integer patAllergStatus;
	
	@Column(name="patallerg_resolveddate")
	public String patAllergResolvedDate;
	
	@Column(name="patallerg_createdby")
	public String patAllergCreatedBy;
	
	@Column(name="patallerg_createdon")
	public Timestamp patAllergCreatedOn;
	
	@Column(name="patallerg_modifiedby")
	public String patAllergModifiedBy;
	
	@Column(name="patallerg_modifiedon")
	public Timestamp patAllergModifiedOn;
	
	@Column(name="patallerg_drugcategory")
	public String patAllergDrugCategory;
	
	@Column(name="patallerg_severity")
	public Integer patAllergSeverity;
	
	@Column(name="patallerg_allergy_code")
	public String patAllergAllergyCode;
	
	@Column(name="patallerg_interactionxml")
	public String patAllergInteractionXml;
	
	@Column(name="patallerg_resolvedby")
	public String patAllergResolvedBy;
	
	@Column(name="patallerg_inactiveby")
	public String patAllergInactiveBy;
	
	@Column(name="patallerg_inactiveon")
	public Timestamp patAllergInactiveOn;
	
	@Column(name="patallerg_inactive_reason")
	public String patAllergInactiveReason;
	
	/*@Column(name="patallerg_uncoded_med")
	public boolean patAllergUncodedMed;*/
	
	@Column(name="patallerg_codesystem")
	public String patAllergCodeSystem;

	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="patallerg_createdby", referencedColumnName="emp_profile_empid", insertable=false, updatable=false)
	EmployeeProfile empProfileAllgCreatedByTable;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="patallerg_resolvedby", referencedColumnName="emp_profile_empid", insertable=false, updatable=false)
	EmployeeProfile empProfileAllgResolvedByTable;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="patallerg_inactiveby", referencedColumnName="emp_profile_empid", insertable=false, updatable=false)
	EmployeeProfile empProfileAllgInactiveByTable;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="patallerg_modifiedby", referencedColumnName="emp_profile_empid", insertable=false, updatable=false)
	EmployeeProfile empProfileAllgModifiedByTable;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="patallerg_typeid", referencedColumnName="allergtype_id", insertable=false, updatable=false)
	AllergiesType allergiesType;
	
	
	
	public AllergiesType getAllergiesType() {
		return allergiesType;
	}

	public void setAllergiesType(AllergiesType allergiesType) {
		this.allergiesType = allergiesType;
	}

	/**
	 * Getters and Setters
	 */
	
	public Integer getPatAllergId() {
		return patAllergId;
	}

	public Integer getPatAllergChartId() {
		return patAllergChartId;
	}

	public Integer getPatAllergEncounterId() {
		return patAllergEncounterId;
	}

	public Integer getPatAllergTypeId() {
		return patAllergTypeId;
	}

	public String getPatAllergAllergicTo() {
		return patAllergAllergicTo;
	}

	public String getPatAllergReactionTo() {
		return patAllergReactionTo;
	}

	public String getPatAllergOnsetDate() {
		return patAllergOnsetDate;
	}

	public Integer getPatAllergStatus() {
		return patAllergStatus;
	}

	public String getPatAllergResolvedDate() {
		return patAllergResolvedDate;
	}

	public String getPatAllergCreatedBy() {
		return patAllergCreatedBy;
	}

	public Timestamp getPatAllergCreatedOn() {
		return patAllergCreatedOn;
	}

	public String getPatAllergModifiedBy() {
		return patAllergModifiedBy;
	}

	public Timestamp getPatAllergModifiedOn() {
		return patAllergModifiedOn;
	}

	public String getPatAllergDrugCategory() {
		return patAllergDrugCategory;
	}

	public Integer getPatAllergSeverity() {
		return patAllergSeverity;
	}

	public String getPatAllergAllergyCode() {
		return patAllergAllergyCode;
	}

	public String getPatAllergInteractionXml() {
		return patAllergInteractionXml;
	}

	public String getPatAllergResolvedBy() {
		return patAllergResolvedBy;
	}

	public String getPatAllergInactiveBy() {
		return patAllergInactiveBy;
	}

	public Timestamp getPatAllergInactiveOn() {
		return patAllergInactiveOn;
	}

	public String getPatAllergInactiveReason() {
		return patAllergInactiveReason;
	}

	/*public boolean isPatAllergUncodedMed() {
		return patAllergUncodedMed;
	}*/

	public String getPatAllergCodeSystem() {
		return patAllergCodeSystem;
	}

	public void setPatAllergId(Integer patAllergId) {
		this.patAllergId = patAllergId;
	}

	public void setPatAllergChartId(Integer patAllergChartId) {
		this.patAllergChartId = patAllergChartId;
	}

	public void setPatAllergEncounterId(Integer patAllergEncounterId) {
		this.patAllergEncounterId = patAllergEncounterId;
	}

	public void setPatAllergTypeId(Integer patAllergTypeId) {
		this.patAllergTypeId = patAllergTypeId;
	}

	public void setPatAllergAllergicTo(String patAllergAllergicTo) {
		this.patAllergAllergicTo = patAllergAllergicTo;
	}

	public void setPatAllergReactionTo(String patAllergReactionTo) {
		this.patAllergReactionTo = patAllergReactionTo;
	}

	public void setPatAllergOnsetDate(String patAllergOnsetDate) {
		this.patAllergOnsetDate = patAllergOnsetDate;
	}

	public void setPatAllergStatus(Integer patAllergStatus) {
		this.patAllergStatus = patAllergStatus;
	}

	public void setPatAllergResolvedDate(String patAllergResolvedDate) {
		this.patAllergResolvedDate = patAllergResolvedDate;
	}

	public void setPatAllergCreatedBy(String patAllergCreatedBy) {
		this.patAllergCreatedBy = patAllergCreatedBy;
	}

	public void setPatAllergCreatedOn(Timestamp patAllergCreatedOn) {
		this.patAllergCreatedOn = patAllergCreatedOn;
	}

	public void setPatAllergModifiedBy(String patAllergModifiedBy) {
		this.patAllergModifiedBy = patAllergModifiedBy;
	}

	public void setPatAllergModifiedOn(Timestamp patAllergModifiedOn) {
		this.patAllergModifiedOn = patAllergModifiedOn;
	}

	public void setPatAllergDrugCategory(String patAllergDrugCategory) {
		this.patAllergDrugCategory = patAllergDrugCategory;
	}

	public void setPatAllergSeverity(Integer patAllergSeverity) {
		this.patAllergSeverity = patAllergSeverity;
	}

	public void setPatAllergAllergyCode(String patAllergAllergyCode) {
		this.patAllergAllergyCode = patAllergAllergyCode;
	}

	public void setPatAllergInteractionXml(String patAllergInteractionXml) {
		this.patAllergInteractionXml = patAllergInteractionXml;
	}

	public void setPatAllergResolvedBy(String patAllergResolvedBy) {
		this.patAllergResolvedBy = patAllergResolvedBy;
	}

	public void setPatAllergInactiveBy(String patAllergInactiveBy) {
		this.patAllergInactiveBy = patAllergInactiveBy;
	}

	public void setPatAllergInactiveOn(Timestamp patAllergInactiveOn) {
		this.patAllergInactiveOn = patAllergInactiveOn;
	}

	public void setPatAllergInactiveReason(String patAllergInactiveReason) {
		this.patAllergInactiveReason = patAllergInactiveReason;
	}

	/*public void setPatAllergUncodedMed(boolean patAllergUncodedMed) {
		this.patAllergUncodedMed = patAllergUncodedMed;
	}*/

	public void setPatAllergCodeSystem(String patAllergCodeSystem) {
		this.patAllergCodeSystem = patAllergCodeSystem;
	}

	public EmployeeProfile getEmpProfileAllgCreatedByTable() {
		return empProfileAllgCreatedByTable;
	}

	public void setEmpProfileAllgCreatedByTable(
			EmployeeProfile empProfileAllgCreatedByTable) {
		this.empProfileAllgCreatedByTable = empProfileAllgCreatedByTable;
	}

	public EmployeeProfile getEmpProfileAllgResolvedByTable() {
		return empProfileAllgResolvedByTable;
	}

	public void setEmpProfileAllgResolvedByTable(
			EmployeeProfile empProfileAllgResolvedByTable) {
		this.empProfileAllgResolvedByTable = empProfileAllgResolvedByTable;
	}

	public EmployeeProfile getEmpProfileAllgInactiveByTable() {
		return empProfileAllgInactiveByTable;
	}

	public void setEmpProfileAllgInactiveByTable(
			EmployeeProfile empProfileAllgInactiveByTable) {
		this.empProfileAllgInactiveByTable = empProfileAllgInactiveByTable;
	}

	public EmployeeProfile getEmpProfileAllgModifiedByTable() {
		return empProfileAllgModifiedByTable;
	}

	public void setEmpProfileAllgModifiedByTable(
			EmployeeProfile empProfileAllgModifiedByTable) {
		this.empProfileAllgModifiedByTable = empProfileAllgModifiedByTable;
	}

	
	
	}
