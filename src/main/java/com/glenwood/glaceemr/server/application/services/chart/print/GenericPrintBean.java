package com.glenwood.glaceemr.server.application.services.chart.print;

import java.util.List;

import com.glenwood.glaceemr.server.application.services.employee.EmployeeDataBean;
import com.glenwood.glaceemr.server.application.services.patient.PatientDataBean;
import com.glenwood.glaceemr.server.application.services.pos.PosDataBean;

public class GenericPrintBean {
	
	private PatientDataBean patientBean;
	private List<EmployeeDataBean> employeeBean;
	private List<PosDataBean> posBean;
	
	public GenericPrintBean(PatientDataBean patientBean,
			List<EmployeeDataBean> employeeBean, List<PosDataBean> posBean) {
		this.patientBean = patientBean;
		this.employeeBean = employeeBean;
		this.posBean = posBean;
	}
	
	public PatientDataBean getPatientBean() {
		return patientBean;
	}
	public void setPatientBean(PatientDataBean patientBean) {
		this.patientBean = patientBean;
	}
	public List<EmployeeDataBean> getEmployeeBean() {
		return employeeBean;
	}
	public void setEmployeeBean(List<EmployeeDataBean> employeeBean) {
		this.employeeBean = employeeBean;
	}
	public List<PosDataBean> getPosBean() {
		return posBean;
	}
	public void setPosBean(List<PosDataBean> posBean) {
		this.posBean = posBean;
	}
	
	
}
