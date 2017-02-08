package com.glenwood.glaceemr.server.application.services.GroupTherapy;

import java.util.List;

public class TherapyPrintBean {
	Integer groupId;
	String  groupName;
	Integer groupProviderId;
	Integer groupLeaderId;
	Integer groupSupervisorId;
	String groupDesc;
	Integer gropPosId;
	String defaultTherapyTime;
	Boolean isActive;
	
	TherapySessionBean therapySessionDetails;
	List<TherapyPatientsBean> therapyPatientDetails;
	List<AddTherapyBean> therapyElementsDetails;
	
	
	public TherapyPrintBean(Integer groupId,String groupName,Integer groupProviderId,Integer groupLeaderId,Integer groupSupervisorId,String groupDesc,Integer gropPosId,String defaultTherapyTime,Boolean isActive){
		super();
		
		this.groupId= groupId;
		this.groupName= groupName;
		this.groupProviderId= groupProviderId;
		this.groupLeaderId= groupLeaderId;
		this.groupSupervisorId= groupSupervisorId;
		this.groupDesc= groupDesc;
		this.gropPosId= gropPosId;
		this.defaultTherapyTime= defaultTherapyTime;
		this.isActive= isActive;
		
	}
	
	
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
	public Integer getGroupProviderId() {
		return groupProviderId;
	}
	public void setGroupProviderId(Integer groupProviderId) {
		this.groupProviderId = groupProviderId;
	}
	public Integer getGroupLeaderId() {
		return groupLeaderId;
	}
	public void setGroupLeaderId(Integer groupLeaderId) {
		this.groupLeaderId = groupLeaderId;
	}
	public Integer getGroupSupervisorId() {
		return groupSupervisorId;
	}
	public void setGroupSupervisorId(Integer groupSupervisorId) {
		this.groupSupervisorId = groupSupervisorId;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public Integer getGropPosId() {
		return gropPosId;
	}
	public void setGropPosId(Integer gropPosId) {
		this.gropPosId = gropPosId;
	}
	public String getDefaultTherapyTime() {
		return defaultTherapyTime;
	}
	public void setDefaultTherapyTime(String defaultTherapyTime) {
		this.defaultTherapyTime = defaultTherapyTime;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public TherapySessionBean getTherapySessionDetails() {
		return therapySessionDetails;
	}
	public void setTherapySessionDetails(TherapySessionBean therapySessionDetails) {
		this.therapySessionDetails = therapySessionDetails;
	}
	public List<TherapyPatientsBean> getTherapyPatientDetails() {
		return therapyPatientDetails;
	}
	public void setTherapyPatientDetails(
			List<TherapyPatientsBean> therapyPatientDetails) {
		this.therapyPatientDetails = therapyPatientDetails;
	}
	public List<AddTherapyBean> getTherapyElementsDetails() {
		return therapyElementsDetails;
	}
	public void setTherapyElementsDetails(
			List<AddTherapyBean> therapyElementsDetails) {
		this.therapyElementsDetails = therapyElementsDetails;
	}
	
	
	
	
}
