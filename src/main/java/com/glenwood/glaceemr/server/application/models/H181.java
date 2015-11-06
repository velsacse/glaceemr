package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

@Entity
@Table(name = "h181")
public class H181 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="h181_h181001_seq")
	@SequenceGenerator(name ="h181_h181001_seq", sequenceName="h181_h181001_seq", allocationSize=1)
	@Column(name="h181001")
	private Integer h181001;

	@Column(name="h181002")
	private Integer h181002;

	@Column(name="h181003")
	private Integer h181003;

	@Column(name="h181004")
	private Double h181004;

	@Column(name="h181005")
	private Double h181005;

	public Integer getH181001() {
		return h181001;
	}

	public void setH181001(Integer h181001) {
		this.h181001 = h181001;
	}

	public Integer getH181002() {
		return h181002;
	}

	public void setH181002(Integer h181002) {
		this.h181002 = h181002;
	}

	public Integer getH181003() {
		return h181003;
	}

	public void setH181003(Integer h181003) {
		this.h181003 = h181003;
	}

	public Double getH181004() {
		return h181004;
	}

	public void setH181004(Double h181004) {
		this.h181004 = h181004;
	}

	public Double getH181005() {
		return h181005;
	}

	public void setH181005(Double h181005) {
		this.h181005 = h181005;
	}
}