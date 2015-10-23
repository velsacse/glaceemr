package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="chart")
public class Chart {
	
	@Id
	Integer chart_id;
	
	@NotNull
	@Column(unique=true)
	Integer chart_patientid;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="chart_patientid",referencedColumnName="patient_registration_id",insertable=false,updatable=false)
    PatientRegistration patientRegistrationTable;

	public Integer getChart_id() {
		return chart_id;
	}

	public void setChart_id(Integer chart_id) {
		this.chart_id = chart_id;
	}

	public Integer getChart_patientid() {
		return chart_patientid;
	}

	public void setChart_patientid(Integer chart_patientid) {
		this.chart_patientid = chart_patientid;
	}
	
}
