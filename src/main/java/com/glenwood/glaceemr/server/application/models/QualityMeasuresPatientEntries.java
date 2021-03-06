package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "quality_measures_patient_entries")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QualityMeasuresPatientEntries {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="quality_measures_patient_entr_quality_measures_patient_entr_seq")
	@SequenceGenerator(name = "quality_measures_patient_entr_quality_measures_patient_entr_seq", sequenceName="quality_measures_patient_entr_quality_measures_patient_entr_seq", allocationSize=1)
	@Column(name="quality_measures_patient_entries_id")
	private Integer qualityMeasuresPatientEntriesId;

	@Column(name="quality_measures_patient_entries_patient_id")
	private Integer qualityMeasuresPatientEntriesPatientId;

	@Column(name="quality_measures_patient_entries_reporting_year")
	private Integer qualityMeasuresPatientEntriesReportingYear;

	@Column(name="quality_measures_patient_entries_measure_id")
	private String qualityMeasuresPatientEntriesMeasureId;

	@Column(name="quality_measures_patient_entries_cmsId")
	private String qualityMeasuresPatientEntriesCMSId;
	
	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="quality_measures_patient_entries_updated_on")
	private Timestamp qualityMeasuresPatientEntriesUpdatedOn;

	@Column(name="quality_measures_patient_entries_ipp")
	private Integer qualityMeasuresPatientEntriesIpp;

	@Column(name="quality_measures_patient_entries_denominator")
	private Integer qualityMeasuresPatientEntriesDenominator;

	@Column(name="quality_measures_patient_entries_denominator_exclusion")
	private Integer qualityMeasuresPatientEntriesDenominatorExclusion;

	@Column(name="quality_measures_patient_entries_numerator")
	private Integer qualityMeasuresPatientEntriesNumerator;

	@Column(name="quality_measures_patient_entries_numerator_exclusion")
	private Integer qualityMeasuresPatientEntriesNumeratorExclusion;

	@Column(name="quality_measures_patient_entries_denominator_exception")
	private Integer qualityMeasuresPatientEntriesDenominatorException;

	@Column(name="quality_measures_patient_entries_measure_population")
	private Integer qualityMeasuresPatientEntriesMeasurePopulation;

	@Column(name="quality_measures_patient_entries_measure_population_exclusion")
	private Integer qualityMeasuresPatientEntriesMeasurePopulationExclusion;

	@Column(name="quality_measures_patient_entries_measure_observation")
	private Integer qualityMeasuresPatientEntriesMeasureObservation;

	@Column(name="quality_measures_patient_entries_provider_id")
	private Integer qualityMeasuresPatientEntriesProviderId;

	@Column(name="quality_measures_patient_entries_criteria")
	private Integer qualityMeasuresPatientEntriesCriteria;

	@Column(name="quality_measures_patient_entries_npi")
	private String qualityMeasuresPatientEntriesNpi;

	@Column(name="quality_measures_patient_entries_tin")
	private String qualityMeasuresPatientEntriesTin;

	public Integer getQualityMeasuresPatientEntriesId() {
		return qualityMeasuresPatientEntriesId;
	}

	public void setQualityMeasuresPatientEntriesId(
			Integer qualityMeasuresPatientEntriesId) {
		this.qualityMeasuresPatientEntriesId = qualityMeasuresPatientEntriesId;
	}

	public Integer getQualityMeasuresPatientEntriesPatientId() {
		return qualityMeasuresPatientEntriesPatientId;
	}

	public void setQualityMeasuresPatientEntriesPatientId(
			Integer qualityMeasuresPatientEntriesPatientId) {
		this.qualityMeasuresPatientEntriesPatientId = qualityMeasuresPatientEntriesPatientId;
	}

	public Integer getQualityMeasuresPatientEntriesReportingYear() {
		return qualityMeasuresPatientEntriesReportingYear;
	}

	public void setQualityMeasuresPatientEntriesReportingYear(
			Integer qualityMeasuresPatientEntriesReportingYear) {
		this.qualityMeasuresPatientEntriesReportingYear = qualityMeasuresPatientEntriesReportingYear;
	}

	public String getQualityMeasuresPatientEntriesMeasureId() {
		return qualityMeasuresPatientEntriesMeasureId;
	}

	public void setQualityMeasuresPatientEntriesMeasureId(
			String qualityMeasuresPatientEntriesMeasureId) {
		this.qualityMeasuresPatientEntriesMeasureId = qualityMeasuresPatientEntriesMeasureId;
	}
	
	public String getQualityMeasuresPatientEntriesCMSId() {
		return qualityMeasuresPatientEntriesCMSId;
	}

	public void setQualityMeasuresPatientEntriesCMSId(
			String qualityMeasuresPatientEntriesCMSId) {
		this.qualityMeasuresPatientEntriesCMSId = qualityMeasuresPatientEntriesCMSId;
	}

	public Timestamp getQualityMeasuresPatientEntriesUpdatedOn() {
		return qualityMeasuresPatientEntriesUpdatedOn;
	}

	public void setQualityMeasuresPatientEntriesUpdatedOn(
			Timestamp qualityMeasuresPatientEntriesUpdatedOn) {
		this.qualityMeasuresPatientEntriesUpdatedOn = qualityMeasuresPatientEntriesUpdatedOn;
	}

	public Integer getQualityMeasuresPatientEntriesIpp() {
		return qualityMeasuresPatientEntriesIpp;
	}

	public void setQualityMeasuresPatientEntriesIpp(
			Integer qualityMeasuresPatientEntriesIpp) {
		this.qualityMeasuresPatientEntriesIpp = qualityMeasuresPatientEntriesIpp;
	}

	public Integer getQualityMeasuresPatientEntriesDenominator() {
		return qualityMeasuresPatientEntriesDenominator;
	}

	public void setQualityMeasuresPatientEntriesDenominator(
			Integer qualityMeasuresPatientEntriesDenominator) {
		this.qualityMeasuresPatientEntriesDenominator = qualityMeasuresPatientEntriesDenominator;
	}

	public Integer getQualityMeasuresPatientEntriesDenominatorExclusion() {
		return qualityMeasuresPatientEntriesDenominatorExclusion;
	}

	public void setQualityMeasuresPatientEntriesDenominatorExclusion(
			Integer qualityMeasuresPatientEntriesDenominatorExclusion) {
		this.qualityMeasuresPatientEntriesDenominatorExclusion = qualityMeasuresPatientEntriesDenominatorExclusion;
	}

	public Integer getQualityMeasuresPatientEntriesNumerator() {
		return qualityMeasuresPatientEntriesNumerator;
	}

	public void setQualityMeasuresPatientEntriesNumerator(
			Integer qualityMeasuresPatientEntriesNumerator) {
		this.qualityMeasuresPatientEntriesNumerator = qualityMeasuresPatientEntriesNumerator;
	}

	public Integer getQualityMeasuresPatientEntriesNumeratorExclusion() {
		return qualityMeasuresPatientEntriesNumeratorExclusion;
	}

	public void setQualityMeasuresPatientEntriesNumeratorExclusion(
			Integer qualityMeasuresPatientEntriesNumeratorExclusion) {
		this.qualityMeasuresPatientEntriesNumeratorExclusion = qualityMeasuresPatientEntriesNumeratorExclusion;
	}

	public Integer getQualityMeasuresPatientEntriesDenominatorException() {
		return qualityMeasuresPatientEntriesDenominatorException;
	}

	public void setQualityMeasuresPatientEntriesDenominatorException(
			Integer qualityMeasuresPatientEntriesDenominatorException) {
		this.qualityMeasuresPatientEntriesDenominatorException = qualityMeasuresPatientEntriesDenominatorException;
	}

	public Integer getQualityMeasuresPatientEntriesMeasurePopulation() {
		return qualityMeasuresPatientEntriesMeasurePopulation;
	}

	public void setQualityMeasuresPatientEntriesMeasurePopulation(
			Integer qualityMeasuresPatientEntriesMeasurePopulation) {
		this.qualityMeasuresPatientEntriesMeasurePopulation = qualityMeasuresPatientEntriesMeasurePopulation;
	}

	public Integer getQualityMeasuresPatientEntriesMeasurePopulationExclusion() {
		return qualityMeasuresPatientEntriesMeasurePopulationExclusion;
	}

	public void setQualityMeasuresPatientEntriesMeasurePopulationExclusion(
			Integer qualityMeasuresPatientEntriesMeasurePopulationExclusion) {
		this.qualityMeasuresPatientEntriesMeasurePopulationExclusion = qualityMeasuresPatientEntriesMeasurePopulationExclusion;
	}

	public Integer getQualityMeasuresPatientEntriesMeasureObservation() {
		return qualityMeasuresPatientEntriesMeasureObservation;
	}

	public void setQualityMeasuresPatientEntriesMeasureObservation(
			Integer qualityMeasuresPatientEntriesMeasureObservation) {
		this.qualityMeasuresPatientEntriesMeasureObservation = qualityMeasuresPatientEntriesMeasureObservation;
	}

	public Integer getQualityMeasuresPatientEntriesProviderId() {
		return qualityMeasuresPatientEntriesProviderId;
	}

	public void setQualityMeasuresPatientEntriesProviderId(
			Integer qualityMeasuresPatientEntriesProviderId) {
		this.qualityMeasuresPatientEntriesProviderId = qualityMeasuresPatientEntriesProviderId;
	}

	public Integer getQualityMeasuresPatientEntriesCriteria() {
		return qualityMeasuresPatientEntriesCriteria;
	}

	public void setQualityMeasuresPatientEntriesCriteria(
			Integer qualityMeasuresPatientEntriesCriteria) {
		this.qualityMeasuresPatientEntriesCriteria = qualityMeasuresPatientEntriesCriteria;
	}

	public String getQualityMeasuresPatientEntriesNpi() {
		return qualityMeasuresPatientEntriesNpi;
	}

	public void setQualityMeasuresPatientEntriesNpi(
			String qualityMeasuresPatientEntriesNpi) {
		this.qualityMeasuresPatientEntriesNpi = qualityMeasuresPatientEntriesNpi;
	}

	public String getQualityMeasuresPatientEntriesTin() {
		return qualityMeasuresPatientEntriesTin;
	}

	public void setQualityMeasuresPatientEntriesTin(
			String qualityMeasuresPatientEntriesTin) {
		this.qualityMeasuresPatientEntriesTin = qualityMeasuresPatientEntriesTin;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="quality_measures_patient_entries_patient_id", referencedColumnName="patient_registration_id", insertable=false, updatable=false)
	@JsonBackReference
	PatientRegistration patientRegistration;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="quality_measures_patient_entries_provider_id", referencedColumnName="emp_profile_empid", insertable=false, updatable=false)
	@JsonBackReference
	EmployeeProfile empProfile;
	
}