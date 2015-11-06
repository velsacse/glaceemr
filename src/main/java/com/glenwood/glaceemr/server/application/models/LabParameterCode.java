package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

@Entity
@Table(name = "lab_parameter_code")
public class LabParameterCode {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="lab_parameter_code_lab_parameter_code_id_seq")
	@SequenceGenerator(name ="lab_parameter_code_lab_parameter_code_id_seq", sequenceName="lab_parameter_code_lab_parameter_code_id_seq", allocationSize=1)
	@Column(name="lab_parameter_code_id")
	private Integer labParameterCodeId;

	@Column(name="lab_parameter_code_paramid")
	private Integer labParameterCodeParamid;

	@Column(name="lab_parameter_code_value")
	private String labParameterCodeValue;

	@Column(name="lab_parameter_code_system")
	private String labParameterCodeSystem;

	public Integer getLabParameterCodeId() {
		return labParameterCodeId;
	}

	public void setLabParameterCodeId(Integer labParameterCodeId) {
		this.labParameterCodeId = labParameterCodeId;
	}

	public Integer getLabParameterCodeParamid() {
		return labParameterCodeParamid;
	}

	public void setLabParameterCodeParamid(Integer labParameterCodeParamid) {
		this.labParameterCodeParamid = labParameterCodeParamid;
	}

	public String getLabParameterCodeValue() {
		return labParameterCodeValue;
	}

	public void setLabParameterCodeValue(String labParameterCodeValue) {
		this.labParameterCodeValue = labParameterCodeValue;
	}

	public String getLabParameterCodeSystem() {
		return labParameterCodeSystem;
	}

	public void setLabParameterCodeSystem(String labParameterCodeSystem) {
		this.labParameterCodeSystem = labParameterCodeSystem;
	}
	
}