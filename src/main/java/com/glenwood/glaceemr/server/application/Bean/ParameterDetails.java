package com.glenwood.glaceemr.server.application.Bean;

public class ParameterDetails {

	int lab_entries_parameter_testdetailid;
	String lab_parameter_code_value;
	String lab_entries_parameter_value;
	
	public int getLab_entries_parameter_testdetailid() {
		return lab_entries_parameter_testdetailid;
	}
	public void setLab_entries_parameter_testdetailid(
			int lab_entries_parameter_testdetailid) {
		this.lab_entries_parameter_testdetailid = lab_entries_parameter_testdetailid;
	}
	public String getLab_parameter_code_value() {
		return lab_parameter_code_value;
	}
	public void setLab_parameter_code_value(String lab_parameter_code_value) {
		this.lab_parameter_code_value = lab_parameter_code_value;
	}
	public String getLab_entries_parameter_value() {
		return lab_entries_parameter_value;
	}
	public void setLab_entries_parameter_value(String lab_entries_parameter_value) {
		this.lab_entries_parameter_value = lab_entries_parameter_value;
	}
	public ParameterDetails(int lab_entries_parameter_testdetailid,String lab_parameter_code_value,String lab_entries_parameter_value){
		super();
		this.lab_entries_parameter_testdetailid=lab_entries_parameter_testdetailid;
		this.lab_parameter_code_value=lab_parameter_code_value;
		this.lab_entries_parameter_value=lab_entries_parameter_value;
	}
}
