package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ins_user_group")
public class InsUserGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ins_user_group_id")
	private Integer insUserGroupId;

	@Column(name="ins_user_group_name")
	private String insUserGroupName;


	@Column(name="ins_user_group_isactive")
	private Boolean insUserGroupIsactive;

	@Column(name="ins_user_group_default")
	private Boolean insUserGroupDefault;


	public Integer getInsUserGroupId() {
		return insUserGroupId;
	}

	public void setInsUserGroupId(Integer insUserGroupId) {
		this.insUserGroupId = insUserGroupId;
	}

	public String getInsUserGroupName() {
		return insUserGroupName;
	}

	public void setInsUserGroupName(String insUserGroupName) {
		this.insUserGroupName = insUserGroupName;
	}

	public Boolean getInsUserGroupIsactive() {
		return insUserGroupIsactive;
	}

	public void setInsUserGroupIsactive(Boolean insUserGroupIsactive) {
		this.insUserGroupIsactive = insUserGroupIsactive;
	}

	public Boolean getInsUserGroupDefault() {
		return insUserGroupDefault;
	}

	public void setInsUserGroupDefault(Boolean insUserGroupDefault) {
		this.insUserGroupDefault = insUserGroupDefault;
	}

}
