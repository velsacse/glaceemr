package com.glenwood.glaceemr.server.application.services.investigation;

import java.util.ArrayList;
import java.util.List;

public class LS_Bean {
	GroupName paramData;
	List<LS_External> externalLabsResult;
	ArrayList<String> paramDate;
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
	public ArrayList<String> getParamDate() {
		return paramDate;
	}
	public void setParamDate(ArrayList<String> paramDate) {
		this.paramDate = paramDate;
	}
}
