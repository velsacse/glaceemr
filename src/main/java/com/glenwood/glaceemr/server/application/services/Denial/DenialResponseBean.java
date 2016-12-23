package com.glenwood.glaceemr.server.application.services.Denial;

import java.util.List;

public class DenialResponseBean {
	
	List<DenialBean> denialResponse;
	boolean status;
	
	public List<DenialBean> getDenialResponse() {
		return denialResponse;
	}
	
	public void setDenialResponse(List<DenialBean> denialResponse) {
		this.denialResponse = denialResponse;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
