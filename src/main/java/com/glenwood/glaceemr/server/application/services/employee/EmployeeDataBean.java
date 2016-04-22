package com.glenwood.glaceemr.server.application.services.employee;

/**
 * Employee data bean
 * @author software
 *
 */
public class EmployeeDataBean {
	
	Integer empId;
	Integer empLoginId;
	String empFullname;
	String empAddress;
	
	public EmployeeDataBean(Integer empId, Integer empLoginId,
			String empFullname, String empAddress) {
		super();
		this.empId = empId;
		this.empLoginId = empLoginId;
		this.empFullname = empFullname;
		this.empAddress = empAddress;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public Integer getEmpLoginId() {
		return empLoginId;
	}
	public void setEmpLoginId(Integer empLoginId) {
		this.empLoginId = empLoginId;
	}
	public String getEmpFullname() {
		return empFullname;
	}
	public void setEmpFullname(String empFullname) {
		this.empFullname = empFullname;
	}
	public String getEmpAddress() {
		return empAddress;
	}
	public void setEmpAddress(String empAddress) {
		this.empAddress = empAddress;
	}
	
		
}
