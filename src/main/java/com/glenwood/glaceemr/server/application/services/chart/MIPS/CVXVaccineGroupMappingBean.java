package com.glenwood.glaceemr.server.application.services.chart.MIPS;


public class CVXVaccineGroupMappingBean {
	public CVXVaccineGroupMappingBean(String cvx_vaccine_group_mapping_cvx_code, String cvx_vaccine_group_mapping_uncertain_formulation_cvx) {
		super();
		this.cvx_vaccine_group_mapping_cvx_code=cvx_vaccine_group_mapping_cvx_code;
		this.cvx_vaccine_group_mapping_uncertain_formulation_cvx=cvx_vaccine_group_mapping_uncertain_formulation_cvx;
		
	}
String cvx_vaccine_group_mapping_cvx_code;
String cvx_vaccine_group_mapping_uncertain_formulation_cvx;
public String getCvx_vaccine_group_mapping_cvx_code() {
	return cvx_vaccine_group_mapping_cvx_code;
}
public void setCvx_vaccine_group_mapping_cvx_code(
		String cvx_vaccine_group_mapping_cvx_code) {
	this.cvx_vaccine_group_mapping_cvx_code = cvx_vaccine_group_mapping_cvx_code;
}
public String getCvx_vaccine_group_mapping_uncertain_formulation_cvx() {
	return cvx_vaccine_group_mapping_uncertain_formulation_cvx;
}
public void setCvx_vaccine_group_mapping_uncertain_formulation_cvx(
		String cvx_vaccine_group_mapping_uncertain_formulation_cvx) {
	this.cvx_vaccine_group_mapping_uncertain_formulation_cvx = cvx_vaccine_group_mapping_uncertain_formulation_cvx;
}

}
