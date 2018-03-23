package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "zipcodes_main")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZipCodesMain {

	@Id
	@Column(name="zipcodes_id")
	private Integer zipCodesId;
	
	@Column(name="zipcodes_city")
	private String zipCodesCity;

	@Column(name="zipcodes_state")
	private Integer zipCodesState;

	@Column(name="zipcodes_zip")
	private String zipCodesZip;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="zipcodes_state", referencedColumnName="billing_config_table_config_id" , insertable=false, updatable=false)
	BillingConfigTable billingConfigTable1;
	
	
	public Integer getZipCodesId() {
		return zipCodesId;
	}

	public void setZipCodesId(Integer zipCodesId) {
		this.zipCodesId = zipCodesId;
	}

	public String getZipCodesCity() {
		return zipCodesCity;
	}

	public void setZipCodesCity(String zipCodesCity) {
		this.zipCodesCity = zipCodesCity;
	}

	public Integer getZipCodesState() {
		return zipCodesState;
	}

	public void setZipCodesState(Integer zipCodesState) {
		this.zipCodesState = zipCodesState;
	}

	public String getZipCodesZip() {
		return zipCodesZip;
	}

	public void setZipCodesZip(String zipCodesZip) {
		this.zipCodesZip = zipCodesZip;
	}

}
