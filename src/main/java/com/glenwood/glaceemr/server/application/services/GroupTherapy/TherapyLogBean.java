package com.glenwood.glaceemr.server.application.services.GroupTherapy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TherapyLogBean {

	String date;
	String docName;
	String pos;
	String groupName;
	Long count;
	int groupId;
	int providerId;
	int posId;
	String endTime="";
	String therapyStartDate="";
	int status;
	
	public TherapyLogBean(Date date,String docName,String groupName,String pos,Long count,int groupId,int providerId,int posId,String endTime,Date therapyStartDate,int status){
		
		this.docName=docName;
		this.count=count;
		this.pos=pos;
		DateFormat mmformat=new SimpleDateFormat("MM/dd/yyyy");
		this.date=mmformat.format(therapyStartDate);
        this.groupName=groupName;
        this.groupId=groupId;
        this.posId=posId;
        this.providerId=providerId;
        this.endTime=endTime;
        DateFormat timeFormat=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        this.therapyStartDate=timeFormat.format(therapyStartDate);
        this.status=status;
	}


	
	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public int getGroupId() {
		return groupId;
	}



	public int getProviderId() {
		return providerId;
	}



	public int getPosId() {
		return posId;
	}



	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}



	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}



	public void setPosId(int posId) {
		this.posId = posId;
	}



	public Long getCount() {
		return count;
	}



	public void setCount(Long count) {
		this.count = count;
	}

    
	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getDocName() {
		return docName;
	}

	public String getPos() {
		return pos;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}



	public String getTherapyStartDate() {
		return therapyStartDate;
	}



	public void setTherapyStartDate(String therapyStartDate) {
		this.therapyStartDate = therapyStartDate;
	}
	
	
	
}
