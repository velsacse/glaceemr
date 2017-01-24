package com.glenwood.glaceemr.server.application.Bean;

import java.util.HashMap;

public class ReportingInfo {

	int reportingYear;
	HashMap<String, HashMap<String, AttestationBean>> reportingInfo;
	
	public int getReportingYear() {
		return reportingYear;
	}
	
	public void setReportingYear(int reportingYear) {
		this.reportingYear = reportingYear;
	}

	public HashMap<String, HashMap<String, AttestationBean>> getReportingInfo() {
		return reportingInfo;
	}

	public void setReportingInfo(
			HashMap<String, HashMap<String, AttestationBean>> reportingInfo) {
		this.reportingInfo = reportingInfo;
	}
	
}
