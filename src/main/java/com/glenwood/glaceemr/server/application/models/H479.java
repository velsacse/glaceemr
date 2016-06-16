package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "h479")
public class H479 {

	@Id
	@Column(name="h479001")
	private Integer h479001;

	@Column(name="h479002")
	private String h479002;

	@Column(name="h479003")
	private Boolean h479003;

	public Integer getH479001() {
		return h479001;
	}

	public void setH479001(Integer h479001) {
		this.h479001 = h479001;
	}

	public String getH479002() {
		return h479002;
	}

	public void setH479002(String h479002) {
		this.h479002 = h479002;
	}

	public Boolean getH479003() {
		return h479003;
	}

	public void setH479003(Boolean h479003) {
		this.h479003 = h479003;
	}
	
	
}