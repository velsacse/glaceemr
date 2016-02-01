package com.glenwood.glaceemr.server.application.services.investigation;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.LabDescription;

public class OrderSetData {

	String orderSetId;
	String orderSetName;
	List<LabDescription> labDetailsList;
		
	public String getOrderSetId() {
		return orderSetId;
	}
	public void setOrderSetId(String orderSetId) {
		this.orderSetId = orderSetId;
	}
	public String getOrderSetName() {
		return orderSetName;
	}
	public void setOrderSetName(String orderSetName) {
		this.orderSetName = orderSetName;
	}
	public List<LabDescription> getLabDetailsList() {
		return labDetailsList;
	}
	public void setLabDetailsList(List<LabDescription> labDetailsList) {
		this.labDetailsList = labDetailsList;
	}	
}
