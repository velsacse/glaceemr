package com.glenwood.glaceemr.server.application.models;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;	
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "patient_registration")
public class PatientRegistration {
	@Id
	@Column(name="patient_registration_id")
	public Integer patRegId;
	
	@Column(name="patient_registration_last_name")
	public String ptLName;
	
	@Column(name="patient_registration_first_name")
	public String ptFName;

	@Column(name="patient_registration_mid_initial")
	private String patientRegistrationMidInitial;
	
	@Column(name="patient_registration_active")
	public boolean ptIsActive;

	@Column(name="patient_registration_accountno")
	public String accno;
	
	@Column(name="patient_registration_dob")
	public Date dob;

	@Column(name="patient_registration_accttype")
	private Integer ptRegAccType;

	@Column(name="patient_registration_phone_no")
	private String patientRegistrationPhoneNo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="patient_registration_accttype", referencedColumnName="account_type_id" , insertable=false, updatable=false)
	private AccountType ptAccType;

	public Integer getPatRegId() {
		return patRegId;
	}

	public void setPatRegId(Integer patRegId) {
		this.patRegId = patRegId;
	}

	public String getPtLName() {
		return ptLName;
	}

	public void setPtLName(String ptLName) {
		this.ptLName = ptLName;
	}

	public String getPtFName() {
		return ptFName;
	}

	public void setPtFName(String ptFName) {
		this.ptFName = ptFName;
	}

	public String getPatientRegistrationMidInitial() {
		return patientRegistrationMidInitial;
	}

	public void setPatientRegistrationMidInitial(String patientRegistrationMidInitial) {
		this.patientRegistrationMidInitial = patientRegistrationMidInitial;
	}

	public boolean isPtIsActive() {
		return ptIsActive;
	}

	public void setPtIsActive(boolean ptIsActive) {
		this.ptIsActive = ptIsActive;
	}

	public String getAccno() {
		return accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Integer getPtRegAccType() {
		return ptRegAccType;
	}

	public void setPtRegAccType(Integer ptRegAccType) {
		this.ptRegAccType = ptRegAccType;
	}

	public AccountType getPtAccType() {
		return ptAccType;
	}

	public void setPtAccType(AccountType ptAccType) {
		this.ptAccType = ptAccType;
	}

	public String getPatientRegistrationPhoneNo() {
		return patientRegistrationPhoneNo;
	}

	public void setPatientRegistrationPhoneNo(String patientRegistrationPhoneNo) {
		this.patientRegistrationPhoneNo = patientRegistrationPhoneNo;
	}
	
	
			
}
