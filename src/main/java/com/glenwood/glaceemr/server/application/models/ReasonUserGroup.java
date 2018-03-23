package com.glenwood.glaceemr.server.application.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "reason_user_group")
public class ReasonUserGroup {

	
	@Id
	@Column(name="reason_user_group_id")
	private Integer reasonUserGroupId;

	@Column(name="reason_user_group_name")
	private String reasonUserGrpName;

	@Column(name="reason_user_group_isactive")
	private Boolean reasonUserGrpIsActive;

	@Column(name="reason_user_group_default")
	private Boolean reasonUserGrpDefault;
	

	@OneToMany(cascade=CascadeType.ALL,mappedBy="reasonUserGroup")
	@JsonManagedReference
	List<ReasonUserGroupMapping> reasonUserGroupMappings;
	
	public ReasonUserGroup(){
		
	}
	
	public ReasonUserGroup(Integer reasonUserGroupId,String reasonUserGrpName
			               ,Boolean reasonUserGrpIsActive,Boolean reasonUserGrpDefault){
		
		this.reasonUserGroupId=reasonUserGroupId;
		this.reasonUserGrpName=reasonUserGrpName;
		this.reasonUserGrpDefault=reasonUserGrpDefault;
		this.reasonUserGrpIsActive=reasonUserGrpIsActive;
	}
	
	public Integer getReasonUserGroupId() {
		return reasonUserGroupId;
	}

	public void setReasonUserGroupId(Integer reasonUserGroupId) {
		this.reasonUserGroupId = reasonUserGroupId;
	}

	public String getReasonUserGrpName() {
		return reasonUserGrpName;
	}

	public void setReasonUserGrpName(String reasonUserGrpName) {
		this.reasonUserGrpName = reasonUserGrpName;
	}

	public Boolean getReasonUserGrpIsActive() {
		return reasonUserGrpIsActive;
	}

	public void setReasonUserGrpIsActive(Boolean reasonUserGrpIsActive) {
		this.reasonUserGrpIsActive = reasonUserGrpIsActive;
	}

	public Boolean getReasonUserGrpDefault() {
		return reasonUserGrpDefault;
	}

	public void setReasonUserGrpDefault(Boolean reasonUserGrpDefault) {
		this.reasonUserGrpDefault = reasonUserGrpDefault;
	}

}
