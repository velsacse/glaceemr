package com.glenwood.glaceemr.server.application.services.patient;

import java.util.List;

import com.glenwood.glaceemr.server.application.services.chart.insurance.InsuranceDataBean;

/**
 * Patient details bean 
 * @author software
 *
 */
public class PatientDataBean {
	
	private String patientName;
	private String age;
	private String dos;
	private String gender;
	private String accountId;
	private String phNum;
	private String dob;
	private String mobileNum;
	private String address;
	private String refPhyName;
	private List<InsuranceDataBean> insuranceDetails;
	private String principalDr;
	private String serviceDr;
	private String ethinicity;
	private String race;
	private String prefLang;
	
	public PatientDataBean(String patientName, String age, String dos,
			String gender, String accountId, String phNum, String dob,
			String mobileNum, String address, String refPhyName,
			List<InsuranceDataBean> insuranceDetails, String principalDr,
			String serviceDr, String ethinicity, String race, String prefLang) {
		super();
		this.patientName = patientName;
		this.age = age;
		this.dos = dos;
		this.gender = gender;
		this.accountId = accountId;
		this.phNum = phNum;
		this.dob = dob;
		this.mobileNum = mobileNum;
		this.address = address;
		this.refPhyName = refPhyName;
		this.insuranceDetails = insuranceDetails;
		this.principalDr = principalDr;
		this.serviceDr = serviceDr;
		this.ethinicity = ethinicity;
		this.race = race;
		this.prefLang = prefLang;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDos() {
		return dos;
	}

	public void setDos(String dos) {
		this.dos = dos;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPhNum() {
		return phNum;
	}

	public void setPhNum(String phNum) {
		this.phNum = phNum;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRefPhyName() {
		return refPhyName;
	}

	public void setRefPhyName(String refPhyName) {
		this.refPhyName = refPhyName;
	}

	public List<InsuranceDataBean> getInsuranceDetails() {
		return insuranceDetails;
	}

	public void setInsuranceDetails(List<InsuranceDataBean> insuranceDetails) {
		this.insuranceDetails = insuranceDetails;
	}

	public String getPrincipalDr() {
		return principalDr;
	}

	public void setPrincipalDr(String principalDr) {
		this.principalDr = principalDr;
	}

	public String getServiceDr() {
		return serviceDr;
	}

	public void setServiceDr(String serviceDr) {
		this.serviceDr = serviceDr;
	}

	public String getEthinicity() {
		return ethinicity;
	}

	public void setEthinicity(String ethinicity) {
		this.ethinicity = ethinicity;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getPrefLang() {
		return prefLang;
	}

	public void setPrefLang(String prefLang) {
		this.prefLang = prefLang;
	}

}