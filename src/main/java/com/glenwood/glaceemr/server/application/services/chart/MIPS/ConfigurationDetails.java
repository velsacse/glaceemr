package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.Date;


public class ConfigurationDetails {
	
	public Integer macraProviderConfigurationReportingYear;

	public Date macraProviderConfigurationReportingStart;

	public Date macraProviderConfigurationReportingEnd;

	public Integer macraProviderConfigurationReportingMethod;
	
	public String qualityMeasuresProviderMappingMeasureId;
	
	public String getQualityMeasuresProviderMappingMeasureId() {
		return qualityMeasuresProviderMappingMeasureId;
	}

	public void setQualityMeasuresProviderMappingMeasureId(
			String qualityMeasuresProviderMappingMeasureId) {
		this.qualityMeasuresProviderMappingMeasureId = qualityMeasuresProviderMappingMeasureId;
	}

	public Integer getMacraProviderConfigurationReportingYear() {
		return macraProviderConfigurationReportingYear;
	}

	public void setMacraProviderConfigurationReportingYear(
			Integer macraProviderConfigurationReportingYear) {
		this.macraProviderConfigurationReportingYear = macraProviderConfigurationReportingYear;
	}

	public Date getMacraProviderConfigurationReportingStart() {
		return macraProviderConfigurationReportingStart;
	}

	public void setMacraProviderConfigurationReportingStart(
			Date macraProviderConfigurationReportingStart) {
		this.macraProviderConfigurationReportingStart = macraProviderConfigurationReportingStart;
	}

	public Date getMacraProviderConfigurationReportingEnd() {
		return macraProviderConfigurationReportingEnd;
	}

	public void setMacraProviderConfigurationReportingEnd(
			Date macraProviderConfigurationReportingEnd) {
		this.macraProviderConfigurationReportingEnd = macraProviderConfigurationReportingEnd;
	}

	public Integer getMacraProviderConfigurationReportingMethod() {
		return macraProviderConfigurationReportingMethod;
	}

	public void setMacraProviderConfigurationReportingMethod(Integer macraProviderConfigurationReportingMethod) {
		this.macraProviderConfigurationReportingMethod = macraProviderConfigurationReportingMethod;
	}

	public ConfigurationDetails(Date macraProviderConfigurationReportingStart,Date macraProviderConfigurationReportingEnd,Integer macraProviderConfigurationReportingYear,Integer macraProviderConfigurationReportingMethod){
		this.macraProviderConfigurationReportingEnd=macraProviderConfigurationReportingEnd;
		this.macraProviderConfigurationReportingStart=macraProviderConfigurationReportingStart;
		this.macraProviderConfigurationReportingYear=macraProviderConfigurationReportingYear;
		this.macraProviderConfigurationReportingMethod=macraProviderConfigurationReportingMethod;
	}
	public ConfigurationDetails(String qualityMeasuresProviderMappingMeasureId){
		this.qualityMeasuresProviderMappingMeasureId=qualityMeasuresProviderMappingMeasureId;
	}
	
}
