package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

@Entity
@Table(name = "h810")
public class H810 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="public.h810_h810001_seq")
	@SequenceGenerator(name ="public.h810_h810001_seq", sequenceName="public.h810_h810001_seq", allocationSize=1)
	@Column(name="h810001")
	private Integer h810001;

	@Column(name="h810002")
	private String h810002;

	@Column(name="h810003")
	private String h810003;

	@Column(name="h810004")
	private String h810004;

	@Column(name="h810005")
	private Boolean h810005;

	public Integer getH810001() {
		return h810001;
	}

	public void setH810001(Integer h810001) {
		this.h810001 = h810001;
	}

	public String getH810002() {
		return h810002;
	}

	public void setH810002(String h810002) {
		this.h810002 = h810002;
	}

	public String getH810003() {
		return h810003;
	}

	public void setH810003(String h810003) {
		this.h810003 = h810003;
	}

	public String getH810004() {
		return h810004;
	}

	public void setH810004(String h810004) {
		this.h810004 = h810004;
	}

	public Boolean getH810005() {
		return h810005;
	}

	public void setH810005(Boolean h810005) {
		this.h810005 = h810005;
	}
	
}