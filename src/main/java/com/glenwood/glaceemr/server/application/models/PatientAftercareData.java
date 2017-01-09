package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "patient_aftercare_data")
public class PatientAftercareData {

	@Id
	@Column(name="patient_aftercare_data_id")
	private Integer patientAftercareDataId;

	@Column(name="patient_aftercare_data_patient_id")
	private Integer patientAftercareDataPatientId;

	@Column(name="patient_aftercare_data_encounter_id")
	private Integer patientAftercareDataEncounterId;

	@Column(name="patient_aftercare_data_aftercare_id")
	private Integer patientAftercareDataAftercareId;

	@Column(name="patient_aftercare_data_unknown1")
	private Integer patientAftercareDataUnknown1;

	@Column(name="patient_aftercare_data_name")
	private String patientAftercareDataName;

	@Column(name="patient_aftercare_data_dirty")
	private Integer patientAftercareDataDirty;

	@Column(name="patient_aftercare_data_status")
	private Boolean patientAftercareDataStatus;

	@Column(name="patient_aftercare_data_url")
	private String patientAftercareDataUrl;

	@Column(name="patient_aftercare_data_category")
	private String patientAftercareDataCategory;

	public Integer getPatientAftercareDataId() {
		return patientAftercareDataId;
	}

	public void setPatientAftercareDataId(Integer patientAftercareDataId) {
		this.patientAftercareDataId = patientAftercareDataId;
	}

	public Integer getPatientAftercareDataPatientId() {
		return patientAftercareDataPatientId;
	}

	public void setPatientAftercareDataPatientId(
			Integer patientAftercareDataPatientId) {
		this.patientAftercareDataPatientId = patientAftercareDataPatientId;
	}

	public Integer getPatientAftercareDataEncounterId() {
		return patientAftercareDataEncounterId;
	}

	public void setPatientAftercareDataEncounterId(
			Integer patientAftercareDataEncounterId) {
		this.patientAftercareDataEncounterId = patientAftercareDataEncounterId;
	}

	public Integer getPatientAftercareDataAftercareId() {
		return patientAftercareDataAftercareId;
	}

	public void setPatientAftercareDataAftercareId(
			Integer patientAftercareDataAftercareId) {
		this.patientAftercareDataAftercareId = patientAftercareDataAftercareId;
	}

	public Integer getPatientAftercareDataUnknown1() {
		return patientAftercareDataUnknown1;
	}

	public void setPatientAftercareDataUnknown1(Integer patientAftercareDataUnknown1) {
		this.patientAftercareDataUnknown1 = patientAftercareDataUnknown1;
	}

	public String getPatientAftercareDataName() {
		return patientAftercareDataName;
	}

	public void setPatientAftercareDataName(String patientAftercareDataName) {
		this.patientAftercareDataName = patientAftercareDataName;
	}

	public Integer getPatientAftercareDataDirty() {
		return patientAftercareDataDirty;
	}

	public void setPatientAftercareDataDirty(Integer patientAftercareDataDirty) {
		this.patientAftercareDataDirty = patientAftercareDataDirty;
	}

	public Boolean getPatientAftercareDataStatus() {
		return patientAftercareDataStatus;
	}

	public void setPatientAftercareDataStatus(Boolean patientAftercareDataStatus) {
		this.patientAftercareDataStatus = patientAftercareDataStatus;
	}

	public String getPatientAftercareDataUrl() {
		return patientAftercareDataUrl;
	}

	public void setPatientAftercareDataUrl(String patientAftercareDataUrl) {
		this.patientAftercareDataUrl = patientAftercareDataUrl;
	}

	public String getPatientAftercareDataCategory() {
		return patientAftercareDataCategory;
	}

	public void setPatientAftercareDataCategory(String patientAftercareDataCategory) {
		this.patientAftercareDataCategory = patientAftercareDataCategory;
	}
	
}