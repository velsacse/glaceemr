package com.glenwood.glaceemr.server.application.Bean;

import java.util.Date;

public class ReferralQDM {
	
	Integer refId;
	Date referredDate;
	Integer status;
	
	public ReferralQDM(Integer refId, Date referredDate, Integer status) {
		super();
		this.refId = refId;
		this.referredDate = referredDate;
		this.status = status;
	}	

	public Integer getRefId() {
		return refId;
	}
	
	public void setRefId(Integer refId) {
		this.refId = refId;
	}
	
	public Date getReferredDate() {
		return referredDate;
	}
	
	public void setReferredDate(Date referredDate) {
		this.referredDate = referredDate;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}

}