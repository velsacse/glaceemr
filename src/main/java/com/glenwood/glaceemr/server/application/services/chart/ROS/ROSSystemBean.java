package com.glenwood.glaceemr.server.application.services.chart.ROS;

import java.util.ArrayList;
import java.util.List;


public class ROSSystemBean {
	
	private int systemId;
	private String systemName;
	private String EandMType;
	private String deferredGWID;
	private List<ROSElementBean> rosElements=new ArrayList<ROSElementBean>();
	
	public int getSystemId(){
		return systemId;
	}
	
	public void setSytemId(int systemId){
		this.systemId = systemId;
	}
	
	public String getSystemName(){
		return systemName;
	}
	
	public void setSystemName(String systemName){
		this.systemName = systemName;
	}
	
	public List<ROSElementBean> getROSElements(){
		return rosElements;
	}
	
	public void setROSElements(List<ROSElementBean> rosElements){
		this.rosElements = rosElements;
	}
	
	public String getEandMType() {
		return EandMType;
	}
	
	public void setEandMType(String EandMType) {
		this.EandMType = EandMType;
	}
	
	public String getDeferredGWID() {
		return deferredGWID;
	}
	
	public void setDeferredGWID(String deferredGWID) {
		this.deferredGWID = deferredGWID;
	}
	
	
}
