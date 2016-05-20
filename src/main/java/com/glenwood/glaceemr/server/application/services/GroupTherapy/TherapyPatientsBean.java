package com.glenwood.glaceemr.server.application.services.GroupTherapy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TherapyPatientsBean {
	int patientId;
    String accountNo;
    String lastName;
    String firstName;
    String dob;
    int sessionId;

    public TherapyPatientsBean(int patientId,String accountNo,String lastName,String firstName,Date dob,int sessionId){
    	this.patientId=patientId;
    	this.accountNo=accountNo;
    	this.lastName=lastName;
    	this.firstName=firstName;
    	DateFormat mmformat=new SimpleDateFormat("MM/dd/yyyy");
		this.dob=mmformat.format(dob);
    	this.sessionId=sessionId;
    	
    }
    

	public int getSessionId() {
		return sessionId;
	}


	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}


	public int getPatientId() {
		return patientId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
    
    
}
