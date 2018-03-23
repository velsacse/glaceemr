package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "patient_eligibility")
public class PatientEligibility {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_eligibility_patient_eligibility_id_seq")
	@SequenceGenerator(name = "patient_eligibility_patient_eligibility_id_seq", sequenceName = "patient_eligibility_patient_eligibility_id_seq", allocationSize = 1)
	@Column(name="patient_eligibility_id")
	private Integer patientEligibilityId;
	
	@Column(name="patient_id")
	private Integer patientId;

	@Column(name="insurance_id")
	private String insuranceId;

	@Column(name="patient_eligibility_active")
	private Boolean patientEligibilityActive;

	@Column(name="transaction_id")
	private Integer transactionId;

	@Column(name="eligibility_check_date")
	private Timestamp eligibilityCheckDate;

	@Column(name="eligibility_service_date")
	private Timestamp eligibilityServiceDate;

	@Column(name="insurance_type")
	private String insuranceType;

	@Column(name="eligibile_status")
	private String eligibleStatus;

	@Column(name="response_file")
	private String responseFile;
	
	@Column(name="patient_eligibility_ccn")
	private Boolean patientEligibilityCcn;
	
	@Column(name="existing_transaction_id")
	private Integer existingTransactionId;

	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="patient_id", referencedColumnName="patient_registration_id" , insertable=false, updatable=false)
	PatientRegistration patientRegistration2;

	public Integer getPatientEligibilityId() {
		return patientEligibilityId;
	}

	public void setPatientEligibilityId(Integer patientEligibilityId) {
		this.patientEligibilityId = patientEligibilityId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(String insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Boolean getPatientEligibilityActive() {
		return patientEligibilityActive;
	}

	public void setPatientEligibilityActive(Boolean patientEligibilityActive) {
		this.patientEligibilityActive = patientEligibilityActive;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public Timestamp getEligibilityCheckDate() {
		return eligibilityCheckDate;
	}

	public void setEligibilityCheckDate(Timestamp eligibilityCheckDate) {
		this.eligibilityCheckDate = eligibilityCheckDate;
	}

	public Timestamp getEligibilityServiceDate() {
		return eligibilityServiceDate;
	}

	public void setEligibilityServiceDate(Timestamp eligibilityServiceDate) {
		this.eligibilityServiceDate = eligibilityServiceDate;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getEligibleStatus() {
		return eligibleStatus;
	}

	public void setEligibleStatus(String eligibleStatus) {
		this.eligibleStatus = eligibleStatus;
	}

	public String getResponseFile() {
		return responseFile;
	}

	public void setResponseFile(String responseFile) {
		this.responseFile = responseFile;
	}

	public Boolean getPatientEligibilityCcn() {
		return patientEligibilityCcn;
	}

	public void setPatientEligibilityCcn(Boolean patientEligibilityCcn) {
		this.patientEligibilityCcn = patientEligibilityCcn;
	}

	public Integer getExistingTransactionId() {
		return existingTransactionId;
	}

	public void setExistingTransactionId(Integer existingTransactionId) {
		this.existingTransactionId = existingTransactionId;
	}

}
