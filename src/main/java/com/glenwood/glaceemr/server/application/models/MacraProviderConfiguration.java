package com.glenwood.glaceemr.server.application.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "macra_provider_configuration")
public class MacraProviderConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="macra_provider_configuration_macra_provider_configuration_i_seq")
	@SequenceGenerator(name = "macra_provider_configuration_macra_provider_configuration_i_seq", sequenceName="macra_provider_configuration_macra_provider_configuration_i_seq", allocationSize=1)
	@Column(name="macra_provider_configuration_id")
	private Integer macraProviderConfigurationId;

	@Column(name="macra_provider_configuration_provider_id")
	private Integer macraProviderConfigurationProviderId;

	@Column(name="macra_provider_configuration_reporting_year")
	private Integer macraProviderConfigurationReportingYear;

	@Column(name="macra_provider_configuration_reporting_start")
	private Date macraProviderConfigurationReportingStart;

	@Column(name="macra_provider_configuration_reporting_end")
	private Date macraProviderConfigurationReportingEnd;

	@Column(name="macra_provider_configuration_reporting_method")
	private Integer macraProviderConfigurationReportingMethod;

	@Column(name="macra_provider_configuration_aci_group")
	private Integer macraProviderConfigurationAciGroup;

	public Integer getMacraProviderConfigurationId() {
		return macraProviderConfigurationId;
	}

	public void setMacraProviderConfigurationId(Integer macraProviderConfigurationId) {
		this.macraProviderConfigurationId = macraProviderConfigurationId;
	}

	public Integer getMacraProviderConfigurationProviderId() {
		return macraProviderConfigurationProviderId;
	}

	public void setMacraProviderConfigurationProviderId(
			Integer macraProviderConfigurationProviderId) {
		this.macraProviderConfigurationProviderId = macraProviderConfigurationProviderId;
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

	public void setMacraProviderConfigurationReportingMethod(
			Integer macraProviderConfigurationReportingMethod) {
		this.macraProviderConfigurationReportingMethod = macraProviderConfigurationReportingMethod;
	}

	public Integer getMacraProviderConfigurationAciGroup() {
		return macraProviderConfigurationAciGroup;
	}

	public void setMacraProviderConfigurationAciGroup(
			Integer macraProviderConfigurationAciGroup) {
		this.macraProviderConfigurationAciGroup = macraProviderConfigurationAciGroup;
	}
	
}