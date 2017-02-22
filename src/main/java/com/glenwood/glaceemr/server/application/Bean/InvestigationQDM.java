package com.glenwood.glaceemr.server.application.Bean;

import java.util.Date;

public class InvestigationQDM {

	String code;
	String codeDescription;
	Integer status;
	Integer companyId;
	Date createdOn;
	Date performeOn;

	public InvestigationQDM(String code, String codeDescription, Integer status, Integer companyId, Date createdOn, Date performeOn) {
		
		super();
		
		this.code = code;
		this.codeDescription = codeDescription;
		this.status = status;
		this.createdOn = createdOn;
		this.companyId = companyId;
		
		if(performeOn!=null){
			this.performeOn = performeOn;
		}
		
	}
	
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeDescription() {
		return codeDescription;
	}

	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getPerformeOn() {
		return performeOn;
	}

	public void setPerformeOn(Date performeOn) {
		this.performeOn = performeOn;
	}
	
}
