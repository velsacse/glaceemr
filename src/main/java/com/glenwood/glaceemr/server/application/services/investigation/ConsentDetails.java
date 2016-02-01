package com.glenwood.glaceemr.server.application.services.investigation;

/**
 * @author yasodha
 * 
 * Bean set for savings consent details
 *
 */
public class ConsentDetails {

	Integer chartId;
	Integer encounterId;	
	String vaccineDetailId;
	String imageContent;
	Boolean consentGiven;
	String signedUser;
	String sharedFolderPath;	
	
	public String getSharedFolderPath() {
		return sharedFolderPath;
	}
	public void setSharedFolderPath(String sharedFolderPath) {
		this.sharedFolderPath = sharedFolderPath;
	}		
	public Integer getChartId() {
		return chartId;
	}
	public void setChartId(Integer chartId) {
		this.chartId = chartId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public String getVaccineDetailId() {
		return vaccineDetailId;
	}
	public void setVaccineDetailId(String testdetailid) {
		this.vaccineDetailId = testdetailid;
	}	
	public String getImageContent() {
		return imageContent;
	}
	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}	
	public Boolean isConsentGiven() {
		return consentGiven;
	}
	public void setConsentGiven(Boolean consentGiven) {
		this.consentGiven = consentGiven;
	}
	public String getSignedUser() {
		return signedUser;
	}
	public void setSignedUser(String signedUser) {
		this.signedUser = signedUser;
	}	
}
