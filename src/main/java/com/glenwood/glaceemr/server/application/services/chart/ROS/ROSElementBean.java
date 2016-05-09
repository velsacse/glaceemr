package com.glenwood.glaceemr.server.application.services.chart.ROS;


public class ROSElementBean {
	
	private String elementGWID;
	private String elementName;
	private String elementPrintText;
	private Integer dataType;
	private String value;
	
	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getElementGWID(){
		return elementGWID;
	}
	
	public void setElementGWID(String elementGWID){
		this.elementGWID = elementGWID;
	}
	
	public String getElementName(){
		return elementName;
	}
	
	public void setElementName(String elementName){
		this.elementName = elementName;
	}
	
	public String getElementPrintText() {
		return elementPrintText;
	}
	
	public void setElementPrintText(String elementPrintText) {
		this.elementPrintText = elementPrintText;
	}
	
}