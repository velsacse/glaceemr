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
    String stopTime;
	String dx1="";
	String dx1Desc="";
	String dx2="";
	String dx2Desc="";
	String dx3="";
	String dx3Desc="";
	String dx4="";
	String dx4Desc="";
	String dx5="";
	String dx5Desc="";
	String dx6="";
	String dx6Desc="";
	String dx7="";
	String dx7Desc="";
	String dx8="";
	String dx8Desc="";
    
    

    public TherapyPatientsBean(int patientId,String accountNo,String lastName,String firstName,Date dob,int sessionId,String stopTime,String dx1,String dx1Desc,String dx2, String dx2Desc,String dx3,String dx3Desc,String dx4,String dx4Desc,String dx5,String dx5Desc,String dx6,String dx6Desc,String dx7,String dx7Desc,String dx8,String dx8Desc){
    	this.patientId=patientId;
    	this.accountNo=accountNo;
    	this.lastName=lastName;
    	this.firstName=firstName;
    	DateFormat mmformat=new SimpleDateFormat("MM/dd/yyyy");
		this.dob=mmformat.format(dob);
    	this.sessionId=sessionId;
    	this.stopTime=stopTime;
    	this.dx1=dx1;
    	this.dx1Desc=dx1Desc;
    	this.dx2=dx2;
    	this.dx2Desc=dx2Desc;
    	this.dx3=dx3;
    	this.dx3Desc=dx3Desc;
    	this.dx4=dx4;
    	this.dx4Desc=dx4Desc;
    	this.dx5=dx5;
    	this.dx5Desc=dx5Desc;
    	this.dx6=dx6;
    	this.dx6Desc=dx6Desc;
    	this.dx7=dx7;
    	this.dx7Desc=dx7Desc;
    	this.dx8=dx8;
    	this.dx8Desc=dx8Desc;
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


	public String getStopTime() {
		return stopTime;
	}


	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}


	public String getDx1() {
		return dx1;
	}


	public void setDx1(String dx1) {
		this.dx1 = dx1;
	}


	public String getDx1Desc() {
		return dx1Desc;
	}


	public void setDx1Desc(String dx1Desc) {
		this.dx1Desc = dx1Desc;
	}


	public String getDx2() {
		return dx2;
	}


	public void setDx2(String dx2) {
		this.dx2 = dx2;
	}


	public String getDx2Desc() {
		return dx2Desc;
	}


	public void setDx2Desc(String dx2Desc) {
		this.dx2Desc = dx2Desc;
	}


	public String getDx3() {
		return dx3;
	}


	public void setDx3(String dx3) {
		this.dx3 = dx3;
	}


	public String getDx3Desc() {
		return dx3Desc;
	}


	public void setDx3Desc(String dx3Desc) {
		this.dx3Desc = dx3Desc;
	}


	public String getDx4() {
		return dx4;
	}


	public void setDx4(String dx4) {
		this.dx4 = dx4;
	}


	public String getDx4Desc() {
		return dx4Desc;
	}


	public void setDx4Desc(String dx4Desc) {
		this.dx4Desc = dx4Desc;
	}
	public String getDx5() {
		return dx5;
	}


	public void setDx5(String dx5) {
		this.dx5 = dx5;
	}


	public String getDx5Desc() {
		return dx5Desc;
	}


	public void setDx5Desc(String dx5Desc) {
		this.dx5Desc = dx5Desc;
	}


	public String getDx6() {
		return dx6;
	}


	public void setDx6(String dx6) {
		this.dx6 = dx6;
	}


	public String getDx6Desc() {
		return dx6Desc;
	}


	public void setDx6Desc(String dx6Desc) {
		this.dx6Desc = dx6Desc;
	}


	public String getDx7() {
		return dx7;
	}


	public void setDx7(String dx7) {
		this.dx7 = dx7;
	}


	public String getDx7Desc() {
		return dx7Desc;
	}


	public void setDx7Desc(String dx7Desc) {
		this.dx7Desc = dx7Desc;
	}


	public String getDx8() {
		return dx8;
	}


	public void setDx8(String dx8) {
		this.dx8 = dx8;
	}


	public String getDx8Desc() {
		return dx8Desc;
	}


	public void setDx8Desc(String dx8Desc) {
		this.dx8Desc = dx8Desc;
	}
	
}
