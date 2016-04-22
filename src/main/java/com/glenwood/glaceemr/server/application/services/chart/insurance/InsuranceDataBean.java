package com.glenwood.glaceemr.server.application.services.chart.insurance;

/**
 * Insurance details bean 
 * @author software
 *
 */
public class InsuranceDataBean {
	
	private String insuranceId;
	
	private String insuranceName;
	
	private String insuranceAddress;

	
	public InsuranceDataBean(String insuranceId, String insuranceName,
			String insuranceAddress) {
		this.insuranceId = insuranceId;
		this.insuranceName = insuranceName;
		this.insuranceAddress = insuranceAddress;
	}

	public String getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(String insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public String getInsuranceAddress() {
		return insuranceAddress;
	}

	public void setInsuranceAddress(String insuranceAddress) {
		this.insuranceAddress = insuranceAddress;
	}
	
	
}
