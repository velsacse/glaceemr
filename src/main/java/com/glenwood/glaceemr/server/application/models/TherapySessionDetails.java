package com.glenwood.glaceemr.server.application.models;



import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "therapy_session_details")
public class TherapySessionDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="therapy_session_details_therapy_session_details_id_seq")
	@SequenceGenerator(name ="therapy_session_details_therapy_session_details_id_seq", sequenceName="therapy_session_details_therapy_session_details_id_seq", allocationSize=1)
	@Column(name="therapy_session_details_id")
	private Integer therapySessionDetailsId;

	@Column(name="therapy_session_details_session_id")
	private Integer therapySessionDetailsSessionId;

	@Column(name="therapy_session_details_patient_id")
	private Integer therapySessionDetailsPatientId;

	@Column(name="therapy_session_details_patient_note")
	private String therapySessionDetailsPatientNote;

	@Column(name="therapy_session_details_patient_xml_note")
	private String therapySessionDetailsPatientXmlNote;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="therapy_session_details_session_id",referencedColumnName="therapy_session_id",insertable=false, updatable=false)
	@JsonBackReference
	TherapySession therapySession ;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name="therapy_session_details_patient_id", referencedColumnName="patient_registration_id" , insertable=false, updatable=false)
	private PatientRegistration patientRegistration;

	
	
	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public TherapySession getTherapySession() {
		return therapySession;
	}

	public void setTherapySession(TherapySession therapySession) {
		this.therapySession = therapySession;
	}

	public Integer getTherapySessionDetailsId() {
		return therapySessionDetailsId;
	}

	public Integer getTherapySessionDetailsSessionId() {
		return therapySessionDetailsSessionId;
	}

	public Integer getTherapySessionDetailsPatientId() {
		return therapySessionDetailsPatientId;
	}

	public String getTherapySessionDetailsPatientNote() {
		return therapySessionDetailsPatientNote;
	}

	public String getTherapySessionDetailsPatientXmlNote() {
		return therapySessionDetailsPatientXmlNote;
	}

	public void setTherapySessionDetailsId(Integer therapySessionDetailsId) {
		this.therapySessionDetailsId = therapySessionDetailsId;
	}

	public void setTherapySessionDetailsSessionId(
			Integer therapySessionDetailsSessionId) {
		this.therapySessionDetailsSessionId = therapySessionDetailsSessionId;
	}

	public void setTherapySessionDetailsPatientId(
			Integer therapySessionDetailsPatientId) {
		this.therapySessionDetailsPatientId = therapySessionDetailsPatientId;
	}

	public void setTherapySessionDetailsPatientNote(
			String therapySessionDetailsPatientNote) {
		this.therapySessionDetailsPatientNote = therapySessionDetailsPatientNote;
	}

	public void setTherapySessionDetailsPatientXmlNote(
			String therapySessionDetailsPatientXmlNote) {
		this.therapySessionDetailsPatientXmlNote = therapySessionDetailsPatientXmlNote;
	}
	
	
}