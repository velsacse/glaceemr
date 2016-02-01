package com.glenwood.glaceemr.server.application.services.investigation;

import java.util.List;

public class LS_Parameter {

	Integer paramId;
	String displayName;
	List<ParamValues> paramValuesList;	
	
	public Integer getParamId() {
		return paramId;
	}
	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<ParamValues> getParamValuesList() {
		return paramValuesList;
	}
	public void setParamValuesList(List<ParamValues> paramValuesList) {
		this.paramValuesList = paramValuesList;
	}
}
