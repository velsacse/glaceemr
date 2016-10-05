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
@Table(name = "h802")
public class H802 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="public.h802_h802001_seq")
	@SequenceGenerator(name ="public.h802_h802001_seq", sequenceName="public.h802_h802001_seq", allocationSize=1)
	@Column(name="h802001")
	private Integer h802001;

	@Column(name="h802002")
	private Integer h802002;

	@Column(name="h802003")
	private Integer h802003;

	@Column(name="h802004")
	private Integer h802004;

	@Column(name="h802005")
	private String h802005;

	@Column(name="h802006")
	private Integer h802006;

	@Column(name="h802007")
	private Integer h802007;

	@Column(name="h802008")
	private Integer h802008;

	@Column(name="h802009")
	private String h802009;

	public Integer getH802001() {
		return h802001;
	}

	public void setH802001(Integer h802001) {
		this.h802001 = h802001;
	}

	public Integer getH802002() {
		return h802002;
	}

	public void setH802002(Integer h802002) {
		this.h802002 = h802002;
	}

	public Integer getH802003() {
		return h802003;
	}

	public void setH802003(Integer h802003) {
		this.h802003 = h802003;
	}

	public Integer getH802004() {
		return h802004;
	}

	public void setH802004(Integer h802004) {
		this.h802004 = h802004;
	}

	public String getH802005() {
		return h802005;
	}

	public void setH802005(String h802005) {
		this.h802005 = h802005;
	}

	public Integer getH802006() {
		return h802006;
	}

	public void setH802006(Integer h802006) {
		this.h802006 = h802006;
	}

	public Integer getH802007() {
		return h802007;
	}

	public void setH802007(Integer h802007) {
		this.h802007 = h802007;
	}

	public Integer getH802008() {
		return h802008;
	}

	public void setH802008(Integer h802008) {
		this.h802008 = h802008;
	}

	public String getH802009() {
		return h802009;
	}

	public void setH802009(String h802009) {
		this.h802009 = h802009;
	}
	
	
}