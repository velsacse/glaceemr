package com.glenwood.glaceemr.server.application.services.investigation;

import java.util.List;


public class LS_Lab {
	String testCategory;
	String labName;
	Integer testId;
	List<ParamData> labParamDetails;
	
	public String getTestCategory() {
		return testCategory;
	}
	public void setTestCategory(String testCategory) {
		this.testCategory = testCategory;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}	
	public Integer getTestId() {
		return testId;
	}
	public void setTestId(Integer testId) {
		this.testId = testId;
	}
	public List<ParamData> getLabParamDetails() {
		return labParamDetails;
	}
	public void setLabParamDetails(List<ParamData> labParamDetails) {
		this.labParamDetails = labParamDetails;
	}
}
