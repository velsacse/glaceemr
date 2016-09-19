package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "h068")
public class H068 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="h068001")
	private Integer h068001;

	@Column(name="h068002")
	private Integer h068002;

	@Column(name="h068003")
	private String h068003;

	@Column(name="h068004")
	private String h068004;

	@Column(name="h068005")
	private Integer h068005;

	@Column(name="h068006")
	private String h068006;

	@Column(name="h068007")
	private Integer h068007;
	
	public Integer getH068001() {
		return h068001;
	}

	public void setH068001(Integer h068001) {
		this.h068001 = h068001;
	}

	public Integer getH068002() {
		return h068002;
	}

	public void setH068002(Integer h068002) {
		this.h068002 = h068002;
	}

	public String getH068003() {
		return h068003;
	}

	public void setH068003(String h068003) {
		this.h068003 = h068003;
	}

	public String getH068004() {
		return h068004;
	}

	public void setH068004(String h068004) {
		this.h068004 = h068004;
	}

	public Integer getH068005() {
		return h068005;
	}

	public void setH068005(Integer h068005) {
		this.h068005 = h068005;
	}

	public String getH068006() {
		return h068006;
	}

	public void setH068006(String h068006) {
		this.h068006 = h068006;
	}

	public Integer getH068007() {
		return h068007;
	}

	public void setH068007(Integer h068007) {
		this.h068007 = h068007;
	}
}