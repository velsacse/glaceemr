package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h478")
public class H478 {
	@Id
	@Column(name="h478001")
	private Integer h478001;

	@Column(name="h478002")
	private String h478002;

	@Column(name="h478003")
	private String h478003;

	@Column(name="h478004")
	private String h478004;

	@Column(name="h478005")
	private String h478005;

	@Column(name="h478006")
	private String h478006;

	public Integer getH478001() {
		return h478001;
	}

	public void setH478001(Integer h478001) {
		this.h478001 = h478001;
	}

	public String getH478002() {
		return h478002;
	}

	public void setH478002(String h478002) {
		this.h478002 = h478002;
	}

	public String getH478003() {
		return h478003;
	}

	public void setH478003(String h478003) {
		this.h478003 = h478003;
	}

	public String getH478004() {
		return h478004;
	}

	public void setH478004(String h478004) {
		this.h478004 = h478004;
	}

	public String getH478005() {
		return h478005;
	}

	public void setH478005(String h478005) {
		this.h478005 = h478005;
	}

	public String getH478006() {
		return h478006;
	}

	public void setH478006(String h478006) {
		this.h478006 = h478006;
	}

	public String getH478007() {
		return h478007;
	}

	public void setH478007(String h478007) {
		this.h478007 = h478007;
	}

	public String getH478008() {
		return h478008;
	}

	public void setH478008(String h478008) {
		this.h478008 = h478008;
	}

	public String getH478009() {
		return h478009;
	}

	public void setH478009(String h478009) {
		this.h478009 = h478009;
	}

	@Column(name="h478007")
	private String h478007;

	@Column(name="h478008")
	private String h478008;

	@Column(name="h478009")
	private String h478009;
}