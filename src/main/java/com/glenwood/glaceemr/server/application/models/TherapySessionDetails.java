package com.glenwood.glaceemr.server.application.models;



import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	
	@Column(name="therapy_session_end_time")
	private String therapySessionEndTime;
	
	@Column(name="therapy_session_details_dx1")
	private String therapySessionDetailsDx1;

	@Column(name="therapy_session_details_dx2")
	private String therapySessionDetailsDx2;

	@Column(name="therapy_session_details_dx3")
	private String therapySessionDetailsDx3;

	@Column(name="therapy_session_details_dx4")
	private String therapySessionDetailsDx4;

	@Column(name="therapy_session_details_dx5")
	private String therapySessionDetailsDx5;

	@Column(name="therapy_session_details_dx6")
	private String therapySessionDetailsDx6;

	@Column(name="therapy_session_details_dx7")
	private String therapySessionDetailsDx7;

	@Column(name="therapy_session_details_dx8")
	private String therapySessionDetailsDx8;

	@Column(name="therapy_session_details_dx1desc")
	private String therapySessionDetailsDx1desc;

	@Column(name="therapy_session_details_dx2desc")
	private String therapySessionDetailsDx2desc;

	@Column(name="therapy_session_details_dx3desc")
	private String therapySessionDetailsDx3desc;

	@Column(name="therapy_session_details_dx4desc")
	private String therapySessionDetailsDx4desc;

	@Column(name="therapy_session_details_dx5desc")
	private String therapySessionDetailsDx5desc;

	@Column(name="therapy_session_details_dx6desc")
	private String therapySessionDetailsDx6desc;

	@Column(name="therapy_session_details_dx7desc")
	private String therapySessionDetailsDx7desc;

	@Column(name="therapy_session_details_dx8desc")
	private String therapySessionDetailsDx8desc;
	
	@Column(name="therapy_session_details_end_reason")
	private String therapySessionDetailsLeftEarlyReason;
	
	@Column(name="therapy_session_details_start_time")
	private String therapySessionDetailsStartTime;
	
	@Column(name="therapy_session_details_start_late_reason")
	private String therapySessionDetailsStartLateReason;
	
	
	public String getTherapySessionDetailsDx1() {
		return therapySessionDetailsDx1;
	}

	public void setTherapySessionDetailsDx1(String therapySessionDetailsDx1) {
		this.therapySessionDetailsDx1 = therapySessionDetailsDx1;
	}

	public String getTherapySessionDetailsDx2() {
		return therapySessionDetailsDx2;
	}

	public void setTherapySessionDetailsDx2(String therapySessionDetailsDx2) {
		this.therapySessionDetailsDx2 = therapySessionDetailsDx2;
	}

	public String getTherapySessionDetailsDx3() {
		return therapySessionDetailsDx3;
	}

	public void setTherapySessionDetailsDx3(String therapySessionDetailsDx3) {
		this.therapySessionDetailsDx3 = therapySessionDetailsDx3;
	}

	public String getTherapySessionDetailsDx4() {
		return therapySessionDetailsDx4;
	}

	public void setTherapySessionDetailsDx4(String therapySessionDetailsDx4) {
		this.therapySessionDetailsDx4 = therapySessionDetailsDx4;
	}

	public String getTherapySessionDetailsDx5() {
		return therapySessionDetailsDx5;
	}

	public void setTherapySessionDetailsDx5(String therapySessionDetailsDx5) {
		this.therapySessionDetailsDx5 = therapySessionDetailsDx5;
	}

	public String getTherapySessionDetailsDx6() {
		return therapySessionDetailsDx6;
	}

	public void setTherapySessionDetailsDx6(String therapySessionDetailsDx6) {
		this.therapySessionDetailsDx6 = therapySessionDetailsDx6;
	}

	public String getTherapySessionDetailsDx7() {
		return therapySessionDetailsDx7;
	}

	public void setTherapySessionDetailsDx7(String therapySessionDetailsDx7) {
		this.therapySessionDetailsDx7 = therapySessionDetailsDx7;
	}

	public String getTherapySessionDetailsDx8() {
		return therapySessionDetailsDx8;
	}

	public void setTherapySessionDetailsDx8(String therapySessionDetailsDx8) {
		this.therapySessionDetailsDx8 = therapySessionDetailsDx8;
	}

	public String getTherapySessionDetailsDx1desc() {
		return therapySessionDetailsDx1desc;
	}

	public void setTherapySessionDetailsDx1desc(String therapySessionDetailsDx1desc) {
		this.therapySessionDetailsDx1desc = therapySessionDetailsDx1desc;
	}

	public String getTherapySessionDetailsDx2desc() {
		return therapySessionDetailsDx2desc;
	}

	public void setTherapySessionDetailsDx2desc(String therapySessionDetailsDx2desc) {
		this.therapySessionDetailsDx2desc = therapySessionDetailsDx2desc;
	}

	public String getTherapySessionDetailsDx3desc() {
		return therapySessionDetailsDx3desc;
	}

	public void setTherapySessionDetailsDx3desc(String therapySessionDetailsDx3desc) {
		this.therapySessionDetailsDx3desc = therapySessionDetailsDx3desc;
	}

	public String getTherapySessionDetailsDx4desc() {
		return therapySessionDetailsDx4desc;
	}

	public void setTherapySessionDetailsDx4desc(String therapySessionDetailsDx4desc) {
		this.therapySessionDetailsDx4desc = therapySessionDetailsDx4desc;
	}

	public String getTherapySessionDetailsDx5desc() {
		return therapySessionDetailsDx5desc;
	}

	public void setTherapySessionDetailsDx5desc(String therapySessionDetailsDx5desc) {
		this.therapySessionDetailsDx5desc = therapySessionDetailsDx5desc;
	}

	public String getTherapySessionDetailsDx6desc() {
		return therapySessionDetailsDx6desc;
	}

	public void setTherapySessionDetailsDx6desc(String therapySessionDetailsDx6desc) {
		this.therapySessionDetailsDx6desc = therapySessionDetailsDx6desc;
	}

	public String getTherapySessionDetailsDx7desc() {
		return therapySessionDetailsDx7desc;
	}

	public void setTherapySessionDetailsDx7desc(String therapySessionDetailsDx7desc) {
		this.therapySessionDetailsDx7desc = therapySessionDetailsDx7desc;
	}

	public String getTherapySessionDetailsDx8desc() {
		return therapySessionDetailsDx8desc;
	}

	public void setTherapySessionDetailsDx8desc(String therapySessionDetailsDx8desc) {
		this.therapySessionDetailsDx8desc = therapySessionDetailsDx8desc;
	}

	public String getTherapySessionDetailsLeftEarlyReason() {
		return therapySessionDetailsLeftEarlyReason;
	}

	public void setTherapySessionDetailsLeftEarlyReason(
			String therapySessionDetailsLeftEarlyReason) {
		this.therapySessionDetailsLeftEarlyReason = therapySessionDetailsLeftEarlyReason;
	}

	public String getTherapySessionEndTime() {
		return therapySessionEndTime;
	}

	public void setTherapySessionEndTime(String therapySessionEndTime) {
		this.therapySessionEndTime = therapySessionEndTime;
	}
	
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
	
	@OneToMany(mappedBy="therapySessionPatientDetails", fetch=FetchType.LAZY)
	@JsonManagedReference
	List<TherapySessionPatientDetails> therapySessionDetails;


	public List<TherapySessionPatientDetails> getTherapySessionDetails() {
		return therapySessionDetails;
	}

	public void setTherapySessionDetails(
			List<TherapySessionPatientDetails> therapySessionDetails) {
		this.therapySessionDetails = therapySessionDetails;
	}


	public List<TherapySessionPatientDetails> getTherapySessionPatientDetails() {
		return therapySessionPatientDetails;
	}

	public void setTherapySessionPatientDetails(
			List<TherapySessionPatientDetails> therapySessionPatientDetails) {
		this.therapySessionPatientDetails = therapySessionPatientDetails;
	}

	@OneToMany(mappedBy="therapySessionDetailsPatientId", fetch=FetchType.LAZY)
	@JsonManagedReference
	List<TherapySessionPatientDetails> therapySessionPatientDetails;
	

	public String getTherapySessionDetailsStartTime() {
		return therapySessionDetailsStartTime;
	}

	public void setTherapySessionDetailsStartTime(
			String therapySessionDetailsStartTime) {
		this.therapySessionDetailsStartTime = therapySessionDetailsStartTime;
	}

	public String getTherapySessionDetailsStartLateReason() {
		return therapySessionDetailsStartLateReason;
	}

	public void setTherapySessionDetailsStartLateReason(
			String therapySessionDetailsStartLateReason) {
		this.therapySessionDetailsStartLateReason = therapySessionDetailsStartLateReason;
	}
}