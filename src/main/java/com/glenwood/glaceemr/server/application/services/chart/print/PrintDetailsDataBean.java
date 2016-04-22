package com.glenwood.glaceemr.server.application.services.chart.print;

public class PrintDetailsDataBean {
	
	int pageSize;
	int pageOrientation;
	String htmlData;
	int encounterId;
	int datatype;
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageOrientation() {
		return pageOrientation;
	}
	public void setPageOrientation(int pageOrientation) {
		this.pageOrientation = pageOrientation;
	}
	public String getHtmlData() {
		return htmlData;
	}
	public void setHtmlData(String htmlData) {
		this.htmlData = htmlData;
	}
	public int getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(int encounterId) {
		this.encounterId = encounterId;
	}
	public int getDatatype() {
		return datatype;
	}
	public void setDatatype(int datatype) {
		this.datatype = datatype;
	}
	
	
}
