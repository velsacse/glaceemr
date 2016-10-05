package com.glenwood.glaceemr.server.application.models;

import java.util.List;

public class AppointmentDetailsBean {

	List<H113> schApptStatusList;
	
	List<H113> schApptTypeList;
	
	List<H113> schApptReasonList;

	public List<H113> getSchApptStatusList() {
		return schApptStatusList;
	}

	public void setSchApptStatusList(List<H113> schApptStatusList) {
		this.schApptStatusList = schApptStatusList;
	}

	public List<H113> getSchApptTypeList() {
		return schApptTypeList;
	}

	public void setSchApptTypeList(List<H113> schApptTypeList) {
		this.schApptTypeList = schApptTypeList;
	}

	public List<H113> getSchApptReasonList() {
		return schApptReasonList;
	}

	public void setSchApptReasonList(List<H113> schApptReasonList) {
		this.schApptReasonList = schApptReasonList;
	}
	
}
