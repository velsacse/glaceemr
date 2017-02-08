package com.glenwood.glaceemr.server.application.services.GroupTherapy;


public class AddTherapyBean {
	private String gwid;
	private String name;
	private Integer Type;
	private String optionsName;
	private String optionsValue;
	private String result = "";
	
	private Integer therapySessionPatientDetailsId;
	public AddTherapyBean(String gwid,String name,Integer Type,String optionsName,String optionsValue,Integer therapySessionPatientDetailsId){
		super();
		this.gwid = gwid;
		this.name = name;
		this.Type = Type;
		this.optionsName = optionsName;
		this.optionsValue = optionsValue;
		this.therapySessionPatientDetailsId = therapySessionPatientDetailsId;
	}
	
	public AddTherapyBean(String gwid,String name,Integer Type,String optionsName,String optionsValue,String result,Integer therapySessionPatientDetailsId){
		super();
		this.gwid = gwid;
		this.name = name;
		this.Type = Type;
		this.optionsName = optionsName;
		this.optionsValue = optionsValue;
		this.result=result;
		this.therapySessionPatientDetailsId = therapySessionPatientDetailsId;
	}
	
	public Integer getTherapySessionPatientDetailsId() {
		return therapySessionPatientDetailsId;
	}

	public void setTherapySessionPatientDetailsId(
			Integer therapySessionPatientDetailsId) {
		this.therapySessionPatientDetailsId = therapySessionPatientDetailsId;
	}

	public AddTherapyBean(String gwid,String name,Integer Type,String result,Integer therapySessionPatientDetailsId){
		super();
		this.gwid = gwid;
		this.name = name;
		this.Type = Type;
		this.result = result;
		this.therapySessionPatientDetailsId = therapySessionPatientDetailsId; 
	}
	public AddTherapyBean(String gwid,String name,Integer Type,Integer therapySessionPatientDetailsId){
		super();
		this.gwid = gwid;
		this.name = name;
		this.Type = Type;
		this.therapySessionPatientDetailsId = therapySessionPatientDetailsId;
	}
	public AddTherapyBean(String gwid,String name,Integer Type,String result){
		super();
		this.gwid = gwid;
		this.name = name;
		this.Type = Type;
		this.result = result;
	}
	public AddTherapyBean(String gwid,String name,Integer Type,String optionsName,String optionsValue,String result){
		this.gwid = gwid;
		this.name = name;
		this.Type = Type;
		this.optionsName = optionsName;
		this.optionsValue = optionsValue;
		this.result=result;
	}
	
	
	public String getGwid() {
		return gwid;
	}

	public void setGwid(String gwid) {
		this.gwid = gwid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return Type;
	}

	public void setType(Integer type) {
		Type = type;
	}

	public String getOptionsName() {
		return optionsName;
	}

	public void setOptionsName(String optionsName) {
		this.optionsName = optionsName;
	}

	public String getOptionsValue() {
		return optionsValue;
	}

	public void setOptionsValue(String optionsValue) {
		this.optionsValue = optionsValue;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
