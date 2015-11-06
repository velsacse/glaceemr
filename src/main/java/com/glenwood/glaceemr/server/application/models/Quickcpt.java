package com.glenwood.glaceemr.server.application.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "quickcpt")
public class Quickcpt {

    @Id
	@Column(name="cpt_code")
	private String cptCode;

	@Column(name="group_no")
	private Integer groupNo;

	@Column(name="group_name")
	private String groupName;

	@Column(name="cpt_desc")
	private String cptDesc;

	public String getCptDesc() {
		return cptDesc;
	}

	public void setCptDesc(String cptDesc) {
		this.cptDesc = cptDesc;
	}

	public Integer getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCptCode() {
		return cptCode;
	}

	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}
	
	
}