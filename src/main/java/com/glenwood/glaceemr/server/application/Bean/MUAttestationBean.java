package com.glenwood.glaceemr.server.application.Bean;

import java.util.HashMap;
import java.util.List;

public class MUAttestationBean {

	int reportingYear;
	HashMap<Integer, HashMap<String, List<MUPerformanceBean>>> reportingStatus;
	
	public int getReportingYear() {
		return reportingYear;
	}
	
	public void setReportingYear(int reportingYear) {
		this.reportingYear = reportingYear;
	}
	
	public HashMap<Integer, HashMap<String, List<MUPerformanceBean>>> getReportingStatus() {
		return reportingStatus;
	}
	
	public void setReportingStatus(
			HashMap<Integer, HashMap<String, List<MUPerformanceBean>>> reportingStatus) {
		this.reportingStatus = reportingStatus;
	}
	
}
