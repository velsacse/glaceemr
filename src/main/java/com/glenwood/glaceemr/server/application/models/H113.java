package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "h113")
public class H113 implements Serializable{

	@Id
	@Column(name="h113001")
	private Integer h113001;

	@Column(name="h113002")
	private Integer h113002;

	@Column(name="h113003")
	private Integer h113003;

	@Column(name="h113004")
	private String h113004;

	@Column(name="h113005")
	private String h113005;

	public Integer getH113001() {
		return h113001;
	}

	public void setH113001(Integer h113001) {
		this.h113001 = h113001;
	}

	public Integer getH113002() {
		return h113002;
	}

	public void setH113002(Integer h113002) {
		this.h113002 = h113002;
	}

	public Integer getH113003() {
		return h113003;
	}

	public void setH113003(Integer h113003) {
		this.h113003 = h113003;
	}

	public String getH113004() {
		return h113004;
	}

	public void setH113004(String h113004) {
		this.h113004 = h113004;
	}

	public String getH113005() {
		return h113005;
	}

	public void setH113005(String h113005) {
		this.h113005 = h113005;
	}

	public String getH113006() {
		return h113006;
	}

	public void setH113006(String h113006) {
		this.h113006 = h113006;
	}

	public Integer getH113007() {
		return h113007;
	}

	public void setH113007(Integer h113007) {
		this.h113007 = h113007;
	}

	public Integer getH113008() {
		return h113008;
	}

	public void setH113008(Integer h113008) {
		this.h113008 = h113008;
	}

	public String getH113009() {
		return h113009;
	}

	public void setH113009(String h113009) {
		this.h113009 = h113009;
	}

	public Boolean getH113010() {
		return h113010;
	}

	public void setH113010(Boolean h113010) {
		this.h113010 = h113010;
	}

	public Boolean getH113011() {
		return h113011;
	}

	public void setH113011(Boolean h113011) {
		this.h113011 = h113011;
	}

	public String getH113012() {
		return h113012;
	}

	public void setH113012(String h113012) {
		this.h113012 = h113012;
	}

	public String getH113013() {
		return h113013;
	}

	public void setH113013(String h113013) {
		this.h113013 = h113013;
	}

	@Column(name="h113006")
	private String h113006;

	@Column(name="h113007")
	private Integer h113007;

	@Column(name="h113008")
	private Integer h113008;

	@Column(name="h113009")
	private String h113009;

	@Column(name="h113010")
	private Boolean h113010;

	@Column(name="h113011")
	private Boolean h113011;

	@Column(name="h113012")
	private String h113012;

	@Column(name="h113013")
	private String h113013;
}