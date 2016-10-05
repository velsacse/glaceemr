package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "h092")
public class H092 implements Serializable{

	@Id
	@Column(name="h092001")
	private Integer h092001;

	@Column(name="h092002")
	private Integer h092002;

	@Column(name="h092003")
	private String h092003;

	@Column(name="h092004")
	private Integer h092004;

	@Column(name="h092005")
	private String h092005;

	@Column(name="h092006")
	private Integer h092006;

	public Integer getH092001() {
		return h092001;
	}

	public void setH092001(Integer h092001) {
		this.h092001 = h092001;
	}

	public Integer getH092002() {
		return h092002;
	}

	public void setH092002(Integer h092002) {
		this.h092002 = h092002;
	}

	public String getH092003() {
		return h092003;
	}

	public void setH092003(String h092003) {
		this.h092003 = h092003;
	}

	public Integer getH092004() {
		return h092004;
	}

	public void setH092004(Integer h092004) {
		this.h092004 = h092004;
	}

	public String getH092005() {
		return h092005;
	}

	public void setH092005(String h092005) {
		this.h092005 = h092005;
	}

	public Integer getH092006() {
		return h092006;
	}

	public void setH092006(Integer h092006) {
		this.h092006 = h092006;
	}
	
	
	
}