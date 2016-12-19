package com.glenwood.glaceemr.server.application.services.Documents;

public class PatientInsConsentFormDetailsBean {
	private Integer patientRegistrationId;
	private String insCompanyName;
	private String patientInsDetailPatientinsuranceid;
	private String patientInsDetailSubscribername;
	private Short patientInsDetailRelationtosubs;
	private String patientInsDetailGroupno;
	private Integer patientInsDetailInstype;
	private String patientInsDetailSubscriberDob;
	
	public PatientInsConsentFormDetailsBean(Integer patientRegistrationId,String insCompanyName,String patientInsDetailPatientinsuranceid,String patientInsDetailSubscribername,Short patientInsDetailRelationtosubs,String patientInsDetailGroupno,Integer patientInsDetailInstype,String patientInsDetailSubscriberDob){
		super();
		this.patientRegistrationId=patientRegistrationId;
		this.insCompanyName=insCompanyName;
		this.patientInsDetailPatientinsuranceid=patientInsDetailPatientinsuranceid;
		this.patientInsDetailSubscribername=patientInsDetailSubscribername;
		this.patientInsDetailRelationtosubs=patientInsDetailRelationtosubs;
		this.patientInsDetailGroupno=patientInsDetailGroupno;
		this.patientInsDetailInstype=patientInsDetailInstype;
		this.patientInsDetailSubscriberDob=patientInsDetailSubscriberDob;
	}

	public String getPatientInsDetailSubscriberDob() {
		return patientInsDetailSubscriberDob;
	}

	public void setPatientInsDetailSubscriberDob(
			String patientInsDetailSubscriberDob) {
		this.patientInsDetailSubscriberDob = patientInsDetailSubscriberDob;
	}

	public Integer getPatientRegistrationId() {
		return patientRegistrationId;
	}

	public void setPatientRegistrationId(Integer patientRegistrationId) {
		this.patientRegistrationId = patientRegistrationId;
	}

	public String getInsCompanyName() {
		return insCompanyName;
	}

	public void setInsCompanyName(String insCompanyName) {
		this.insCompanyName = insCompanyName;
	}

	public String getPatientInsDetailPatientinsuranceid() {
		return patientInsDetailPatientinsuranceid;
	}

	public void setPatientInsDetailPatientinsuranceid(
			String patientInsDetailPatientinsuranceid) {
		this.patientInsDetailPatientinsuranceid = patientInsDetailPatientinsuranceid;
	}

	public String getPatientInsDetailSubscribername() {
		return patientInsDetailSubscribername;
	}

	public void setPatientInsDetailSubscribername(
			String patientInsDetailSubscribername) {
		this.patientInsDetailSubscribername = patientInsDetailSubscribername;
	}

	public Short getPatientInsDetailRelationtosubs() {
		return patientInsDetailRelationtosubs;
	}

	public void setPatientInsDetailRelationtosubs(
			Short patientInsDetailRelationtosubs) {
		this.patientInsDetailRelationtosubs = patientInsDetailRelationtosubs;
	}

	public String getPatientInsDetailGroupno() {
		return patientInsDetailGroupno;
	}

	public void setPatientInsDetailGroupno(String patientInsDetailGroupno) {
		this.patientInsDetailGroupno = patientInsDetailGroupno;
	}

	public Integer getPatientInsDetailInstype() {
		return patientInsDetailInstype;
	}

	public void setPatientInsDetailInstype(Integer patientInsDetailInstype) {
		this.patientInsDetailInstype = patientInsDetailInstype;
	}
}
