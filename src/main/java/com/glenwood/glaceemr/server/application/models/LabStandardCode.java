package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "lab_standard_code")
public class LabStandardCode {

	@Id
	@Column(name="lab_standard_code_id", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="lab_standard_code_lab_standard_code_id_seq")
	@SequenceGenerator(name ="lab_standard_code_lab_standard_code_id_seq", sequenceName="lab_standard_code_lab_standard_code_id_seq", allocationSize=1)
	private Integer labStandardCodeId;

	@Column(name="lab_standard_code_group_id")
	private Integer labStandardCodeGroupId;

	@Column(name="lab_standard_code_group_code", length=50)
	private String labStandardCodeGroupCode;
	
	@Column(name="lab_standard_code_group_gwid")
	private Integer labStandardCodeGroupGwid;
	
	@Column(name="lab_standard_code_group_codesystem", length=100)
	private String labStandardCodeGroupCodesystem;

	@OneToOne
	@JoinColumn(name="lab_standard_code_group_gwid",referencedColumnName="lab_description_testid",insertable=false,updatable=false)
	@JsonManagedReference
	LabDescription labDescriptionTable;
	
	public Integer getLabStandardCodeId() {
		return labStandardCodeId;
	}

	public void setLabStandardCodeId(Integer labStandardCodeId) {
		this.labStandardCodeId = labStandardCodeId;
	}

	public Integer getLabStandardCodeGroupId() {
		return labStandardCodeGroupId;
	}

	public void setLabStandardCodeGroupId(Integer labStandardCodeGroupId) {
		this.labStandardCodeGroupId = labStandardCodeGroupId;
	}

	public String getLabStandardCodeGroupCode() {
		return labStandardCodeGroupCode;
	}

	public void setLabStandardCodeGroupCode(String labStandardCodeGroupCode) {
		this.labStandardCodeGroupCode = labStandardCodeGroupCode;
	}

	public Integer getLabStandardCodeGroupGwid() {
		return labStandardCodeGroupGwid;
	}

	public void setLabStandardCodeGroupGwid(Integer labStandardCodeGroupGwid) {
		this.labStandardCodeGroupGwid = labStandardCodeGroupGwid;
	}
	
	public String getLabStandardCodeGroupCodesystem() {
		return labStandardCodeGroupCodesystem;
	}

	public void setLabStandardCodeGroupCodesystem(
			String labStandardCodeGroupCodesystem) {
		this.labStandardCodeGroupCodesystem = labStandardCodeGroupCodesystem;
	}

	public LabDescription getLabDescriptionTable() {
		return labDescriptionTable;
	}

	public void setLabDescriptionTable(LabDescription labDescriptionTable) {
		this.labDescriptionTable = labDescriptionTable;
	}
}