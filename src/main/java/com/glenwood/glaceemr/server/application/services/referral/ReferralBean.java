package com.glenwood.glaceemr.server.application.services.referral;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.models.Referral;

public class ReferralBean {
	
	
	List<Referral> referralList;
	
	List<H611> dxList;
	
	List<ProblemList> problemList;
	
	public ReferralBean(List<Referral> referralList,					   
					    List<H611> dxList,
					    List<ProblemList> problemList) {
		
		this.referralList = referralList;
		this.dxList = dxList;
		this.problemList = problemList;
		
	}
	
	public List<Referral> getReferralList() {
		return referralList;
	}

	public void setReferralList(List<Referral> referralList) {
		this.referralList = referralList;
	}

	public List<H611> getDxList() {
		return dxList;
	}

	public void setDxList(List<H611> dxList) {
		this.dxList = dxList;
	}

	public List<ProblemList> getProblemList() {
		return problemList;
	}

	public void setProblemList(List<ProblemList> problemList) {
		this.problemList = problemList;
	}
	
}