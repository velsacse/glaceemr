package com.glenwood.glaceemr.server.application.services.chart.skintest;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.models.ProblemList;

public class DxAndPendingOrdersBean {
	List<H611> dxList;
	List<ProblemList> problemList;
	List<LabEntries> pendingOrders;
	
	public List<H611> getdxList() {
		return dxList;
	}
	public void setdxList(List<H611> dxList) {
		this.dxList = dxList;
	}
	public List<ProblemList> getProblemList() {
		return problemList;
	}
	public void setProblemList(List<ProblemList> problemList) {
		this.problemList = problemList;
	}
	public List<LabEntries> getPendingOrders() {
		return pendingOrders;
	}
	public void setPendingOrders(List<LabEntries> pendingOrders) {
		this.pendingOrders = pendingOrders;
	}
	
}
