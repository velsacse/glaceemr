package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "reason_user_group_mapping")
public class ReasonUserGroupMapping implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="reason_user_group_mappping_id")
	private Integer reasonUserGroupMapId;

	@Column(name="reason_user_group_mapping_reasonid")
	private Integer reasonUserGrpMapReasonId;

	@Column(name="reason_user_group_mapping_groupid")
	private Integer reasonUserGrpMapGrpId;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="reason_user_group_mapping_reasonid", referencedColumnName="App_Reference_Values_statusId" , insertable=false, updatable=false)
	AppReferenceValues appReferenceValues;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="reason_user_group_mapping_groupid", referencedColumnName="reason_user_group_id" , insertable=false, updatable=false)
	ReasonUserGroup reasonUserGroup;

	public Integer getReasonUserGroupMapId() {
		return reasonUserGroupMapId;
	}

	public void setReasonUserGroupMapId(Integer reasonUserGroupMapId) {
		this.reasonUserGroupMapId = reasonUserGroupMapId;
	}

	public Integer getReasonUserGrpMapReasonId() {
		return reasonUserGrpMapReasonId;
	}

	public void setReasonUserGrpMapReasonId(Integer reasonUserGrpMapReasonId) {
		this.reasonUserGrpMapReasonId = reasonUserGrpMapReasonId;
	}

	public Integer getReasonUserGrpMapGrpId() {
		return reasonUserGrpMapGrpId;
	}

	public void setReasonUserGrpMapGrpId(Integer reasonUserGrpMapGrpId) {
		this.reasonUserGrpMapGrpId = reasonUserGrpMapGrpId;
	}

}
