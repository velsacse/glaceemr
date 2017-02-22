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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "quality_measures_provider_mapping")
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="quality_measures_provider_mapping_provider_id",referencedColumnName="macra_provider_configuration_provider_id",insertable=false,updatable=false)
    MacraProviderConfiguration macraProviderConfiguration;
	
}