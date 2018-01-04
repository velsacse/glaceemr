package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "quality_measures_provider_mapping")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QualityMeasuresProviderMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="quality_measures_provider_map_quality_measures_provider_map_seq")
	@SequenceGenerator(name = "quality_measures_provider_map_quality_measures_provider_map_seq", sequenceName="quality_measures_provider_map_quality_measures_provider_map_seq", allocationSize=1)
	@Column(name="quality_measures_provider_mapping_id")
	private Integer qualityMeasuresProviderMappingId;

	@Column(name="quality_measures_provider_mapping_provider_id")
	private Integer qualityMeasuresProviderMappingProviderId;

	@Column(name="quality_measures_provider_mapping_reporting_year")
	private Integer qualityMeasuresProviderMappingReportingYear;

	@Column(name="quality_measures_provider_mapping_measure_id")
	private String qualityMeasuresProviderMappingMeasureId;

	@Column(name="quality_measures_provider_mapping_cmsId")
	private String qualityMeasuresProviderMappingCMSId;

	@Column(name="quality_measures_provider_mapping_title")
	private String qualityMeasuresProviderMappingTitle;

	@Column(name="quality_measures_provider_mapping_priority")
	private String qualityMeasuresProviderMappingPriority;


	public Integer getQualityMeasuresProviderMappingId() {
		return qualityMeasuresProviderMappingId;
	}

	public void setQualityMeasuresProviderMappingId(
			Integer qualityMeasuresProviderMappingId) {
		this.qualityMeasuresProviderMappingId = qualityMeasuresProviderMappingId;
	}

	public Integer getQualityMeasuresProviderMappingProviderId() {
		return qualityMeasuresProviderMappingProviderId;
	}

	public void setQualityMeasuresProviderMappingProviderId(
			Integer qualityMeasuresProviderMappingProviderId) {
		this.qualityMeasuresProviderMappingProviderId = qualityMeasuresProviderMappingProviderId;
	}

	public Integer getQualityMeasuresProviderMappingReportingYear() {
		return qualityMeasuresProviderMappingReportingYear;
	}

	public void setQualityMeasuresProviderMappingReportingYear(
			Integer qualityMeasuresProviderMappingReportingYear) {
		this.qualityMeasuresProviderMappingReportingYear = qualityMeasuresProviderMappingReportingYear;
	}

	public String getQualityMeasuresProviderMappingMeasureId() {
		return qualityMeasuresProviderMappingMeasureId;
	}

	public void setQualityMeasuresProviderMappingMeasureId(
			String qualityMeasuresProviderMappingMeasureId) {
		this.qualityMeasuresProviderMappingMeasureId = qualityMeasuresProviderMappingMeasureId;
	}
	
	public String getQualityMeasuresProviderMappingCMSId() {
		return qualityMeasuresProviderMappingCMSId;
	}

	public void setQualityMeasuresProviderMappingCMSId(
			String qualityMeasuresProviderMappingCMSId) {
		this.qualityMeasuresProviderMappingCMSId = qualityMeasuresProviderMappingCMSId;
	}
	
	public String getQualityMeasuresProviderMappingTitle() {
		return qualityMeasuresProviderMappingTitle;
	}

	public void setQualityMeasuresProviderMappingTitle(
			String qualityMeasuresProviderMappingTitle) {
		this.qualityMeasuresProviderMappingTitle = qualityMeasuresProviderMappingTitle;
	}

	public String getQualityMeasuresProviderMappingPriority() {
		return qualityMeasuresProviderMappingPriority;
	}

	public void setQualityMeasuresProviderMappingPriority(
			String qualityMeasuresProviderMappingPriority) {
		this.qualityMeasuresProviderMappingPriority = qualityMeasuresProviderMappingPriority;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="quality_measures_provider_mapping_provider_id", referencedColumnName="emp_profile_empid" , insertable=false, updatable=false)
	private EmployeeProfile empProfile;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="quality_measures_provider_mapping_measure_id", referencedColumnName="ia_measures_measure_id" , insertable=false, updatable=false)
	private IAMeasures iaMeasures;
	
	
	
	
}