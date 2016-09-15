package com.glenwood.glaceemr.server.application.models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "h066")
public class H066 {
	@Id
	@Column(name="h066001")
	private Integer h066001;
	
	@Column(name="h066002")
	private Integer h066002;
	
	@Column(name="h066003")
	private String h066003;
	
	@Column(name="h066004")
	private String h066004;
	
	@Column(name="h066005")
	private Integer h066005;
	
	@Column(name="h066006")
	private String h066006;
	
	@Column(name="h066007")
	private BigDecimal h066007;
	
	@Column(name="h555555")
	private Integer h555555;

	@Column(name="h066008")
	private Integer h066008;
	
	@Column(name="h066009")
	private Integer h066009;
	
	@Column(name="h066011")
	private String h066011;
	
	@Column(name="h066013")
	private String h066013;
	
	@Column(name="h066010")
	private String h066010;
	
	@Column(name="h066012")
	private String h066012;

	public Integer getH066001() {
		return h066001;
	}

	public void setH066001(Integer h066001) {
		this.h066001 = h066001;
	}

	public Integer getH066002() {
		return h066002;
	}

	public void setH066002(Integer h066002) {
		this.h066002 = h066002;
	}

	public String getH066003() {
		return h066003;
	}

	public void setH066003(String h066003) {
		this.h066003 = h066003;
	}

	public String getH066004() {
		return h066004;
	}

	public void setH066004(String h066004) {
		this.h066004 = h066004;
	}

	public Integer getH066005() {
		return h066005;
	}

	public void setH066005(Integer h066005) {
		this.h066005 = h066005;
	}

	public String getH066006() {
		return h066006;
	}

	public void setH066006(String h066006) {
		this.h066006 = h066006;
	}

	public BigDecimal getH066007() {
		return h066007;
	}

	public void setH066007(BigDecimal h066007) {
		this.h066007 = h066007;
	}

	public Integer getH555555() {
		return h555555;
	}

	public void setH555555(Integer h555555) {
		this.h555555 = h555555;
	}

	public Integer getH066008() {
		return h066008;
	}

	public void setH066008(Integer h066008) {
		this.h066008 = h066008;
	}

	public Integer getH066009() {
		return h066009;
	}

	public void setH066009(Integer h066009) {
		this.h066009 = h066009;
	}

	public String getH066011() {
		return h066011;
	}

	public void setH066011(String h066011) {
		this.h066011 = h066011;
	}

	public String getH066013() {
		return h066013;
	}

	public void setH066013(String h066013) {
		this.h066013 = h066013;
	}

	public String getH066010() {
		return h066010;
	}

	public void setH066010(String h066010) {
		this.h066010 = h066010;
	}

	public String getH066012() {
		return h066012;
	}

	public void setH066012(String h066012) {
		this.h066012 = h066012;
	}
}
