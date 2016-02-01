package com.glenwood.glaceemr.server.application.services.investigation;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.LabDescription;

public class FrequentOrders {

	Integer groupId;
	String groupName;
	List<LabDescription> labs;
	
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<LabDescription> getLabs() {
		return labs;
	}
	public void setLabs(List<LabDescription> labs) {
		this.labs = labs;
	}	
}
