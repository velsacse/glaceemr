package com.glenwood.glaceemr.server.application.services.GroupTherapy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TherapyPatientsBean {
	Integer patientId;
    String accountNo;
    String lastName;
    String firstName;
    String dob;
    Integer sessionId;
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
	String lastSession;
	Integer groupId;
	String fullName;
	String Dx;
	String Dxdesc;
	long count;
	String startTime;
	Integer status;
	String sessionTopic = "";
    
    public TherapyPatientsBean(long count,String startTime,String stopTime,Integer status,Integer groupId,Integer sessionId,Integer patientId,String accountNo,String fullName,Date dob,Date lastSession,String Dx,String Dxdesc) {
    	if(count !=  0) {
    		this.count=count;
    	}
    	else {
    		this.count=-1;
    	}
    	if(status != 0) {
    		this.status=status;
    	}
    	else {
    		this.status=-1;
    	}
    	if(startTime!=null) {
    		this.startTime=startTime;
    	}
    	else {
    		this.startTime="";
    	}
    	if(stopTime!=null){
    		this.stopTime=stopTime;
    	}
    	else{
    		this.stopTime="";
    	}
    	if(groupId != null){
        	this.groupId=groupId;
    	}
    	else{
    		this.groupId=-1;
    	}
    	if(sessionId != null){
        	this.sessionId=sessionId;
    	}
    	else{
    		this.sessionId=-1;
    	}
    	if(patientId != null){
        	this.patientId=patientId;
    	}
    	else{
    		this.patientId=-1;
    	}
    	if(accountNo!=null){
    		this.accountNo=accountNo;
    	}
    	else{
    		this.accountNo="";
    	}
    	if(fullName!=null){
    		this.fullName=fullName;
    	}
    	else{
    		this.fullName="";
    	}
    	DateFormat mmformat=new SimpleDateFormat("MM/dd/yyyy");
    	if(dob!=null){
    		this.dob=mmformat.format(dob);
    	}
    	else{
    		this.dob="";
    	}
    	if(lastSession!=null){
    		this.lastSession=mmformat.format(lastSession);
    	}
    	else{
    		this.lastSession="";
    	}
    	if(Dx!=null){
    		this.Dx=Dx;
    	}
    	else{
    		this.Dx="";
    	}
    	if(Dxdesc!=null){
    		this.Dxdesc=Dxdesc;
    	}
    	else{
    		this.Dxdesc="";
    	}
    }
    public TherapyPatientsBean(Integer patientId,String accountNo,String lastName,String firstName,Date dob,String sessionTopic,Integer sessionId,String startTime,String stopTime,String dx1,String dx1Desc,String dx2, String dx2Desc,String dx3,String dx3Desc,String dx4,String dx4Desc,String dx5,String dx5Desc,String dx6,String dx6Desc,String dx7,String dx7Desc,String dx8,String dx8Desc){
    	if(patientId != null){
        	this.patientId=patientId;
    	}
    	else{
    		this.patientId=-1;
    	}
    	if(accountNo!=null){
    		this.accountNo=accountNo;
    	}
    	else{
    		this.accountNo="";
    	}
    	if(lastName!=null){
    		this.lastName=lastName;
    	}
    	else{
    		this.lastName="";
    	}
    	if(firstName!=null){
    		this.firstName=firstName;
    	}
    	else{
    		this.lastName="";
    	}
    	DateFormat mmformat=new SimpleDateFormat("MM/dd/yyyy");
    	if(dob!=null){
    		this.dob=mmformat.format(dob);
    	}
    	else{
    		this.dob="";
    	}
    	if(sessionId!=null){
    		this.sessionId=sessionId;
    	}
    	else{
    		this.sessionId=-1;
    	}
    	if(startTime!=null) {
    		this.startTime=startTime;
    	}
    	else {
    		this.startTime="";
    	}
    	if(stopTime!=null){
    		this.stopTime=stopTime;
    	}
    	else{
    		this.stopTime="";
    	}
    	if(dx1!=null){
    		this.dx1=dx1;
    	}
    	else{
    		this.dx1="";
    	}
    	if(dx1Desc!=null){
    		this.dx1Desc=dx1Desc;
    	}
    	else{
    		this.dx1Desc="";
    	}
    	if(dx2!=null){
    		this.dx2=dx2;
    	}
    	else{
    		this.dx2="";
    	}
    	if(dx2Desc!=null){
    		this.dx2Desc=dx2Desc;
    	}
    	else{
    		this.dx2Desc="";
    	}
    	if(dx3!=null){
    		this.dx3=dx3;
    	}
    	else{
    		this.dx3="";
    	}
    	if(dx3Desc!=null){
    		this.dx3Desc=dx3Desc;
    	}
    	else{
    		this.dx3Desc="";
    	}
    	if(dx4!=null){
    		this.dx4=dx4;
    	}
    	else{
    		this.dx4="";
    	}
    	if(dx5Desc!=null){
    		this.dx4Desc=dx4Desc;
    	}
    	else{
    		this.dx4Desc="";
    	}
    	if(dx5!=null){
    		this.dx5=dx5;
    	}
    	else{
    		this.dx5="";
    	}
    	if(dx5Desc!=null){
    		this.dx5Desc=dx5Desc;
    	}
    	else{
    		this.dx5Desc="";
    	}
    	if(dx6!=null){
    		this.dx6=dx6;
    	}
    	else{
    		this.dx6="";
    	}
    	if(dx6Desc!=null){
    		this.dx6Desc=dx6Desc;
    	}
    	else{
    		this.dx6Desc="";
    	}
    	if(dx7!=null){
    		this.dx7=dx7;
    	}
    	else{
    		this.dx7="";
    	}
    	if(dx7Desc!=null){
    		this.dx7Desc=dx7Desc;
    	}
    	else{
    		this.dx7Desc="";
    	}
    	if(dx8!=null){
    		this.dx8=dx8;
    	}
    	else{
    		this.dx8="";
    	}
    	if(dx8Desc!=null){
    		this.dx8Desc=dx8Desc;
    	}
    	else{
    		this.dx8Desc="";
    	}
    	if(sessionTopic!=null) {
    		this.sessionTopic=sessionTopic;
    	}
    	else {
    		this.sessionTopic="";
    	}
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

	public String getLastSession() {
		return lastSession;
	}
	
	public void setLastSession(String lastSession) {
		this.lastSession = lastSession;
	}
	
	public Integer getGroupId() {
		return groupId;
	}
	
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getDx() {
		return Dx;
	}
	
	public void setDx(String dx) {
		Dx = dx;
	}
	
	public String getDxdesc() {
		return Dxdesc;
	}
	
	public void setDxdesc(String dxdesc) {
		Dxdesc = dxdesc;
	}
	
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}
	
	public long getCount() {
		return count;
	}
	
	public void setCount(long count) {
		this.count = count;
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
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getSessionTopic() {
		return sessionTopic;
	}
	public void setSessionTopic(String sessionTopic) {
		this.sessionTopic = sessionTopic;
	}
	
}