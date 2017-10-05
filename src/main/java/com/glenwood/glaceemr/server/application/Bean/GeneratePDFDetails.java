package com.glenwood.glaceemr.server.application.Bean;

import java.util.Date;
import java.util.List;

public class GeneratePDFDetails {


	String measureName;
	String measureType;
	String cmsID;
	String providerName;
	String submissionMethod;
	String reportStart;
	String reportEnd;
	List<MIPSPatientInformation> patientInfo;
	
	public String getMeasureName() {
		return measureName;
	}
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
	public String getMeasureType() {
		return measureType;
	}
	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}
	public String getCmsID() {
		return cmsID;
	}
	public void setCmsID(String cmsID) {
		this.cmsID = cmsID;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getSubmissionMethod() {
		return submissionMethod;
	}
	public void setSubmissionMethod(String submissionMethod) {
		this.submissionMethod = submissionMethod;
	}
	public String getReportStart() {
		return reportStart;
	}
	public void setReportStart(String reportStart) {
		this.reportStart = reportStart;
	}
	public String getReportEnd() {
		return reportEnd;
	}
	public void setReportEnd(String reportEnd) {
		this.reportEnd = reportEnd;
	}



}
