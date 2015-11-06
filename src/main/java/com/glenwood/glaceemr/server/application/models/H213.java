package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "h213")
public class H213 {
	@Id
	@Column(name="h213001")
	private Integer h213001;
	
	@Column(name="h213002")
	private String h213002;
	
	@Column(name="h213003")
	private Integer h213003;
	
	@Column(name="h213004")
	private Integer h213004;
	
	@Column(name="h213005")
	private Integer h213005;
	
	@Column(name="h213006")
	private Integer h213006;
	
	@Column(name="h213007",columnDefinition="integer default 0")
	private Integer h213007;

	public Integer getH213001() {
		return h213001;
	}

	public void setH213001(Integer h213001) {
		this.h213001 = h213001;
	}

	public String getH213002() {
		return h213002;
	}

	public void setH213002(String h213002) {
		this.h213002 = h213002;
	}

	public Integer getH213003() {
		return h213003;
	}

	public void setH213003(Integer h213003) {
		this.h213003 = h213003;
	}

	public Integer getH213004() {
		return h213004;
	}

	public void setH213004(Integer h213004) {
		this.h213004 = h213004;
	}

	public Integer getH213005() {
		return h213005;
	}

	public void setH213005(Integer h213005) {
		this.h213005 = h213005;
	}

	public Integer getH213006() {
		return h213006;
	}

	public void setH213006(Integer h213006) {
		this.h213006 = h213006;
	}

	public Integer getH213007() {
		return h213007;
	}

	public void setH213007(Integer h213007) {
		this.h213007 = h213007;
	}
}
