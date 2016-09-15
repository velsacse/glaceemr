package com.glenwood.glaceemr.server.application.services.chart.charges;

import java.util.ArrayList;
import java.util.List;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.H076;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PosTable;

public class ChargesPageBasicDetailsBean {
	
	List<PosTable> posDetails=new ArrayList<PosTable>();
	List<EmployeeProfile> employeeDetails=new ArrayList<EmployeeProfile>();
	List<PatientInsDetail> insDetails=new ArrayList<PatientInsDetail>();
	List<H076> h076ReferringDr=new ArrayList<H076>();
	public List<PosTable> getPosDetails() {
		return posDetails;
	}
	public void setPosDetails(List<PosTable> posDetails) {
		this.posDetails = posDetails;
	}
	public List<EmployeeProfile> getEmployeeDetails() {
		return employeeDetails;
	}
	public void setEmployeeDetails(List<EmployeeProfile> employeeDetails) {
		this.employeeDetails = employeeDetails;
	}
	public List<PatientInsDetail> getInsDetails() {
		return insDetails;
	}
	public void setInsDetails(List<PatientInsDetail> insDetails) {
		this.insDetails = insDetails;
	}
	public List<H076> getH076ReferringDr() {
		return h076ReferringDr;
	}
	public void setH076ReferringDr(List<H076> h076ReferringDr) {
		this.h076ReferringDr = h076ReferringDr;
	}
}
