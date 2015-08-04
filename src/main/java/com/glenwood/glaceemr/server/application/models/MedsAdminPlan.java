package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;
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
@Table(name = "meds_admin_plan")
public class MedsAdminPlan {

	@Id
	@Column(name="meds_admin_plan_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="meds_admin_plan_id")
	@SequenceGenerator(name="meds_admin_plan_id",sequenceName="meds_admin_plan_id",allocationSize=1)
	private Integer medsAdminPlanId;

	@Column(name="meds_admin_plan_patient_id")
	private Integer medsAdminPlanPatientId;

	@Column(name="meds_admin_plan_medication_id")
	private Integer medsAdminPlanMedicationId;

	@Column(name="meds_admin_plan_time")
	private String medsAdminPlanTime;
	
	@Column(name="meds_admin_plan_saved_time")
	private Timestamp medsAdminPlanSavedTime;
	
@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="meds_admin_plan_medication_id",referencedColumnName="doc_presc_id",insertable=false, updatable=false)
	@JsonBackReference
	Prescription prescription;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="meds_admin_plan_medication_id",referencedColumnName="current_medication_id",insertable=false, updatable=false)
	@JsonBackReference
	CurrentMedication currentMedication;
	
	@OneToMany(mappedBy="medsAdminPlan", fetch=FetchType.EAGER)
	@JsonManagedReference
	List<MedsAdminLog> medsAdminLog;
	
	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}
	
	public List<MedsAdminLog> getMedsAdminLog() {
		return medsAdminLog;
	}
	
	public CurrentMedication getCurrentMedication() {
		return currentMedication;
	}

	public void setCurrentMedication(CurrentMedication currentMedication) {
		this.currentMedication = currentMedication;
	}

	public void setMedsAdminLog(List<MedsAdminLog> medsAdminLog) {
		this.medsAdminLog = medsAdminLog;
	}

	public Integer getMedsAdminPlanId() {
		return medsAdminPlanId;
	}

	public void setMedsAdminPlanId(Integer medsAdminPlanId) {
		this.medsAdminPlanId = medsAdminPlanId;
	}

	public Integer getMedsAdminPlanPatientId() {
		return medsAdminPlanPatientId;
	}

	public void setMedsAdminPlanPatientId(Integer medsAdminPlanPatientId) {
		this.medsAdminPlanPatientId = medsAdminPlanPatientId;
	}

	public Integer getMedsAdminPlanMedicationId() {
		return medsAdminPlanMedicationId;
	}

	public void setMedsAdminPlanMedicationId(Integer medsAdminPlanMedicationId) {
		this.medsAdminPlanMedicationId = medsAdminPlanMedicationId;
	}

	public String getMedsAdminPlanTime() {
		return medsAdminPlanTime;
	}

	public void setMedsAdminPlanTime(String medsAdminPlanTime) {
		this.medsAdminPlanTime = medsAdminPlanTime;
	}


	public Timestamp getMedsAdminPlanSavedTime() {
		return medsAdminPlanSavedTime;
	}

	public void setMedsAdminPlanSavedTime(Timestamp medsAdminPlanSavedTime) {
		this.medsAdminPlanSavedTime = medsAdminPlanSavedTime;
	}

}