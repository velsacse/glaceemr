package com.glenwood.glaceemr.server.application.services.investigation;

import java.util.List;

public class LS_Bean {
	GroupName paramData;
	List<LS_External> externalLabsResult;
	Integer chartId;
	Integer patientId;
	public GroupName getParamData() {
		return paramData;
	}
	public void setParamData(GroupName paramData) {
		this.paramData = paramData;
	}
	public List<LS_External> getExternalLabsResult() {
		return externalLabsResult;
	}
	public void setExternalLabsResult(List<LS_External> externalLabsResult) {
		this.externalLabsResult = externalLabsResult;
	}
	public Integer getChartId() {
		return chartId;
	}
	public void setChartId(Integer chartId) {
		this.chartId = chartId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
}
