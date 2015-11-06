package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "patient_doc_category")
public class PatientDocCategory {

	@Id
	@Column(name="patient_doc_category_id")
	private Integer patientDocCategoryId;

	@Column(name="patient_doc_category_name")
	private String patientDocCategoryName;

	@Column(name="patient_doc_category_priority")
	private Integer patientDocCategoryPriority;

	@Column(name="patient_doc_category_order")
	private Short patientDocCategoryOrder;
	
	@Column(name="patient_doc_category_isactive")
	private Boolean patientDocCategoryIsactive;

	@Column(name="patient_doc_category_isbilling")
	private Boolean patientDocCategoryIsbilling;

	
	public Integer getPatientDocCategoryId() {
		return patientDocCategoryId;
	}

	public String getPatientDocCategoryName() {
		return patientDocCategoryName;
	}

	public Integer getPatientDocCategoryPriority() {
		return patientDocCategoryPriority;
	}

	public Short getPatientDocCategoryOrder() {
		return patientDocCategoryOrder;
	}

	public Boolean getPatientDocCategoryIsactive() {
		return patientDocCategoryIsactive;
	}

	public Boolean getPatientDocCategoryIsbilling() {
		return patientDocCategoryIsbilling;
	}

	public void setPatientDocCategoryId(Integer patientDocCategoryId) {
		this.patientDocCategoryId = patientDocCategoryId;
	}

	public void setPatientDocCategoryName(String patientDocCategoryName) {
		this.patientDocCategoryName = patientDocCategoryName;
	}

	public void setPatientDocCategoryPriority(Integer patientDocCategoryPriority) {
		this.patientDocCategoryPriority = patientDocCategoryPriority;
	}

	public void setPatientDocCategoryOrder(Short patientDocCategoryOrder) {
		this.patientDocCategoryOrder = patientDocCategoryOrder;
	}

	public void setPatientDocCategoryIsactive(Boolean patientDocCategoryIsactive) {
		this.patientDocCategoryIsactive = patientDocCategoryIsactive;
	}

	public void setPatientDocCategoryIsbilling(Boolean patientDocCategoryIsbilling) {
		this.patientDocCategoryIsbilling = patientDocCategoryIsbilling;
	}
	
	
}