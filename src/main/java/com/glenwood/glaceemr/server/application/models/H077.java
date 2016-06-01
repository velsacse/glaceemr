package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "h077")
public class H077 {

	@Id
	@Column(name="h077001")
	private Integer h077001;

	@Column(name="h077002")
	private String h077002;

	@Column(name="h077003")
	private String h077003;

	@Column(name="nucc_code")
	private String nuccCode;

	public Integer getH077001() {
		return h077001;
	}

	public void setH077001(Integer h077001) {
		this.h077001 = h077001;
	}

	public String getH077002() {
		return h077002;
	}

	public void setH077002(String h077002) {
		this.h077002 = h077002;
	}

	public String getH077003() {
		return h077003;
	}

	public void setH077003(String h077003) {
		this.h077003 = h077003;
	}

	public String getNuccCode() {
		return nuccCode;
	}

	public void setNuccCode(String nuccCode) {
		this.nuccCode = nuccCode;
	}
	
}