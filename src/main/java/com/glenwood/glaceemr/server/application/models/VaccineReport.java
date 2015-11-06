package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "vaccine_report")
public class VaccineReport {

	@Id
	@Column(name="vaccine_report_id")
	private Integer vaccineReportId;

	@Column(name="vaccine_report_chart_id")
	private Integer vaccineReportChartId;

	@Column(name="vaccine_report_encounter_id")
	private Integer vaccineReportEncounterId;

	@Column(name="vaccine_report_vaccine_id")
	private Integer vaccineReportVaccineId;

	@JsonSerialize(using=JsonTimestampSerializer.class)
	@Column(name="vaccine_report_given_date")
	private Timestamp vaccineReportGivenDate;

	@Column(name="vaccine_report_dosage_level")
	private Integer vaccineReportDosageLevel;

	@Column(name="vaccine_report_comments")
	private String vaccineReportComments;

	@Column(name="vaccine_report_isemr")
	private Integer vaccineReportIsemr;

	@Column(name="vaccine_report_userid")
	private Integer vaccineReportUserid;

	@JsonSerialize(using=JsonTimestampSerializer.class)
	@Column(name="vaccine_report_last_modified")
	private Timestamp vaccineReportLastModified;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="vaccine_report_vaccine_id", referencedColumnName="lab_description_testid", insertable=false, updatable=false)
	LabDescription labDescriptionTable;

	public Integer getVaccineReportId() {
		return vaccineReportId;
	}

	public void setVaccineReportId(Integer vaccineReportId) {
		this.vaccineReportId = vaccineReportId;
	}

	public Integer getVaccineReportChartId() {
		return vaccineReportChartId;
	}

	public void setVaccineReportChartId(Integer vaccineReportChartId) {
		this.vaccineReportChartId = vaccineReportChartId;
	}

	public Integer getVaccineReportEncounterId() {
		return vaccineReportEncounterId;
	}

	public void setVaccineReportEncounterId(Integer vaccineReportEncounterId) {
		this.vaccineReportEncounterId = vaccineReportEncounterId;
	}

	public Integer getVaccineReportVaccineId() {
		return vaccineReportVaccineId;
	}

	public void setVaccineReportVaccineId(Integer vaccineReportVaccineId) {
		this.vaccineReportVaccineId = vaccineReportVaccineId;
	}

	public Timestamp getVaccineReportGivenDate() {
		return vaccineReportGivenDate;
	}

	public void setVaccineReportGivenDate(Timestamp vaccineReportGivenDate) {
		this.vaccineReportGivenDate = vaccineReportGivenDate;
	}

	public Integer getVaccineReportDosageLevel() {
		return vaccineReportDosageLevel;
	}

	public void setVaccineReportDosageLevel(Integer vaccineReportDosageLevel) {
		this.vaccineReportDosageLevel = vaccineReportDosageLevel;
	}

	public String getVaccineReportComments() {
		return vaccineReportComments;
	}

	public void setVaccineReportComments(String vaccineReportComments) {
		this.vaccineReportComments = vaccineReportComments;
	}

	public Integer getVaccineReportIsemr() {
		return vaccineReportIsemr;
	}

	public void setVaccineReportIsemr(Integer vaccineReportIsemr) {
		this.vaccineReportIsemr = vaccineReportIsemr;
	}

	public Integer getVaccineReportUserid() {
		return vaccineReportUserid;
	}

	public void setVaccineReportUserid(Integer vaccineReportUserid) {
		this.vaccineReportUserid = vaccineReportUserid;
	}

	public Timestamp getVaccineReportLastModified() {
		return vaccineReportLastModified;
	}

	public void setVaccineReportLastModified(Timestamp vaccineReportLastModified) {
		this.vaccineReportLastModified = vaccineReportLastModified;
	}
}