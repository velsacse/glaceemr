package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "h448")
public class H448 {

	@Column(name="h448001")
	private Integer h448001;

	@Column(name="h448002")
	private String h448002;

	@Id
	@Column(name="h448003")
	private Integer h448003;

	

	public Integer getH448001() {
		return h448001;
	}

	public void setH448001(Integer h448001) {
		this.h448001 = h448001;
	}

	public String getH448002() {
		return h448002;
	}

	public void setH448002(String h448002) {
		this.h448002 = h448002;
	}

	public Integer getH448003() {
		return h448003;
	}

	public void setH448003(Integer h448003) {
		this.h448003 = h448003;
	}
	
	
}