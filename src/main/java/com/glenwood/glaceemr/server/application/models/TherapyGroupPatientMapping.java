package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;
import java.sql.Date;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "therapy_group_patient_mapping")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TherapyGroupPatientMapping {
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="therapy_group_patient_mapping_therapy_group_patient_mapping_seq")
	@SequenceGenerator(name ="therapy_group_patient_mapping_therapy_group_patient_mapping_seq", sequenceName="therapy_group_patient_mapping_therapy_group_patient_mapping_seq", allocationSize=1)
	@Column(name="therapy_group_patient_mapping_id")
	private Integer therapyGroupPatientMappingId;

	@Column(name="therapy_group_patient_mapping_group_id")
	private Integer therapyGroupPatientMappingGroupId;

	@Column(name="therapy_group_patient_mapping_patient_id")
	private Integer therapyGroupPatientMappingPatientId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name="therapy_group_patient_mapping_patient_id", referencedColumnName="patient_registration_id" , insertable=false, updatable=false)
	private PatientRegistration patientRegistration;

	
	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public Integer getTherapyGroupPatientMappingId() {
		return therapyGroupPatientMappingId;
	}

	public Integer getTherapyGroupPatientMappingGroupId() {
		return therapyGroupPatientMappingGroupId;
	}

	public Integer getTherapyGroupPatientMappingPatientId() {
		return therapyGroupPatientMappingPatientId;
	}

	public void setTherapyGroupPatientMappingId(Integer therapyGroupPatientMappingId) {
		this.therapyGroupPatientMappingId = therapyGroupPatientMappingId;
	}

	public void setTherapyGroupPatientMappingGroupId(
			Integer therapyGroupPatientMappingGroupId) {
		this.therapyGroupPatientMappingGroupId = therapyGroupPatientMappingGroupId;
	}

	public void setTherapyGroupPatientMappingPatientId(
			Integer therapyGroupPatientMappingPatientId) {
		this.therapyGroupPatientMappingPatientId = therapyGroupPatientMappingPatientId;
	}
	
	
}